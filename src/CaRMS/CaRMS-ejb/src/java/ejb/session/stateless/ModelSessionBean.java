/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Model;
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
import util.exception.ModelNameExistException;
import util.exception.ModelNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author dtjldamien
 */
@Stateless
@Local(ModelSessionBeanLocal.class)
@Remote(ModelSessionBeanRemote.class)
public class ModelSessionBean implements ModelSessionBeanRemote, ModelSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public ModelSessionBean() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createNewModel(Model newModel) throws ModelNameExistException, UnknownPersistenceException, InputDataValidationException {
        try {
            Set<ConstraintViolation<Model>> constraintViolations = validator.validate(newModel);

            if (constraintViolations.isEmpty()) {
                em.persist(newModel);
                em.flush();

                return newModel.getModelId();
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new ModelNameExistException();
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Model>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

    @Override
    public List<Model> retrieveAllModels() {
        // should be sorted in ascending order by car category, make and model.
        Query query = em.createQuery("SELECT m FROM Model m ORDER BY m.carCategory.carCategoryName ASC");

        return query.getResultList();
    }

    @Override
    public Model retrieveModelByModelId(Long modelId) throws ModelNotFoundException {
        Model model = em.find(Model.class, modelId);

        if (model != null) {
            return model;
        } else {
            throw new ModelNotFoundException("Model ID " + modelId + " does not exist!");
        }
    }

    @Override
    public void updateModel(Model model) throws ModelNotFoundException, InputDataValidationException {
        if (model != null && model.getModelId() != null) {
            Set<ConstraintViolation<Model>> constraintViolations = validator.validate(model);

            if (constraintViolations.isEmpty()) {
                Model modelToUpdate = retrieveModelByModelId(model.getModelId());
                modelToUpdate.setMakeName(model.getMakeName());
                modelToUpdate.setModelName(model.getModelName());
                modelToUpdate.setCars(model.getCars());
                modelToUpdate.setCarCategory(model.getCarCategory());
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new ModelNotFoundException("Model Id not provided for rental rate to be updated");
        }
    }

    /*
    @Override
    public void deleteModel(Long modelId) throws ModelNotFoundException, DeleteModelException {
        Model modelToRemove = retrieveModelByModelId(modelId);

        List<Model> cars = model.retrieveSaleTransactionLineItemsByRentalDayId(rentalDayId);

        if (cars.isEmpty()) {
            em.remove(modelToRemove);
        } else {
            throw new DeleteRentalRateException("Model ID " + modelId + " is associated with cars(s) and cannot be deleted!");
        }
    }
     */
}
