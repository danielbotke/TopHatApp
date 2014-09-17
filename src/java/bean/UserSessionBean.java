package bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.brickred.socialauth.AuthProvider;
import org.brickred.socialauth.Profile;
import org.brickred.socialauth.SocialAuthConfig;
import org.brickred.socialauth.SocialAuthManager;
import org.brickred.socialauth.util.SocialAuthUtil;


@ManagedBean(name = "userSession")
@SessionScoped
public class UserSessionBean implements Serializable {
    
    private SocialAuthManager manager;
    private String originalURL;
    private String providerID;
    private Profile profile;
    
    public UserSessionBean() {
        //... 
    }
    
    public void socialConnect(String providerIDSelected) throws Exception {
        setProviderID(providerIDSelected);
        // Put your keys and secrets from the providers here
        SocialAuthConfig config = SocialAuthConfig.getDefault();        
        String propUrl = "oauth_consumer.properties";
        config.load(propUrl);
        manager = new SocialAuthManager();
        manager.setSocialAuthConfig(config);
        // 'successURL' is the page you'll be redirected to on successful login
        String successURL = "http://tophat.com.br/TopHatApp/faces/home.xhtml";
        String authenticationURL;
        authenticationURL = manager.getAuthenticationUrl(providerID, successURL);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("authManager", manager);        
        FacesContext.getCurrentInstance().responseComplete();
        FacesContext.getCurrentInstance().getExternalContext().redirect(authenticationURL);
        this.pullUserInfo();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userSession", this);
    }
    
    public void pullUserInfo() throws Exception {
        try {
            // Pull user's data from the provider
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
            Map map = SocialAuthUtil.getRequestParametersMap(request);
            if (this.manager != null) {
                AuthProvider provider = manager.connect(map);
                this.profile = provider.getUserProfile();
                // Do what you want with the data (e.g. persist to the database, etc.)
                System.out.println("User's Social profile: " + profile);
                // Redirect the user back to where they have been before logging in
                FacesContext.getCurrentInstance().getExternalContext().redirect(originalURL);
            } else {
                FacesContext.getCurrentInstance().getExternalContext().redirect(externalContext.getRequestContextPath() + "home.xhtml");
            }
        } catch (Exception ex) {
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
