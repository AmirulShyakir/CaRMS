/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.OwnCustomerSessionBeanRemote;
import ejb.session.stateless.RentalReservationSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author dtjldamien
 */
public class Main {

    @EJB
    private static OwnCustomerSessionBeanRemote ownCustomerSessionBeanRemote;
    @EJB
    private static CarSessionBeanRemote carSessionBeanRemote;
    @EJB
    private static RentalReservationSessionBeanRemote rentalReservationSessionBeanRemote;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        MainApp mainApp = new MainApp(ownCustomerSessionBeanRemote, carSessionBeanRemote, rentalReservationSessionBeanRemote);
        mainApp.runApp();
    }

}
