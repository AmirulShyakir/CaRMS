/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.PartnerNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author dtjldamien
 */
public interface CustomerSessionBeanRemote {

    public Customer retrieveCustomerByCustomerId(Long customerId) throws CustomerNotFoundException;

    public Long createNewCustomer(Long partnerId, Customer newCustomer) throws PartnerNotFoundException, UnknownPersistenceException, InputDataValidationException;

}
