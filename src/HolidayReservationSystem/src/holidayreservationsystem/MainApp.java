/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package holidayreservationsystem;

import java.util.Scanner;
import ws.client.CarCategoryNotFoundException_Exception;
import ws.client.CarNotFoundException_Exception;
import ws.client.ModelNotFoundException_Exception;
import ws.client.OutletNotFoundException_Exception;
import ws.client.RentalReservation;
import ws.client.RentalReservationNotFoundException_Exception;

/**
 *
 * @author sw_be
 */
class MainApp {

    void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response;

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
                    doLogin();
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

            if (response == 6) {
                break;
            }
        }
    }

    private void doLogin() {
        Scanner scanner = new Scanner(System.in);
        String partnerName = "";
        String password = "";

        System.out.println("*** Holiday Reservation System :: Login ***\n");
        System.out.print("Enter partner name> ");
        partnerName = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
    }

    private void doSearchCar() {
        System.out.println("*** Holiday Reservation System :: Search Car ***\n");

    }

    private void menuMain() {
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
        System.out.println("*** Holiday Reservation System :: Reserve Car ***\n");
        System.out.print("Enter username> ");
    }

    private void doCancelReservation() {
        System.out.println("*** Holiday Reservation System :: Cancel Reservation ***\n");
        try {
            deleteReservation(1l);
        } catch (RentalReservationNotFoundException_Exception ex) {
            ex.printStackTrace();
        }
    }

    private void doViewAllReservations() {
        System.out.println("*** Holiday Reservation System :: View All Reservations ***\n");
    }
    
    private static void deleteReservation(java.lang.Long arg0) throws RentalReservationNotFoundException_Exception {
        ws.client.PartnerReservationWebService_Service service = new ws.client.PartnerReservationWebService_Service();
        ws.client.PartnerReservationWebService port = service.getPartnerReservationWebServicePort();
        port.deleteReservation(arg0);
    }

    private static java.util.List<ws.client.RentalReservation> retrieveAllRentalReservations() {
        ws.client.PartnerReservationWebService_Service service = new ws.client.PartnerReservationWebService_Service();
        ws.client.PartnerReservationWebService port = service.getPartnerReservationWebServicePort();
        return port.retrieveAllRentalReservations();
    }

    private static RentalReservation retrieveRentalReservationByRentalReservationId(java.lang.Long arg0) throws RentalReservationNotFoundException_Exception {
        ws.client.PartnerReservationWebService_Service service = new ws.client.PartnerReservationWebService_Service();
        ws.client.PartnerReservationWebService port = service.getPartnerReservationWebServicePort();
        return port.retrieveRentalReservationByRentalReservationId(arg0);
    }

    private static java.util.List<ws.client.Car> searchCar(java.lang.Long arg0, java.lang.Long arg1, javax.xml.datatype.XMLGregorianCalendar arg2, javax.xml.datatype.XMLGregorianCalendar arg3, java.lang.Long arg4, java.lang.Long arg5) throws OutletNotFoundException_Exception, ModelNotFoundException_Exception, CarNotFoundException_Exception, CarCategoryNotFoundException_Exception {
        ws.client.PartnerReservationWebService_Service service = new ws.client.PartnerReservationWebService_Service();
        ws.client.PartnerReservationWebService port = service.getPartnerReservationWebServicePort();
        return port.searchCar(arg0, arg1, arg2, arg3, arg4, arg5);
    }
}
