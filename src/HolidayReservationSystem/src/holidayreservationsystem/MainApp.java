/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package holidayreservationsystem;

import java.util.Scanner;

/**
 *
 * @author dtjldamien
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
    }

    private void doViewAllReservations() {
        System.out.println("*** Holiday Reservation System :: View All Reservations ***\n");
    }
}
