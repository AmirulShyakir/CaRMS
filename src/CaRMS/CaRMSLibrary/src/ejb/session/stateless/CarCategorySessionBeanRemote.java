/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import javax.ejb.Remote;
import util.exception.CarCategoryExistException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author dtjldamien
 */
@Remote
public interface CarCategorySessionBeanRemote {

    public Long createCarCategory(CarCategory newCarCategory) throws CarCategoryExistException, UnknownPersistenceException, InputDataValidationException;

}
