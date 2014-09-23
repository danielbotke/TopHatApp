/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.HistActionDao;
import dao.IUserDao;
import dao.ToDoActionDao;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
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
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        UserSessionBean userSession = (UserSessionBean) session.getAttribute("userSession");
        userSession.pullUserInfo();
        userSession = (UserSessionBean) session.getAttribute("userSession");
        if (userSession != null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("http://tophat.tcc.br/TopHatApp/newUser.jsf");
            Profile profile = userSession.getProfile();
            IUserDao userDao = new IUserDao();
            user = userDao.getFacebookId(profile.getValidatedId());
            if (user == null) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("http://tophat.tcc.br/TopHatApp/newUser.jsf");
            }else{
                bean = user.getHome();
            }
        }
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
