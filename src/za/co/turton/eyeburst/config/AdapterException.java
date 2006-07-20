/*
 * AdaptorException.java
 *
 * Created on July 6, 2006, 1:33 PM
 *
 */

package za.co.turton.eyeburst.config;

/**
 * This exception is thrown by PropertyTypeAdapters during transformation
 * @author james
 */
public class AdapterException extends Exception {
    
    /**
     * Creates a new instance of AdaptorException
     * @param cause the cause of this exception
     */
    public AdapterException(Throwable cause) {
        super(cause);
    }
}
