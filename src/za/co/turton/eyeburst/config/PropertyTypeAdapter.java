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
    public Object transform(String property) throws AdapterException;
}

class StringAdapter implements PropertyTypeAdapter {
    public String transform(String property) {
        return property;
    }
}

class IntegerAdapter implements PropertyTypeAdapter {
    public Integer transform(String property) throws AdapterException {
        try {
            return Integer.parseInt(property);
        } catch (NumberFormatException e) {
            throw new AdapterException(e);
        }
    }
}

class ClassAdapter implements PropertyTypeAdapter {
    public Class transform(String property) throws AdapterException {
        try {
            return Class.forName(property);
        } catch (ClassNotFoundException e) {
            throw new AdapterException(e);
        }
    }
}

class InetSocketAddressAdapter implements PropertyTypeAdapter {
    public InetSocketAddress transform(String property) {
        String[] addr = property.split(":");
        return new InetSocketAddress(addr[0], Integer.parseInt(addr[1]));
    }
}

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

class LoggerLevelAdapter implements PropertyTypeAdapter {
    public Level transform(String property) {
        return Level.parse(property);
    }
}