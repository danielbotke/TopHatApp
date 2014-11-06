package Mineracao;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import bean.TopHatMB;
import dao.HomeDao;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import models.IAction;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author Daniel
 */
public class ExecuteToDoAction implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("teste quartz");
        IAction act = (IAction) context.getJobDetail().getJobDataMap().get("action");
        
     //   FacesContext facesContext = FacesContext.getCurrentInstance();
       // HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        TopHatMB topHatMB = new TopHatMB();    //(TopHatMB) session.getAttribute("topHatMB");
        topHatMB.setCurrentDevice(act.getDevice());
        topHatMB.setBean(act.getDevicePopulate().getRoomPopulate().getHome());
        try {
            topHatMB.executeAction(act.getName());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExecuteToDoAction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ExecuteToDoAction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
