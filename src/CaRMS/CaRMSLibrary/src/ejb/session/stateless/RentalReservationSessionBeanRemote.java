/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalReservation;
import java.util.List;
import util.exception.InputDataValidationException;
import util.exception.RentalReservationNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author dtjldamien
 */
public interface RentalReservationSessionBeanRemote {

    public Long createNewRentalReservation(RentalReservation newRentalReservation) throws InputDataValidationException, UnknownPersistenceException;

    public List<RentalReservation> retrieveAllRentalReservation();

    public RentalReservation retrieveRentalReservationByRentalReservationId(Long rentalReservationId) throws RentalReservationNotFoundException;

}
