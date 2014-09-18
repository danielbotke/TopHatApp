/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.DeviceDao;
import dao.HistActionDao;
import dao.HomeDao;
import dao.IUserDao;
import dao.RoomDao;
import dao.ToDoActionDao;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.StringTokenizer;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import models.Device;
import models.HistAction;
import models.Home;
import models.IAction;
import models.IUser;
import models.Room;
import org.brickred.socialauth.Profile;
import org.omnifaces.util.Faces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Daniel
 */
@ManagedBean(name = "topHatMB")
public class TopHatMB {
    
    private Home bean = new Home();
    private Room currentRoom = null;
    private IUser user;
    private Logger logger = LoggerFactory.getLogger(TopHatMB.class);
    
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
     * @throws Exception 
     */
    public TopHatMB() throws Exception {
        Room createdRoom = null;
        String aux = "";
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        UserSessionBean userSession = (UserSessionBean) session.getAttribute("userSession");
        userSession.pullUserInfo();
        userSession = (UserSessionBean) session.getAttribute("userSession");
        if (userSession != null) {
            Profile profile = userSession.getProfile();
            IUserDao userDao = new IUserDao();
            user = userDao.getFacebookId(profile.getValidatedId());
            if (user != null) {
                bean = user.getHome();
            } else {
                bean.setIp(Faces.getRemoteAddr());
                bean.getUsers().add(user);
            }
        }
        
        URL url = new URL("https://" + bean.getIp() + ":9898");
        // cria um stream de entrada do conteÃºdo 
        InputStreamReader inputReader = new InputStreamReader(url.openStream());
        BufferedReader bufferedReader = new BufferedReader(inputReader);
        String linha = "";
        while ((linha = bufferedReader.readLine()) != null) {
            aux += linha;
        }
        
        //aux = "2/Kitchen;l;9;1;w;13;1/Living;l;10;0;w;14;1/Bad1;l;11;0;w;15;1/Bad2;l;12;1;w;16;0";
        // Ao fim deve-se ter a estrutura da resudÃªncia retornada pelo Arduino.
        StringTokenizer rooms = new StringTokenizer(aux, "/");
        bean.setId(Integer.parseInt(rooms.nextToken()));
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
                            System.out.println("Dispositivo nÃ£o reconhecido");
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
    
    public void addHistAction(String action, Device d) throws MalformedURLException, IOException {
        HistAction hist = new HistAction();
        hist.setHome(bean);
        hist.setDateTime(new Date());
        hist.setAction(new IAction(action, d));
        HistActionDao histActDao = new HistActionDao();
        histActDao.save(hist);
        URL url = new URL("http://" + bean.getIp() +":9898/?" + action + d.getActionPort());
        url.openConnection();
    }
    
    public void activateTodoAction(int actionId){
        ToDoActionDao dao = new ToDoActionDao();
        bean.getToDoAction().get(actionId).setActivated(Boolean.TRUE);
        dao.save(bean.getToDoAction().get(actionId));
    }
}
