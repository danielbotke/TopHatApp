package bean;

import java.io.IOException;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.brickred.socialauth.AuthProvider;
import org.brickred.socialauth.Profile;
import org.brickred.socialauth.SocialAuthConfig;
import org.brickred.socialauth.SocialAuthManager;
import org.brickred.socialauth.util.SocialAuthUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ManagedBean(name = "userSession")
@SessionScoped
public class UserSessionBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private SocialAuthManager manager;
    private String originalURL;
    private String providerID;
    private Profile profile;
    private Logger logger = LoggerFactory.getLogger(UserSessionBean.class);

    public UserSessionBean() {
        //... 
    }

    public void socialConnect(String providerIDSelected) throws Exception {
        this.setProviderID(providerIDSelected);
        System.out.println(providerID);
        SocialAuthConfig config = SocialAuthConfig.getDefault();

        config.load();

        SocialAuthManager manager = new SocialAuthManager();
        manager.setSocialAuthConfig(config);

        String successUrl = "http://tophat.tcc.br/TopHatApp/home.jsf";

        String url = manager.getAuthenticationUrl(providerID, successUrl);

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.setAttribute("authManager", manager);
        FacesContext.getCurrentInstance().getExternalContext().redirect(url);
        session.setAttribute("userSession", this);
    }

    public void pullUserInfo() throws Exception {
        // Pull user's data from the provider
        // get the social auth manager from session
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        manager = (SocialAuthManager) session.getAttribute("authManager");

        // call connect method of manager which returns the provider object. 
        // Pass request parameter map while calling connect method. 
        AuthProvider provider = manager.connect(SocialAuthUtil.getRequestParametersMap(request));

        // get profile
        profile = provider.getUserProfile();

        session.setAttribute("userSession", this);
    }

    public void logOut() throws IOException {
        // Disconnect from the provider
        manager.disconnectProvider(providerID);
        // Invalidate session
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        // Redirect to home page
        FacesContext.getCurrentInstance().getExternalContext().redirect("http://tophat.tcc.br/TopHatApp/index.jsf");
    }

    public SocialAuthManager getManager() {
        return manager;
    }

    public void setManager(SocialAuthManager manager) {
        this.manager = manager;
    }

    public String getOriginalURL() {
        return originalURL;
    }

    public void setOriginalURL(String originalURL) {
        this.originalURL = originalURL;
    }

    public String getProviderID() {
        return providerID;
    }

    public void setProviderID(String providerID) {
        this.providerID = providerID;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
