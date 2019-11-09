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
public class RentalRateDisabledException extends Exception {

    /**
     * Creates a new instance of <code>RentalRateDisabledException</code>
     * without detail message.
     */
    public RentalRateDisabledException() {
    }

    /**
     * Constructs an instance of <code>RentalRateDisabledException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public RentalRateDisabledException(String msg) {
        super(msg);
    }
}
