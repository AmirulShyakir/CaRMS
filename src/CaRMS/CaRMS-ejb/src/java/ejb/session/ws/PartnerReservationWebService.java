/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.CarCategorySessionBeanLocal;
import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.ModelSessionBeanLocal;
import ejb.session.stateless.OutletSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.RentalReservationSessionBeanLocal;
import entity.CarCategory;
import entity.Customer;
import entity.Model;
import entity.Outlet;
import entity.Partner;
import entity.RentalReservation;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import util.exception.CarCategoryNotFoundException;
import util.exception.CustomerNotFoundException;
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
    private PartnerSessionBeanLocal partnerSessionBeanLocal;
    @EJB
    private OutletSessionBeanLocal outletSessionBeanLocal;
    @EJB
    private CarCategorySessionBeanLocal carCategorySessionBeanLocal;
    @EJB
    private ModelSessionBeanLocal modelSessionBeanLocal;
    @EJB
    private RentalReservationSessionBeanLocal rentalReservationSessionBeanLocal;
    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;
    
    @WebMethod
    public Long createNewCustomer(Long partnerId, Customer newCustomer) throws PartnerNotFoundException, UnknownPersistenceException, InputDataValidationException {
        return customerSessionBeanLocal.createNewCustomer(partnerId, newCustomer);
    }

    @WebMethod
    public Long createNewPartner(@WebParam Partner newPartner) throws PartnerNameExistException, UnknownPersistenceException, InputDataValidationException {
        return partnerSessionBeanLocal.createNewPartner(newPartner);
    }
    
    @WebMethod
    public Long partnerLogin(@WebParam String partnerName, @WebParam String password) throws InvalidLoginCredentialException {
        return partnerSessionBeanLocal.partnerLogin(partnerName, password);
    }
    
    @WebMethod
    public Partner retrievePartnerByPartnerId(@WebParam Long partnerId) throws PartnerNotFoundException {
        return partnerSessionBeanLocal.retrievePartnerByPartnerId(partnerId);
    }
    
    @WebMethod
    public Outlet retrieveOutletByOutletId(@WebParam Long outletId) throws OutletNotFoundException {
        return outletSessionBeanLocal.retrieveOutletByOutletId(outletId);
    }
    
    @WebMethod
    public CarCategory retrieveCarCategoryByCarCategoryId(@WebParam Long carCategoryId) throws CarCategoryNotFoundException {
        return carCategorySessionBeanLocal.retrieveCarCategoryByCarCategoryId(carCategoryId);
    }
    
    @WebMethod
    public Model retrieveModelByModelId(Long modelId) throws ModelNotFoundException {
        return modelSessionBeanLocal.retrieveModelByModelId(modelId);
    }
    
    @WebMethod
    public BigDecimal calculateTotalRentalFee(@WebParam Long carCategoryId, @WebParam Date pickUpDateTime, @WebParam Date returnDateTime) throws CarCategoryNotFoundException, NoAvailableRentalRateException {
        return carCategorySessionBeanLocal.calculateTotalRentalFee(carCategoryId, pickUpDateTime, returnDateTime);
    }
    
    @WebMethod
    public RentalReservation retrieveRentalReservationByRentalReservationId(@WebParam Long rentalReservationId) throws RentalReservationNotFoundException {
        return rentalReservationSessionBeanLocal.retrieveRentalReservationByRentalReservationId(rentalReservationId);
    }
    
    @WebMethod
    public List<RentalReservation> retrieveAllRentalReservations() {
        return rentalReservationSessionBeanLocal.retrieveAllRentalReservations();
    }
    
    @WebMethod
    public Long createNewRentalReservation(Long carCategoryId, Long modelId, Long customerId,
            Long pickupOutletId, Long returnOutletId, RentalReservation newRentalReservation)
            throws OutletNotFoundException, CustomerNotFoundException, InputDataValidationException, UnknownPersistenceException,
            CarCategoryNotFoundException, ModelNotFoundException {
        return rentalReservationSessionBeanLocal.createNewRentalReservation(carCategoryId, modelId, customerId, pickupOutletId, returnOutletId, newRentalReservation);
    }
    
    @WebMethod
    public BigDecimal cancelReservation(@WebParam Long rentalReservationId) throws RentalReservationNotFoundException {
        return rentalReservationSessionBeanLocal.cancelReservation(rentalReservationId);
    }
    
    @WebMethod
    public Boolean searchCarByCategory(@WebParam Date pickUpDateTime, @WebParam Date returnDateTime, @WebParam Long pickupOutletId, @WebParam Long returnOutletId, @WebParam Long carCategoryId) throws NoAvailableRentalRateException, CarCategoryNotFoundException, OutletNotFoundException {
        return rentalReservationSessionBeanLocal.searchCarByCategory(pickUpDateTime, returnDateTime, pickupOutletId, returnOutletId, carCategoryId);
    }
    
    @WebMethod
    public Boolean searchCarByModel(@WebParam Date pickUpDateTime, @WebParam Date returnDateTime, @WebParam Long pickupOutletId, @WebParam Long returnOutletId, @WebParam Long modelId) throws NoAvailableRentalRateException, CarCategoryNotFoundException, OutletNotFoundException, ModelNotFoundException {
        return rentalReservationSessionBeanLocal.searchCarByModel(pickUpDateTime, returnDateTime, pickupOutletId, returnOutletId, modelId);
    }
    
}
