/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import ejb.session.stateless.OwnCustomerSessionBeanRemote;
import entity.Customer;
import entity.OwnCustomer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.OwnCustomerUsernameExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author dtjldamien
 */
public class MainApp {

    private OwnCustomerSessionBeanRemote ownCustomerSessionBeanRemote;

    private Customer currentCustomer;

    public MainApp() {
    }

    public MainApp(OwnCustomerSessionBeanRemote ownCustomerSessionBeanRemote) {
        this();

        this.ownCustomerSessionBeanRemote = ownCustomerSessionBeanRemote;
    }

    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Welcome to CaRMS Reservation Client ***\n");
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
                } else if (response == 3) {
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

        System.out.println("*** CarMS Reservation Client :: Register ***\n");
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
        OwnCustomer newOwnCustomer = new OwnCustomer(firstName, lastName, username, password, email, phoneNumber, passportNumber);
        try {
            ownCustomerSessionBeanRemote.createNewOwnCustomer(newOwnCustomer);
        } catch (InputDataValidationException ex) {
            System.out.println("Input Data Validation Exception! " + ex.getMessage());
        } catch (OwnCustomerUsernameExistException ex) {
            System.out.println("Customer Username " + username + " already exists in the database! " + ex.getMessage());
        } catch (UnknownPersistenceException ex) {
            System.out.println("Unknown Persistence Exception! " + ex.getMessage());
        }
    }

    private void doLogin() throws InvalidLoginCredentialException {

        Scanner scanner = new Scanner(System.in);
        String username = "";
        String password = "";

        System.out.println("*** CarMS Reservation Client :: Login ***\n");
        System.out.print("Enter username> ");
        username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        if (username.length() > 0 && password.length() > 0) {
            currentCustomer = ownCustomerSessionBeanRemote.login(username, password);
        } else {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
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

            System.out.println("*** CaRMS Reservation Client :: Search Car ***\n");
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

        } catch (ParseException ex) {
            System.out.println("Invalid date input!\n");
        }
    }

    private void menuMain() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** CaRMS Reservation Client ***\n");
            System.out.println("You are login as " + currentCustomer.getFullName() + "\n");
            System.out.println("1: Search Car");
            System.out.println("2: Reserve Car");
            System.out.println("3: Cancel Reservation");
            System.out.println("3: View Reservation Details");
            System.out.println("4: View All My Reservations");
            System.out.println("5: Logout\n");
            response = 0;

            while (response < 1 || response > 3) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doSearchCar();
                } else if (response == 2) {
                    doReserveCar();
                } else if (response == 3) {
                    doCancelReservation();
                } else if (response == 4) {
                    doViewAllReservations();
                } else if (response == 5) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 5) {
                break;
            }
        }
    }

    private void doReserveCar() {
        System.out.println("*** CaRMS Reservation Client :: Reserve Car ***\n");
    }

    private void doCancelReservation() {
        System.out.println("*** CaRMS Reservation Client :: Cancel Reservation ***\n");
    }

    private void doViewAllReservations() {
        System.out.println("*** CaRMS Reservation Client :: View All Reservations ***\n");
    }
}
