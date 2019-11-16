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
public class ReturnDateBeforePickupDateException extends Exception {

    /**
     * Creates a new instance of
     * <code>ReturnDateBeforePickupDateException</code> without detail message.
     */
    public ReturnDateBeforePickupDateException() {
    }

    /**
     * Constructs an instance of
     * <code>ReturnDateBeforePickupDateException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public ReturnDateBeforePickupDateException(String msg) {
        super(msg);
    }
}
