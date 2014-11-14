package bean;


import Mineracao.Mineracao;
import dao.AirConditionerDao;
import dao.DeviceDao;
import dao.HomeDao;
import dao.RoomDao;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import models.AirConditioner;
import models.Device;
import models.Home;
import models.Room;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.LoggerFactory;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Daniel
 */
@ManagedBean(name = "main")
public class Cluster {

    public static SchedulerFactory schFactory;
    public static Scheduler sch;

    public void UDPListener() {
        if (!udp.isAlive()) {
            udp.start();
        }
        if (!mining.isAlive()) {
            mining.start();
        }
    }

    public static SchedulerFactory getSchFactory() throws SchedulerException {
        //schedule the job
        if (schFactory == null) {
            schFactory = new StdSchedulerFactory();
            sch = schFactory.getScheduler();
        }
        return schFactory;
    }

    public static Scheduler getSch() throws SchedulerException {
        if(sch == null){
            getSchFactory();
        }
        if (!sch.isStarted() || sch.isShutdown() || sch.isInStandbyMode()) {
            sch.start();
        }
        return sch;
    }
    
    static Thread udp = new Thread() {
        @Override
        public void run() {
            DatagramSocket sock = null;
            byte[] msg = new byte[256];
            DatagramPacket pack = new DatagramPacket(msg, msg.length);
            try {
                sock = new DatagramSocket(1901);
            } catch (SocketException ex) {
                org.slf4j.Logger logger = LoggerFactory.getLogger(TopHatMB.class);
                logger.error(ex.getMessage(), ex);
            }


            //Recebimento da mensagem
            while (true) {
                try {
                    sock.receive(pack);
                    String aux = new String(pack.getData()).trim();
                    System.out.println("Pacote recebido: " + aux);

                    Room createdRoom = null;
                    Device createdDevice;
                    AirConditioner createdAir;
                    Room r;
                    Device d;
                    AirConditioner a = null;
                    Home bean = new Home();

                    StringTokenizer rooms = new StringTokenizer(aux, "/");
                    bean.setId(rooms.nextToken());
                    bean.setIp(rooms.nextToken());

                    RoomDao daoRoom = new RoomDao();
                    DeviceDao daoDevice = new DeviceDao();
                    HomeDao daoHome = new HomeDao();
                    AirConditionerDao daoAir = new AirConditionerDao();



                    if (daoHome.get(bean.getId()) != null) {
                        while (rooms.hasMoreElements()) {
                            StringTokenizer devices = new StringTokenizer(rooms.nextToken(), ";");
                            while (devices.hasMoreElements()) {
                                String nxt = devices.nextToken();
                                if (nxt.length() > 1) {
                                    createdRoom = new Room(nxt);
                                    createdRoom.setHome(bean);
                                    r = daoRoom.get(createdRoom);
                                    if (r != null) {
                                        createdRoom.setId(r.getId());
                                    }
                                    daoRoom.save(createdRoom);
                                } else if (createdRoom != null) {
                                    switch (nxt) {
                                        case "l":
                                            createdDevice = new Device("Luz", Integer.parseInt(devices.nextToken()), Integer.parseInt(devices.nextToken()), nxt.charAt(0));
                                            break;
                                        case "w":
                                            createdDevice = new Device("Janela", Integer.parseInt(devices.nextToken()), Integer.parseInt(devices.nextToken()), nxt.charAt(0));
                                            break;
                                        case "a":
                                            createdDevice = new Device("Ar condicionado", Integer.parseInt(devices.nextToken()), 0, nxt.charAt(0));
                                            break;
                                        default:
                                            createdDevice = null;
                                            System.out.println("Dispositivo não reconhecido");
                                            break;
                                    }
                                    if (createdDevice != null) {
                                        createdDevice.setRoom(createdRoom);
                                        d = daoDevice.get(createdDevice);
                                        if (d != null) {
                                            createdDevice.setId(d.getId());
                                        }
                                        daoDevice.save(createdDevice);
                                        if (nxt.equalsIgnoreCase("a") && d == null) {
                                            createdDevice = daoDevice.get(createdDevice);
                                            createdAir = new AirConditioner();
                                            createdAir.setDevice(createdDevice);
                                            a = daoAir.getByDevice(createdDevice.getId());
                                            if (a != null) {
                                                createdAir.setId(a.getId());
                                            }
                                            daoAir.save(createdAir);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    daoHome.save(bean);
                    System.out.println("Home" + bean.getId() + "criada");
                } catch (IOException ex) {
                    org.slf4j.Logger logger = LoggerFactory.getLogger(TopHatMB.class);
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
    };
    static Thread mining = new Thread() {
        @Override
        public void run() {
            Date d = new Date();
            if (d.getHours() == 3 && d.getMinutes() >= 20 && d.getMinutes() <= 30) {
                HomeDao daoHome = new HomeDao();
                Mineracao miningHistory = new Mineracao();
                List<Home> homes = daoHome.list();
                for (int i = 0; i < homes.size(); i++) {
                    miningHistory.miningHistory(homes.get(i));
                }
            } else {
                try {
                    sleep(2400000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Cluster.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    };
}
