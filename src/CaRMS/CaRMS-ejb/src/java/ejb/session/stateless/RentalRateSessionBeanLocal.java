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
import javax.ejb.Local;
import util.exception.CarCategoryNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.NoAvailableRentalRateException;
import util.exception.RentalRateNameExistException;
import util.exception.RentalRateNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author dtjldamien
 */
@Local
public interface RentalRateSessionBeanLocal {

    public Long createNewRentalRate(Long carCategoryId, RentalRate newRentalRate) throws CarCategoryNotFoundException, UnknownPersistenceException, InputDataValidationException;

    public List<RentalRate> retrieveAllRentalRates();

    public RentalRate retrieveRentalRateByRentalRateId(Long rentalRateId) throws RentalRateNotFoundException;

    public void updateRentalRate(RentalRate rentalRate) throws RentalRateNotFoundException, InputDataValidationException;

    public void deleteRentalRate(Long rentalRateId) throws RentalRateNotFoundException;

    public RentalRate retrieveCheapestRentalRate(Long carCategoryId, Date currentCheckedDate) throws NoAvailableRentalRateException;

}
