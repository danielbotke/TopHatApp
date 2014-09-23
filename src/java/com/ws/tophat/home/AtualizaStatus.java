/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.tophat.home;

import dao.DeviceDao;
import dao.HomeDao;
import dao.RoomDao;
import java.util.StringTokenizer;
import javax.faces.context.FacesContext;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import models.Device;
import models.Home;
import models.Room;

/**
 *
 * @author Daniel
 */
@WebService(serviceName = "AtualizaStatus")
public class AtualizaStatus {

    /**
     * Operação de Web service
     */
    @WebMethod(operationName = "updateStatus")
    public void updateStatus(@WebParam(name = "homeStatus") String homeStatus) {
        Room createdRoom = null;
        String aux = "";
        Home bean = new Home();
        StringTokenizer rooms = new StringTokenizer(aux, "/");
        bean.setId(rooms.nextToken());
        RoomDao daoRoom = new RoomDao();
        DeviceDao daoDevice = new DeviceDao();
        Room r = null;
        Device d = null;
        Device createdDevice;
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
                    bean.getRooms().add(createdRoom);
                } else if (createdRoom != null) {
                    switch (nxt) {
                        case "l":
                            createdRoom.getDevices().add(new Device("Light", Integer.parseInt(devices.nextToken()), Integer.parseInt(devices.nextToken()), nxt.charAt(0)));
                            break;
                        case "w":
                            createdRoom.getDevices().add(new Device("Window", Integer.parseInt(devices.nextToken()), Integer.parseInt(devices.nextToken()), nxt.charAt(0)));
                            break;
                        case "a":
                            createdRoom.getDevices().add(new Device("Air conditioner", Integer.parseInt(devices.nextToken()), Integer.parseInt(devices.nextToken()), nxt.charAt(0)));
                            break;
                        default:
                            System.out.println("Dispositivo não reconhecido");
                            break;
                    }
                    createdDevice = createdRoom.getDevices().get(createdRoom.getDevices().size() - 1);
                    createdDevice.setRoom(createdRoom);
                    d = daoDevice.get(createdDevice);
                    if (d != null) {
                        createdDevice.setId(d.getId());
                    }
                }

            }

        }
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        bean.setIp(request.getRemoteAddr());
        HomeDao dao = new HomeDao();
        dao.save(bean);
        System.out.println("TopHatBean criado");
    }
}
