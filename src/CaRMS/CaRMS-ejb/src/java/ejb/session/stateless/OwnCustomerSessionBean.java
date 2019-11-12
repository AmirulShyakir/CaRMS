/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.OwnCustomer;
import java.util.Set;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.OwnCustomerUsernameExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author dtjldamien
 */
@Stateless
@Local(OwnCustomerSessionBeanLocal.class)
@Remote(OwnCustomerSessionBeanRemote.class)
public class OwnCustomerSessionBean implements OwnCustomerSessionBeanRemote, OwnCustomerSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public OwnCustomerSessionBean() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createNewOwnCustomer(OwnCustomer newOwnCustomer) throws OwnCustomerUsernameExistException, UnknownPersistenceException, InputDataValidationException {
        try {
            Set<ConstraintViolation<OwnCustomer>> constraintViolations = validator.validate(newOwnCustomer);

            if (constraintViolations.isEmpty()) {
                em.persist(newOwnCustomer);
                em.flush();

                return newOwnCustomer.getCustomerId();
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new OwnCustomerUsernameExistException();
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }
    
    @Override
    public OwnCustomer login(String username, String password) throws InvalidLoginCredentialException {
        try {
            Query query = em.createQuery("SELECT c FROM OwnCustomer c WHERE c.username = :inUsername");
            query.setParameter("inUsername", username);
            OwnCustomer ownCustomer = (OwnCustomer) query.getSingleResult();

            if (ownCustomer.getPassword().equals(password)) {
                return ownCustomer;
            } else {
                throw new InvalidLoginCredentialException("Invalid login credential");
            }
        } catch (NoResultException ex) {
            throw new InvalidLoginCredentialException("Invalid login credential");
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<OwnCustomer>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
   
}
