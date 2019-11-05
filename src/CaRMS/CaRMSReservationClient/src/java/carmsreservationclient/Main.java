/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import ejb.session.stateless.OwnCustomerSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author dtjldamien
 */
public class Main {

    @EJB
    private static OwnCustomerSessionBeanRemote ownCustomerSessionBeanRemote;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        MainApp mainApp = new MainApp(ownCustomerSessionBeanRemote);
        mainApp.runApp();
    }

}
