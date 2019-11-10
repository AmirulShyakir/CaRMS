/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalReservation;
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
import util.exception.RentalReservationNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author dtjldamien
 */
@Stateless
@Local(RentalReservationSessionBeanLocal.class)
@Remote(RentalReservationSessionBeanRemote.class)
public class RentalReservationSessionBean implements RentalReservationSessionBeanRemote, RentalReservationSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public RentalReservationSessionBean() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createNewRentalReservation(RentalReservation newRentalReservation) throws InputDataValidationException, UnknownPersistenceException {
        try {
            Set<ConstraintViolation<RentalReservation>> constraintViolations = validator.validate(newRentalReservation);

            if (constraintViolations.isEmpty()) {
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
        Query query = em.createQuery("SELECT rr FROM RentalReservation rr");
        return query.getResultList();
    }
    
    @Override
    public void deleteReservation(Long rentalReservationId) throws RentalReservationNotFoundException {
        try {
            RentalReservation rentalReservationToRemove = retrieveRentalReservationByRentalReservationId(rentalReservationId);
            if (rentalReservationToRemove.getPaid()) {
                
                // refund credit card amount - penalty amoung
            } else {
                // charge credit card penalty amount
            }
            if (rentalReservationToRemove.getRentalDays().isEmpty()) {
                em.remove(rentalReservationToRemove);
            } else {
                rentalReservationToRemove.setIsCancelled(true);
            }
        } catch (RentalReservationNotFoundException ex) {
            throw new RentalReservationNotFoundException("Rental Reservation of ID: " + rentalReservationId + " not found!");
        }
    }
}
