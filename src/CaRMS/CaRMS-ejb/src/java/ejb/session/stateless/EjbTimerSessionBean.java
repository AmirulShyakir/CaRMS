/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ejb.Schedule;
import javax.ejb.Stateless;

/**
 *
 * @author dtjldamien
 */
@Stateless

public class EjbTimerSessionBean implements EjbTimerSessionBeanRemote, EjbTimerSessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Schedule(hour = "*", minute = "*/5", info = "allocateCarsToCurrentDayReservations")
    public void allocateCarsToCurrentDayReservations() {
        /*
        • Retrieve a list of all car rental reservations for pickup on the current date
        and allocate an available car for the reserved car (make and) model or category. 
        • When allocating cars, priority should be accorded to cars that are already in the pickup outlet,
        or will be returned to the pickup outlet in time. 
        • Cars that are at a different outlet from the pickup outlet should be allocated only when necessary. 
         */
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        System.out.println("********** EjbTimerSessionBean.allocateCarsToCurrentDayReservations(): Timeout at " + timeStamp);
    }

    @Schedule(hour = "*", minute = "*/5", info = "generateTransitDriverDispatchRecords")
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
