/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.HomeDao;
import dao.IUserDao;
import javax.faces.application.FacesMessage;
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

    public HomeMB() {
    }
    

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
        Home auxHome;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        UserSessionBean userSession = (UserSessionBean) session.getAttribute("userSession");
        if (userSession != null) {
            Profile profile = userSession.getProfile();
            auxHome = daoHome.get(bean.getId());
            if (auxHome != null) {
                bean = auxHome;
                user.setHome(auxHome);
                user.setEmail(profile.getEmail());
                user.setFacebookId(profile.getValidatedId());
                user.setName(profile.getFullName());
                user.setSex(profile.getGender());
                bean.getUsers().add(user);
                daoHome.save(bean);
                daoUser.save(user);
                session.setAttribute("homeSession", bean);
            } else {
                //apresentar mensagem de que o código informado é inválido
                facesContext.addMessage(null, new FacesMessage("O código informado é inválido"));
            }
        }
        return "home";
    }
    
}
