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
import entity.CarCategory;
import entity.Customer;
import entity.Model;
import entity.Outlet;
import entity.OwnCustomer;
import entity.RentalReservation;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.exception.CarCategoryNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ModelNotFoundException;
import util.exception.NoAvailableRentalRateException;
import util.exception.OutletNotFoundException;
import util.exception.OutsideOperatingHoursException;
import util.exception.OwnCustomerUsernameExistException;
import util.exception.RentalReservationNotFoundException;
import util.exception.ReturnDateBeforePickupDateException;
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

    public MainApp(OwnCustomerSessionBeanRemote ownCustomerSessionBeanRemote,
            CarCategorySessionBeanRemote carCategorySessionBeanRemote,
            RentalReservationSessionBeanRemote rentalReservationSessionBeanRemote,
            ModelSessionBeanRemote modelSessionBeanRemote,
            OutletSessionBeanRemote outletSessionBeanRemote) {
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
            //System.out.println("5: Do Evaluation Test Part 2");
            response = 0;

            // remember to change response boundaries when removing evaluation test
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
                //} else if (response == 5) {
                //    doEvaluationPartTwo();
                } else {
                    System.out.println("Invalid option, please try again\n");
                }
            }
            if (response == 4) {
                break;
            }
        }
    }

    // exception not thrown
    private void doRegisterCustomer() {
        Scanner scanner = new Scanner(System.in);

        OwnCustomer newOwnCustomer = new OwnCustomer();
        System.out.println("*** CarMS Reservation Client :: Register ***\n");
        System.out.print("Enter username> ");
        String username = scanner.nextLine().trim();
        newOwnCustomer.setUsername(username);
        System.out.print("Enter password> ");
        newOwnCustomer.setPassword(scanner.nextLine().trim());
        System.out.print("Enter first name> ");
        newOwnCustomer.setFirstName(scanner.nextLine().trim());
        System.out.print("Enter last name> ");
        newOwnCustomer.setLastName(scanner.nextLine().trim());
        System.out.print("Enter email> ");
        newOwnCustomer.setEmail(scanner.nextLine().trim());
        System.out.print("Enter phone number> ");
        newOwnCustomer.setPhoneNumber(scanner.nextLine().trim());
        System.out.print("Enter passport number> ");
        newOwnCustomer.setPassportNumber(scanner.nextLine().trim());

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
        Long carCategoryId = new Long(-1); // to avoid error
        Long modelId = new Long(-1); // to avoid error
        Date pickUpDateTime;
        Long pickupOutletId;
        Date returnDateTime;
        Long returnOutletId;

        System.out.println("*** CaRMS Reservation Client :: Search Car ***\n");
        Boolean canReserve = false;

        try {
            System.out.print("Enter Pickup Date & Time (DD/MM/YYYY HH:MM)> ");
            pickUpDateTime = inputDateFormat.parse(scanner.nextLine().trim());
            System.out.print("Enter Return Date & Time (DD/MM/YYYY HH:MM)> ");
            returnDateTime = inputDateFormat.parse(scanner.nextLine().trim());

            if (returnDateTime.before(pickUpDateTime)) {
                throw new ReturnDateBeforePickupDateException();
            }

            List<Outlet> outlets = outletSessionBeanRemote.retrieveAllOutlets();
            System.out.printf("%4s%64s%20s%20s\n", "ID", "Outlet Name", "Opening Hour", "Closing Hour");
            SimpleDateFormat operatingHours = new SimpleDateFormat("HH:mm");
            for (Outlet outlet : outlets) {
                String openingHour = "24/7";
                if (outlet.getOpeningHour() != null) {
                    openingHour = operatingHours.format(outlet.getOpeningHour());
                }
                String closingHour = "";
                if (outlet.getClosingHour() != null) {
                    closingHour = operatingHours.format(outlet.getClosingHour());
                }
                System.out.printf("%4s%64s%20s%20s\n", outlet.getOutletId(), outlet.getOutletName(),
                        openingHour, closingHour);
            }

            System.out.print("Enter Pickup Outlet ID> ");
            pickupOutletId = scanner.nextLong();
            System.out.print("Enter Return Outlet ID> ");
            returnOutletId = scanner.nextLong();

            Outlet pickupOutlet = outletSessionBeanRemote.retrieveOutletByOutletId(pickupOutletId);
            if (pickupOutlet.getOpeningHour() != null) {
                if ((pickUpDateTime.getHours() < pickupOutlet.getOpeningHour().getHours())
                        || (pickUpDateTime.getHours() == pickupOutlet.getOpeningHour().getHours()
                        && pickUpDateTime.getMinutes() < pickupOutlet.getOpeningHour().getMinutes())) {
                    throw new OutsideOperatingHoursException("Pickup time is before opening hours of the pickup outlet");
                } else if ((pickUpDateTime.getHours() > pickupOutlet.getClosingHour().getHours())
                        || (pickUpDateTime.getHours() == pickupOutlet.getClosingHour().getHours()
                        && pickUpDateTime.getMinutes() > pickupOutlet.getClosingHour().getMinutes())) {
                    throw new OutsideOperatingHoursException("Pickup time is after closing hours of the pickup outlet");
                }
            }
            Outlet returnOutlet = outletSessionBeanRemote.retrieveOutletByOutletId(returnOutletId);
            if (returnOutlet.getClosingHour() != null) {
                if ((returnDateTime.getHours() > returnOutlet.getClosingHour().getHours())
                        || (returnDateTime.getHours() == returnOutlet.getClosingHour().getHours()
                        && returnDateTime.getMinutes() > returnOutlet.getClosingHour().getMinutes())) {
                    throw new OutsideOperatingHoursException("Return time is after operating hours of the pickup outlet");
                } else if ((returnDateTime.getHours() < returnOutlet.getClosingHour().getHours())
                        || (returnDateTime.getHours() == returnOutlet.getClosingHour().getHours()
                        && returnDateTime.getMinutes() < returnOutlet.getClosingHour().getMinutes())) {
                    throw new OutsideOperatingHoursException("Return time is before operating hours of the pickup outlet");
                }
            }
            while (true) {
                System.out.println("*** Search by Car Category or Car Model? ***\n");
                System.out.println("1: Search by Car Category");
                System.out.println("2: Search by Car Model");
                response = 0;

                while (response < 1 || response > 2) {
                    System.out.print("> ");
                    response = scanner.nextInt();

                    if (response == 1) {
                        List<CarCategory> carCategories = carCategorySessionBeanRemote.retrieveAllCarCategories();
                        System.out.printf("%4s%64s\n", "ID", "Car Category Name");

                        for (CarCategory carCategory : carCategories) {
                            System.out.printf("%4s%64s\n",
                                    carCategory.getCarCategoryId(), carCategory.getCarCategoryName());
                        }
                        System.out.print("Enter Car Category ID> ");
                        carCategoryId = scanner.nextLong();
                        canReserve = rentalReservationSessionBeanRemote.searchCarByCategory(pickUpDateTime, returnDateTime, pickupOutletId, returnOutletId, carCategoryId);
                        break;
                    } else if (response == 2) {
                        List<Model> models = modelSessionBeanRemote.retrieveAllModels();
                        System.out.printf("%4s%64s%32s%32s\n", "ID", "Car Category Name", "Make", "Model");
                        for (Model model : models) {
                            System.out.printf("%4s%64s%32s%32s\n",
                                    model.getModelId(), model.getCarCategory().getCarCategoryName(),
                                    model.getMakeName(), model.getModelName());
                        }
                        System.out.print("Enter Car Model ID> ");
                        modelId = scanner.nextLong();
                        carCategoryId = modelSessionBeanRemote.retrieveModelByModelId(modelId).getCarCategory().getCarCategoryId();
                        canReserve = rentalReservationSessionBeanRemote.searchCarByModel(pickUpDateTime, returnDateTime, pickupOutletId, returnOutletId, modelId);
                        break;
                    } else {
                        System.out.println("Invalid option, please try again\n");
                    }
                }
                if (response == 1 || response == 2) {
                    break;
                }
            }
            scanner.nextLine();
            if (!canReserve) {
                System.out.println("No cars are available under the provided criteria!");
            } else {
                BigDecimal totalRentalFee = carCategorySessionBeanRemote.calculateTotalRentalFee(carCategoryId, pickUpDateTime, returnDateTime);
                System.out.println("There are cars available! Total rental fee is SGD" + totalRentalFee + ". ");
                if (currentCustomer != null) {
                    System.out.print("Reserve a car? (Enter 'Y' to reserve a car)> ");
                    String input = scanner.nextLine().trim();
                    if (input.equals("Y")) {
                        doReserveCar(response, carCategoryId, modelId, pickUpDateTime, returnDateTime, pickupOutletId, returnOutletId, totalRentalFee);
                    }
                } else {
                    System.out.println("Please login to reserve the car!");
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
        } catch (OutsideOperatingHoursException ex) {
            System.out.println(ex.getMessage());
        } catch (ReturnDateBeforePickupDateException ex) {
            System.out.println("ReturnDateBeforePickupDateException");
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
            System.out.println("2: View Reservation Details");
            System.out.println("3: View All My Reservations");
            System.out.println("4: Logout\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doSearchCar();
                } else if (response == 2) {
                    doViewReservationDetails();
                } else if (response == 3) {
                    doViewAllReservations();
                } else if (response == 4) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 4) {
                break;
            }
        }
    }

    private void doReserveCar(Integer response, Long carCategoryId, Long modelId, Date pickUpDateTime, Date returnDateTime, Long pickupOutletId, Long returnOutletId, BigDecimal totalRentalFee) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** CaRMS Reservation Client :: Reserve Car ***\n");

        RentalReservation rentalReservation = new RentalReservation();

        try {
            rentalReservation.setStartDate(pickUpDateTime);
            rentalReservation.setEndDate(returnDateTime);
            rentalReservation.setPrice(totalRentalFee);

            System.out.print("Enter Credit Card Number> ");
            String creditCardNumber = scanner.nextLine().trim();
            rentalReservation.setCreditCardNumber(creditCardNumber);

            System.out.print("Would you like to pay now? (Enter 'Y' to enter payment details)> ");
            String input = scanner.nextLine().trim();
            if (input.equals("Y")) {
                rentalReservation.setPaid(Boolean.TRUE);
                System.out.println("Charged " + totalRentalFee.toString() + " to credit card: " + creditCardNumber);
            } else {
                rentalReservation.setPaid(Boolean.FALSE);
            }
            Long rentalReservationId = rentalReservationSessionBeanRemote.createNewRentalReservation(carCategoryId, modelId, currentCustomer.getCustomerId(), pickupOutletId, returnOutletId, rentalReservation);
            System.out.println("Rental reservation created with ID: " + rentalReservationId);
        } catch (CarCategoryNotFoundException ex) {
            System.out.println("Car Category not found for ID: " + carCategoryId + "\n");
        } catch (ModelNotFoundException ex) {
            System.out.println("Model not found!\n");
        } catch (OutletNotFoundException ex) {
            System.out.println("Outlet not found!\n");
        } catch (InputDataValidationException ex) {
            System.out.println(ex.getMessage());
        } catch (UnknownPersistenceException ex) {
            System.out.println(ex.getMessage());
        } catch (CustomerNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void doCancelReservation(Long rentalReservationId) {
        Scanner scanner = new Scanner(System.in);
        RentalReservation rentalReservation;

        System.out.println("*** CaRMS Reservation Client :: Cancel Reservation ***\n");
        try {
            BigDecimal penalty = rentalReservationSessionBeanRemote.cancelReservation(rentalReservationId);
            rentalReservation = rentalReservationSessionBeanRemote.retrieveRentalReservationByRentalReservationId(rentalReservationId);

            System.out.println("Reservation successfully cancelled!");

            if (rentalReservation.getPaid()) {
                System.out.println("You have been refunded SGD $"
                        + rentalReservation.getPrice().subtract(penalty) + " to your card "
                        + rentalReservation.getCreditCardNumber()
                        + " after deducting cancellation penalty of SGD" + penalty + ".");
            } else {
                System.out.println("Your card : " + rentalReservation.getCreditCardNumber() + " has been charged SGD $" + penalty + " as a cancellation penalty.");
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
        scanner.nextLine();

        try {
            RentalReservation rentalReservation = rentalReservationSessionBeanRemote.retrieveRentalReservationByRentalReservationId(rentalReservationId);
            System.out.printf("%4s%20s%20s%20s%12s%12s%32s%32s\n",
                    "ID", "Start Date",
                    "End Date", "Rental Fee",
                    "Paid", "Cancelled",
                    "Car Category", "Model");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String modelName = "null";
            if (rentalReservation.getModel() != null) {
                modelName = rentalReservation.getModel().getMakeName() + " " + rentalReservation.getModel().getModelName();
            }
            System.out.printf("%4s%20s%20s%20s%12s%12s%32s%32s\n",
                    rentalReservation.getRentalReservationId(), sdf.format(rentalReservation.getStartDate()),
                    sdf.format(rentalReservation.getEndDate()), rentalReservation.getPrice().toString(),
                    rentalReservation.getPaid().toString(), rentalReservation.getIsCancelled().toString(),
                    rentalReservation.getCarCategory().getCarCategoryName(), modelName);
            System.out.print("Would you like to cancel the reservation? (Enter 'Y' to enter cancel the reservation)> ");
            String input = scanner.nextLine().trim();
            if (input.equals("Y")) {
                doCancelReservation(rentalReservationId);
            } else {
                System.out.print("Press any key to continue...> ");
            }
        } catch (RentalReservationNotFoundException ex) {
            System.out.println("Rental Reservation not found for ID " + rentalReservationId);
        }
        scanner.nextLine();
    }

    private void doViewAllReservations() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** CaRMS Reservation Client :: View All Reservations ***\n");
        List<RentalReservation> rentalReservations = rentalReservationSessionBeanRemote.retrieveCustomerRentalReservations(currentCustomer.getCustomerId());
        System.out.printf("%4s%20s%20s\n", "ID", "Start Date", "End Date");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        for (RentalReservation rentalReservation : rentalReservations) {
            System.out.printf("%4s%20s%20s\n", rentalReservation.getRentalReservationId(),
                    sdf.format(rentalReservation.getStartDate()),
                    sdf.format(rentalReservation.getEndDate()));
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doEvaluationPartTwo() {
        try {
            OwnCustomer alice = new OwnCustomer();
            alice.setUsername("alice");
            alice.setPassword("123");
            alice.setFirstName("Alice");
            alice.setLastName("Tan");
            alice.setEmail("alicetan@gmail.com");
            alice.setPhoneNumber("999");
            alice.setPassportNumber("ABCDEFGHI");
            Long aliceCustomerId = ownCustomerSessionBeanRemote.createNewOwnCustomer(alice);
            System.out.println("Created new Customer Alice with ID: " + aliceCustomerId);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            RentalReservation rentalReservation1 = new RentalReservation();
            Date startDate = sdf.parse("03/12/2019 14:00");
            Date endDate = sdf.parse("07/12/2019 12:00");
            rentalReservation1.setStartDate(startDate);
            rentalReservation1.setEndDate(endDate);
            rentalReservation1.setPrice(BigDecimal.valueOf(380));
            rentalReservation1.setPaid(Boolean.FALSE);
            rentalReservation1.setCreditCardNumber("1099001461");
            Long rentalReservation1Id = rentalReservationSessionBeanRemote.createNewRentalReservation(
                    new Long("3"), new Long("1"), aliceCustomerId, new Long("1"), new Long("1"), rentalReservation1);
            System.out.println("Reserved Toyota Corolla"
                    + "\n Pickup: 03/12/2019 14:00 Outlet A"
                    + "\n Return: 07/12/2019 12:00 Outlet A"
                    + "\n ID: " + rentalReservation1Id);

            OwnCustomer bob = new OwnCustomer();
            bob.setUsername("bob");
            bob.setPassword("123");
            bob.setFirstName("Bob");
            bob.setLastName("Lee");
            bob.setEmail("boblee@gmail.com");
            bob.setPhoneNumber("995");
            bob.setPassportNumber("A0123456789B");
            Long bobCustomerId = ownCustomerSessionBeanRemote.createNewOwnCustomer(bob);
            System.out.println("Created new Customer Bob with ID: " + bobCustomerId);

            RentalReservation rentalReservation2 = new RentalReservation();
            startDate = sdf.parse("02/12/2019 10:00");
            endDate = sdf.parse("05/12/2019 10:00");
            rentalReservation2.setStartDate(startDate);
            rentalReservation2.setEndDate(endDate);
            rentalReservation2.setPrice(BigDecimal.valueOf(300));
            rentalReservation2.setPaid(Boolean.FALSE);
            rentalReservation2.setCreditCardNumber("1099001461");
            Long rentalReservation2Id = rentalReservationSessionBeanRemote.createNewRentalReservation(
                    new Long("3"), new Long("1"), bobCustomerId, new Long("1"), new Long("1"), rentalReservation2);
            System.out.println("Reserved Toyota Corolla"
                    + "\n Pickup: 02/12/2019 10:00 Outlet A"
                    + "\n Return: 05/12/2019 10:00 Outlet A"
                    + "\n ID: " + rentalReservation2Id);

            OwnCustomer charles = new OwnCustomer();
            charles.setUsername("charles");
            charles.setPassword("123");
            charles.setFirstName("Charles");
            charles.setLastName("Chua");
            charles.setEmail("charleschua@gmail.com");
            charles.setPhoneNumber("1777");
            charles.setPassportNumber("A9876543210C");
            Long charlesCustomerId = ownCustomerSessionBeanRemote.createNewOwnCustomer(charles);
            System.out.println("Created new Customer Charles with ID: " + charlesCustomerId);

            RentalReservation rentalReservation3 = new RentalReservation();
            startDate = sdf.parse("06/12/2019 10:00");
            endDate = sdf.parse("08/12/2019 10:00");
            rentalReservation3.setStartDate(startDate);
            rentalReservation3.setEndDate(endDate);
            rentalReservation3.setPrice(BigDecimal.valueOf(180));
            rentalReservation3.setPaid(Boolean.FALSE);
            rentalReservation3.setCreditCardNumber("1099001461");
            Long rentalReservation3Id = rentalReservationSessionBeanRemote.createNewRentalReservation(
                    new Long("3"), new Long("1"), charlesCustomerId, new Long("2"), new Long("2"), rentalReservation3);
            System.out.println("Reserved Toyota Corolla"
                    + "\n Pickup: 06/12/2019 10:00 Outlet B"
                    + "\n Return: 08/12/2019 10:00 Outlet B"
                    + "\n ID: " + rentalReservation3Id);

            System.out.println("Search Car for Standard Sedan with Dates"
                    + "\n Pickup: 03/12/2019 14:00 Outlet A"
                    + "\n Return: 07/12/2019 12:00 Outlet A"
                    + "\n Car Category : Standard Sedan, ID = 3"
                    + "\n should return Cars Available with price 380");
            doSearchCar();
            System.out.println("Please run part three in the management client");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
