/*
 * ConfigurationException.java
 *
 * Created on July 8, 2006, 10:23 AM
 *
 */

package za.co.turton.eyeburst.config;

/**
 * Thrown when exceptional circumstances arise during configuration
 * @author james
 */
public class ConfigurationException extends RuntimeException {
    /**
     * 
     * @see Exception
     */
    public ConfigurationException(Throwable cause) {
        super(cause);
    }
    
    /**
     * 
     * @see Exception
     */
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * 
     * @see Exception
     */
    public ConfigurationException(String message) {
        super(message);
    }
}
