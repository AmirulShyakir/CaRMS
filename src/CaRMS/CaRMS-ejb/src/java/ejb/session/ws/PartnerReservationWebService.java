/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.CarSessionBeanLocal;
import ejb.session.stateless.RentalReservationSessionBeanLocal;
import entity.Car;
import entity.RentalReservation;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarNotFoundException;
import util.exception.ModelNotFoundException;
import util.exception.OutletNotFoundException;
import util.exception.RentalReservationNotFoundException;

/**
 *
 * @author sw_be
 */
@WebService(serviceName = "PartnerReservationWebService")
@Stateless()
public class PartnerReservationWebService {

    @EJB
    private CarSessionBeanLocal carSessionBean;
    @EJB
    private RentalReservationSessionBeanLocal rentalReservationSessionBean;

    @WebMethod
    public RentalReservation retrieveRentalReservationByRentalReservationId(@WebParam Long rentalReservationId) throws RentalReservationNotFoundException {
        return rentalReservationSessionBean.retrieveRentalReservationByRentalReservationId(rentalReservationId);
    }

    @WebMethod
    public List<RentalReservation> retrieveAllRentalReservations() {
        return rentalReservationSessionBean.retrieveAllRentalReservations();
    }

    @WebMethod
    public void cancelReservation(@WebParam Long rentalReservationId) throws RentalReservationNotFoundException {
        rentalReservationSessionBean.cancelReservation(rentalReservationId);
    }

}
