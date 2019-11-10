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
public class NoAllocatableCarException extends Exception {

    /**
     * Creates a new instance of <code>NoAllocatableCarException</code> without
     * detail message.
     */
    public NoAllocatableCarException() {
    }

    /**
     * Constructs an instance of <code>NoAllocatableCarException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NoAllocatableCarException(String msg) {
        super(msg);
    }
}
