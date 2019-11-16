/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.ModelSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.TransitDriverDispatchRecordSessionBeanRemote;
import entity.Car;
import entity.Employee;
import entity.Model;
import entity.Outlet;
import entity.RentalRate;
import entity.TransitDriverDispatchRecord;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import util.enumeration.EmployeeRoleEnum;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarNotFoundException;
import util.exception.DriverNotWorkingInSameOutletException;
import util.exception.EmployeeNotFoundException;
import util.exception.EndDateBeforeStartDateException;
import util.exception.InputDataValidationException;
import util.exception.InvalidAccessRightException;
import util.exception.LicensePlateExistException;
import util.exception.ModelDisabledException;
import util.exception.ModelNameExistException;
import util.exception.ModelNotFoundException;
import util.exception.OutletNotFoundException;
import util.exception.RentalRateNotFoundException;
import util.exception.TransitDriverDispatchRecordNotFoundException;
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
    private OutletSessionBeanRemote outletSessionBeanRemote;

    public SalesManagementModule() {
    }

    public SalesManagementModule(Employee currentEmployee, RentalRateSessionBeanRemote rentalRateSessionBeanRemote,
            ModelSessionBeanRemote modelSessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote,
            TransitDriverDispatchRecordSessionBeanRemote transitDriverDispatchRecordSessionBeanRemote,
            CarCategorySessionBeanRemote carCategorySessionBeanRemote, OutletSessionBeanRemote outletSessionBeanRemote) {
        this();

        this.currentEmployee = currentEmployee;
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
        this.modelSessionBeanRemote = modelSessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.transitDriverDispatchRecordSessionBeanRemote = transitDriverDispatchRecordSessionBeanRemote;
        this.carCategorySessionBeanRemote = carCategorySessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
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
            System.out.println("3: View Rental Rate Details");
            System.out.println("4: Exit\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");
                response = scanner.nextInt();
                if (response == 1) {
                    doCreateRentalRate();
                } else if (response == 2) {
                    doViewAllRentalRates();
                } else if (response == 3) {
                    doViewRentalRateDetails();
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
            System.out.println("4: Exit\n");
            response = 0;

            while (response < 1 || response > 4) {
                System.out.print("> ");
                response = scanner.nextInt();
                if (response == 1) {
                    doCreateNewCar();
                } else if (response == 2) {
                    doViewAllCars();
                } else if (response == 3) {
                    doViewCarDetails();
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
        Long carCategoryId;
        System.out.println("*** CarMS Management Client :: Sales Management :: Create Rental Rate***\n");
        System.out.print("Enter Rental Rate Name> ");
        rentalRate.setRentalRateName(scanner.nextLine().trim());
        System.out.print("Enter Car Category ID > ");
        carCategoryId = scanner.nextLong();
        try {
            rentalRate.setCarCategory(carCategorySessionBeanRemote.retrieveCarCategoryByCarCategoryId(carCategoryId));
            System.out.print("Enter rate per day> ");
            rentalRate.setRatePerDay(scanner.nextBigDecimal());
            scanner.nextLine();
            System.out.print("Enter validity period? (Enter 'Y' to set validity period> ");
            String input = scanner.nextLine().trim();
            if (input.equals("Y")) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                System.out.print("Enter start date (DD/MM/YYYY HH:MM)> ");
                Date startDate = sdf.parse(scanner.nextLine().trim());
                System.out.print("Enter end date (DD/MM/YYYY HH:MM)> ");
                Date endDate = sdf.parse(scanner.nextLine().trim());
                if (endDate.before(startDate)) {
                    throw new EndDateBeforeStartDateException();
                }
                rentalRate.setStartDate(startDate);
                rentalRate.setEndDate(endDate);
            } else {
                System.out.println("Validity period not entered!");
            }
            Long rentalRateId = rentalRateSessionBeanRemote.createNewRentalRate(carCategoryId, rentalRate);
            System.out.println("Rental Rate ID: " + rentalRateId + " sucessfully created!");
        } catch (ParseException ex) {
            System.out.println("Invalid Date/Time Format!");
        } catch (CarCategoryNotFoundException ex) {
            System.out.println("No such Car Category of ID: " + carCategoryId);
        } catch (UnknownPersistenceException ex) {
            System.out.println("UnknownPersistenceException when creating new Rental Rate");
        } catch (InputDataValidationException ex) {
            System.out.println("Invalid fields for the rental rate");
        } catch (EndDateBeforeStartDateException ex) {
            System.out.println("End date is before start date!");
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doViewAllRentalRates() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** CarMS Management Client :: Sales Management :: View All Rental Rates***\n");
        List<RentalRate> rentalRates = rentalRateSessionBeanRemote.retrieveAllRentalRates();
        System.out.printf("%4s%32s%32s%16s%16s%20s%20s\n", "ID", "Rental Rate Name", "Car Category", "Rate Per Day", "Is Enabled?", "Start Period", "End Period");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        for (RentalRate rentalRate : rentalRates) {
            String isEnabled = "false";
            if (rentalRate.getIsEnabled()) {
                isEnabled = "true";
            }
            String startDate = "null";
            if (rentalRate.getStartDate() != null) {
                startDate = sdf.format(rentalRate.getStartDate());
            }
            String endDate = "null";
            if (rentalRate.getEndDate() != null) {
                endDate = sdf.format(rentalRate.getEndDate());
            }
            System.out.printf("%4s%32s%32s%16s%16s%20s%20s\n", rentalRate.getRentalRateId(),
                    rentalRate.getRentalRateName(), rentalRate.getCarCategory().getCarCategoryName(),
                    rentalRate.getRatePerDay(), isEnabled, startDate, endDate);
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doViewRentalRateDetails() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        System.out.println("*** CarMS Management Client :: Sales Management :: View Rental Rate Details***\n");
        System.out.print("Enter Rental Rate ID> ");
        Long rentalRateId = scanner.nextLong();
        try {
            RentalRate rentalRate = rentalRateSessionBeanRemote.retrieveRentalRateByRentalRateId(rentalRateId);
            String isEnabled = "false";
            if (rentalRate.getIsEnabled()) {
                isEnabled = "true";
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String startDate = "Always Available";
            if (rentalRate.getStartDate() != null) {
                startDate = sdf.format(rentalRate.getStartDate());
            }
            String endDate = "";
            if (rentalRate.getEndDate() != null) {
                endDate = sdf.format(rentalRate.getEndDate());
            }
            System.out.printf("%4s%32s%32s%16s%16s%20s%20s\n", "ID", "Rental Rate Name", "Car Category", "Rate Per Day", "Is Enabled?", "Start Period", "End Period");
            System.out.printf("%4s%32s%32s%16s%16s%20s%20s\n", rentalRate.getRentalRateId(),
                    rentalRate.getRentalRateName(), rentalRate.getCarCategory().getCarCategoryName(),
                    rentalRate.getRatePerDay(), isEnabled, startDate, endDate);
            System.out.println("------------------------");
            System.out.println("1: Update Rental Rate");
            System.out.println("2: Delete Rental Rate");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = scanner.nextInt();
            if (response == 1) {
                doUpdateRentalRate(rentalRate);
            } else if (response == 2) {
                doDeleteRentalRate(rentalRate);
            }
            System.out.print("Press any key to continue...> ");
        } catch (RentalRateNotFoundException ex) {
            System.out.println("Rental Rate not found for ID: " + rentalRateId);
        }
    }

    private void doUpdateRentalRate(RentalRate rentalRate) {
        Scanner scanner = new Scanner(System.in);
        RentalRate newRentalRate = new RentalRate();
        System.out.println("*** CarMS Management Client :: Sales Management :: Update Rental Rate***\n");
        Long rentalRateId = rentalRate.getRentalRateId();
        newRentalRate.setRentalRateId(rentalRateId);
        System.out.print("Enter new Rental Rate Name> ");
        newRentalRate.setRentalRateName(scanner.nextLine().trim());
        System.out.print("Enter new Car Category ID > ");
        Long carCategoryId = scanner.nextLong();
        try {
            newRentalRate.setCarCategory(carCategorySessionBeanRemote.retrieveCarCategoryByCarCategoryId(carCategoryId));
            System.out.print("Enter rate per day> ");
            newRentalRate.setRatePerDay(scanner.nextBigDecimal());
            scanner.nextLine();
            System.out.print("Enter validity period? (Enter 'Y' to set validity period> ");
            String input = scanner.nextLine().trim();
            if (input.equals("Y")) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                System.out.print("Enter start date (DD/MM/YYYY HH:MM)> ");
                Date startDate = sdf.parse(scanner.nextLine().trim());
                newRentalRate.setStartDate(startDate);
                System.out.print("Enter end date (DD/MM/YYYY HH:MM)> ");
                Date endDate = sdf.parse(scanner.nextLine().trim());
                newRentalRate.setEndDate(endDate);
            } else {
                System.out.println("Validity period not entered!");
            }
            rentalRateSessionBeanRemote.updateRentalRate(newRentalRate);
            System.out.println("Rental Rate ID: " + rentalRateId + " updated!");
        } catch (CarCategoryNotFoundException ex) {
            System.out.println("No such Car Category of ID: " + carCategoryId);
        } catch (InputDataValidationException ex) {
            System.out.println("Rental Rate name already exists in the database! " + newRentalRate.getRentalRateName());
        } catch (RentalRateNotFoundException ex) {
            System.out.println("Rental Rate not found for ID: " + newRentalRate.getRentalRateId());
        } catch (ParseException ex) {
            System.out.println("Invalid Date/Time Format!");
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doDeleteRentalRate(RentalRate rentalRate) {
        Scanner scanner = new Scanner(System.in);
        RentalRate newRentalRate = new RentalRate();
        System.out.println("*** CarMS Management Client :: Sales Management :: Delete Rental Rate***\n");
        Long rentalRateId = rentalRate.getRentalRateId();
        try {
            rentalRateSessionBeanRemote.deleteRentalRate(rentalRateId);
            System.out.println("Rental Rate ID: " + rentalRateId + " sucessfully deleted!");
        } catch (RentalRateNotFoundException ex) {
            System.out.println("Rental Rate not found for ID: " + rentalRateId);
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
            System.out.println("New Model succesfully created with ID " + modelId);
        } catch (CarCategoryNotFoundException ex) {
            System.out.println("Car Category ID: " + carCategoryId + " not found!");
        } catch (InputDataValidationException ex) {
            System.out.println("Input Data Validation Exception");
        } catch (ModelNameExistException ex) {
            System.out.println("Model name already exists in the database! " + model.getModelName());
        } catch (UnknownPersistenceException ex) {
            System.out.println("Unknown persistence exception");
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doViewAllModels() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** CarMS Management Client :: Sales Management :: View All Models***\n");
        List<Model> models = modelSessionBeanRemote.retrieveAllModels();
        System.out.printf("%4s%32s%32s%32s\n", "ID", "Car Category", "Make Name", "Model Name");
        for (Model model : models) {
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
            System.out.println("Model ID: " + newModel.getModelId() + " successfully updated!");
        } catch (ModelNotFoundException ex) {
            System.out.println("Model not found for ID: " + modelId);
        } catch (InputDataValidationException ex) {
            System.out.println("Model name: " + newModel.getModelName() + " already exists in the database!");
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
        scanner.nextLine();
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
            System.out.println("New Car succesfully created with ID " + carId);
        } catch (ModelNotFoundException ex) {
            System.out.println("Model ID: " + modelId + " not found!");
        } catch (OutletNotFoundException ex) {
            System.out.println("Outlet ID: " + outletId + " not found!");
        } catch (LicensePlateExistException ex) {
            System.out.println("License Plate : + " + car.getLicensePlate() + " already exists!");
        } catch (UnknownPersistenceException ex) {
            System.out.println("Unknown persistence exception");
        } catch (InputDataValidationException ex) {
            System.out.println("Input data validation exception");
        } catch (ModelDisabledException ex) {
            System.out.println("Model is disabled and new car record should not be created with the disabled model");
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doViewAllCars() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** CarMS Management Client :: Sales Management :: View All Cars***\n");
        List<Car> cars = carSessionBeanRemote.retrieveAllCars();
        System.out.printf("%4s%32s%16s%16s%22s\n", "ID", "Car Category", "Make", "Model", "License Plate Number");
        for (Car car : cars) {
            System.out.printf("%4s%32s%16s%16s%22s\n", car.getCarId(),
                    car.getModel().getCarCategory().getCarCategoryName(),
                    car.getModel().getMakeName(), car.getModel().getModelName(),
                    car.getLicensePlate());
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doViewCarDetails() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        System.out.println("*** CarMS Management Client :: Sales Management :: View Car Details***\n");
        System.out.print("Enter Car ID> ");
        Long carId = scanner.nextLong();
        scanner.nextLine();
        try {
            Car car = carSessionBeanRemote.retrieveCarByCarId(carId);
            System.out.printf("%4s%32s%16s%16s%22s\n", "ID", "Car Category", "Make", "Model", "License Plate Number");
            System.out.printf("%4s%32s%16s%16s%22s\n", car.getCarId(),
                    car.getModel().getCarCategory().getCarCategoryName(),
                    car.getModel().getMakeName(), car.getModel().getModelName(),
                    car.getLicensePlate());
            System.out.println("------------------------");
            System.out.println("1: Update Car");
            System.out.println("2: Delete Car");
            System.out.println("3: Back\n");
            System.out.print("> ");
            response = scanner.nextInt();

            if (response == 1) {
                doUpdateCar(car);
            } else if (response == 2) {
                doDeleteCar(car);
            }
        } catch (CarNotFoundException ex) {
            System.out.println("Car not found for ID: " + carId);
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doUpdateCar(Car car) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** CarMS Management Client :: Sales Management :: Update Car***\n");
        Car newCar = new Car();
        Long carId = car.getCarId();
        newCar.setCarId(carId);
        System.out.print("Enter new license plate> ");
        newCar.setLicensePlate(scanner.nextLine().trim());
        System.out.print("Enter new colour> ");
        newCar.setColour(scanner.nextLine().trim());
        try {
            carSessionBeanRemote.updateCar(newCar);
            System.out.print("Car ID: " + car.getCarId() + " successfully updated!");
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

    private void doDeleteCar(Car car) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** CarMS Management Client :: Sales Management :: Delete Car***\n");
        Long carId = car.getCarId();
        try {
            carSessionBeanRemote.deleteCar(carId);
            System.out.println("Car ID: " + carId + " sucessfully deleted!");
        } catch (CarNotFoundException ex) {
            System.out.println("Model Rate not found for ID: " + carId);
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doViewTransitDriverDispatchRecordsForCurrentDayReservations() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** CarMS Management Client :: Sales Management :: View Transit Driver Dispatch Records for Current Day Reservations***\n");
        System.out.print("Enter Date (DD/MM/YYYY) > ");
        String inputDate = scanner.nextLine().trim();
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = inputFormat.parse(inputDate);
            /*
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
            System.out.print("Enter Outlet ID> ");
            Long outletId = scanner.nextLong();
            scanner.nextLine();
             */
            System.out.println("Dispatch records for " + currentEmployee.getOutlet().getOutletName() + " on " + inputDate + "\n");
            List<TransitDriverDispatchRecord> transitDriverDispatchRecords = transitDriverDispatchRecordSessionBeanRemote.
                    retrieveTransitDriverDispatchRecordByOutletId(date, currentEmployee.getOutlet().getOutletId());
            System.out.printf("%12s%32s%32s%20s%20s\n",
                    "Record ID", "Destination Outlet", "Driver", "Status", "Transit Time");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            for (TransitDriverDispatchRecord transitDriverDispatchRecord : transitDriverDispatchRecords) {
                String isCompleted = "Not Completed";
                if (transitDriverDispatchRecord.getIsCompleted()) {
                    isCompleted = "Completed";
                }
                String dispatchDriverName = "Unassigned";
                if (transitDriverDispatchRecord.getDispatchDriver() != null) {
                    dispatchDriverName = transitDriverDispatchRecord.getDispatchDriver().getFullName();
                }
                String transitDate = sdf.format(transitDriverDispatchRecord.getTransitDate());
                System.out.printf("%12s%32s%32s%20s%20s\n",
                        transitDriverDispatchRecord.getTransitDriverDispatchRecordId(),
                        transitDriverDispatchRecord.getDestinationOutlet().getOutletName(),
                        dispatchDriverName, isCompleted, transitDate);
            }
        } catch (ParseException ex) {
            System.out.println("Invalid date input!");
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doAssignTransitDriver() {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("*** CarMS Management Client :: Sales Management :: Assign Transit Driver***\n");
            System.out.print("Enter Date (DD/MM/YYYY) > ");
            String inputDate = scanner.nextLine().trim();
            System.out.println("Dispatch records for " + currentEmployee.getOutlet().getOutletName() + " on " + inputDate + "\n");
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = inputFormat.parse(inputDate);
            List<TransitDriverDispatchRecord> transitDriverDispatchRecords = transitDriverDispatchRecordSessionBeanRemote.
                    retrieveTransitDriverDispatchRecordByOutletId(date, currentEmployee.getOutlet().getOutletId());
            System.out.printf("%12s%32s%32s%20s%20s\n",
                    "Record ID", "Destination Outlet", "Driver", "Status", "Transit Time");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            for (TransitDriverDispatchRecord transitDriverDispatchRecord : transitDriverDispatchRecords) {
                String isCompleted = "Not Completed";
                if (transitDriverDispatchRecord.getIsCompleted()) {
                    isCompleted = "Completed";
                }
                String dispatchDriverName = "Unassigned";
                if (transitDriverDispatchRecord.getDispatchDriver() != null) {
                    dispatchDriverName = transitDriverDispatchRecord.getDispatchDriver().getFullName();
                }
                String transitDate = sdf.format(transitDriverDispatchRecord.getTransitDate());
                System.out.printf("%12s%32s%32s%20s%20s\n",
                        transitDriverDispatchRecord.getTransitDriverDispatchRecordId(),
                        transitDriverDispatchRecord.getDestinationOutlet().getOutletName(),
                        dispatchDriverName, isCompleted, transitDate);
            }
            System.out.print("Enter Transit Driver Dispatch Record ID> ");
            Long transitDriverDispatchRecordId = scanner.nextLong();
            System.out.print("Enter Dispatch Driver ID> ");
            Long dispatchDriverId = scanner.nextLong();
            scanner.nextLine();
            transitDriverDispatchRecordSessionBeanRemote.assignDriver(dispatchDriverId, transitDriverDispatchRecordId);
            System.out.println("Succesfully assigned transit driver " + dispatchDriverId + " to a dispatch record " + transitDriverDispatchRecordId);
        } catch (DriverNotWorkingInSameOutletException ex) {
            System.out.println("Driver is not working in the same outlet as the car is currently at!");
        } catch (EmployeeNotFoundException ex) {
            System.out.println("Employee not found!");
        } catch (TransitDriverDispatchRecordNotFoundException ex) {
            System.out.println("Transit driver dispatch record not found!");
        } catch (ParseException ex) {
            System.out.println("Invalid Date Format");
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doUpdateTransitAsCompleted() {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("*** CarMS Management Client :: Sales Management :: Update Transit As Completed***\n");
            System.out.print("Enter Date (DD/MM/YYYY) > ");
            String inputDate = scanner.nextLine().trim();
            System.out.println("Dispatch records for " + currentEmployee.getOutlet().getOutletName() + " on " + inputDate + "\n");
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = inputFormat.parse(inputDate);
            List<TransitDriverDispatchRecord> transitDriverDispatchRecords = transitDriverDispatchRecordSessionBeanRemote.
                    retrieveTransitDriverDispatchRecordByOutletId(date, currentEmployee.getOutlet().getOutletId());
            System.out.printf("%12s%32s%32s%20s%20s\n",
                    "Record ID", "Destination Outlet", "Driver", "Status", "Transit Time");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            for (TransitDriverDispatchRecord transitDriverDispatchRecord : transitDriverDispatchRecords) {
                String isCompleted = "Not Completed";
                if (transitDriverDispatchRecord.getIsCompleted()) {
                    isCompleted = "Completed";
                }
                String dispatchDriverName = "Unassigned";
                if (transitDriverDispatchRecord.getDispatchDriver() != null) {
                    dispatchDriverName = transitDriverDispatchRecord.getDispatchDriver().getFullName();
                }
                String transitDate = sdf.format(transitDriverDispatchRecord.getTransitDate());
                System.out.printf("%12s%32s%32s%20s%20s\n",
                        transitDriverDispatchRecord.getTransitDriverDispatchRecordId(),
                        transitDriverDispatchRecord.getDestinationOutlet().getOutletName(),
                        dispatchDriverName, isCompleted, transitDate);
            }
            System.out.print("Enter Transit Dispatch Record ID> ");
            Long transitDriverDispatchRecordId = scanner.nextLong();
            scanner.nextLine();
            transitDriverDispatchRecordSessionBeanRemote.updateTransitAsCompleted(transitDriverDispatchRecordId);
            System.out.println("Successfully updated transit record id: " + transitDriverDispatchRecordId + " as completed!");
        } catch (TransitDriverDispatchRecordNotFoundException ex) {
            System.out.println("Transit driver dispatch record not found!");
        } catch (ParseException ex) {
            System.out.println("Invalid date format!");
        }
        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }
}
