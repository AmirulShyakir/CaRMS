/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Model;
import java.util.List;
import javax.ejb.Local;
import util.exception.CarCategoryNotFoundException;
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

    public List<Model> retrieveAllModels();

    public Model retrieveModelByModelId(Long modelId) throws ModelNotFoundException;

    public Long createNewModel(Long carCategoryId, Model newModel) throws CarCategoryNotFoundException, ModelNameExistException, UnknownPersistenceException, InputDataValidationException;

    public void deleteModel(Long modelId) throws ModelNotFoundException;

    public void updateModel(Long carCategoryId, Model model) throws CarCategoryNotFoundException, ModelNotFoundException, InputDataValidationException;
    
}
