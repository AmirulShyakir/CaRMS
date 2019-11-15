/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package holidayreservationsystem;

import javax.xml.datatype.DatatypeConfigurationException;
import ws.client.CarCategoryNotFoundException_Exception;
import ws.client.CustomerNotFoundException_Exception;
import ws.client.InputDataValidationException_Exception;
import ws.client.InvalidLoginCredentialException_Exception;
import ws.client.ModelNotFoundException_Exception;
import ws.client.NoAvailableRentalRateException_Exception;
import ws.client.OutletNotFoundException_Exception;
import ws.client.PartnerNotFoundException_Exception;
import ws.client.UnknownPersistenceException_Exception;


/**
 *
 * @author sw_be
 */
public class Main {

    /**
     * @param args the command line arguments
     * @throws ws.client.InvalidLoginCredentialException_Exception
     * @throws ws.client.OutletNotFoundException_Exception
     * @throws ws.client.CarCategoryNotFoundException_Exception
     * @throws ws.client.ModelNotFoundException_Exception
     * @throws ws.client.NoAvailableRentalRateException_Exception
     */
    public static void main(String[] args) throws InvalidLoginCredentialException_Exception, OutletNotFoundException_Exception, OutletNotFoundException_Exception, CarCategoryNotFoundException_Exception, CarCategoryNotFoundException_Exception, NoAvailableRentalRateException_Exception, ModelNotFoundException_Exception, DatatypeConfigurationException {
        // TODO code application logic here
        MainApp mainApp = new MainApp();
        mainApp.runApp();
    }    

}