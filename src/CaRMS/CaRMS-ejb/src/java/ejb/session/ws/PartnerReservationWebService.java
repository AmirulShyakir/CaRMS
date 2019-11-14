/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.RentalReservationSessionBeanRemote;
import entity.CarCategory;
import entity.Model;
import entity.Outlet;
import entity.Partner;
import entity.RentalReservation;
import java.math.BigDecimal;
import java.util.Date;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import util.exception.CarCategoryNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ModelNotFoundException;
import util.exception.NoAvailableRentalRateException;
import util.exception.OutletNotFoundException;
import util.exception.PartnerNameExistException;
import util.exception.PartnerNotFoundException;
import util.exception.RentalReservationNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author sw_be
 */
@WebService(serviceName = "PartnerReservationWebService")
@Stateless()
public class PartnerReservationWebService {
    
    @EJB
    private PartnerSessionBeanLocal partnerSessionBean;
    @EJB
    private OutletSessionBeanLocal outletSessionBeanLocal;
    @EJB
    private CarCategorySessionBeanRemote carCategorySessionBeanRemote;
    @EJB
    private ModelSessionBeanRemote modelSessionBeanRemote;
    @EJB
    private RentalReservationSessionBeanRemote rentalReservationSessionBeanRemote;

    @WebMethod
    public Long createNewPartner(@WebParam Partner newPartner) throws PartnerNameExistException, UnknownPersistenceException, InputDataValidationException {
        return partnerSessionBean.createNewPartner(newPartner);
    }
    
    @WebMethod
    public Partner partnerLogin(@WebParam String partnerName, @WebParam String password) throws InvalidLoginCredentialException {
        return partnerSessionBean.partnerLogin(partnerName, password);
    }
    
    @WebMethod
    public Partner retrievePartnerByPartnerId(@WebParam Long partnerId) throws PartnerNotFoundException {
        return partnerSessionBean.retrievePartnerByPartnerId(partnerId);
    }
    
    @WebMethod
    public Outlet retrieveOutletByOutletId(@WebParam Long outletId) throws OutletNotFoundException {
        return outletSessionBeanLocal.retrieveOutletByOutletId(outletId);
    }
    
    @WebMethod
    public CarCategory retrieveCarCategoryByCarCategoryId(@WebParam Long carCategoryId) throws CarCategoryNotFoundException {
        return carCategorySessionBeanRemote.retrieveCarCategoryByCarCategoryId(carCategoryId);
    }
    
    @WebMethod
    public Model retrieveModelByModelId(Long modelId) throws ModelNotFoundException {
        return modelSessionBeanRemote.retrieveModelByModelId(modelId);
    }
    
    @WebMethod
    public BigDecimal calculateTotalRentalFee(@WebParam Long carCategoryId, @WebParam Date pickUpDateTime, @WebParam Date returnDateTime) throws CarCategoryNotFoundException, NoAvailableRentalRateException {
        return carCategorySessionBeanRemote.calculateTotalRentalFee(carCategoryId, pickUpDateTime, returnDateTime);
    }
    
    @WebMethod
    public RentalReservation retrieveRentalReservationByRentalReservationId(@WebParam Long rentalReservationId) throws RentalReservationNotFoundException {
        return rentalReservationSessionBeanRemote.retrieveRentalReservationByRentalReservationId(rentalReservationId);
    }
    
    @WebMethod
    public Long createNewRentalReservation(@WebParam RentalReservation newRentalReservation) throws InputDataValidationException, UnknownPersistenceException {
        return rentalReservationSessionBeanRemote.createNewRentalReservation(newRentalReservation);
    }
    
    @WebMethod
    public BigDecimal cancelReservation(@WebParam Long rentalReservationId) throws RentalReservationNotFoundException {
        return rentalReservationSessionBeanRemote.cancelReservation(rentalReservationId);
    }
    
    @WebMethod
    public Boolean searchCarByCategory(@WebParam Date pickUpDateTime, @WebParam Date returnDateTime, @WebParam Long pickupOutletId, @WebParam Long returnOutletId, @WebParam Long carCategoryId) throws NoAvailableRentalRateException, CarCategoryNotFoundException, OutletNotFoundException {
        return rentalReservationSessionBeanRemote.searchCarByCategory(pickUpDateTime, returnDateTime, pickupOutletId, returnOutletId, carCategoryId);
    }
    
    @WebMethod
    public Boolean searchCarByModel(@WebParam Date pickUpDateTime, @WebParam Date returnDateTime, @WebParam Long pickupOutletId, @WebParam Long returnOutletId, @WebParam Long modelId) throws NoAvailableRentalRateException, CarCategoryNotFoundException, OutletNotFoundException, ModelNotFoundException {
        return rentalReservationSessionBeanRemote.searchCarByModel(pickUpDateTime, returnDateTime, pickupOutletId, returnOutletId, modelId);
    }
    
}
