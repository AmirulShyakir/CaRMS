/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import java.util.Scanner;

/**
 *
 * @author dtjldamien
 */
public class SalesManagementModule {

    public SalesManagementModule() {
    }

    public void menuSalesManagement() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** CarMS Management Client :: Sales Management ***\n");
            System.out.println("1: Create Rental Rate");
            System.out.println("2: View All Rental Rates");
            System.out.println("3: View All Rental Rate Details");
            System.out.println("4: Update Rental Rate");
            System.out.println("5: Delete Rental Rate");
            System.out.println("-----------------------");
            System.out.println("6: Create New Model");
            System.out.println("7: View All Models");
            System.out.println("8: Update Model");
            System.out.println("9: Delete Model");
            System.out.println("-----------------------");
            System.out.println("10: Create New Car");
            System.out.println("11: View All Cars");
            System.out.println("12: View Car Details");
            System.out.println("13: Update Car");
            System.out.println("14: Delete Car");
            System.out.println("-----------------------");
            System.out.println("15: View Transit Driver Dispatch Records for Current Day Reservations");
            System.out.println("16: Assign Transit Driver");
            System.out.println("17: Update Transit As Completed");
            System.out.println("18: Back\n");
            response = 0;

            while (response < 1 || response > 18) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doCreateRentalRate();
                } else if (response == 2) {
                    doViewAllRentalRates();
                } else if (response == 3) {
                    doViewRentalRateDetails();
                } else if (response == 4) {
                    doUpdateRentalRate();
                } else if (response == 5) {
                    doDeleteRentalRate();
                } else if (response == 6) {
                    doCreateNewModel();
                } else if (response == 7) {
                    doViewAllModels();
                } else if (response == 8) {
                    doUpdateModel();
                } else if (response == 9) {
                    doDeleteModel();
                } else if (response == 10) {
                    doCreateNewCar();
                } else if (response == 11) {
                    doViewAllCars();
                } else if (response == 12) {
                    doViewCarDetails();
                } else if (response == 13) {
                    doUpdateCar();
                } else if (response == 14) {
                    doDeleteCar();
                } else if (response == 15) {
                    doViewTransitDriverDispatchRecordsForCurrentDayReservations();
                } else if (response == 16) {
                    doAssignTransitDriver();
                } else if (response == 17) {
                    doUpdateTransitAsCompleted();
                } else if (response == 18) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 19) {
                break;
            }
        }
    }

    private void doCreateRentalRate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doViewAllRentalRates() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doViewRentalRateDetails() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doUpdateRentalRate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doDeleteRentalRate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doCreateNewModel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doViewAllModels() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doUpdateModel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doDeleteModel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doCreateNewCar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doViewAllCars() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doViewCarDetails() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doUpdateCar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doDeleteCar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doViewTransitDriverDispatchRecordsForCurrentDayReservations() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doAssignTransitDriver() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doUpdateTransitAsCompleted() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
