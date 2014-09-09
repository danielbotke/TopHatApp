package bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.Properties;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.brickred.socialauth.AuthProvider;
import org.brickred.socialauth.Profile;
import org.brickred.socialauth.SocialAuthConfig;
import org.brickred.socialauth.SocialAuthManager;
import org.brickred.socialauth.util.Base64.InputStream;
import org.brickred.socialauth.util.SocialAuthUtil;

@Named(value = "userSession")
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
        Properties props = System.getProperties();
        props.put("graph.facebook.com.consumer_key", "654094184666610");
        props.put("graph.facebook.com.consumer_secret", "07c5d082557431eeca769dab44a65f1b");
        // Define your custom permission if needed
        props.put("graph.facebook.com.custom_permissions", "publish_stream,email,user_birthday,user_location,offline_access");
        // Initiate required components
        SocialAuthConfig config = SocialAuthConfig.getDefault();
        config.load(props);
        manager = new SocialAuthManager();
        manager.setSocialAuthConfig(config);
        // 'successURL' is the page you'll be redirected to on successful login
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String successURL = "http://localhost:8080/" + externalContext.getRequestContextPath() + "/faces/home.xhtml";
        String authenticationURL;
        authenticationURL = manager.getAuthenticationUrl(providerID, successURL);
        FacesContext.getCurrentInstance().getExternalContext().redirect(authenticationURL);
        this.pullUserInfo();
        ExternalContext ec;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext == null) {
            ec = null;
        } else {
            ec = facesContext.getExternalContext();
        }
        if (ec != null) {
            Map attrMap = ec.getSessionMap();
            if (attrMap != null) {
                attrMap.put("userSession", this);
            }
        }
    }

    public void pullUserInfo() {
        try {
            // Pull user's data from the provider
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
            Map map = SocialAuthUtil.getRequestParametersMap(request);
            if (this.manager != null) {
                AuthProvider provider = (AuthProvider) manager.connect(map);
                this.profile = provider.getUserProfile();
                // Do what you want with the data (e.g. persist to the database, etc.)
                System.out.println("User's Social profile: " + profile);
                // Redirect the user back to where they have been before logging in
                FacesContext.getCurrentInstance().getExternalContext().redirect(originalURL);
            } else {
                FacesContext.getCurrentInstance().getExternalContext().redirect(externalContext.getRequestContextPath() + "home.xhtml");
            }
        } catch (Exception ex) {
            System.out.println("UserSession - Exception: " + ex.toString());
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

    public void teste() throws IOException {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        FacesContext.getCurrentInstance().getExternalContext().redirect(externalContext.getRequestContextPath() + "home.xhtml");
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

    /**
     * Loads the application configuration from the given input stream Format of
     * the input stream should be as follows: <br/> www.google.com.consumer_key
     * = opensource.brickred.com
     *
     * @param inputStream property file input stream which contains the
     * configuration.
     * @throws Exception
     */
    public void load(final InputStream inputStream) throws Exception {
    }

    /**
     * Loads the application configuration from the given file
     *
     * @param fileName the file name which contains the application
     * configuration properties
     * @throws Exception
     */
    public void load(final String fileName) throws Exception {
    }

    /**
     * Loads the application properties from oauth_consumer.properties file.
     *
     * @throws Exception
     */
    public void load() throws Exception {
    }

    /**
     * Loads the application configuration from the given properties
     *
     * @param properties application configuration properties
     * @throws Exception
     */
    public void load(final Properties properties) throws Exception {
    }
}