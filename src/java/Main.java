
import bean.TopHatMB;
import dao.DeviceDao;
import dao.HomeDao;
import dao.RoomDao;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.faces.bean.ManagedBean;
import models.Device;
import models.Home;
import models.Room;
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
public class Main {

    public void UDPListener() {
        if (!rx.isAlive()) {
            rx.start();
        }
    }
    static Thread rx = new Thread() {
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
                    Room r;
                    Device d;
                    Home bean = new Home();

                    StringTokenizer rooms = new StringTokenizer(aux, "/");
                    bean.setId(rooms.nextToken());
                    bean.setIp(rooms.nextToken());

                    RoomDao daoRoom = new RoomDao();
                    DeviceDao daoDevice = new DeviceDao();
                    HomeDao daoHome = new HomeDao();


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
                                            createdDevice = new Device("Light", Integer.parseInt(devices.nextToken()), Integer.parseInt(devices.nextToken()), nxt.charAt(0));
                                            break;
                                        case "w":
                                            createdDevice = new Device("Window", Integer.parseInt(devices.nextToken()), Integer.parseInt(devices.nextToken()), nxt.charAt(0));
                                            break;
                                        case "a":
                                            createdDevice = new Device("Air conditioner", Integer.parseInt(devices.nextToken()), Integer.parseInt(devices.nextToken()), nxt.charAt(0));
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
}
