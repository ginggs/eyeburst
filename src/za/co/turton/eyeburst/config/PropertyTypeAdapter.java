/*
 * PropertyTypeAdapter.java
 *
 * Created on July 5, 2006, 10:20 AM
 *
 */

package za.co.turton.eyeburst.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.logging.Level;

/**
 * A <code>PropertyTypeAdapter</code> converts a string into a configuration
 * type by applying a transformation.  It is intended that transformations should
 * be generic so that they may be reused over multiple configuration properties.
 *
 * @author james
 */
public interface PropertyTypeAdapter {
    /**
     * Transforms from a string to another configuration type
     * @param property the string value to be transformed
     * @throws za.co.turton.eyeburst.config.AdapterException if an exception is encountered during transformation
     * @return the transformed configuration property
     */
    public Object transform(String property) throws AdapterException;
}

/**
 * The identity transformation
 */
class StringAdapter implements PropertyTypeAdapter {
    public String transform(String property) {
        return property;
    }
}

/**
 * Parses an Integer
 */
class IntegerAdapter implements PropertyTypeAdapter {
    public Integer transform(String property) throws AdapterException {
        try {
            return Integer.parseInt(property);
        } catch (NumberFormatException e) {
            throw new AdapterException(e);
        }
    }
}

/**
 * Loads a named class
 */
class ClassAdapter implements PropertyTypeAdapter {
    public Class transform(String property) throws AdapterException {
        try {
            return Class.forName(property);
        } catch (ClassNotFoundException e) {
            throw new AdapterException(e);
        }
    }
}

/**
 * Parses an IP address in the format DOTTED_QUAD:PORT
 */
class InetSocketAddressAdapter implements PropertyTypeAdapter {
    public InetSocketAddress transform(String property) {
        String[] addr = property.split(":");
        return new InetSocketAddress(addr[0], Integer.parseInt(addr[1]));
    }
}

/**
 * Loads the named properties file into a new <code>Properties</code> object
 */
class PropertiesAdapter implements PropertyTypeAdapter {
    public Properties transform(String property) throws AdapterException {
        try {
            Properties props = new Properties();
            InputStream in = new FileInputStream(property);
            props.load(in);
            return props;
        } catch (IOException e) {
            throw new AdapterException(e);
        }
    }
}

/**
 * Parses a logger level
 */
class LoggerLevelAdapter implements PropertyTypeAdapter {
    public Level transform(String property) {
        return Level.parse(property);
    }
}