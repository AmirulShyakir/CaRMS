/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Model;
import entity.Outlet;
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
import util.exception.CarNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.LicensePlateExistException;
import util.exception.ModelDisabledException;
import util.exception.ModelNotFoundException;
import util.exception.OutletNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author sw_be
 */
@Stateless
@Local(CarSessionBeanLocal.class)
@Remote(CarSessionBeanRemote.class)
public class CarSessionBean implements CarSessionBeanRemote, CarSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    @EJB
    private OutletSessionBeanLocal outletSessionBeanLocal;
    @EJB
    private ModelSessionBeanLocal modelSessionBeanLocal;

    public CarSessionBean() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        this.validator = validatorFactory.getValidator();
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createNewCar(Long modelId, Long outletId, Car newCar) throws ModelDisabledException, ModelNotFoundException, OutletNotFoundException, LicensePlateExistException, UnknownPersistenceException, InputDataValidationException {
        try {
            Set<ConstraintViolation<Car>> constraintViolations = validator.validate(newCar);

            if (constraintViolations.isEmpty()) {
                try {
                    Outlet outlet = outletSessionBeanLocal.retrieveOutletByOutletId(outletId);
                    Model model = modelSessionBeanLocal.retrieveModelByModelId(modelId);
                    if (model.getIsEnabled()) {
                        newCar.setModel(model);
                        newCar.setOutlet(outlet);
                        outlet.addCar(newCar);
                        model.addCar(newCar);
                        em.persist(newCar);
                        em.flush();
                        return newCar.getCarId();
                    } else {
                        throw new ModelDisabledException();
                    }
                } catch (ModelNotFoundException ex) {
                    throw new ModelNotFoundException("Model Not Found for ID: " + modelId);
                } catch (OutletNotFoundException ex) {
                    throw new OutletNotFoundException("Outlet Not Found for ID: " + outletId);
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new LicensePlateExistException("License Plate: " + newCar.getLicensePlate() + " exists!");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Car>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

    @Override
    public List<Car> retrieveAllCars() {
        // should be sorted in ascending order by car category, make and model.
        Query query = em.createQuery("SELECT c FROM Car c ORDER BY c.model.carCategory.carCategoryName, c.model.makeName, c.model.modelName, c.licensePlate ASC");

        return query.getResultList();
    }

    @Override
    public Car retrieveCarByCarId(Long carId) throws CarNotFoundException {
        Car car = em.find(Car.class, carId);

        if (car != null) {
            return car;
        } else {
            throw new CarNotFoundException("Car ID " + carId + " does not exist!");
        }
    }

    @Override
    public List<Car> retrieveCarsByModelId(Long modelId) {
        Query query = em.createQuery("SELECT c FROM Car c WHERE c.model.modelId = :inModelId");
        query.setParameter("inModelId", modelId);
        return query.getResultList();
    }

    @Override
    public List<Car> retrieveCarsByCarCategoryId(Long carCategoryId) {
        Query query = em.createQuery("SELECT c FROM Car c WHERE c.model.carCategory.carCategoryId = :inCarCategoryId");
        query.setParameter("inCarCategoryId", carCategoryId);
        return query.getResultList();
    }

    @Override
    public void updateCar(Car car) throws CarNotFoundException, InputDataValidationException {
        if (car != null && car.getCarId() != null) {
            Set<ConstraintViolation<Car>> constraintViolations = validator.validate(car);

            if (constraintViolations.isEmpty()) {
                Car carToUpdate = retrieveCarByCarId(car.getCarId());
                carToUpdate.setLicensePlate(car.getLicensePlate());
                carToUpdate.setColour(car.getColour());
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new CarNotFoundException("Model Id not provided for rental rate to be updated");
        }
    }

    @Override
    public void deleteCar(Long carId) throws CarNotFoundException {
        Car carToRemove = retrieveCarByCarId(carId);

        if (carToRemove.getRentalReservation() == null) {
            em.remove(carToRemove);
        } else {
            carToRemove.setOutlet(null);
            carToRemove.setIsDisabled(true);
        }
    }

    @Override
    public List<Car> retrieveCarsByOutletId(Long outletId) {
        Query query = em.createQuery("SELECT c FROM Car c WHERE c.outlet.outletId = :inOutletId");
        query.setParameter("inOutletId", outletId);
        return query.getResultList();
    }
}
