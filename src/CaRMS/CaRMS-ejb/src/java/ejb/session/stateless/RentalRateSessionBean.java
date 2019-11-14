/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import entity.RentalRate;
import java.util.Date;
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
import util.exception.CarCategoryNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.NoAvailableRentalRateException;
import util.exception.RentalRateNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author dtjldamien
 */
@Stateless
@Local(RentalRateSessionBeanLocal.class)
@Remote(RentalRateSessionBeanRemote.class)
public class RentalRateSessionBean implements RentalRateSessionBeanRemote, RentalRateSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    @EJB
    private CarCategorySessionBeanLocal carCategorySessionBeanLocal;

    public RentalRateSessionBean() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createNewRentalRate(Long carCategoryId, RentalRate newRentalRate) throws CarCategoryNotFoundException, UnknownPersistenceException, InputDataValidationException {
        try {
            Set<ConstraintViolation<RentalRate>> constraintViolations = validator.validate(newRentalRate);

            if (constraintViolations.isEmpty()) {
                try {
                    CarCategory carCategory = carCategorySessionBeanLocal.retrieveCarCategoryByCarCategoryId(carCategoryId);
                    carCategory.addRentalRate(newRentalRate);
                    newRentalRate.setCarCategory(carCategory);
                    em.persist(newRentalRate);
                    em.flush();
                    return newRentalRate.getRentalRateId();
                } catch (CarCategoryNotFoundException ex) {
                    throw new CarCategoryNotFoundException("Car Category ID: " + carCategoryId + " not found!");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                throw new UnknownPersistenceException(ex.getMessage());
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<RentalRate>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

    @Override
    public List<RentalRate> retrieveAllRentalRates() {
        // should be sorted in ascending order by car category and validity period
        //        Query query = em.createQuery("SELECT r FROM RentalRate r WHERE ((r.startDate IS NULL AND r.endDate IS NULL) OR (r.startDate <= :inDate AND r.endDate >= :inDate)) ORDER BY r.carCategory.carCategoryName ASC, r.startDate ASC");
        // Query query = em.createQuery("SELECT r FROM RentalRate r WHERE ((r.startDate IS NULL AND r.endDate IS NULL) OR (r.startDate <= :inDate AND r.endDate >= :inDate)) ORDER BY r.carCategory.carCategoryName ASC");
        Query query = em.createQuery("SELECT r FROM RentalRate r ORDER BY r.carCategory.carCategoryName, r.startDate, r.endDate ASC");
        return query.getResultList();
    }

    @Override
    public RentalRate retrieveRentalRateByRentalRateId(Long rentalRateId) throws RentalRateNotFoundException {
        RentalRate rentalRate = em.find(RentalRate.class, rentalRateId);

        if (rentalRate != null) {
            return rentalRate;
        } else {
            throw new RentalRateNotFoundException("Rental Rate ID " + rentalRateId + " does not exist!");
        }
    }

    @Override
    public void updateRentalRate(RentalRate rentalRate) throws RentalRateNotFoundException, InputDataValidationException {
        if (rentalRate != null && rentalRate.getRentalRateId() != null) {
            Set<ConstraintViolation<RentalRate>> constraintViolations = validator.validate(rentalRate);

            if (constraintViolations.isEmpty()) {
                RentalRate rentalRateToUpdate = retrieveRentalRateByRentalRateId(rentalRate.getRentalRateId());
                rentalRateToUpdate.setRentalRateName(rentalRate.getRentalRateName());
                rentalRateToUpdate.setRatePerDay(rentalRate.getRatePerDay());
                rentalRateToUpdate.setStartDate(rentalRate.getStartDate());
                rentalRateToUpdate.setEndDate(rentalRate.getEndDate());
                rentalRateToUpdate.setCarCategory(rentalRate.getCarCategory());
                rentalRateToUpdate.setRentalDays(rentalRate.getRentalDays());
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new RentalRateNotFoundException("Rental Rate Id not provided for rental rate to be updated");
        }
    }

    @Override
    public void deleteRentalRate(Long rentalRateId) throws RentalRateNotFoundException {
        try {
            RentalRate rentalRateToRemove = retrieveRentalRateByRentalRateId(rentalRateId);
            if (rentalRateToRemove.getRentalDays().isEmpty()) {
                em.remove(rentalRateToRemove);
            } else {
                rentalRateToRemove.setIsEnabled(false);
            }
        } catch (RentalRateNotFoundException ex) {
            throw new RentalRateNotFoundException("Rental rate of ID: " + rentalRateId + " not found!");
        }
    }

    @Override
    public RentalRate retrieveCheapestRentalRate(CarCategory carcategory, Date currentCheckedDate) throws NoAvailableRentalRateException {
        Query query = em.createQuery("SELECT r FROM RentalRate r WHERE r.startDate >= :inCurrentCheckedDate AND r.endDate <= :inCurrentCheckedDate ORDER BY r.ratePerDay ASC");
        query.setParameter("inCurrentCheckedDate", currentCheckedDate);
        List<RentalRate> rentalRates = query.getResultList();
        if (rentalRates.isEmpty()) {
            throw new NoAvailableRentalRateException();
        }
        return (RentalRate) query.getSingleResult();
    }

}
