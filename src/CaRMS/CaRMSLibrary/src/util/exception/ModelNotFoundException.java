/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author dtjldamien
 */
public class ModelNotFoundException extends Exception {

    public ModelNotFoundException() {
    }

    public ModelNotFoundException(String msg) {
        super(msg);
    }
}
