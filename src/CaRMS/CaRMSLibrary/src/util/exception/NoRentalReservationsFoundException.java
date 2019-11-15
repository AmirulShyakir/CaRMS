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
public class NoRentalReservationsFoundException extends Exception {

    /**
     * Creates a new instance of <code>NoRentalReservationsFoundException</code>
     * without detail message.
     */
    public NoRentalReservationsFoundException() {
    }

    /**
     * Constructs an instance of <code>NoRentalReservationsFoundException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public NoRentalReservationsFoundException(String msg) {
        super(msg);
    }
}
