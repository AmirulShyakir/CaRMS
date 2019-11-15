/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.OwnCustomerSessionBeanRemote;
import ejb.session.stateless.RentalReservationSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author sw_be
 */
public class Main {

    @EJB
    private static OwnCustomerSessionBeanRemote ownCustomerSessionBeanRemote;
    @EJB
    private static CarCategorySessionBeanRemote carCategorySessionBeanRemote;
    @EJB
    private static RentalReservationSessionBeanRemote rentalReservationSessionBeanRemote;
    @EJB
    private static ModelSessionBeanRemote modelSessionBeanRemote;
    @EJB
    private static OutletSessionBeanRemote outletSessionBeanRemote;
    @EJB
    private static CustomerSessionBeanRemote customerSessionBeanRemote;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        MainApp mainApp = new MainApp(ownCustomerSessionBeanRemote, carCategorySessionBeanRemote,
                rentalReservationSessionBeanRemote, modelSessionBeanRemote, outletSessionBeanRemote,
                customerSessionBeanRemote);
        mainApp.runApp();
    }
}
