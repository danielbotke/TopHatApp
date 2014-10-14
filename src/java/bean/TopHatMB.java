/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import Mineracao.ExecuteToDoAction;
import dao.AirConditionerDao;
import dao.DeviceDao;
import dao.HistActionDao;
import dao.HomeDao;
import dao.IActionDao;
import dao.IUserDao;
import dao.ToDoActionDao;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Properties;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import models.AirConditioner;
import models.Device;
import models.HistAction;
import models.Home;
import models.IAction;
import models.IUser;
import models.Room;
import models.ToDoAction;
import org.brickred.socialauth.Profile;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
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
    private Device currentDevice = null;
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

    public Device getCurrentDevice() {
        return currentDevice;
    }

    public void setCurrentDevice(Device currentDevice) {
        this.currentDevice = currentDevice;
    }

    public boolean isValidatedUser() {
        return validatedUser;
    }

    public void setValidatedUser(boolean validatedUser) {
        this.validatedUser = validatedUser;
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
        currentDevice = d;
        act = this.executeAction(act);
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
    
    public String executeAction(String act) throws FileNotFoundException, IOException{
        String actAir = "";
                if (currentDevice.getType() == 'a') {
            Properties props = new Properties();
            File f = new File("src/komeco.properties");
            FileInputStream file = new FileInputStream("C:\\airs\\komeco.properties");
            props.load(file);
            AirConditioner a = currentDevice.getAirConditioner();
            switch (act) {
                case "temp+":
                    if (a.getTemperatura() != 30) {
                        a.setTemperatuda(currentDevice.getAirConditioner().getTemperatura() + 1);
                    }
                    actAir = "auto" + currentDevice.getAirConditioner().getTemperatura();
                    act = props.getProperty(actAir);
                    break;
                case "temp-":
                    if (a.getTemperatura() != 17) {
                        a.setTemperatuda(currentDevice.getAirConditioner().getTemperatura() - 1);
                    }
                    actAir = "auto" + currentDevice.getAirConditioner().getTemperatura();
                    act = props.getProperty(actAir);
                    break;
                case "swing":
                    actAir = "swing";
                    act = props.getProperty("swing");
                    break;
                case "a":
                    if (currentDevice.getAirConditioner().getLigado()) {
                        actAir = "turOff";
                        act = props.getProperty("turOff");
                    } else {
                        actAir = "turOn";
                        act = props.getProperty("auto" + currentDevice.getAirConditioner().getTemperatura());
                    }
                    a.setLigado(!a.getLigado());
                    break;
                default:
                    System.out.println("Action inválida");
                    break;
            }
            new AirConditionerDao().save(a);
            currentDevice.setAirConditioner(a);
        }
        try {
            URL url = new URL("http://" + bean.getIp() + "/?" + act + currentDevice.getActionPort());
            URLConnection con = url.openConnection();
            con.getContent();
        } catch (IOException e) {
        }
        if (currentDevice.getType() == 'l') {
            if (currentDevice.getStatus() == 0) {
                currentDevice.setStatusDevice(1);
            } else {
                currentDevice.setStatusDevice(0);
            }
        }
        if(act.equalsIgnoreCase("a")){
            return actAir;
        }else{
            return act;
        }
    }

    public String activateTodoAction(int actionId) {
        ToDoActionDao dao = new ToDoActionDao();
        ToDoAction toDoAction = bean.getToDoAction().get(actionId);
        toDoAction.setActivated(!toDoAction.getActivated());
        dao.save(toDoAction);
        if (bean.getToDoAction().get(actionId).getActivated()) {
            //ativar agendamento
            try {
                // Grab the Scheduler instance from the Factory
                Scheduler scheduler = Cluster.getSch();
                
                JobDataMap jdm = new JobDataMap();
                jdm.put("action", toDoAction.getAction());


                // specify the job' s details..
                JobDetail job = JobBuilder.newJob(ExecuteToDoAction.class)
                        .withIdentity(bean.getId() + "." + toDoAction.getAction().getDevice().getRoom().getName() + "." + toDoAction.getAction().getDevice().getName() + "." + toDoAction.getAction().getName() + "." + toDoAction.getDateTime())
                        .setJobData(jdm)
                        .build();

                // specify the running period of the job
                
                CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger1", "group1")
                    .withSchedule(cronSchedule("0/20 * * * * ?"))
                    .build();

                //schedule the job
                scheduler.scheduleJob(job, trigger);


            } catch (SchedulerException se) {
                se.printStackTrace();
            }
        } else if (!bean.getToDoAction().get(actionId).getActivated()) {
            //desativar agendamento
        }
        return "programation";
    }
}
