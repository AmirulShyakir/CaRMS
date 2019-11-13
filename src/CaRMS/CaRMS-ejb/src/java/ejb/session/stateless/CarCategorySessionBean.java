/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import entity.RentalRate;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CarCategoryExistException;
import util.exception.CarCategoryNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.NoAvailableRentalRateException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author dtjldamien
 */
@Stateless
@Local(CarCategorySessionBeanLocal.class)
@Remote(CarCategorySessionBeanRemote.class)
public class CarCategorySessionBean implements CarCategorySessionBeanRemote, CarCategorySessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    @EJB
    private RentalRateSessionBeanLocal rentalRateSessionBeanLocal;

    public CarCategorySessionBean() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createCarCategory(CarCategory newCarCategory) throws CarCategoryExistException, UnknownPersistenceException, InputDataValidationException {
        try {
            Set<ConstraintViolation<CarCategory>> constraintViolations = validator.validate(newCarCategory);

            if (constraintViolations.isEmpty()) {
                em.persist(newCarCategory);
                em.flush();

                return newCarCategory.getCarCategoryId();
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new CarCategoryExistException();
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CarCategory>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

    @Override
    public CarCategory retrieveCarCategoryByCarCategoryId(Long carCategoryId) throws CarCategoryNotFoundException {
        CarCategory carCategory = em.find(CarCategory.class, carCategoryId);

        if (carCategory != null) {
            return carCategory;
        } else {
            throw new CarCategoryNotFoundException("Car Category ID " + carCategoryId + " does not exist!");
        }
    }

    @Override
    public BigDecimal calculateTotalRentalFee(Long carCategoryId, Date pickUpDateTime, Date returnDateTime) throws CarCategoryNotFoundException, NoAvailableRentalRateException {
        BigDecimal totalRentalFee = new BigDecimal(0);

        try {
            CarCategory carCategory = retrieveCarCategoryByCarCategoryId(carCategoryId);
            LocalDateTime pickUpTemporal = pickUpDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime returnTemporal = returnDateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            Long daysToRent = ChronoUnit.DAYS.between(pickUpTemporal, returnTemporal);

            GregorianCalendar transitCalendar = new GregorianCalendar(
                    pickUpDateTime.getYear() + 1900,
                    pickUpDateTime.getMonth(),
                    pickUpDateTime.getDate(),
                    pickUpDateTime.getHours(),
                    pickUpDateTime.getMinutes(),
                    pickUpDateTime.getSeconds());

            for (int i = 0; i < daysToRent; i++) {
                RentalRate cheapestRentalRate = rentalRateSessionBeanLocal.retrieveCheapestRentalRate(carCategory, transitCalendar.getTime());
                transitCalendar.add(Calendar.DATE, 1);
                totalRentalFee = totalRentalFee.add(cheapestRentalRate.getRatePerDay());
            }
            return totalRentalFee;
        } catch (NoAvailableRentalRateException ex) {
            throw new NoAvailableRentalRateException();
        } catch (CarCategoryNotFoundException ex) {
            throw new CarCategoryNotFoundException();
        }
    }

}
