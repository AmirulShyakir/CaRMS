/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.OwnCustomer;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.OwnCustomerUsernameExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author dtjldamien
 */
public interface OwnCustomerSessionBeanRemote {

    public Long createNewOwnCustomer(OwnCustomer newOwnCustomer) throws OwnCustomerUsernameExistException, UnknownPersistenceException, InputDataValidationException;

    public OwnCustomer login(String username, String password) throws InvalidLoginCredentialException;

}
