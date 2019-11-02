/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Outlet;
import util.exception.InputDataValidationException;
import util.exception.OutletNameExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author dtjldamien
 */
public interface OutletSessionBeanLocal {

    public Long createNewOutlet(Outlet newOutlet) throws OutletNameExistException, UnknownPersistenceException, InputDataValidationException;
    
}
