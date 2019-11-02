/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import util.exception.EmployeeUsernameExistException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author dtjldamien
 */
public interface EmployeeSessionBeanLocal {
   
    public Long createNewEmployee(Employee newEmployee) throws EmployeeUsernameExistException, UnknownPersistenceException, InputDataValidationException;

    public Employee login(String username, String password) throws InvalidLoginCredentialException;
    
}
