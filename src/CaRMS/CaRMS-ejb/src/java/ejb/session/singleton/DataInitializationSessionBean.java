/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.CarCategorySessionBeanLocal;
import ejb.session.stateless.CarSessionBeanLocal;
import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.ModelSessionBeanLocal;
import ejb.session.stateless.OutletSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.RentalRateSessionBeanLocal;
import entity.Car;
import entity.CarCategory;
import entity.Employee;
import entity.Model;
import entity.Outlet;
import entity.Partner;
import entity.RentalRate;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.CarStatusEnum;
import util.enumeration.EmployeeRoleEnum;
import util.exception.CarCategoryExistException;
import util.exception.CarCategoryNotFoundException;
import util.exception.EmployeeUsernameExistException;
import util.exception.InputDataValidationException;
import util.exception.LicensePlateExistException;
import util.exception.ModelDisabledException;
import util.exception.ModelNameExistException;
import util.exception.ModelNotFoundException;
import util.exception.OutletNameExistException;
import util.exception.OutletNotFoundException;
import util.exception.PartnerNameExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author dtjldamien
 */
@Singleton
@LocalBean
@Startup
public class DataInitializationSessionBean {

    @EJB
    private OutletSessionBeanLocal outletSessionBeanLocal;
    @EJB
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;
    @EJB
    private PartnerSessionBeanLocal partnerSessionBeanLocal;
    @EJB
    private CarCategorySessionBeanLocal carCategorySessionBeanLocal;
    @EJB
    private ModelSessionBeanLocal modelSessionBeanLocal;
    @EJB
    private CarSessionBeanLocal carSessionBeanLocal;
    @EJB
    private RentalRateSessionBeanLocal rentalRateSessionBeanLocal;

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    public DataInitializationSessionBean() {
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PostConstruct
    public void postConstruct() {
        if (em.find(Car.class, 1l) == null && em.find(RentalRate.class, 1l) == null && em.find(Outlet.class, 1l) == null && em.find(Employee.class, 1l) == null && em.find(Partner.class, 1l) == null && em.find(CarCategory.class, 1l) == null) {
            System.out.println("###adding data");
            initialiseData();
        }
    }

    private void initialiseData() {
        Date openingHour = new Date();
        openingHour.setYear(2019 - 1900);
        openingHour.setMonth(11);
        openingHour.setDate(15);
        openingHour.setHours(10);
        openingHour.setMinutes(0);
        Date closingHour = new Date();
        closingHour.setYear(2019 - 1900);
        closingHour.setMonth(11);
        closingHour.setDate(15);
        closingHour.setHours(22);
        closingHour.setMinutes(0);

        Outlet outletA = new Outlet("Outlet A", "13 Computing Drive");
        Outlet outletB = new Outlet("Outlet B", "25 Lower Kent Ridge Road");
        Outlet outletC = new Outlet("Outlet C", "301 South Buona Vista Road", openingHour, closingHour);

        try {
            Long outletAId = outletSessionBeanLocal.createNewOutlet(outletA);
            Long outletBId = outletSessionBeanLocal.createNewOutlet(outletB);
            Long outletCId = outletSessionBeanLocal.createNewOutlet(outletC);

            Employee a1 = new Employee("Employee", "A1", "socsales", "123", EmployeeRoleEnum.SALES_MANAGER);
            Employee b1 = new Employee("Employee", "B1", "rvsales", "123", EmployeeRoleEnum.SALES_MANAGER);
            Employee c1 = new Employee("Employee", "C1", "krmrtsales", "123", EmployeeRoleEnum.SALES_MANAGER);

            Employee a2 = new Employee("Employee", "A2", "socops", "123", EmployeeRoleEnum.OPERATIONS_MANAGER);
            Employee b2 = new Employee("Employee", "B2", "rvops", "123", EmployeeRoleEnum.OPERATIONS_MANAGER);
            Employee c2 = new Employee("Employee", "C2", "krops", "123", EmployeeRoleEnum.OPERATIONS_MANAGER);

            Employee a3 = new Employee("Employee", "A3", "soccust", "123", EmployeeRoleEnum.CUSTOMER_EXECUTIVE);
            Employee b3 = new Employee("Employee", "B3", "rvcust", "123", EmployeeRoleEnum.CUSTOMER_EXECUTIVE);
            Employee c3 = new Employee("Employee", "C3", "krcust", "123", EmployeeRoleEnum.CUSTOMER_EXECUTIVE);

            Employee a4 = new Employee("Employee", "A4", "socemployee4", "123", EmployeeRoleEnum.EMPLOYEE);
            Employee a5 = new Employee("Employee", "A5", "socemployee5", "123", EmployeeRoleEnum.EMPLOYEE);

            employeeSessionBeanLocal.createNewEmployee(outletAId, a1);
            employeeSessionBeanLocal.createNewEmployee(outletAId, a2);
            employeeSessionBeanLocal.createNewEmployee(outletAId, a3);
            employeeSessionBeanLocal.createNewEmployee(outletAId, a4);
            employeeSessionBeanLocal.createNewEmployee(outletAId, a5);

            employeeSessionBeanLocal.createNewEmployee(outletBId, b1);
            employeeSessionBeanLocal.createNewEmployee(outletBId, b2);
            employeeSessionBeanLocal.createNewEmployee(outletBId, b3);

            employeeSessionBeanLocal.createNewEmployee(outletCId, c1);
            employeeSessionBeanLocal.createNewEmployee(outletCId, c2);
            employeeSessionBeanLocal.createNewEmployee(outletCId, c3);

            Partner holidayDotCom = new Partner("Holiday.com", "123");
            partnerSessionBeanLocal.createNewPartner(holidayDotCom);

            CarCategory luxurySedan = new CarCategory("Luxury Sedan");
            CarCategory familySedan = new CarCategory("Family Sedan");
            CarCategory standardSedan = new CarCategory("Standard Sedan");
            CarCategory suvMinivan = new CarCategory("SUV and Minivan");

            Long luxurySedanId = carCategorySessionBeanLocal.createCarCategory(luxurySedan);
            Long familySedanId = carCategorySessionBeanLocal.createCarCategory(familySedan);
            Long standardSedanId = carCategorySessionBeanLocal.createCarCategory(standardSedan);
            Long suvMinivanId = carCategorySessionBeanLocal.createCarCategory(suvMinivan);

            Model toyotaCorolla = new Model("Toyota", "Corolla");
            Model hondaCivic = new Model("Honda", "Civic");
            Model nissanSunny = new Model("Nissan", "Sunny");
            Model mercedesEclass = new Model("Mercedes", "E Class");
            Model bmw5Series = new Model("BMW", "5 Series");
            Model audiA6 = new Model("Audi", "A6");

            Long toyotaCorollaId = modelSessionBeanLocal.createNewModel(standardSedanId, toyotaCorolla);
            Long hondaCivicId = modelSessionBeanLocal.createNewModel(standardSedanId, hondaCivic);
            Long nissanSunnyId = modelSessionBeanLocal.createNewModel(standardSedanId, nissanSunny);
            Long mercedesEclassId = modelSessionBeanLocal.createNewModel(luxurySedanId, mercedesEclass);
            Long bmw5SeriesId = modelSessionBeanLocal.createNewModel(luxurySedanId, bmw5Series);
            Long audiA6Id = modelSessionBeanLocal.createNewModel(luxurySedanId, audiA6);

            Car car1 = new Car("SS00A1TC", "RED", CarStatusEnum.AVAILABLE);
            Car car2 = new Car("SS00A2TC", "GREEN", CarStatusEnum.AVAILABLE);
            Car car3 = new Car("SS00A3TC", "BLUE", CarStatusEnum.AVAILABLE);

            Car car4 = new Car("SS00B1HC", "RED", CarStatusEnum.AVAILABLE);
            Car car5 = new Car("SS00B2HC", "GREEN", CarStatusEnum.AVAILABLE);
            Car car6 = new Car("SS00B3HC", "BLUE", CarStatusEnum.AVAILABLE);

            Car car7 = new Car("SS00C1NS", "RED", CarStatusEnum.AVAILABLE);
            Car car8 = new Car("SS00C2NS", "GREEN", CarStatusEnum.AVAILABLE);
            Car car9 = new Car("SS00C3NS", "BLUE", CarStatusEnum.REPAIR);

            Car car10 = new Car("LS00A4ME", "RED", CarStatusEnum.AVAILABLE);
            Car car11 = new Car("LS00B4B5", "GREEN", CarStatusEnum.AVAILABLE);
            Car car12 = new Car("LS00C4A6", "BLUE", CarStatusEnum.AVAILABLE);

            carSessionBeanLocal.createNewCar(toyotaCorollaId, outletAId, car1);
            carSessionBeanLocal.createNewCar(toyotaCorollaId, outletAId, car2);
            carSessionBeanLocal.createNewCar(toyotaCorollaId, outletAId, car3);

            carSessionBeanLocal.createNewCar(hondaCivicId, outletBId, car4);
            carSessionBeanLocal.createNewCar(hondaCivicId, outletBId, car5);
            carSessionBeanLocal.createNewCar(hondaCivicId, outletBId, car6);

            carSessionBeanLocal.createNewCar(nissanSunnyId, outletCId, car7);
            carSessionBeanLocal.createNewCar(nissanSunnyId, outletCId, car8);
            carSessionBeanLocal.createNewCar(nissanSunnyId, outletCId, car9);

            carSessionBeanLocal.createNewCar(mercedesEclassId, outletAId, car10);
            carSessionBeanLocal.createNewCar(bmw5SeriesId, outletBId, car11);
            carSessionBeanLocal.createNewCar(audiA6Id, outletCId, car12);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            RentalRate standardSedanDefault = new RentalRate("Standard Sedan - Default", BigDecimal.valueOf(100.0));
            Date startDateTime = sdf.parse("06/12/2019 12:00");
            Date endDateTime = sdf.parse("08/12/2019 00:00");
            RentalRate standardSedanWeekendPromo = new RentalRate("Standard Sedan - Weekend Promo", BigDecimal.valueOf(80.0), startDateTime, endDateTime);
            RentalRate familySedanDefault = new RentalRate("Family Sedan - Default", BigDecimal.valueOf(200.0));
            startDateTime = sdf.parse("02/12/2019 00:00");
            endDateTime = sdf.parse("02/12/2019 23:59");
            RentalRate luxurySedanMonday = new RentalRate("Luxury Sedan - Monday", BigDecimal.valueOf(310.0), startDateTime, endDateTime);
            startDateTime = sdf.parse("03/12/2019 00:00");
            endDateTime = sdf.parse("03/12/2019 23:59");
            RentalRate luxurySedanTuesday = new RentalRate("Luxury Sedan - Tuesday", BigDecimal.valueOf(320.0), startDateTime, endDateTime);
            startDateTime = sdf.parse("04/12/2019 00:00");
            endDateTime = sdf.parse("04/12/2019 23:59");
            RentalRate luxurySedanWednesday = new RentalRate("Luxury Sedan - Wednesday", BigDecimal.valueOf(330.0), startDateTime, endDateTime);
            startDateTime = sdf.parse("04/12/2019 00:00");
            endDateTime = sdf.parse("04/12/2019 23:59");
            RentalRate luxurySedanWeekdayPromo = new RentalRate("Luxury Sedan - Weekday Promo", BigDecimal.valueOf(250.0), startDateTime, endDateTime);

            rentalRateSessionBeanLocal.createNewRentalRate(standardSedanId, standardSedanDefault);
            rentalRateSessionBeanLocal.createNewRentalRate(standardSedanId, standardSedanWeekendPromo);
            rentalRateSessionBeanLocal.createNewRentalRate(familySedanId, familySedanDefault);
            rentalRateSessionBeanLocal.createNewRentalRate(luxurySedanId, luxurySedanMonday);
            rentalRateSessionBeanLocal.createNewRentalRate(luxurySedanId, luxurySedanTuesday);
            rentalRateSessionBeanLocal.createNewRentalRate(luxurySedanId, luxurySedanWednesday);
            rentalRateSessionBeanLocal.createNewRentalRate(luxurySedanId, luxurySedanWeekdayPromo);

        } catch (ParseException ex) {
            System.out.println(ex.getMessage());
        } catch (CarCategoryNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (UnknownPersistenceException ex) {
            System.out.println(ex.getMessage());
        } catch (InputDataValidationException ex) {
            System.out.println(ex.getMessage());
        } catch (ModelDisabledException ex) {
            System.out.println(ex.getMessage());
        } catch (ModelNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (OutletNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (LicensePlateExistException ex) {
            System.out.println(ex.getMessage());
        } catch (ModelNameExistException ex) {
            System.out.println(ex.getMessage());
        } catch (CarCategoryExistException ex) {
            System.out.println("Car Category already exists in the database!");
        } catch (PartnerNameExistException ex) {
            System.out.println("Partner name already exists in the database!");
        } catch (OutletNameExistException ex) {
            System.out.println("Outlet name already exists in the database!");
        } catch (EmployeeUsernameExistException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
