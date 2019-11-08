/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.TransitDriverDispatchRecordSessionBeanRemote;
import entity.Employee;
import entity.RentalRate;
import java.util.List;
import java.util.Scanner;
import util.enumeration.EmployeeRoleEnum;
import util.exception.CarCategoryNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidAccessRightException;
import util.exception.RentalRateNameExistException;
import util.exception.RentalRateNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author dtjldamien
 */
public class SalesManagementModule {

    private Employee currentEmployee;
    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    private ModelSessionBeanRemote modelSessionBeanRemote;
    private CarSessionBeanRemote carSessionBeanRemote;
    private TransitDriverDispatchRecordSessionBeanRemote transitDriverDispatchRecordSessionBeanRemote;
    private CarCategorySessionBeanRemote carCategorySessionBeanRemote;

    public SalesManagementModule() {
    }

    public SalesManagementModule(Employee currentEmployee, RentalRateSessionBeanRemote rentalRateSessionBeanRemote,
            ModelSessionBeanRemote modelSessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote,
            TransitDriverDispatchRecordSessionBeanRemote transitDriverDispatchRecordSessionBeanRemote,
            CarCategorySessionBeanRemote carCategorySessionBeanRemote) {
        this();

        this.currentEmployee = currentEmployee;
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
        this.modelSessionBeanRemote = modelSessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.transitDriverDispatchRecordSessionBeanRemote = transitDriverDispatchRecordSessionBeanRemote;
        this.carCategorySessionBeanRemote = carCategorySessionBeanRemote;
    }

    public void menuSalesManagement() throws InvalidAccessRightException {
        System.out.println("*** CarMS Management Client :: Sales Management ***\n");
        if (currentEmployee.getEmployeeRole() == EmployeeRoleEnum.SALES_MANAGER) {
            menuSalesManager();
        } else if (currentEmployee.getEmployeeRole() == EmployeeRoleEnum.OPERATIONS_MANAGER) {
            menuOperationsManager();
        } else {
            throw new InvalidAccessRightException("You don't have MANAGER rights to access the sales management module.");
        }
    }

    private void menuSalesManager() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        while (true) {
            System.out.println("1: Create Rental Rate");
            System.out.println("2: View All Rental Rates");
            System.out.println("3: View All Rental Rate Details");
            System.out.println("4: Update Rental Rate");
            System.out.println("5: Delete Rental Rate");
            System.out.println("6: Exit\n");
            response = 0;

            while (response < 1 || response > 6) {
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
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 6) {
                break;
            }
        }
    }

    private void menuOperationsManager() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        while (true) {
            System.out.println("1: Create New Model");
            System.out.println("2: View All Models");
            System.out.println("3: Update Model");
            System.out.println("4: Delete Model");
            System.out.println("-----------------------");
            System.out.println("5: Create New Car");
            System.out.println("6: View All Cars");
            System.out.println("7: View Car Details");
            System.out.println("8: Update Car");
            System.out.println("9: Delete Car");
            System.out.println("-----------------------");
            System.out.println("10: View Transit Driver Dispatch Records for Current Day Reservations");
            System.out.println("11: Assign Transit Driver");
            System.out.println("12: Update Transit As Completed");
            System.out.println("13: Exit\n");
            response = 0;

            while (response < 1 || response > 13) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doCreateNewModel();
                } else if (response == 2) {
                    doViewAllModels();
                } else if (response == 3) {
                    doUpdateModel();
                } else if (response == 4) {
                    doDeleteModel();
                } else if (response == 5) {
                    doCreateNewCar();
                } else if (response == 6) {
                    doViewAllCars();
                } else if (response == 7) {
                    doViewCarDetails();
                } else if (response == 8) {
                    doUpdateCar();
                } else if (response == 9) {
                    doDeleteCar();
                } else if (response == 10) {
                    doViewTransitDriverDispatchRecordsForCurrentDayReservations();
                } else if (response == 11) {
                    doAssignTransitDriver();
                } else if (response == 12) {
                    doUpdateTransitAsCompleted();
                } else if (response == 13) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 13) {
                break;
            }
        }
    }

    private void doCreateRentalRate() {
        Scanner scanner = new Scanner(System.in);
        RentalRate rentalRate = new RentalRate();
        System.out.println("*** CarMS Management Client :: Sales Management :: Create Rental Rate***\n");
        System.out.print("Enter Rental Rate Name> ");
        rentalRate.setRentalRateName(scanner.nextLine().trim());
        System.out.print("Enter Car Category ID > ");
        Long carCategoryId = scanner.nextLong();
        try {
            rentalRate.setCarCategory(carCategorySessionBeanRemote.retrieveCarCategoryByCarCategoryId(carCategoryId));
        } catch (CarCategoryNotFoundException ex) {
            System.out.println("No such Car Category of ID: " + carCategoryId);
        }
        System.out.print("Enter rate per day> ");
        rentalRate.setRatePerDay(scanner.nextBigDecimal());
        scanner.nextLine();
        System.out.print("Enter validity period? (Enter 'Y' to set validity period> ");
        String input = scanner.nextLine().trim();
        if (input.equals("Y")) {
            System.out.print("Enter start date");
            System.out.print("Enter end date");
        }
        try {
            Long rentalRateId = rentalRateSessionBeanRemote.createNewRentalRate(carCategoryId, rentalRate);
            System.out.println("New Rental Rate created with ID: " + rentalRateId);
        } catch (RentalRateNameExistException ex) {
            System.out.println("Rental Name " + rentalRate.getRentalRateName() + " already exists!");
        } catch (UnknownPersistenceException ex) {
            System.out.println("UnknownPersistenceException when creating new Rental Rate");
        } catch (InputDataValidationException ex) {
            System.out.println("Invalid fields for the rental rate");
        } catch (CarCategoryNotFoundException ex) {
            System.out.println("Car Category not found for ID: " + carCategoryId);
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doViewAllRentalRates() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** CarMS Management Client :: Sales Management :: View All Rental Rates***\n");
        List<RentalRate> rentalRates = rentalRateSessionBeanRemote.retrieveAllRentalRates();
        System.out.printf("%4s%16s%32s%10s%16s%32s\n", "Rental Rate ID", "Rental Rate Name", "Car Category", "Rate Per Day", "Is Enabled?", "Validty Period");
        for (RentalRate rentalRate : rentalRates) {
            String isEnabled = "false";
            if (rentalRate.getIsEnabled()) {
                isEnabled = "true";
            }
            System.out.printf("%4s%16s%32s%10s%16s%32s\n", rentalRate.getRentalRateId(),
                    rentalRate.getRentalRateName(), rentalRate.getCarCategory().getCarCategoryName(),
                    rentalRate.getRatePerDay(), isEnabled, "Validty Period");
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doViewRentalRateDetails() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** CarMS Management Client :: Sales Management :: View Rental Rate Details***\n");
        System.out.print("Enter Rental Rate ID> ");
        Long rentalRateId = scanner.nextLong();
        try {
            RentalRate rentalRate = rentalRateSessionBeanRemote.retrieveRentalRateByRentalRateId(rentalRateId);
            String isEnabled = "false";
            if (rentalRate.getIsEnabled()) {
                isEnabled = "true";
            }
            System.out.printf("%4s%16s%32s%10s%16s%32s\n", rentalRate.getRentalRateId(),
                    rentalRate.getRentalRateName(), rentalRate.getCarCategory().getCarCategoryName(),
                    rentalRate.getRatePerDay(), isEnabled, "Validty Period");
        } catch (RentalRateNotFoundException ex) {
            System.out.println("Rental Rate not found for ID: " + rentalRateId);
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doUpdateRentalRate() {
        Scanner scanner = new Scanner(System.in);
        RentalRate newRentalRate = new RentalRate();
        System.out.println("*** CarMS Management Client :: Sales Management :: Update Rental Rate***\n");
        System.out.print("Enter new Rental Rate ID> ");
        Long rentalRateId = scanner.nextLong();
        newRentalRate.setRentalRateId(rentalRateId);
        scanner.nextLine();
        System.out.print("Enter new Rental Rate Name> ");
        newRentalRate.setRentalRateName(scanner.nextLine().trim());
        System.out.print("Enter new Car Category ID > ");
        Long carCategoryId = scanner.nextLong();
        try {
            newRentalRate.setCarCategory(carCategorySessionBeanRemote.retrieveCarCategoryByCarCategoryId(carCategoryId));
        } catch (CarCategoryNotFoundException ex) {
            System.out.println("No such Car Category of ID: " + carCategoryId);
        }
        System.out.print("Enter rate per day> ");
        newRentalRate.setRatePerDay(scanner.nextBigDecimal());
        scanner.nextLine();
        System.out.print("Enter validity period? (Enter 'Y' to set validity period> ");
        String input = scanner.nextLine().trim();
        if (input.equals("Y")) {
            System.out.print("Enter start date");
            System.out.print("Enter end date");
        }
        try {
            rentalRateSessionBeanRemote.updateRentalRate(newRentalRate);
            System.out.println("Rental Rate ID: " + rentalRateId + " updated!");
        } catch (InputDataValidationException ex) {
            System.out.println("Rental Rate name already exists in the database! " + newRentalRate.getRentalRateName());
        } catch (RentalRateNotFoundException ex) {
            System.out.println("Rental Rate not found for ID: " + newRentalRate.getRentalRateId());
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doDeleteRentalRate() {
        Scanner scanner = new Scanner(System.in);
        RentalRate newRentalRate = new RentalRate();
        System.out.println("*** CarMS Management Client :: Sales Management :: Delete Rental Rate***\n");
        System.out.print("Enter Rental Rate ID> ");
        Long rentalRateId = scanner.nextLong();
        try {
            rentalRateSessionBeanRemote.deleteRentalRate(rentalRateId);
        } catch (RentalRateNotFoundException ex) {
            System.out.println("Rental Rate not found for ID: " + rentalRateId);
        }
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

    private void doRentalRate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
