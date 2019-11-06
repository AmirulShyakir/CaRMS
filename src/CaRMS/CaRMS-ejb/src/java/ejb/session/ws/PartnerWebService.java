/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.PartnerSessionBeanLocal;
import entity.Car;
import entity.Partner;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author dtjldamien
 */
@WebService(serviceName = "PartnerWebService")
@Stateless
public class PartnerWebService {
    
    @EJB
    private PartnerSessionBeanLocal partnerSessionBeanLocal;

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "partnerLogin")
    public Partner partnerLogin(@WebParam(name = "partnerName") String partnerName,
                                @WebParam(name = "password") String password)
            throws InvalidLoginCredentialException {
        Partner partner = partnerSessionBeanLocal.login(partnerName, password);
        System.out.println("********** PartnerWebService.login(): Partner \n"
                            + partner.getPartnerName()
                            + " login remotely via web service\n");
        return partner;
    }

    @WebMethod(operationName = "partnerSearchCar")
    public String partnerSearchCar(@WebParam(name = "partnerSearchCar") String txt) {
        return "Hello " + txt + " !";
    }

    @WebMethod(operationName = "partnerReserveCar")
    public String partnerReserveCar(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }

    @WebMethod(operationName = "partnerCancelReservation")
    public String partnerCancelReservation(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }

    @WebMethod(operationName = "partnerViewReservationDetails")
    public String partnerViewReservationDetails(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }

    @WebMethod(operationName = "partnerViewAllReservations")
    public String partnerViewAllReservations(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }
}
