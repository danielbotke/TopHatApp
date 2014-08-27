/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ws.tophat.home;

import dao.HomeDao;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import models.Home;

/**
 *
 * @author Daniel
 */
@WebService(serviceName = "AtualizaIp")
@Stateless()
public class AtualizaIp {

    /**
     * Operação de Web service
     */
    @Resource
    WebServiceContext wsContext;

    @WebMethod(operationName = "atualizaIp")
    public String atualizaIp(@WebParam(name = "ip") String ip, @WebParam(name = "homeId") int homeId) {
        /**
        MessageContext mc = wsContext.getMessageContext();
        HttpServletRequest req = (HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST);
        System.out.println(req.getRemoteAddr());**/
        HomeDao homeDao = new HomeDao();
        Home home = homeDao.get(homeId);
        if(home != null){
            home.setIp(ip);
        }
        //TODO write your implementation code here:
        return null;
    }
}
