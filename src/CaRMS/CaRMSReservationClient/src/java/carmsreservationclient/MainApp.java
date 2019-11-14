/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.OwnCustomerSessionBeanRemote;
import ejb.session.stateless.RentalReservationSessionBeanRemote;
import entity.Customer;
import entity.OwnCustomer;
import entity.RentalReservation;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.exception.CarCategoryNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ModelNotFoundException;
import util.exception.NoAvailableRentalRateException;
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
    private CarCategorySessionBeanRemote carCategorySessionBeanRemote;
    private RentalReservationSessionBeanRemote rentalReservationSessionBeanRemote;
    private ModelSessionBeanRemote modelSessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;

    private Customer currentCustomer;

    public MainApp() {
    }

    public MainApp(OwnCustomerSessionBeanRemote ownCustomerSessionBeanRemote, CarCategorySessionBeanRemote carCategorySessionBeanRemote, RentalReservationSessionBeanRemote rentalReservationSessionBeanRemote, ModelSessionBeanRemote modelSessionBeanRemote, OutletSessionBeanRemote outletSessionBeanRemote) {
        this();

        this.ownCustomerSessionBeanRemote = ownCustomerSessionBeanRemote;
        this.carCategorySessionBeanRemote = carCategorySessionBeanRemote;
        this.rentalReservationSessionBeanRemote = rentalReservationSessionBeanRemote;
        this.modelSessionBeanRemote = modelSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
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
                    doRegisterCustomer();
                } else if (response == 3) {
                    doSearchCar();
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again\n");
                }
                if (response == 4) {
                    break;
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
        Long ownCustomerId;

        try {
            ownCustomerId = ownCustomerSessionBeanRemote.createNewOwnCustomer(newOwnCustomer);
            System.out.println("Customer successful registered!");
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
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Long carCategoryId = new Long(0); // to avoid error
        Long modelId = new Long(0); // to avoid error
        Date pickUpDateTime;
        Long pickupOutletId;
        Date returnDateTime;
        Long returnOutletId;

        System.out.println("*** CaRMS Reservation Client :: Search Car ***\n");

        try {
            System.out.print("Enter Pickup Date & Time (DD/MM/YYYY HH:MM)> ");
            pickUpDateTime = inputDateFormat.parse(scanner.nextLine().trim());
            System.out.print("Enter Return Date & Time (DD/MM/YYYY HH:MM)> ");
            returnDateTime = inputDateFormat.parse(scanner.nextLine().trim());
            System.out.print("Enter Pickup Outlet ID> ");
            pickupOutletId = scanner.nextLong();
            System.out.print("Enter Return Outlet ID> ");
            returnOutletId = scanner.nextLong();

            System.out.println("*** Search by Car Category or Car Model? ***\n");
            System.out.println("1: Car Category");
            System.out.println("2: Car Model");
            System.out.println();
            response = scanner.nextInt();

            Boolean canReserve = false;

            if (response == 1) {
                System.out.print("Enter Car Category ID> ");
                carCategoryId = scanner.nextLong();
                canReserve = rentalReservationSessionBeanRemote.searchCarByCategory(pickUpDateTime, returnDateTime, pickupOutletId, returnOutletId, carCategoryId);
            } else if (response == 2) {
                System.out.print("Enter Car Model ID> ");
                modelId = scanner.nextLong();
                carCategoryId = modelSessionBeanRemote.retrieveModelByModelId(modelId).getCarCategory().getCarCategoryId();
                canReserve = rentalReservationSessionBeanRemote.searchCarByModel(pickUpDateTime, returnDateTime, pickupOutletId, returnOutletId, modelId);
            }

            if (!canReserve) {
                System.out.println("No cars are available under the provided criteria!");
            } else {
                BigDecimal totalRentalFee = carCategorySessionBeanRemote.calculateTotalRentalFee(carCategoryId, pickUpDateTime, returnDateTime);
                System.out.println("There are cars available! Total rental fee is SGD" + totalRentalFee + ". ");
                if (currentCustomer != null) {
                    System.out.print("Reserve a car? (Enter 'Y' to reserve a car)> ");
                    String input = scanner.nextLine().trim();
                    doReserveCar(response, carCategoryId, modelId, pickUpDateTime, returnDateTime, pickupOutletId, returnOutletId, totalRentalFee);
                } else {
                    System.out.println("Please login first!");
                }
            }
        } catch (ParseException ex) {
            System.out.println("Invalid date input!\n");
        } catch (NoAvailableRentalRateException ex) {
            System.out.println("There are no available rental rates for the period!\n");
        } catch (CarCategoryNotFoundException ex) {
            System.out.println("Car Category not found for ID: " + carCategoryId + "\n");
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
            System.out.println("2: Cancel Reservation");
            System.out.println("3: View Reservation Details");
            System.out.println("4: View All My Reservations");
            System.out.println("5: Logout\n");
            response = 0;

            while (response < 1 || response > 5) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doSearchCar();
                } else if (response == 2) {
                    doCancelReservation();
                } else if (response == 3) {
                    doViewReservationDetails();
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

    private void doReserveCar(Integer response, Long carCategoryId, Long modelId, Date pickUpDateTime, Date returnDateTime, Long pickupOutletId, Long returnOutletId, BigDecimal totalRentalFee) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** CaRMS Reservation Client :: Reserve Car ***\n");

        RentalReservation rentalReservation = new RentalReservation();

        try {
            if (response == 1) {
                rentalReservation.setCarCategory(carCategorySessionBeanRemote.retrieveCarCategoryByCarCategoryId(carCategoryId));
            } else if (response == 2) {
                rentalReservation.setModel(modelSessionBeanRemote.retrieveModelByModelId(modelId));
            }

            rentalReservation.setCustomer(currentCustomer);
            rentalReservation.setStartDate(pickUpDateTime);
            rentalReservation.setEndDate(returnDateTime);
            rentalReservation.setPickupOutlet(outletSessionBeanRemote.retrieveOutletByOutletId(pickupOutletId));
            rentalReservation.setReturnOutlet(outletSessionBeanRemote.retrieveOutletByOutletId(returnOutletId));
            rentalReservation.setPrice(totalRentalFee);

            System.out.println("Would you like to pay now? (Enter 'Y' to enter payment details)> ");
            String input = scanner.nextLine().trim();
            if (input.equals("Y")) {
                rentalReservation.setPaid(Boolean.TRUE);
            }

            System.out.print("Enter Credit Card Number> ");
            String creditCardNumber = scanner.nextLine().trim();
            currentCustomer.setCreditCardNumber(creditCardNumber);

        } catch (CarCategoryNotFoundException ex) {
            System.out.println("Car Category not found for ID: " + carCategoryId + "\n");
        } catch (ModelNotFoundException ex) {
            System.out.println("Model not found!\n");
        } catch (OutletNotFoundException ex) {
            System.out.println("Outlet not found!\n");
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doCancelReservation() {
        Scanner scanner = new Scanner(System.in);
        RentalReservation rentalReservation;

        System.out.println("*** CaRMS Reservation Client :: Cancel Reservation ***\n");
        System.out.print("Enter Reservation ID> ");
        Long rentalReservationId = scanner.nextLong();

        try {
            BigDecimal penalty = rentalReservationSessionBeanRemote.cancelReservation(rentalReservationId);
            rentalReservation = rentalReservationSessionBeanRemote.retrieveRentalReservationByRentalReservationId(rentalReservationId);

            System.out.println("Reservation successfully cancelled!");

            if (rentalReservation.getPaid()) {
                System.out.println("You have been refunded SGD" + rentalReservation.getPrice().subtract(penalty) + " after deducting cancellation penalty of SGD" + penalty + ".");
            } else if (!rentalReservation.getPaid()) {
                System.out.println("Your card has been charged SGD" + penalty + " as a cancellation penalty.");
            }

        } catch (RentalReservationNotFoundException ex) {
            System.out.println("Rental Reservation not found for ID " + rentalReservationId);
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doViewReservationDetails() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** CaRMS Reservation Client :: View Reservation Details ***\n");
        System.out.print("Enter Reservation ID> ");
        Long rentalReservationId = scanner.nextLong();

        try {
            RentalReservation rentalReservation = rentalReservationSessionBeanRemote.retrieveRentalReservationByRentalReservationId(rentalReservationId);
            System.out.printf("%4s%20s%20s%9s%3s%3s%129s%64s\n",
                    "Rental Reservation ID", "Start Date",
                    "End Date", "Rental Fee",
                    "Paid? (T/F)", "Cancelled? (T/F)",
                    "Customer Name", "Partner Name");
            System.out.printf("%4s%20s%20s%9s%3s%3s%129s%64s\n",
                    rentalReservation.getRentalReservationId(), rentalReservation.getStartDate().toString(),
                    rentalReservation.getEndDate().toString(), rentalReservation.getPrice().toString(),
                    rentalReservation.getPaid().toString(), rentalReservation.getIsCancelled().toString(),
                    rentalReservation.getCustomer().getFullName(), rentalReservation.getPartner().getPartnerName());

        } catch (RentalReservationNotFoundException ex) {
            System.out.println("Rental Reservation not found for ID " + rentalReservationId);
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
