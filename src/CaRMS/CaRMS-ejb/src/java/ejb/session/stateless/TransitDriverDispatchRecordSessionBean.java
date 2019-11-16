/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import entity.Outlet;
import entity.RentalReservation;
import entity.TransitDriverDispatchRecord;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.DriverNotWorkingInSameOutletException;
import util.exception.EmployeeNotFoundException;
import util.exception.OutletNotFoundException;
import util.exception.RentalReservationNotFoundException;
import util.exception.TransitDriverDispatchRecordNotFoundException;

/**
 *
 * @author dtjldamien
 */
@Stateless
@Local(TransitDriverDispatchRecordSessionBeanLocal.class)
@Remote(TransitDriverDispatchRecordSessionBeanRemote.class)
public class TransitDriverDispatchRecordSessionBean implements TransitDriverDispatchRecordSessionBeanRemote, TransitDriverDispatchRecordSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;
    @EJB
    private OutletSessionBeanLocal outletSessionBeanLocal;
    @EJB
    private RentalReservationSessionBeanLocal rentalReservationSessionBeanLocal;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Long createNewTranspatchDriverRecord(Long destinationOutletId, Long rentalReservationId, Date transitDate) throws RentalReservationNotFoundException, OutletNotFoundException {
        try {
            TransitDriverDispatchRecord transitDriverDispatchRecord = new TransitDriverDispatchRecord(transitDate);
            Outlet destinationOutlet = outletSessionBeanLocal.retrieveOutletByOutletId(destinationOutletId);
            RentalReservation rentalReservation = rentalReservationSessionBeanLocal.retrieveRentalReservationByRentalReservationId(rentalReservationId);
            transitDriverDispatchRecord.setDestinationOutlet(destinationOutlet);
            destinationOutlet.getTransitDriverDispatchRecords().add(transitDriverDispatchRecord);
            transitDriverDispatchRecord.setRentalReservation(rentalReservation);
            rentalReservation.setTransitDriverDispatchRecord(transitDriverDispatchRecord);
            em.persist(transitDriverDispatchRecord);
            em.flush();
            return transitDriverDispatchRecord.getTransitDriverDispatchRecordId();
        } catch (OutletNotFoundException ex) {
            throw new OutletNotFoundException("Outlet ID: " + destinationOutletId + " not found!");
        } catch (RentalReservationNotFoundException ex) {
            throw new RentalReservationNotFoundException("Rental Reservation ID: " + rentalReservationId + " not found!");
        }
    }

    @Override
    public List<TransitDriverDispatchRecord> retrieveTransitDriverDispatchRecordByOutletId(Date date, Long outletId) {
        date.setHours(2);
        date.setMinutes(0);
        date.setSeconds(0);
        GregorianCalendar calendar = new GregorianCalendar(date.getYear() + 1900,
                date.getMonth(), date.getDate(), date.getHours(), date.getMinutes(), date.getSeconds());
        calendar.add(Calendar.DATE, 1);
        Date nextDay = calendar.getTime();
        Query query = em.createQuery("SELECT t FROM TransitDriverDispatchRecord t WHERE t.destinationOutlet.outletId = :inOutletId AND t.transitDate >= :inToday AND t.transitDate < :inNextDay");
        query.setParameter("inOutletId", outletId);
        query.setParameter("inToday", date);
        query.setParameter("inNextDay", nextDay);
        return query.getResultList();
    }

    @Override
    public TransitDriverDispatchRecord retrieveTransitDriverDispatchRecordByTransitDriverDispatchRecordId(Long transitDriverDispatchRecordId)
            throws TransitDriverDispatchRecordNotFoundException {
        TransitDriverDispatchRecord transitDriverDispatchRecord = em.find(TransitDriverDispatchRecord.class, transitDriverDispatchRecordId);

        if (transitDriverDispatchRecord != null) {
            return transitDriverDispatchRecord;
        } else {
            throw new TransitDriverDispatchRecordNotFoundException("Transit Driver Dispatch Record ID " + transitDriverDispatchRecordId + " does not exist!");
        }
    }

    @Override
    public void assignDriver(Long dispatchDriverId, Long transitDriverDispatchRecordId) throws DriverNotWorkingInSameOutletException, TransitDriverDispatchRecordNotFoundException, EmployeeNotFoundException {
        try {
            Employee dispatchDriver = employeeSessionBeanLocal.retrieveEmployeeByEmployeeId(dispatchDriverId);
            TransitDriverDispatchRecord transitDriverDispatchRecord = retrieveTransitDriverDispatchRecordByTransitDriverDispatchRecordId(transitDriverDispatchRecordId);
            if (dispatchDriver.getOutlet().getOutletName().equals(transitDriverDispatchRecord.getDestinationOutlet().getOutletName())) {
                transitDriverDispatchRecord.setDispatchDriver(dispatchDriver);
                dispatchDriver.addTransitDriverDispatchRecord(transitDriverDispatchRecord);
            } else {
                throw new DriverNotWorkingInSameOutletException("Employee is not working in the current outlet");
            }
        } catch (EmployeeNotFoundException ex) {
            throw new EmployeeNotFoundException("Employee ID: " + dispatchDriverId + " not found!");
        } catch (TransitDriverDispatchRecordNotFoundException ex) {
            throw new TransitDriverDispatchRecordNotFoundException("Transit Driver Dispatch Record ID: " + transitDriverDispatchRecordId + " not found!");
        }
    }

    @Override
    public void updateTransitAsCompleted(Long transitDriverDispatchRecordId) throws TransitDriverDispatchRecordNotFoundException {
        try {
            TransitDriverDispatchRecord transitDriverDispatchRecord = retrieveTransitDriverDispatchRecordByTransitDriverDispatchRecordId(transitDriverDispatchRecordId);
            transitDriverDispatchRecord.setIsCompleted(true);
        } catch (TransitDriverDispatchRecordNotFoundException ex) {
            throw new TransitDriverDispatchRecordNotFoundException("Transit Driver Dispatch Record ID: " + transitDriverDispatchRecordId + " not found!");
        }
    }
}
