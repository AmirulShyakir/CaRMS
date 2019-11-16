/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalReservation;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import util.exception.CarCategoryNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.ModelNotFoundException;
import util.exception.NoAvailableRentalRateException;
import util.exception.OutletNotFoundException;
import util.exception.PartnerNotFoundException;
import util.exception.RentalReservationNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author dtjldamien
 */
public interface RentalReservationSessionBeanRemote {

    public Long createNewRentalReservation(Long carCategoryId, Long modelId, Long customerId, Long pickupOutletId, Long returnOutletId, RentalReservation newRentalReservation) throws OutletNotFoundException, CustomerNotFoundException, InputDataValidationException, UnknownPersistenceException, CarCategoryNotFoundException, ModelNotFoundException;

    public Long createNewPartnerRentalReservation(Long carCategoryId, Long partnerId, Long modelId, Long customerId,
            Long pickupOutletId, Long returnOutletId, RentalReservation newRentalReservation)
            throws OutletNotFoundException, CustomerNotFoundException, InputDataValidationException, UnknownPersistenceException,
            CarCategoryNotFoundException, ModelNotFoundException, PartnerNotFoundException;

    public RentalReservation retrieveRentalReservationByRentalReservationId(Long rentalReservationId) throws RentalReservationNotFoundException;

    public List<RentalReservation> retrieveAllRentalReservations();

    public BigDecimal cancelReservation(Long rentalReservationId) throws RentalReservationNotFoundException;

    public void pickupCar(Long rentalReservationId) throws RentalReservationNotFoundException;

    public void returnCar(Long rentalReservationId) throws RentalReservationNotFoundException;

    public Boolean searchCarByCategory(Date pickUpDateTime, Date returnDateTime, Long pickupOutletId, Long returnOutletId, Long carCategoryId) throws NoAvailableRentalRateException, CarCategoryNotFoundException, OutletNotFoundException;

    public Boolean searchCarByModel(Date pickUpDateTime, Date returnDateTime, Long pickupOutletId, Long returnOutletId, Long modelId) throws NoAvailableRentalRateException, CarCategoryNotFoundException, OutletNotFoundException, ModelNotFoundException;

    public List<RentalReservation> retrievePartnerRentalReservations(Long partnerId);

    public List<RentalReservation> retrieveCustomerRentalReservations(Long customerId);

    public List<RentalReservation> retrieveCustomerRentalReservationsByPickupOutletId(Long outletId);

    public List<RentalReservation> retrieveCustomerRentalReservationsByReturnOutletId(Long outletId);

}
