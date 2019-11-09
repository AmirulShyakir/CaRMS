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
public class CarDisabledException extends Exception {

    /**
     * Creates a new instance of <code>CarDisabledException</code> without
     * detail message.
     */
    public CarDisabledException() {
    }

    /**
     * Constructs an instance of <code>CarDisabledException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CarDisabledException(String msg) {
        super(msg);
    }
}
