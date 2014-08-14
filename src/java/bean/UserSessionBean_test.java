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
import javax.servlet.http.HttpSession;
import org.brickred.socialauth.AuthProvider;
import org.brickred.socialauth.Profile;
import org.brickred.socialauth.SocialAuthConfig;
import org.brickred.socialauth.SocialAuthManager;
import org.brickred.socialauth.util.Base64.InputStream;
import org.brickred.socialauth.util.SocialAuthUtil;

@Named(value = "userSession_test")
@SessionScoped
public class UserSessionBean_test implements Serializable {

    private SocialAuthManager manager;
    private String originalURL;
    private String providerID;
    private Profile profile;

    public UserSessionBean_test() {
        //... 
    }

    public void socialConnect() throws Exception {
        //Create an instance of SocialAuthConfgi object
        SocialAuthConfig config = SocialAuthConfig.getDefault();

        //load configuration. By default load the configuration from oauth_consumer.properties. 
        //You can also pass input stream, properties object or properties file name.
        config.load();

        //Create an instance of SocialAuthManager and set config
        SocialAuthManager manager = new SocialAuthManager();
        manager.setSocialAuthConfig(config);

        // URL of YOUR application which will be called after authentication
        String successUrl = "http://opensource.brickred.com/socialauthdemo/socialAuthSuccessAction.do";

        // get Provider URL to which you should redirect for authentication.
        // id can have values "facebook", "twitter", "yahoo" etc. or the OpenID URL
        String url = manager.getAuthenticationUrl("facebook", successUrl);

        // Store in session
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
        session.setAttribute("authManager", manager);
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