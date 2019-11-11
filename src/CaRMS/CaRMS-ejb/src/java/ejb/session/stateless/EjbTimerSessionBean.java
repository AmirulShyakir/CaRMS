/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalReservation;
import entity.TransitDriverDispatchRecord;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.NoAllocatableCarException;
import util.exception.RentalReservationNotFoundException;

/**
 *
 * @author dtjldamien
 */
@Stateless
@Local(EjbTimerSessionBeanLocal.class)
@Remote(EjbTimerSessionBeanRemote.class)
public class EjbTimerSessionBean implements EjbTimerSessionBeanRemote, EjbTimerSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    @EJB
    private RentalReservationSessionBeanLocal rentalReservationSessionBeanLocal;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Schedule(hour = "0", minute = "0", second = "0", info = "allocateCarsToCurrentDayReservations")
    public void allocateCarsToCurrentDayReservations() throws RentalReservationNotFoundException, NoAllocatableCarException {
        /*
        • Retrieve a list of all car rental reservations for pickup on the current date
        and allocate an available car for the reserved car (make and) model or category. 
        • When allocating cars, priority should be accorded to cars that are already in the pickup outlet,
        or will be returned to the pickup outlet in time. 
        • Cars that are at a different outlet from the pickup outlet should be allocated only when necessary. 
         */
        Date today = new Date();
        today.setHours(0);
        today.setMinutes(0);
        today.setSeconds(0);
        GregorianCalendar calendar = new GregorianCalendar(today.getYear() + 1900, today.getMonth(), today.getDate(), today.getHours(), today.getMinutes(), today.getSeconds());
        calendar.add(Calendar.DATE, 1);
        Date nextDay = calendar.getTime();
        Query query = em.createQuery("SELECT r FROM RentalReservation r WHERE r.startDate >= :inStartDate AND r.endDate < :inEndDate");
        query.setParameter(":inStartDate", today);
        query.setParameter(":endDate", nextDay);
        List<RentalReservation> rentalReservationsToBeAllocated = query.getResultList();
        
        // cars that are already in the pickup outlet
        // cars that will be returned in time
        // cars from a different outlet
        
        generateTransitDriverDispatchRecords(rentalReservationsToBeAllocated);
    }

    public void generateTransitDriverDispatchRecords(List<RentalReservation> rentalReservationsToBeAllocated) {
        /*
        • Retrieve a list of car allocations for pickup on the current date that require movement from another different outlet. 
        • Generate a transit driver dispatch record for each car. 
        • Each outlet should only manage dispatch records for cars that are to be moved to itself. 
         */
        List<TransitDriverDispatchRecord> transitDriverDispatchRecords = new ArrayList<>();
        for (RentalReservation rentalReservation : rentalReservationsToBeAllocated) {
            if (!(rentalReservation.getPickupOutlet().equals(rentalReservation.getCar().getOutlet()))) {
                // generate dispatch record
            }
        }
    }
}
