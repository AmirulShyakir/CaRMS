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
public class OutsideOperatingHoursException extends Exception {

    /**
     * Creates a new instance of <code>OutsideOperatingHoursException</code>
     * without detail message.
     */
    public OutsideOperatingHoursException() {
    }

    /**
     * Constructs an instance of <code>OutsideOperatingHoursException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public OutsideOperatingHoursException(String msg) {
        super(msg);
    }
}
