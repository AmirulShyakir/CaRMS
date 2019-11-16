/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.EjbTimerSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.RentalReservationSessionBeanRemote;
import ejb.session.stateless.TransitDriverDispatchRecordSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author dtjldamien
 */
public class Main {

    @EJB
    private static EmployeeSessionBeanRemote employeeSessionBeanRemote;
    @EJB
    private static RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    @EJB
    private static ModelSessionBeanRemote modelSessionBeanRemote;
    @EJB
    private static CarSessionBeanRemote carSessionBeanRemote;
    @EJB
    private static TransitDriverDispatchRecordSessionBeanRemote transitDriverDispatchRecordSessionBeanRemote;
    @EJB
    private static CarCategorySessionBeanRemote carCategorySessionBeanRemote;
    @EJB
    private static RentalReservationSessionBeanRemote rentalReservationSessionBeanRemote;
    @EJB
    private static EjbTimerSessionBeanRemote ejbTimerSessionBeanRemote;
    @EJB
    private static OutletSessionBeanRemote outletSessionBeanRemote;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        MainApp mainApp = new MainApp(employeeSessionBeanRemote, rentalRateSessionBeanRemote,
                modelSessionBeanRemote, carSessionBeanRemote,
                transitDriverDispatchRecordSessionBeanRemote, carCategorySessionBeanRemote,
                rentalReservationSessionBeanRemote, ejbTimerSessionBeanRemote,
                outletSessionBeanRemote);
        mainApp.runApp();
    }
}
