/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import entity.Customer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author dtjldamien
 */
public class MainApp {

    private Customer currentCustomer;

    public MainApp() {
        currentCustomer = null;
    }

    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Welcome to Car Rental Management System Reservation Client ***\n");
            System.out.println("1: Login");
            System.out.println("2: Register as customer");
            System.out.println("3: Search Car");
            System.out.println("4: Exit\n");
            response = 0;

            while (response < 1 || response > 4) {
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
                    // try {
                        doRegisterCustomer();
                    // } catch ({
                        
                    // }
                }  else if (response == 3) {
                    // try catch
                    doSearchCar();
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try  again\n");
                }
            }
        }
    }
        
    // exception not thrown
    private void doRegisterCustomer() {
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";
        String firstName = "";
        String lastName = "";

        String email = "";
        String phoneNumber = "";
        String passportNumber = "";

        System.out.println("*** CarMS Reservation Client :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        System.out.print("Enter first name> ");
        firstName = scanner.nextLine().trim();
        System.out.print("Enter last name> ");
        lastName = scanner.nextLine().trim();
        System.out.print("Enter email> ");
        email = scanner.nextLine().trim();
        System.out.print("Enter email> ");
        phoneNumber = scanner.nextLine().trim();
        System.out.print("Enter email> ");
        passportNumber = scanner.nextLine().trim();
        
    }

    private void doLogin() throws InvalidLoginCredentialException {
        /*
        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";

        System.out.println("*** CarMS Reservation Client :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        if (username.length() > 0 && password.length() > 0) {
        //    currentCustomer = customerSessionBeanRemote.customerLogin(username, password);
        } else {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
        */
    }

    private void doSearchCar() {
        try {
            Scanner scanner = new Scanner(System.in);
            Integer response = 0;
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
            String category;
            String model;
            Date pickUpDateTime;
            String pickupOutlet;
            Date returnDateTime;
            String returnOutlet;
            
            System.out.println("*** Car Rental Management System Reservation Client :: Search Car ***\n");
            System.out.print("Enter Category> "); // should show a list of category
            category = scanner.nextLine().trim();
            System.out.print("Enter Model> "); // should show a list of models
            category = scanner.nextLine().trim();
            System.out.print("Enter Pickup Date (dd/mm/yyyy)> ");
            pickUpDateTime = inputDateFormat.parse(scanner.nextLine().trim());
            System.out.print("Enter Return Date (dd/mm/yyyy)> ");
            returnDateTime = inputDateFormat.parse(scanner.nextLine().trim());            
            System.out.print("Enter Pickup Outlet> ");
            pickupOutlet = scanner.nextLine().trim();
            System.out.print("Enter Return Outlet> ");
            returnOutlet = scanner.nextLine().trim();

        } catch (ParseException ex){
            System.out.println("Invalid date input!\n");
        }
    }
    
    private void menuMain() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // register as customer
    // customer login
    // search car
    // reserve car
    // cancel reservation
    // view reservation details
    // view all my reservations
    // customer logout
}
