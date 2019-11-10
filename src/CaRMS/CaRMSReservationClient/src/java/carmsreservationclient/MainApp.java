/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.OwnCustomerSessionBeanRemote;
import ejb.session.stateless.RentalReservationSessionBeanRemote;
import entity.Car;
import entity.Customer;
import entity.OwnCustomer;
import entity.RentalReservation;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ModelNotFoundException;
import util.exception.OutletNotFoundException;
import util.exception.OwnCustomerUsernameExistException;
import util.exception.RentalReservationNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author dtjldamien
 */
public class MainApp {

    private OwnCustomerSessionBeanRemote ownCustomerSessionBeanRemote;
    private CarSessionBeanRemote carSessionBeanRemote;
    private RentalReservationSessionBeanRemote rentalReservationSessionBeanRemote;

    private Customer currentCustomer;

    public MainApp() {
    }

    public MainApp(OwnCustomerSessionBeanRemote ownCustomerSessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote, RentalReservationSessionBeanRemote rentalReservationSessionBeanRemote) {
        this();

        this.ownCustomerSessionBeanRemote = ownCustomerSessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.rentalReservationSessionBeanRemote = rentalReservationSessionBeanRemote;
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
        System.out.print("Enter phone number> ");
        phoneNumber = scanner.nextLine().trim();
        System.out.print("Enter passport number> ");
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

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        Long carCategoryId;
        Long modelId;
        Date pickUpDateTime;
        Long pickupOutletId;
        Date returnDateTime;
        Long returnOutletId;

        System.out.println("*** CaRMS Reservation Client :: Search Car ***\n");
        System.out.print("Enter Car Category ID> "); // should show a list of category
        carCategoryId = scanner.nextLong();
        System.out.print("Enter Model ID> "); // should show a list of models
        modelId = scanner.nextLong();

        try {
            System.out.print("Enter Pickup Date (dd/mm/yyyy)> ");
            pickUpDateTime = inputDateFormat.parse(scanner.nextLine().trim());
            System.out.print("Enter Return Date (dd/mm/yyyy)> ");
            returnDateTime = inputDateFormat.parse(scanner.nextLine().trim());
            System.out.print("Enter Pickup Outlet ID> ");
            pickupOutletId = scanner.nextLong();
            System.out.print("Enter Return Outlet ID> ");
            returnOutletId = scanner.nextLong();

            List<Car> cars = carSessionBeanRemote.searchCar(carCategoryId, modelId, pickUpDateTime, returnDateTime, pickupOutletId, returnOutletId);
            System.out.printf("%4s%16s%32s%10s%16s%32s\n", "Car Category ID", "Model ID", "Pick-up Date Time", "Return Date Time", "Pick-up Outlet ID", "Return Outlet ID");

        } catch (ParseException ex) {
            System.out.println("Invalid date input!\n");
        } catch (CarNotFoundException ex) {
            System.out.println("Car not found!\n");
        } catch (CarCategoryNotFoundException ex) {
            System.out.println("Car Category not found for ID: " + carCategoryId);
        } catch (ModelNotFoundException ex) {
            System.out.println("Model not found!\n");
        } catch (OutletNotFoundException ex) {
            System.out.println("Outlet not found!\n");
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
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
        Scanner scanner = new Scanner(System.in);
        RentalReservation rentalReservation = new RentalReservation();
        System.out.println("*** CaRMS Reservation Client :: Reserve Car ***\n");
        System.out.print("Enter Car ID> ");
        Long carId = scanner.nextLong();
    }

    private void doCancelReservation() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** CaRMS Reservation Client :: Cancel Reservation ***\n");
        System.out.print("Enter Reservation ID> ");
        Long rentalReservationId = scanner.nextLong();
        try {
            rentalReservationSessionBeanRemote.deleteReservation(rentalReservationId);
        } catch (RentalReservationNotFoundException ex) {
            System.out.print("Rental Reservation not found for ID " + rentalReservationId);
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doViewAllReservations() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** CaRMS Reservation Client :: View All Reservations ***\n");
        List<RentalReservation> rentalReservations = rentalReservationSessionBeanRemote.retrieveAllRentalReservations();
        System.out.printf("%4s\n", "Rental Reservation ID");
        for (RentalReservation rentalReservation : rentalReservations) {
            System.out.printf("%4s\n", rentalReservation.getRentalReservationId());
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
}
