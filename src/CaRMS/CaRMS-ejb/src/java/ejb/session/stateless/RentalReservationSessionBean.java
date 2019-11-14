/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.CarCategory;
import entity.Customer;
import entity.Model;
import entity.Outlet;
import entity.RentalReservation;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.CarStatusEnum;
import util.exception.CarCategoryNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.ModelNotFoundException;
import util.exception.NoAvailableRentalRateException;
import util.exception.OutletNotFoundException;
import util.exception.RentalReservationNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author sw_be
 */
@Stateless
@Local(RentalReservationSessionBeanLocal.class)
@Remote(RentalReservationSessionBeanRemote.class)
public class RentalReservationSessionBean implements RentalReservationSessionBeanRemote, RentalReservationSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;
    @EJB
    private OutletSessionBeanLocal outletSessionBeanLocal;
    @EJB
    private CarCategorySessionBeanLocal carCategorySessionBeanLocal;
    @EJB
    private ModelSessionBeanLocal modelSessionBeanLocal;

    public RentalReservationSessionBean() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createNewRentalReservation(Long carCategoryId, Long modelId, Long customerId,
            Long pickupOutletId, Long returnOutletId, RentalReservation newRentalReservation)
            throws OutletNotFoundException, CustomerNotFoundException, InputDataValidationException, UnknownPersistenceException,
            CarCategoryNotFoundException, ModelNotFoundException {
        try {
            Set<ConstraintViolation<RentalReservation>> constraintViolations = validator.validate(newRentalReservation);

            if (constraintViolations.isEmpty()) {
                Customer customer = customerSessionBeanLocal.retrieveCustomerByCustomerId(customerId);
                Outlet pickupOutlet = outletSessionBeanLocal.retrieveOutletByOutletId(pickupOutletId);
                Outlet returnOutlet = outletSessionBeanLocal.retrieveOutletByOutletId(returnOutletId);
                newRentalReservation.setCustomer(customer);
                newRentalReservation.setPickupOutlet(pickupOutlet);
                newRentalReservation.setReturnOutlet(returnOutlet);
                customer.addRentalReservation(newRentalReservation);
                CarCategory carCategory = null;
                Model model = null;
                if (carCategoryId > -1) {
                    carCategory = carCategorySessionBeanLocal.retrieveCarCategoryByCarCategoryId(carCategoryId);
                    newRentalReservation.setCarCategory(carCategory);
                } else {
                    model = modelSessionBeanLocal.retrieveModelByModelId(modelId);
                    newRentalReservation.setModel(model);
                }
                em.persist(newRentalReservation);
                em.flush();
                return newRentalReservation.getRentalReservationId();
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                throw new UnknownPersistenceException(ex.getMessage());
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        } catch (OutletNotFoundException ex) {
            throw new OutletNotFoundException("Outlet IDs: " + pickupOutletId + " and " + returnOutletId + " either or both does not exist!");
        } catch (CustomerNotFoundException ex) {
            throw new CustomerNotFoundException("Customer ID: " + customerId + " does not exist!");
        } catch (CarCategoryNotFoundException ex) {
            throw new CarCategoryNotFoundException("Car Category ID: " + carCategoryId + " does not exist!");
        } catch (ModelNotFoundException ex) {
            throw new ModelNotFoundException("Model ID: " + modelId + " does not exist!");
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<RentalReservation>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        return msg;
    }

    @Override
    public RentalReservation retrieveRentalReservationByRentalReservationId(Long rentalReservationId) throws RentalReservationNotFoundException {
        RentalReservation carCategory = em.find(RentalReservation.class, rentalReservationId);

        if (carCategory != null) {
            return carCategory;
        } else {
            throw new RentalReservationNotFoundException("Car Category ID " + rentalReservationId + " does not exist!");
        }
    }

    @Override
    public List<RentalReservation> retrieveAllRentalReservations() {
        Query query = em.createQuery("SELECT r FROM RentalReservation r");
        return query.getResultList();
    }

    @Override
    public BigDecimal cancelReservation(Long rentalReservationId) throws RentalReservationNotFoundException {
        try {
            RentalReservation rentalReservation = retrieveRentalReservationByRentalReservationId(rentalReservationId);
            rentalReservation.setIsCancelled(Boolean.TRUE);
            LocalDateTime startDateTemporal = rentalReservation.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime today = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            Long noOfDaysToStartReservation = ChronoUnit.DAYS.between(today, startDateTemporal);
            BigDecimal price = rentalReservation.getPrice();
            BigDecimal penalty = null;

            if (noOfDaysToStartReservation >= 14) {
                penalty = new BigDecimal(0);
            } else if (noOfDaysToStartReservation < 14 && noOfDaysToStartReservation >= 7) {
                penalty = price.multiply(new BigDecimal(0.2));
            } else if (noOfDaysToStartReservation < 7 && noOfDaysToStartReservation >= 3) {
                penalty = price.multiply(new BigDecimal(0.5));
            } else if (noOfDaysToStartReservation < 3) {
                penalty = price.multiply(new BigDecimal(0.7));
            }

            return penalty;

        } catch (RentalReservationNotFoundException ex) {
            throw new RentalReservationNotFoundException("Rental Reservation of ID: " + rentalReservationId + " not found!");
        }

    }

    @Override
    public void pickupCar(Long rentalReservationId) throws RentalReservationNotFoundException {
        try {
            RentalReservation rentalReservation = retrieveRentalReservationByRentalReservationId(rentalReservationId);
            Car car = rentalReservation.getCar();
            car.setCarStatus(CarStatusEnum.ON_RENT);
            car.setOutlet(null);
            car.setRentalReservation(rentalReservation);
            rentalReservation.getPickupOutlet().removeCar(car);
        } catch (RentalReservationNotFoundException ex) {
            throw new RentalReservationNotFoundException("Rental Reservation ID: " + rentalReservationId + "not found!");
        }
    }

    @Override
    public void returnCar(Long rentalReservationId) throws RentalReservationNotFoundException {
        try {
            RentalReservation rentalReservation = retrieveRentalReservationByRentalReservationId(rentalReservationId);
            Outlet returnOutlet = rentalReservation.getReturnOutlet();
            Car car = rentalReservation.getCar();
            car.setCarStatus(CarStatusEnum.AVAILABLE);
            car.setOutlet(returnOutlet);
            car.setRentalReservation(null);
            returnOutlet.addCar(car);
        } catch (RentalReservationNotFoundException ex) {
            throw new RentalReservationNotFoundException("Rental Reservation ID: " + rentalReservationId + "not found!");
        }
    }

    @Override
    public Boolean searchCarByCategory(Date pickUpDateTime, Date returnDateTime, Long pickupOutletId, Long returnOutletId, Long carCategoryId) throws NoAvailableRentalRateException, CarCategoryNotFoundException, OutletNotFoundException {
        List<RentalReservation> rentalReservations = new ArrayList<>();

        Query query = em.createQuery("SELECT r FROM RentalReservation r WHERE r.carCategory.carCategoryId = :inCategoryId"
                + " AND r.startDate < :inPickupDate AND r.endDate <= :inReturnDate"
                + " AND r.isCancelled = FALSE");
        query.setParameter("inCategoryId", carCategoryId);
        query.setParameter("inPickupDate", pickUpDateTime);
        query.setParameter("inReturnDate", returnDateTime);
        rentalReservations.addAll(query.getResultList());

        query = em.createQuery("SELECT r FROM RentalReservation r WHERE r.carCategory.carCategoryId = :inCategoryId"
                + " AND r.startDate >= :inPickupDate AND r.endDate <= :inReturnDate"
                + " AND r.isCancelled = FALSE");
        query.setParameter("inCategoryId", carCategoryId);
        query.setParameter("inPickupDate", pickUpDateTime);
        query.setParameter("inReturnDate", returnDateTime);
        rentalReservations.addAll(query.getResultList());
        System.out.println("1 - query.getResultList() : " + query.getResultList());

        query = em.createQuery("SELECT r FROM RentalReservation r WHERE r.carCategory.carCategoryId = :inCategoryId"
                + " AND r.startDate >= :inPickupDate AND r.endDate > :inReturnDate"
                + " AND r.isCancelled = FALSE");
        query.setParameter("inCategoryId", carCategoryId);
        query.setParameter("inPickupDate", pickUpDateTime);
        query.setParameter("inReturnDate", returnDateTime);
        rentalReservations.addAll(query.getResultList());
        System.out.println("2 - query.getResultList() : " + query.getResultList());

        query = em.createQuery("SELECT r FROM RentalReservation r WHERE r.carCategory.carCategoryId = :inCategoryId"
                + " AND r.startDate <= :inPickupDate AND r.endDate >= :inReturnDate"
                + " AND r.isCancelled = FALSE");
        query.setParameter("inCategoryId", carCategoryId);
        query.setParameter("inPickupDate", pickUpDateTime);
        query.setParameter("inReturnDate", returnDateTime);
        rentalReservations.addAll(query.getResultList());
        System.out.println("3 - query.getResultList() : " + query.getResultList());

        GregorianCalendar calendar = new GregorianCalendar(pickUpDateTime.getYear() + 1900,
                pickUpDateTime.getMonth(), pickUpDateTime.getDate(), pickUpDateTime.getHours(),
                pickUpDateTime.getMinutes(), pickUpDateTime.getSeconds());
        calendar.add(Calendar.HOUR, -2);
        Date transitDate = calendar.getTime();

        query = em.createQuery("SELECT r FROM RentalReservation r WHERE r.carCategory.carCategoryId = :inCategoryId"
                + " AND r.startDate < :inPickupDate AND r.endDate > :inTransitDate"
                + " AND r.returnOutlet.outletId <> :inPickupOutletId"
                + " AND r.isCancelled = FALSE");
        query.setParameter("inCategoryId", carCategoryId);
        query.setParameter("inPickupDate", pickUpDateTime);
        query.setParameter("inTransitDate", transitDate);
        query.setParameter("inPickupOutletId", pickupOutletId);
        rentalReservations.addAll(query.getResultList());
        System.out.println("4 - query.getResultList() : " + query.getResultList());

        CarCategory carCategory = carCategorySessionBeanLocal.retrieveCarCategoryByCarCategoryId(carCategoryId);
        List<Car> cars = new ArrayList<>();
        for (Model model : carCategory.getModels()) {
            cars.addAll(model.getCars());
        }
        System.out.println("cars : " + cars.size());
        System.out.println("rentalReservations.size() : " + rentalReservations.size());

        if (cars.size() > rentalReservations.size()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean searchCarByModel(Date pickUpDateTime, Date returnDateTime, Long pickupOutletId, Long returnOutletId, Long modelId) throws NoAvailableRentalRateException, CarCategoryNotFoundException, OutletNotFoundException, ModelNotFoundException {
        List<RentalReservation> rentalReservations = new ArrayList<>();

        Query query = em.createQuery("SELECT r FROM RentalReservation r WHERE r.model.modelId = :inModelId"
                + " AND r.startDate < :inPickupDate AND r.endDate <= :inReturnDate"
                + " AND r.isCancelled = FALSE");
        query.setParameter("inModelId", modelId);
        query.setParameter("inPickupDate", pickUpDateTime);
        query.setParameter("inReturnDate", returnDateTime);
        rentalReservations.addAll(query.getResultList());

        query = em.createQuery("SELECT r FROM RentalReservation r WHERE r.model.modelId = :inModelId"
                + " AND r.startDate >= :inPickupDate AND r.endDate <= :inReturnDate"
                + " AND r.isCancelled = FALSE");
        query.setParameter("inModelId", modelId);
        query.setParameter("inPickupDate", pickUpDateTime);
        query.setParameter("inReturnDate", returnDateTime);
        rentalReservations.addAll(query.getResultList());

        query = em.createQuery("SELECT r FROM RentalReservation r WHERE r.model.modelId = :inModelId"
                + " AND r.startDate >= :inPickupDate AND r.endDate > :inReturnDate"
                + " AND r.isCancelled = FALSE");
        query.setParameter("inModelId", modelId);
        query.setParameter("inPickupDate", pickUpDateTime);
        query.setParameter("inReturnDate", returnDateTime);
        rentalReservations.addAll(query.getResultList());

        query = em.createQuery("SELECT r FROM RentalReservation r WHERE r.model.modelId = :inModelId"
                + " AND r.startDate <= :inPickupDate AND r.endDate >= :inReturnDate"
                + " AND r.isCancelled = FALSE");
        query.setParameter("inModelId", modelId);
        query.setParameter("inPickupDate", pickUpDateTime);
        query.setParameter("inReturnDate", returnDateTime);
        rentalReservations.addAll(query.getResultList());

        GregorianCalendar calendar = new GregorianCalendar(pickUpDateTime.getYear() + 1900,
                pickUpDateTime.getMonth(), pickUpDateTime.getDate(), pickUpDateTime.getHours(),
                pickUpDateTime.getMinutes(), pickUpDateTime.getSeconds());
        calendar.add(Calendar.HOUR, -2);
        Date transitDate = calendar.getTime();

        query = em.createQuery("SELECT r FROM RentalReservation r WHERE r.model.modelId = :inModelId"
                + " AND r.startDate < :inPickupDate AND r.endDate > :inTransitDate"
                + " AND r.returnOutlet.outletId <> :inPickupOutletId"
                + " AND r.isCancelled = FALSE");
        query.setParameter("inModelId", modelId);
        query.setParameter("inPickupDate", pickUpDateTime);
        query.setParameter("inTransitDate", transitDate);
        query.setParameter("inPickupOutletId", pickupOutletId);
        rentalReservations.addAll(query.getResultList());

        Model model = modelSessionBeanLocal.retrieveModelByModelId(modelId);
        if (model.getCars().size() > rentalReservations.size()) {
            return true;
        } else {
            return false;
        }
    }
}
