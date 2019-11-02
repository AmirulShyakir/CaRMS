/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalRate;
import java.util.List;
import javax.ejb.Remote;
import util.exception.InputDataValidationException;
import util.exception.RentalRateNameExistException;
import util.exception.RentalRateNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author dtjldamien
 */
@Remote
public interface RentalRateSessionBeanRemote {

    public Long createNewPartner(RentalRate newRentalRate) throws RentalRateNameExistException, UnknownPersistenceException, InputDataValidationException;

    public List<RentalRate> retrieveAllRentalRates();

    public RentalRate retrieveRentalRateByRentalRateId(Long rentalRateId) throws RentalRateNotFoundException;

    public void updateRentalRate(RentalRate rentalRate) throws RentalRateNotFoundException, InputDataValidationException;

}
