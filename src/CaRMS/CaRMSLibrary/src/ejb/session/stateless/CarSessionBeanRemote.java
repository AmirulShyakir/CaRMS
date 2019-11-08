/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import java.util.List;
import util.exception.CarNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.ModelNameExistException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCarException;

/**
 *
 * @author dtjldamien
 */
public interface CarSessionBeanRemote {

    public Long createNewCar(Car newCar) throws ModelNameExistException, UnknownPersistenceException, InputDataValidationException;

    public List<Car> retrieveAllCars();

    public Car retrieveCarByCarId(Long carId) throws CarNotFoundException;

    public void updateCar(Car car) throws CarNotFoundException, UpdateCarException, InputDataValidationException;

}
