/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.DeviceDao;
import dao.HistActionDao;
import dao.HomeDao;
import dao.IActionDao;
import dao.IUserDao;
import dao.ToDoActionDao;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import models.Device;
import models.HistAction;
import models.Home;
import models.IAction;
import models.IUser;
import models.Room;
import org.brickred.socialauth.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Daniel
 */
@ManagedBean(name = "topHatMB")
@SessionScoped
public class TopHatMB {

    private Home bean = new Home();
    private Room currentRoom = null;
    private IUser user;
    private Logger logger = LoggerFactory.getLogger(TopHatMB.class);
    private boolean isUser = false;
    private boolean validatedUser = false;

    public Home getBean() throws Exception {
        this.validateUser();
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

    public IUser getUser() {
        return user;
    }

    public void setUser(IUser user) {
        this.user = user;
    }

    public boolean isIsUser() {
        return isUser;
    }

    public void setIsUser(boolean isUser) {
        this.isUser = isUser;
    }

    public TopHatMB() {
    }

    /**
     * Creates a new instance of TopHatMB
     *
     * @throws Exception
     */
    public void validateUser() throws Exception {
        if (!validatedUser) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
            UserSessionBean userSession = (UserSessionBean) session.getAttribute("userSession");
            if (session.getAttribute("pullUserExecuted") == null) {
                userSession.pullUserInfo();
                session.setAttribute("pullUserExecuted", true);
            }
            userSession = (UserSessionBean) session.getAttribute("userSession");
            if (userSession != null) {
                Profile profile = userSession.getProfile();
                IUserDao userDao = new IUserDao();
                user = userDao.get(Long.parseLong(profile.getValidatedId()));
                if (user == null) {
                    isUser = false;
                } else {
                    bean = user.getHome();
                    //    bean.setRooms(bean.getRooms());
                    isUser = true;
                }
            }
        }
    }

    public String addUser() throws Exception {
        HomeDao daoHome = new HomeDao();
        IUserDao daoUser = new IUserDao();
        IUser userAux = new IUser();
        Home auxHome;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        UserSessionBean userSession = (UserSessionBean) session.getAttribute("userSession");
        if (userSession != null) {
            Profile profile = userSession.getProfile();
            auxHome = daoHome.get(bean.getId());
            if (auxHome != null) {
                bean = auxHome;
                userAux.setHome(bean);
                userAux.setEmail(profile.getEmail());
                userAux.setProviderId(Long.parseLong(profile.getValidatedId()));
                userAux.setName(profile.getFullName());
                daoUser.save(userAux);
                isUser = true;
                return "home";
            } else {
                //apresentar mensagem de que o código informado é inválido
                facesContext.addMessage(null, new FacesMessage("O código informado é inválido"));
                return "home";
            }
        }
        return "home";
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

    public String addHistAction(Device d, String act) throws MalformedURLException, IOException {
        //String action = "";
        //Device d = new Device();
        try {
            URL url = new URL("http://" + bean.getIp() + "/?" + act + d.getActionPort());
            URLConnection con = url.openConnection();
            con.getContent();
        } catch (IOException e) {
            return null;
        }
        if (d.getType() == 'l') {
            if (d.getStatus() == 0) {
                d.setStatusDevice(1);
            } else {
                d.setStatusDevice(0);
            }
        }
        DeviceDao daoDevice = new DeviceDao();
        daoDevice.save(d);
        HistAction hist = new HistAction();
        hist.setHome(bean);
        hist.setDateTime(new Date());
        IAction action = new IAction(act, d);
        IActionDao daoIAction = new IActionDao();
        IAction auxAction = daoIAction.get(action);
        if (auxAction == null) {
            daoIAction.save(action);
        } else {
            action.setId(auxAction.getId());
        }
        hist.setAction(action);
        HistActionDao histActDao = new HistActionDao();
        histActDao.save(hist);
        return "room";
    }

    public void activateTodoAction(int actionId) {
        ToDoActionDao dao = new ToDoActionDao();
        bean.getToDoAction().get(actionId).setActivated(Boolean.TRUE);
        dao.save(bean.getToDoAction().get(actionId));
    }
}
