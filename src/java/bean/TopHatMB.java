/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.HomeDao;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.StringTokenizer;
import javax.faces.bean.ManagedBean;
import models.Device;
import models.Home;
import models.Room;

/**
 *
 * @author Daniel
 */
@ManagedBean(name = "topHatMB")
public class TopHatMB {

    private Home bean = new Home();
    private Room currentRoom = null;

    public Home getBean() {
        return bean;
    }

    public void setBean(Home bean) {
        this.bean = bean;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    

    /**
     * Creates a new instance of TopHatMB
     */
    public TopHatMB() throws MalformedURLException, IOException {
        Room createdRoom = null;
        String aux = "";
        /**
         * URL url = new URL("https://www.google.com.br/"); // cria um stream de
         * entrada do conteúdo InputStreamReader inputReader = new
         * InputStreamReader(url.openStream()); BufferedReader bufferedReader =
         * new BufferedReader(inputReader); String linha = ""; while ((linha =
         * bufferedReader.readLine()) != null) { aux += linha; }*
         */
        aux = "1/Kitchen;l;9;1;w;13;1/Living;l;10;0;w;14;1/Bad1;l;11;0;w;15;1/Bad2;l;12;1;w;16;0";
        // Ao fim deve-se ter a estrutura da resudência retornada pelo Arduino.
        StringTokenizer rooms = new StringTokenizer(aux, "/");
        bean.setId(Integer.parseInt(rooms.nextToken()));
        while (rooms.hasMoreElements()) {
            StringTokenizer devices = new StringTokenizer(rooms.nextToken(), ";");
            while (devices.hasMoreElements()) {
                String nxt = devices.nextToken();
                if (nxt.length() > 1) {
                    createdRoom = new Room(nxt);
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
                }

            }

        }
        HomeDao dao = new HomeDao();
        dao.save(bean);
        System.out.println("TopHatBean criado");

    }

    public String selectRoom(String room) {
        for (int i = 0; i < bean.getRooms().size(); i++) {
            if ((bean.getRooms().get(i).getName()).equalsIgnoreCase(room)) {
                currentRoom = bean.getRooms().get(i);
                return "room";
            }
        }
        return "";
    }
}
