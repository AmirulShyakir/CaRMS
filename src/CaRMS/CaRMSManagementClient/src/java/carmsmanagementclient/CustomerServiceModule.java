/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import util.enumeration.EmployeeRoleEnum;
import util.exception.InvalidAccessRightException;

/**
 *
 * @author dtjldamien
 */
public class CustomerServiceModule {

    private Employee currentEmployee; 
    private CarSessionBeanRemote carSessionBeanRemote;
    
    public CustomerServiceModule() {
    }

    public CustomerServiceModule(Employee currentEmployee, CarSessionBeanRemote carSessionBeanRemote) {
        this();
        
        this.currentEmployee = currentEmployee;
        this.carSessionBeanRemote = carSessionBeanRemote;
    }
    
    public void menuCustomerService() throws InvalidAccessRightException {

        if (currentEmployee.getEmployeeRole() != EmployeeRoleEnum.CUSTOMER_EXECUTIVE) {
            throw new InvalidAccessRightException("You don't have CUSTOMER_EXECUTIVE rights to access the customer service module.");
        }
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** CarMS Management Client :: Customer Service ***\n");
            System.out.println("1: Pickup Car");
            System.out.println("2: Return Car");
            System.out.println("3: Back\n");
            response = 0;

            while (response < 1 || response > 3) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doPickupCar();
                } else if (response == 2) {
                    doReturnCar();
                } else if (response == 3) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 3) {
                break;
            }
        }
    }

    private void doPickupCar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doReturnCar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
