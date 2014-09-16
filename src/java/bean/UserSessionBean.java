package bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import org.jboss.weld.context.http.HttpRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ManagedBean(name = "userSession")
@SessionScoped
public class UserSessionBean implements Serializable {

    private SocialAuthManager manager;
    private String originalURL;
    private String providerID;
    private Profile profile;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public UserSessionBean() {
        //... 
    }

    public void socialConnect(String providerIDSelected) throws Exception {

        //Create an instance of SocialAuthConfgi object
        SocialAuthConfig config = SocialAuthConfig.getDefault();

        //load configuration. By default load the configuration from oauth_consumer.properties. 
        //You can also pass input stream, properties object or properties file name.
        config.load();

        //Create an instance of SocialAuthManager and set config
        manager = new SocialAuthManager();
        manager.setSocialAuthConfig(config);

        // URL of YOUR application which will be called after authentication
        String successUrl = "http://tophat.com.br/TopHatApp/faces/home.xhtml";

        // get Provider URL to which you should redirect for authentication.
        // id can have values "facebook", "twitter", "yahoo" etc. or the OpenID URL
        String url = manager.getAuthenticationUrl(providerIDSelected, successUrl);

        // Store in session
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("authManager", manager);
        this.pullUserInfo();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userSession", this);
    }

    public void pullUserInfo() throws Exception {
        try {
            // get the social auth manager from session
            manager = (SocialAuthManager) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("authManager");

            // call connect method of manager which returns the provider object. 
            // Pass request parameter map while calling connect method.
            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
            AuthProvider provider = manager.connect(SocialAuthUtil.getRequestParametersMap(request));
            logger.debug("Após manager.connect");

            // get profile
            Profile p = provider.getUserProfile();

            // you can obtain profile information
            System.out.println(p.getFirstName());

            // OR also obtain list of contacts
         //   List<Contact> contactsList = provider.getContactList();
        } catch (Exception ex) {
            logger.error(ex.toString());
            throw ex;
        }
    }

    public void logOut() {
        try {
            // Disconnect from the provider
            manager.disconnectProvider(providerID);
            // Invalidate session
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            // Redirect to home page
            FacesContext.getCurrentInstance().getExternalContext().redirect(externalContext.getRequestContextPath() + "home.xhtml");
        } catch (IOException ex) {
            System.out.println("UserSessionBean - IOException: " + ex.toString());
        }
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
