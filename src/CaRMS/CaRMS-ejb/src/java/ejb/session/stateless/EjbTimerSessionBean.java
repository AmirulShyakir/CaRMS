/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalReservation;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
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
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String timeStamp = sdf.format(new Date());
        System.out.println("********** EjbTimerSessionBean.allocateCarsToCurrentDayReservations(): Timeout at " + timeStamp);
        List<RentalReservation> rentalReservations = rentalReservationSessionBeanLocal.retrieveAllRentalReservation();
        List<RentalReservation> rentalReservationsToBeAllocated = new ArrayList<>();
        
        for (RentalReservation rentalReservation : rentalReservations) {
            String startDate = sdf.format(rentalReservation.getStartDate());
            if (startDate.equals(timeStamp)) {
                rentalReservationsToBeAllocated.add(rentalReservation);
            }
        }
    }

    @Schedule(hour = "0", minute = "0", info = "generateTransitDriverDispatchRecords")
    public void generateTransitDriverDispatchRecords() {
        /*
        • Retrieve a list of car allocations for pickup on the current date that require movement from another different outlet. 
        • Generate a transit driver dispatch record for each car. 
        • Each outlet should only manage dispatch records for cars that are to be moved to itself. 
         */
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        System.out.println("********** EjbTimerSessionBean.generateTransitDriverDispatchRecords(): Timeout at " + timeStamp);
        /*
        List<ProductEntity> productEntities = productEntitySessionBeanLocal.retrieveAllProducts();

        for (ProductEntity productEntity : productEntities) {
            if (productEntity.getQuantityOnHand().compareTo(productEntity.getReorderQuantity()) <= 0) {
                System.out.println("********** Product " + productEntity.getSkuCode() + " requires reordering: QOH = " + productEntity.getQuantityOnHand() + "; RQ = " + productEntity.getReorderQuantity());
            }
        }
         */
    }
}
