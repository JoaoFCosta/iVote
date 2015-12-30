/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exception;

/**
 *
 * @author Gustavo
 */
public class FailedInsert extends Exception {
     public FailedInsert () {
        super();
    }
    public FailedInsert (String message) {
        super(message);
    }
}
