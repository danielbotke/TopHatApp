/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.HistActionDao;
import dao.HomeDao;
import dao.IUserDao;
import dao.ToDoActionDao;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import javax.faces.application.FacesMessage;
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
    private boolean isUser = false;

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

    public TopHatMB() throws Exception {
    }

    /**
     * Creates a new instance of TopHatMB
     *
     * @throws Exception
     */
    public boolean validateUser() throws Exception {
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
            user = userDao.getFacebookId(profile.getValidatedId());
            if (user == null) {
                return false;
            } else {
                bean = user.getHome();
                return true;
            }
        }
        return false;
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
                userAux.setFacebookId(profile.getValidatedId());
                userAux.setName(profile.getFullName());
                userAux.setSex(profile.getGender());
                if(bean.getUsers() == null){
                    bean.setUsers(new ArrayList<IUser>()); 
                }
                bean.getUsers().add(userAux);
                daoHome.save(bean);
                daoUser.save(userAux);
                isUser = true;
                return "home";
            } else {
                //apresentar mensagem de que o código informado é inválido
                facesContext.addMessage(null, new FacesMessage("O código informado é inválido"));
                return "newUser";
            }
        }
        return "index";
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
        URL url = new URL("http://" + bean.getIp() + ":9898/?" + action + d.getActionPort());
        url.openConnection();
    }

    public void activateTodoAction(int actionId) {
        ToDoActionDao dao = new ToDoActionDao();
        bean.getToDoAction().get(actionId).setActivated(Boolean.TRUE);
        dao.save(bean.getToDoAction().get(actionId));
    }
}
