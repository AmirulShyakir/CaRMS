/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.RentalReservationSessionBeanRemote;
import ejb.session.stateless.TransitDriverDispatchRecordSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import util.exception.InvalidAccessRightException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnpaidRentalReservationException;

/**
 *
 * @author dtjldamien
 */
public class MainApp {

    private EmployeeSessionBeanRemote employeeSessionBeanRemote;
    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    private ModelSessionBeanRemote modelSessionBeanRemote;
    private CarSessionBeanRemote carSessionBeanRemote;
    private TransitDriverDispatchRecordSessionBeanRemote transitDriverDispatchRecordSessionBeanRemote;
    private CarCategorySessionBeanRemote carCategorySessionBeanRemote;
    private RentalReservationSessionBeanRemote rentalReservationSessionBeanRemote;

    private SalesManagementModule salesManagementModule;
    private CustomerServiceModule customerServiceModule;

    private Employee currentEmployee;

    public MainApp() {
    }

    public MainApp(EmployeeSessionBeanRemote employeeSessionBeanRemote, RentalRateSessionBeanRemote rentalRateSessionBeanRemote,
            ModelSessionBeanRemote modelSessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote,
            TransitDriverDispatchRecordSessionBeanRemote transitDriverDispatchRecordSessionBeanRemote,
            CarCategorySessionBeanRemote carCategorySessionBeanRemote, RentalReservationSessionBeanRemote rentalReservationSessionBeanRemote) {
        this();

        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
        this.modelSessionBeanRemote = modelSessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.transitDriverDispatchRecordSessionBeanRemote = transitDriverDispatchRecordSessionBeanRemote;
        this.carCategorySessionBeanRemote = carCategorySessionBeanRemote;
        this.rentalReservationSessionBeanRemote = rentalReservationSessionBeanRemote;
    }

    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Welcome to CaRMS Management Client ***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            response = 0;

            while (response < 1 || response > 2) {
                System.out.print("> ");

                response = scanner.nextInt();
                if (response == 1) {
                    try {
                        doLogin();
                        System.out.println("Login successful\n");
                        menuMain();
                    } catch (InvalidLoginCredentialException ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage() + "\n");
                    }
                } else if (response == 2) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 2) {
                break;
            }
        }
    }

    private void doLogin() throws InvalidLoginCredentialException {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";

        System.out.println("*** CarMS Management Client :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        if (username.length() > 0 && password.length() > 0) {
            currentEmployee = employeeSessionBeanRemote.login(username, password);
        } else {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }

    private void menuMain() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** CarMS Reservation Client ***\n");
            System.out.println("You are login as " + currentEmployee.getFirstName() + " " + currentEmployee.getLastName() + " with " + currentEmployee.getEmployeeRole().toString() + " rights\n");
            System.out.println("1: Sales Management");
            System.out.println("2: Customer Service");
            System.out.println("3: Logout\n");
            response = 0;

            while (response < 1 || response > 3) {
                System.out.print("> ");
                response = scanner.nextInt();

                try {
                    if (response == 1) {
                        salesManagementModule = new SalesManagementModule(currentEmployee, rentalRateSessionBeanRemote,
                                modelSessionBeanRemote, carSessionBeanRemote, transitDriverDispatchRecordSessionBeanRemote,
                                carCategorySessionBeanRemote);
                        salesManagementModule.menuSalesManagement();
                    } else if (response == 2) {
                        customerServiceModule = new CustomerServiceModule(currentEmployee, rentalReservationSessionBeanRemote);
                        customerServiceModule.menuCustomerService();
                    } else if (response == 3) {
                        break;
                    } else {
                        System.out.println("Invalid option, please try again!\n");
                    }
                } catch (InvalidAccessRightException ex) {
                    System.out.println("Invalid option, please try again!: " + ex.getMessage() + "\n");
                } catch (UnpaidRentalReservationException ex) {
                    System.out.println("Customer has not paid for the car rental reservation!");
                }
            }
            if (response == 3) {
                break;
            }
        }
    }
}
