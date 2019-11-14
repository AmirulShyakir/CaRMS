/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import util.exception.CarCategoryExistException;
import util.exception.CarCategoryNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.NoAvailableRentalRateException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author dtjldamien
 */
public interface CarCategorySessionBeanRemote {

    public Long createCarCategory(CarCategory newCarCategory) throws CarCategoryExistException, UnknownPersistenceException, InputDataValidationException;

    public CarCategory retrieveCarCategoryByCarCategoryId(Long carCategoryId) throws CarCategoryNotFoundException;

    public BigDecimal calculateTotalRentalFee(Long carCategoryId, Date pickUpDateTime, Date returnDateTime) throws CarCategoryNotFoundException, NoAvailableRentalRateException;

    public List<CarCategory> retrieveAllCarCategories();

}
