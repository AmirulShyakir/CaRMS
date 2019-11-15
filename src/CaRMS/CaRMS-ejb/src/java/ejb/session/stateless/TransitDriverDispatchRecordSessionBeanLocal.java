/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.TransitDriverDispatchRecord;
import java.util.Date;
import java.util.List;
import util.exception.DriverNotWorkingInSameOutletException;
import util.exception.EmployeeNotFoundException;
import util.exception.OutletNotFoundException;
import util.exception.RentalReservationNotFoundException;
import util.exception.TransitDriverDispatchRecordNotFoundException;

/**
 *
 * @author dtjldamien
 */
public interface TransitDriverDispatchRecordSessionBeanLocal {

    public List<TransitDriverDispatchRecord> retrieveTransitDriverDispatchRecordByOutletId(Date date, Long outletId);

    public TransitDriverDispatchRecord retrieveTransitDriverDispatchRecordByTransitDriverDispatchRecordId(Long transitDriverDispatchRecordId) throws TransitDriverDispatchRecordNotFoundException;

    public void updateTransitAsCompleted(Long transitDriverDispatchRecordId) throws TransitDriverDispatchRecordNotFoundException;

    public void assignDriver(Long dispatchDriverId, Long transitDriverDispatchRecordId) throws DriverNotWorkingInSameOutletException, TransitDriverDispatchRecordNotFoundException, EmployeeNotFoundException;

    public Long createNewTranspatchDriverRecord(Long destinationOutletId, Long rentalReservationId, Date transitDate) throws RentalReservationNotFoundException, OutletNotFoundException;

}
