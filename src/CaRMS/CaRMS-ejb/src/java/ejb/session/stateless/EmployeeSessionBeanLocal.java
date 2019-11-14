/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import util.exception.EmployeeNotFoundException;
import util.exception.EmployeeUsernameExistException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.OutletNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author dtjldamien
 */
public interface EmployeeSessionBeanLocal {
   
    public Employee login(String username, String password) throws InvalidLoginCredentialException;

    public Employee retrieveEmployeeByEmployeeId(Long employeeId) throws EmployeeNotFoundException;

    public Long createNewEmployee(Long outletId, Employee newEmployee) throws OutletNotFoundException, EmployeeUsernameExistException, UnknownPersistenceException, InputDataValidationException;
    
}
