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
import entity.Car;
import entity.Employee;
import entity.Model;
import entity.RentalRate;
import java.util.List;
import java.util.Scanner;
import util.enumeration.EmployeeRoleEnum;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidAccessRightException;
import util.exception.LicensePlateExistException;
import util.exception.ModelDisabledException;
import util.exception.ModelNameExistException;
import util.exception.ModelNotFoundException;
import util.exception.OutletNotFoundException;
import util.exception.RentalRateNameExistException;
import util.exception.RentalRateNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCarException;

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
            System.out.println("*** CarMS Management Client :: Sales Manager Menu ***\n");
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
                    System.out.print("Invalid option, please try again!\n");
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
            System.out.println("*** CarMS Management Client :: Operations Manager Menu ***\n");
            System.out.println("1: Model Menu");
            System.out.println("2: Car Menu");
            System.out.println("3: Transit Menu");
            System.out.println("4: Exit\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");
                response = scanner.nextInt();
                if (response == 1) {
                    menuModel();
                } else if (response == 2) {
                    menuCar();
                } else if (response == 3) {
                    menuTransit();
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

    private void menuModel() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        while (true) {
            System.out.println("*** CarMS Management Client :: Model Menu ***\n");
            System.out.println("1: Create New Model");
            System.out.println("2: View All Models");
            System.out.println("3: Update Model");
            System.out.println("4: Delete Model");
            System.out.println("5: Exit\n");
            response = 0;

            while (response < 1 || response > 5) {
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
                    break;
                } else {
                    System.out.print("Invalid option, please try again!\n");
                }
            }
            if (response == 5) {
                break;
            }
        }
    }

    private void menuCar() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        while (true) {
            System.out.println("*** CarMS Management Client :: Car Menu ***\n");
            System.out.println("1: Create New Car");
            System.out.println("2: View All Cars");
            System.out.println("3: View Car Details");
            System.out.println("4: Update Car");
            System.out.println("5: Delete Car");
            System.out.println("6: Exit\n");
            response = 0;

            while (response < 1 || response > 6) {
                System.out.print("> ");
                response = scanner.nextInt();
                if (response == 1) {
                    doCreateNewCar();
                } else if (response == 2) {
                    doViewAllCars();
                } else if (response == 3) {
                    doViewCarDetails();
                } else if (response == 4) {
                    doUpdateCar();
                } else if (response == 5) {
                    doDeleteCar();
                } else if (response == 6) {
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

    private void menuTransit() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        while (true) {
            System.out.println("*** CarMS Management Client :: Transit Menu ***\n");
            System.out.println("1: View Transit Driver Dispatch Records for Current Day Reservations");
            System.out.println("2: Assign Transit Driver");
            System.out.println("3: Update Transit As Completed");
            System.out.println("4: Exit\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");
                response = scanner.nextInt();
                if (response == 1) {
                    doViewTransitDriverDispatchRecordsForCurrentDayReservations();
                } else if (response == 2) {
                    doAssignTransitDriver();
                } else if (response == 3) {
                    doUpdateTransitAsCompleted();
                } else if (response == 4) {
                    break;
                } else {
                    System.out.print("Invalid option, please try again!\n");
                }
            }
            if (response == 4) {
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
            System.out.print("No such Car Category of ID: " + carCategoryId);
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
            System.out.print("New Rental Rate created with ID: " + rentalRateId);
        } catch (RentalRateNameExistException ex) {
            System.out.print("Rental Name " + rentalRate.getRentalRateName() + " already exists!");
        } catch (UnknownPersistenceException ex) {
            System.out.print("UnknownPersistenceException when creating new Rental Rate");
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
            System.out.print("Rental Rate not found for ID: " + rentalRateId);
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
            System.out.print("No such Car Category of ID: " + carCategoryId);
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
            System.out.print("Rental Rate ID: " + rentalRateId + " updated!");
        } catch (InputDataValidationException ex) {
            System.out.print("Rental Rate name already exists in the database! " + newRentalRate.getRentalRateName());
        } catch (RentalRateNotFoundException ex) {
            System.out.print("Rental Rate not found for ID: " + newRentalRate.getRentalRateId());
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
            System.out.print("Rental Rate ID: " + rentalRateId + " sucessfully deleted!");
        } catch (RentalRateNotFoundException ex) {
            System.out.print("Rental Rate not found for ID: " + rentalRateId);
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doCreateNewModel() {
        Scanner scanner = new Scanner(System.in);
        Model model = new Model();
        System.out.println("*** CarMS Management Client :: Sales Management :: Create new Model***\n");
        System.out.print("Enter Make name> ");
        model.setMakeName(scanner.nextLine().trim());
        System.out.print("Enter Model name> ");
        model.setModelName(scanner.nextLine().trim());
        System.out.print("Enter Car Category ID> ");
        Long carCategoryId = scanner.nextLong();
        scanner.nextLine();
        try {
            Long modelId = modelSessionBeanRemote.createNewModel(carCategoryId, model);
            System.out.print("New Model succesfully created with ID " + modelId);
        } catch (CarCategoryNotFoundException ex) {
            System.out.print("Car Category ID: " + carCategoryId + " not found!");
        } catch (InputDataValidationException ex) {
            System.out.print("Input Data Validation Exception");
        } catch (ModelNameExistException ex) {
            System.out.print("Model name already exists in the database! " + model.getModelName());
        } catch (UnknownPersistenceException ex) {
            System.out.print("Unknown persistence exception");
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doViewAllModels() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** CarMS Management Client :: Sales Management :: View All Models***\n");
        List<Model> models = modelSessionBeanRemote.retrieveAllModels();
        System.out.printf("%4s%32s%32s%32s\n", "Model ID", "Car Category", "Make Name", "Model Name");
        for (Model model : models) {
            String isEnabled = "false";
            System.out.printf("%4s%32s%32s%32s\n", model.getModelId(), model.getCarCategory().getCarCategoryName(),
                    model.getMakeName(), model.getModelName());
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doUpdateModel() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** CarMS Management Client :: Sales Management :: Update Model***\n");
        Model newModel = new Model();
        System.out.print("Enter Model ID> ");
        Long modelId = scanner.nextLong();
        scanner.nextLine();
        newModel.setModelId(modelId);
        System.out.print("Enter new Make name> ");
        newModel.setMakeName(scanner.nextLine().trim());
        System.out.print("Enter new Model name> ");
        newModel.setModelName(scanner.nextLine().trim());
        System.out.print("Enter new Car Category ID> ");
        Long carCategoryId = scanner.nextLong();
        scanner.nextLine();
        try {
            modelSessionBeanRemote.updateModel(carCategoryId, newModel);
            System.out.print("Model ID: " + newModel.getModelId() + " successfully updated!");
        } catch (ModelNotFoundException ex) {
            System.out.print("Model not found for ID: " + modelId);
        } catch (InputDataValidationException ex) {
            System.out.print("Model name: " + newModel.getModelName() + " already exists in the database!");
        } catch (CarCategoryNotFoundException ex) {
            System.out.println("Car Category not found for ID: " + carCategoryId);
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doDeleteModel() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** CarMS Management Client :: Sales Management :: Delete Model***\n");
        System.out.print("Enter Model ID> ");
        Long modelId = scanner.nextLong();
        try {
            modelSessionBeanRemote.deleteModel(modelId);
            System.out.print("Model ID: " + modelId + " sucessfully deleted!");
        } catch (ModelNotFoundException ex) {
            System.out.print("Model Rate not found for ID: " + modelId);
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doCreateNewCar() {
        Scanner scanner = new Scanner(System.in);
        Car car = new Car();
        System.out.println("*** CarMS Management Client :: Sales Management :: Create new Car***\n");
        System.out.print("Enter license plate number> ");
        car.setLicensePlate(scanner.nextLine().trim());
        System.out.print("Enter colour> ");
        car.setColour(scanner.nextLine().trim());
        System.out.print("Enter model ID> ");
        Long modelId = scanner.nextLong();
        System.out.print("Enter outlet ID> ");
        Long outletId = scanner.nextLong();
        scanner.nextLine();
        try {
            Long carId = carSessionBeanRemote.createNewCar(modelId, outletId, car);
            System.out.print("New Car succesfully created with ID " + carId);
        } catch (ModelNotFoundException ex) {
            System.out.print("Model ID: " + modelId + " not found!");
        } catch (OutletNotFoundException ex) {
            System.out.print("Outlet ID: " + outletId + " not found!");
        } catch (LicensePlateExistException ex) {
            System.out.print("License Plate : + " + car.getLicensePlate() + " already exists!");
        } catch (UnknownPersistenceException ex) {
            System.out.print("Unknown persistence exception");
        } catch (InputDataValidationException ex) {
            System.out.print("Input data validation exception");
        } catch (ModelDisabledException ex) {
            System.out.print("Model is disabled and new car record should not be created with the disabled model");
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doViewAllCars() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** CarMS Management Client :: Sales Management :: View All Cars***\n");
        List<Car> cars = carSessionBeanRemote.retrieveAllCars();
        System.out.printf("%4s%16s%16s\n", "Car ID", "License Plate", "On Rental");
        for (Car car : cars) {
            String onRental = "false";
            if (car.getOnRental()) {
                onRental = "true";
            }
            System.out.printf("%4s%16s%16s\n", car.getCarId(), car.getLicensePlate(), onRental);
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doViewCarDetails() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** CarMS Management Client :: Sales Management :: View Car Details***\n");
        System.out.print("Enter car ID> ");
        Long carId = scanner.nextLong();
        scanner.nextLine();
        try {
            Car car = carSessionBeanRemote.retrieveCarByCarId(carId);
            String onRental = "false";
            if (car.getOnRental()) {
                onRental = "true";
            }
            System.out.printf("%4s%16s%16s\n", car.getCarId(), car.getLicensePlate(), onRental);
        } catch (CarNotFoundException ex) {
            System.out.println("Car not found for ID: " + carId);
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doUpdateCar() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** CarMS Management Client :: Sales Management :: Update Car***\n");
        Car newCar = new Car();
        System.out.print("Enter Car ID> ");
        Long carId = scanner.nextLong();
        scanner.nextLine();
        newCar.setCarId(carId);
        System.out.print("Enter new license plate> ");
        newCar.setLicensePlate(scanner.nextLine().trim());
        System.out.print("Enter new colour> ");
        newCar.setColour(scanner.nextLine().trim());
        try {
            carSessionBeanRemote.updateCar(newCar);
            System.out.print("Car ID: " + carId + " successfully updated!");
        } catch (CarNotFoundException ex) {
            System.out.print("Car not found for ID: " + carId);
        } catch (InputDataValidationException ex) {
            System.out.print("License plate: " + newCar.getLicensePlate() + " already exists in the database!");
        } catch (UpdateCarException ex) {
            System.out.print("License plate: " + newCar.getLicensePlate() + " already exists in the database!");
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doDeleteCar() {
        Scanner scanner = new Scanner(System.in);
        RentalRate newRentalRate = new RentalRate();
        System.out.println("*** CarMS Management Client :: Sales Management :: Delete Car***\n");
        System.out.print("Enter Car ID> ");
        Long carId = scanner.nextLong();
        try {
            carSessionBeanRemote.deleteCar(carId);
            System.out.print("Car ID: " + carId + " sucessfully deleted!");
        } catch (CarNotFoundException ex) {
            System.out.print("Model Rate not found for ID: " + carId);
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
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
