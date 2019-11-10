/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.CarCategorySessionBeanLocal;
import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.OutletSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import entity.CarCategory;
import entity.Employee;
import entity.Outlet;
import entity.Partner;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.EmployeeRoleEnum;
import util.exception.CarCategoryExistException;
import util.exception.EmployeeUsernameExistException;
import util.exception.InputDataValidationException;
import util.exception.OutletNameExistException;
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
    
    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    public DataInitializationSessionBean() {
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PostConstruct
    public void postConstruct() {
        if (em.find(Outlet.class, 1l) == null && em.find(Employee.class, 1l) == null && em.find(Partner.class, 1l) == null && em.find(CarCategory.class, 1l) == null) {
        initialiseData();
        } 
    }

    private void initialiseData() {
        Date openingHour = new Date();
        Date closingHour = new Date();

        Outlet soc = new Outlet("SoC", "13 Computing Drive", openingHour, closingHour);
        Outlet rvrc = new Outlet("RVRC", "25 Lower Kent Ridge Road", openingHour, closingHour);
        Outlet krMRT = new Outlet("KR MRT", "301 South Buona Vista Road", openingHour, closingHour);

        try {
            outletSessionBeanLocal.createNewOutlet(soc);
            outletSessionBeanLocal.createNewOutlet(rvrc);
            outletSessionBeanLocal.createNewOutlet(krMRT);
        } catch (InputDataValidationException ex) {
            System.out.println(ex.getMessage());
        } catch (OutletNameExistException ex) {
            System.out.println("Outlet name already exists in the database!");
        } catch (UnknownPersistenceException ex) {
            System.out.println(ex.getMessage());
        }

        Employee socSalesManager = new Employee("Bob", "Ross", "socsales", "123", EmployeeRoleEnum.SALES_MANAGER, soc);
        Employee rvrcSalesManager = new Employee("Kanye", "West", "rvrcsales", "123", EmployeeRoleEnum.SALES_MANAGER, rvrc);
        Employee krMrtSalesManager = new Employee("John", "Johnson", "krmrtsales", "123", EmployeeRoleEnum.SALES_MANAGER, krMRT);

        Employee socOpsManager = new Employee("Haley", "Red", "socops", "123", EmployeeRoleEnum.OPERATIONS_MANAGER, soc);
        Employee rvrcOpsManager = new Employee("Alex", "Green", "rvrcops", "123", EmployeeRoleEnum.OPERATIONS_MANAGER, rvrc);
        Employee krMrtOpsManager = new Employee("Luke", "Blue", "krtmrtops", "123", EmployeeRoleEnum.OPERATIONS_MANAGER, krMRT);

        Employee socCustomerExecutive = new Employee("Peter", "Griffin", "soccustexec", "123", EmployeeRoleEnum.CUSTOMER_EXECUTIVE, soc);
        Employee rvrcCustomerExecutive = new Employee("Claire", "Pritchett", "rvrccustexec", "123", EmployeeRoleEnum.CUSTOMER_EXECUTIVE, rvrc);
        Employee krMrtCustomerExecutive = new Employee("Phil", "Dunphy", "krtmrtcustexec", "123", EmployeeRoleEnum.CUSTOMER_EXECUTIVE, krMRT);

        try {
            employeeSessionBeanLocal.createNewEmployee(socSalesManager);
            employeeSessionBeanLocal.createNewEmployee(rvrcSalesManager);
            employeeSessionBeanLocal.createNewEmployee(krMrtSalesManager);
            employeeSessionBeanLocal.createNewEmployee(socOpsManager);
            employeeSessionBeanLocal.createNewEmployee(rvrcOpsManager);
            employeeSessionBeanLocal.createNewEmployee(krMrtOpsManager);
            employeeSessionBeanLocal.createNewEmployee(socCustomerExecutive);
            employeeSessionBeanLocal.createNewEmployee(rvrcCustomerExecutive);
            employeeSessionBeanLocal.createNewEmployee(socSalesManager);
            employeeSessionBeanLocal.createNewEmployee(krMrtCustomerExecutive);
        } catch (EmployeeUsernameExistException ex) {
            System.out.println("Employee username already exists in the database!");
        } catch (InputDataValidationException ex) {
            System.out.println(ex.getMessage());
        } catch (UnknownPersistenceException ex) {
            System.out.println(ex.getMessage());
        }

        Partner holidayDotCom = new Partner("holiday", "123");
        try {
            partnerSessionBeanLocal.createNewPartner(holidayDotCom);
        } catch (InputDataValidationException ex) {
            System.out.println(ex.getMessage());
        } catch (PartnerNameExistException ex) {
            System.out.println("Partner name already exists in the database!");
        } catch (UnknownPersistenceException ex) {
            System.out.println(ex.getMessage());
        }
        
        CarCategory luxurySedan = new CarCategory("Luxury Sedan");
        CarCategory familySedan = new CarCategory("Family Sedan");
        CarCategory standardSedan = new CarCategory("Standard Sedan");
        CarCategory suvMinivan = new CarCategory("SUV/Minivan");

        try {
            carCategorySessionBeanLocal.createCarCategory(luxurySedan);
            carCategorySessionBeanLocal.createCarCategory(familySedan);
            carCategorySessionBeanLocal.createCarCategory(standardSedan);
            carCategorySessionBeanLocal.createCarCategory(suvMinivan);
        } catch (CarCategoryExistException ex) {
            System.out.println("Car Category already exists in the database!");
        } catch (InputDataValidationException ex) {
            System.out.println(ex.getMessage());
        } catch (UnknownPersistenceException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void persist(Object object) {
        em.persist(object);
    }
}
