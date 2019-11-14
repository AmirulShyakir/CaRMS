/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package holidayreservationsystem;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import ws.client.CarCategory;
import ws.client.CarCategoryNotFoundException;
import ws.client.CarCategoryNotFoundException_Exception;
import ws.client.InputDataValidationException_Exception;
import ws.client.InvalidLoginCredentialException;
import ws.client.InvalidLoginCredentialException_Exception;
import ws.client.Model;
import ws.client.ModelNotFoundException_Exception;
import ws.client.NoAvailableRentalRateException;
import ws.client.NoAvailableRentalRateException_Exception;
import ws.client.Outlet;
import ws.client.OutletNotFoundException_Exception;
import ws.client.Partner;
import ws.client.PartnerNameExistException_Exception;
import ws.client.PartnerNotFoundException_Exception;
import ws.client.RentalReservation;
import ws.client.RentalReservationNotFoundException_Exception;
import ws.client.UnknownPersistenceException_Exception;

/**
 *
 * @author sw_be
 */
class MainApp {

    private Partner currentPartner;

    void runApp() throws InvalidLoginCredentialException_Exception {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Welcome to Holiday Reservation System ***\n");
            System.out.println("1: Login");
            System.out.println("2: Search Car");
            System.out.println("3: Exit\n");
            response = 0;

            while (response < 1 || response > 3) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doPartnerLogin();
                    System.out.println("Login successful\n");
                    menuMain();
                } else if (response == 2) {
                    doSearchCar();
                } else if (response == 3) {
                    break;
                } else {
                    System.out.print("Invalid option, please try again!\n");
                }
            }
            if (response == 3) {
                break;
            }
        }
    }

    private void doPartnerLogin() throws InvalidLoginCredentialException_Exception {
        Scanner scanner = new Scanner(System.in);
        String partnerName = "";
        String password = "";

        System.out.println("*** Holiday Reservation System :: Login ***\n");
        System.out.print("Enter partner name> ");
        partnerName = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();

        if (partnerName.length() > 0 && password.length() > 0) {
            currentPartner = partnerLogin(partnerName, password);
        } else {
            InvalidLoginCredentialException ex = new InvalidLoginCredentialException();
            throw new InvalidLoginCredentialException_Exception("Missing login credential!", ex);
        }
    }

    private void doSearchCar() throws OutletNotFoundException_Exception, CarCategoryNotFoundException_Exception, CarCategoryNotFoundException_Exception, NoAvailableRentalRateException_Exception, ModelNotFoundException_Exception, ModelNotFoundException_Exception, ModelNotFoundException_Exception, NoAvailableRentalRateException_Exception, ModelNotFoundException_Exception, ModelNotFoundException_Exception {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Long carCategoryId = new Long(0); // to avoid error
        Long modelId = new Long(0); // to avoid error
        Date pickUpDateTime;
        Long pickupOutletId;
        Date returnDateTime;
        Long returnOutletId;

        System.out.println("*** Holiday Reservation System :: Search Car ***\n");

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
                canReserve = searchCarByCategory(pickUpDateTime, returnDateTime, pickupOutletId, returnOutletId, carCategoryId);
            } else if (response == 2) {
                System.out.print("Enter Car Model ID> ");
                modelId = scanner.nextLong();
                carCategoryId = retrieveModelByModelId(modelId).getCarCategory().getCarCategoryId();
                canReserve = searchCarByModel(pickUpDateTime, returnDateTime, pickupOutletId, returnOutletId, modelId);
            }
            scanner.nextLine();
            if (!canReserve) {
                System.out.println("No cars are available under the provided criteria!");
            } else {
                BigDecimal totalRentalFee = calculateTotalRentalFee(carCategoryId, pickUpDateTime, returnDateTime);
                System.out.println("There are cars available! Total rental fee is SGD" + totalRentalFee + ". ");
                if (currentPartner != null) {
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
        } catch (NoAvailableRentalRateException_Exception ex) {
            System.out.println("There are no available rental rates for the period!\n");
        } catch (CarCategoryNotFoundException_Exception ex) {
            System.out.println("Car Category not found for ID: " + carCategoryId + "\n");
        } catch (ModelNotFoundException_Exception ex) {
            System.out.println("Model not found!\n");
        } catch (OutletNotFoundException_Exception ex) {
            System.out.println("Outlet not found!\n");
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void menuMain() throws OutletNotFoundException_Exception, OutletNotFoundException_Exception, CarCategoryNotFoundException_Exception, CarCategoryNotFoundException_Exception, NoAvailableRentalRateException_Exception, ModelNotFoundException_Exception, ModelNotFoundException_Exception {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Holiday Reservation System ***\n");
            System.out.println("You are login as " + "partner name" + " rights\n");
            System.out.println("1: Search Car");
            System.out.println("2: Reserve Car");
            System.out.println("3: Cancel Reservation");
            System.out.println("3: View Reservation Details");
            System.out.println("4: View All Partner Reservations");
            System.out.println("5: Logout\n");
            response = 0;

            while (response < 1 || response > 5) {
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
        System.out.println("*** Holiday Reservation System :: Reserve Car ***\n");

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
            Long rentalReservationId = rentalReservationSessionBeanRemote.createNewRentalReservation(rentalReservation);
            System.out.println("Rental reservation created with ID: " + rentalReservationId);
            scanner.nextLine();
        } catch (CarCategoryNotFoundException ex) {
            System.out.println("Car Category not found for ID: " + carCategoryId + "\n");
        } catch (ModelNotFoundException ex) {
            System.out.println("Model not found!\n");
        } catch (OutletNotFoundException ex) {
            System.out.println("Outlet not found!\n");
        } catch (InputDataValidationException ex) {
            System.err.println(ex.getMessage());
        } catch (UnknownPersistenceException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void doCancelReservation() {
        Scanner scanner = new Scanner(System.in);
        RentalReservation rentalReservation;

        System.out.println("*** Holiday Reservation System :: Cancel Reservation ***\n");
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

    private void doViewAllReservations() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Holiday Reservation System :: View All Reservations ***\n");
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

    private static Partner partnerLogin(java.lang.String arg0, java.lang.String arg1) throws InvalidLoginCredentialException_Exception {
        ws.client.PartnerReservationWebService_Service service = new ws.client.PartnerReservationWebService_Service();
        ws.client.PartnerReservationWebService port = service.getPartnerReservationWebServicePort();
        return port.partnerLogin(arg0, arg1);
    }

    private static BigDecimal calculateTotalRentalFee(Long arg0, Date arg1, Date arg2) throws CarCategoryNotFoundException_Exception, NoAvailableRentalRateException_Exception {
        ws.client.PartnerReservationWebService_Service service = new ws.client.PartnerReservationWebService_Service();
        ws.client.PartnerReservationWebService port = service.getPartnerReservationWebServicePort();
        return port.calculateTotalRentalFee(arg0, arg1, arg2);
    }

    private static BigDecimal cancelReservation(java.lang.Long arg0) throws RentalReservationNotFoundException_Exception {
        ws.client.PartnerReservationWebService_Service service = new ws.client.PartnerReservationWebService_Service();
        ws.client.PartnerReservationWebService port = service.getPartnerReservationWebServicePort();
        return port.cancelReservation(arg0);
    }

    private static Long createNewPartner(ws.client.Partner arg0) throws PartnerNameExistException_Exception, UnknownPersistenceException_Exception, InputDataValidationException_Exception {
        ws.client.PartnerReservationWebService_Service service = new ws.client.PartnerReservationWebService_Service();
        ws.client.PartnerReservationWebService port = service.getPartnerReservationWebServicePort();
        return port.createNewPartner(arg0);
    }

    private static Long createNewRentalReservation(ws.client.RentalReservation arg0) throws InputDataValidationException_Exception, UnknownPersistenceException_Exception {
        ws.client.PartnerReservationWebService_Service service = new ws.client.PartnerReservationWebService_Service();
        ws.client.PartnerReservationWebService port = service.getPartnerReservationWebServicePort();
        return port.createNewRentalReservation(arg0);
    }

    private static CarCategory retrieveCarCategoryByCarCategoryId(java.lang.Long arg0) throws CarCategoryNotFoundException_Exception {
        ws.client.PartnerReservationWebService_Service service = new ws.client.PartnerReservationWebService_Service();
        ws.client.PartnerReservationWebService port = service.getPartnerReservationWebServicePort();
        return port.retrieveCarCategoryByCarCategoryId(arg0);
    }

    private static Model retrieveModelByModelId(java.lang.Long arg0) throws ModelNotFoundException_Exception {
        ws.client.PartnerReservationWebService_Service service = new ws.client.PartnerReservationWebService_Service();
        ws.client.PartnerReservationWebService port = service.getPartnerReservationWebServicePort();
        return port.retrieveModelByModelId(arg0);
    }

    private static Outlet retrieveOutletByOutletId(java.lang.Long arg0) throws OutletNotFoundException_Exception {
        ws.client.PartnerReservationWebService_Service service = new ws.client.PartnerReservationWebService_Service();
        ws.client.PartnerReservationWebService port = service.getPartnerReservationWebServicePort();
        return port.retrieveOutletByOutletId(arg0);
    }

    private static Partner retrievePartnerByPartnerId(java.lang.Long arg0) throws PartnerNotFoundException_Exception {
        ws.client.PartnerReservationWebService_Service service = new ws.client.PartnerReservationWebService_Service();
        ws.client.PartnerReservationWebService port = service.getPartnerReservationWebServicePort();
        return port.retrievePartnerByPartnerId(arg0);
    }

    private static RentalReservation retrieveRentalReservationByRentalReservationId(java.lang.Long arg0) throws RentalReservationNotFoundException_Exception {
        ws.client.PartnerReservationWebService_Service service = new ws.client.PartnerReservationWebService_Service();
        ws.client.PartnerReservationWebService port = service.getPartnerReservationWebServicePort();
        return port.retrieveRentalReservationByRentalReservationId(arg0);
    }

    private static Boolean searchCarByCategory(Date arg0, Date arg1, Long arg2, Long arg3, Long arg4) throws OutletNotFoundException_Exception, CarCategoryNotFoundException_Exception, NoAvailableRentalRateException_Exception {
        ws.client.PartnerReservationWebService_Service service = new ws.client.PartnerReservationWebService_Service();
        ws.client.PartnerReservationWebService port = service.getPartnerReservationWebServicePort();
        return port.searchCarByCategory(arg0, arg1, arg2, arg3, arg4);
    }

    private static Boolean searchCarByModel(Date arg0, Date arg1, Long arg2, Long arg3, Long arg4) throws NoAvailableRentalRateException_Exception, CarCategoryNotFoundException_Exception, ModelNotFoundException_Exception, OutletNotFoundException_Exception {
        ws.client.PartnerReservationWebService_Service service = new ws.client.PartnerReservationWebService_Service();
        ws.client.PartnerReservationWebService port = service.getPartnerReservationWebServicePort();
        return port.searchCarByModel(arg0, arg1, arg2, arg3, arg4);
    }

}
