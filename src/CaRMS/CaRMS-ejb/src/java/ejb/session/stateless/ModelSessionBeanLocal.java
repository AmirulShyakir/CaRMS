/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Model;
import java.util.List;
import javax.ejb.Local;
import util.exception.InputDataValidationException;
import util.exception.ModelNameExistException;
import util.exception.ModelNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author dtjldamien
 */
@Local
public interface ModelSessionBeanLocal {

    public Long createNewModel(Model newModel) throws ModelNameExistException, UnknownPersistenceException, InputDataValidationException;

    public List<Model> retrieveAllModels();

    public Model retrieveModelByModelId(Long modelId) throws ModelNotFoundException;

    public void updateModel(Model model) throws ModelNotFoundException, InputDataValidationException;
    
}
