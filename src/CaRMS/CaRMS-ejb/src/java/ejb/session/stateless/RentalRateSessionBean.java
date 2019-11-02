/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalRate;
import java.util.List;
import java.util.Set;
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
import util.exception.InputDataValidationException;
import util.exception.RentalRateNameExistException;
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

    public RentalRateSessionBean() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createNewPartner(RentalRate newRentalRate) throws RentalRateNameExistException, UnknownPersistenceException, InputDataValidationException {
        try {
            Set<ConstraintViolation<RentalRate>> constraintViolations = validator.validate(newRentalRate);

            if (constraintViolations.isEmpty()) {
                em.persist(newRentalRate);
                em.flush();

                return newRentalRate.getRentalRateId();
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new RentalRateNameExistException();
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
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
        Query query = em.createQuery("SELECT r FROM RentalRate r ORDER BY r.carCategory.carCategoryName ASC");

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

    /*
    @Override
    public void deleteRentalRate(Long rentalRateId) throws RentalRateNotFoundException, DeleteRentalRateException {
        RentalRate productEntityToRemove = retrieveRentalRateByRentalRateId(rentalRateId);

        List<RentalDay> rentalDays = rentalDaySessionBeanLocal.retrieveSaleTransactionLineItemsByRentalDayId(rentalDayId);

        if (rentalDays.isEmpty()) {
            em.remove(productEntityToRemove);
        } else {
            throw new DeleteRentalRateException("Product ID " + productId + " is associated with existing sale transaction line item(s) and cannot be deleted!");
        }
    }
    */
}
