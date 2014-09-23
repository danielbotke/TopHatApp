/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.HomeDao;
import dao.IUserDao;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import models.Home;
import models.IUser;
import org.brickred.socialauth.Profile;

/**
 *
 * @author Daniel
 */
@ManagedBean(name = "homeMB")
public class HomeMB {
    
    private Home bean = new Home();

    public Home getBean() {
        return bean;
    }

    public void setBean(Home bean) {
        this.bean = bean;
    }
        
    public String addUser() throws Exception{
        HomeDao daoHome = new HomeDao();
        IUserDao daoUser = new IUserDao();
        IUser user = new IUser();
        Home auxHome = new Home();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        UserSessionBean userSession = (UserSessionBean) session.getAttribute("userSession");
        userSession.pullUserInfo();
        if (userSession != null) {
            Profile profile = userSession.getProfile();
            auxHome = daoHome.get(bean.getId());
            if (auxHome != null) {
                bean = user.getHome();
                user.setEmail(profile.getEmail());
                user.setFacebookId(profile.getValidatedId());
                user.setName(profile.getFullName());
                user.setSex(profile.getGender());
                bean.getUsers().add(user);
                return "home";
            } else {
                //apresentar mensagem de que o código informado é inválido
                return "newUser";
            }
        }
        return "index";
    }
    
}
