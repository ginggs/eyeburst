/*
 * Configuration.java
 *
 * Created on July 4, 2006, 12:24 PM
 *
 */

package za.co.turton.eyeburst.config;

import java.beans.Introspector;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;

/**
 * Central registry of configuration properties that can be set with Strings by
 * means of appropriate <code>PropertyTypeAdapter</code>s and can configure all
 * of its properties from a properties file.
 *
 * @author james
 */
public abstract class Configuration {
    
    private static final Logger logger = Logger.getLogger("eyeBurst");
    
    private static String appTitle;
    
    private static int signalLowerBound;
    
    private static String chartTitle;
    
    private static String xAxisTitle;
    
    private static String yAxisTitle;
    
    private static String utdDebugMarker;
    
    private static String alignedCode;
    
    private static String dataCode;
    
    private static Class lineProvider;
    
    private static int writerSleep;
    
    private static String resourcePath;
    
    private static int connectTimeout;
    
    private static int readTimeout;
    
    private static String utdCurrentTower;
    
    private static String utdDebugOn;
    
    private static String utdDebugOff;
    
    private static InetSocketAddress utdIPAddress;
    
    private static int chartDataExpiry;
    
    private static int fileLineProviderInterval;
    
    private static Properties towerNames;
    
    private static Map<Class, PropertyTypeAdapter> adapters;
    
    private static final String CONFIG_PROPERTIES = "conf/config.properties";
        
    private static final String DEV_CONFIG_PROPERTIES = "conf/dev.config.properties";
    
    /**
     * Loads configuration properties from the CONFIG_PROPERTIES file and attempts
     * to set all of this class's properties from it.  Each property string value read
     * in is passed through the registered type adapter for the argument type of
     * the setter for the relevant property
     *
     * @throws za.co.turton.eyeburst.config.ConfigurationException if there is an error while loading configuration
     */
    public static void configure() throws ConfigurationException {
                
        logger.getParent().getHandlers()[0].setLevel(Level.FINEST);
        logger.setLevel(Level.CONFIG);
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());                    
        } catch (Exception e) {
            logger.log(Level.WARNING, "Could not set system look and feel", e);
        }
        
        loadAdapters();
        Properties config = new Properties();
        InputStream in = null;
        
        try {
            in = new FileInputStream(CONFIG_PROPERTIES);
            config.load(in);            
            
            in = new FileInputStream(DEV_CONFIG_PROPERTIES);
            config.load(in);
        
        } catch (FileNotFoundException e) {
            throw new ConfigurationException("Could load configuration proeprties from disk", e);
        } catch (IOException e) {
            throw new ConfigurationException("Could not read configuration properties from "+in, e);
        }        
        
        for (Method method : Configuration.class.getDeclaredMethods()) {
            
            if (!method.isAnnotationPresent(Configure.class))
                continue;
            
            if (!method.getName().startsWith("set") || method.getParameterTypes().length != 1)
                throw new ConfigurationException("@Configure annotation is only allowed on one-argument setters");
            
            String name = Introspector.decapitalize(method.getName().substring(3));
            String value = config.getProperty(name);
            
            if (value == null)
                logger.warning("Property \""+name+"\" not found in configuration file");
            
            transformAndSet(value, method);
            config.remove(name);
        }
        
        for (Object key : config.keySet())
            logger.log(Level.WARNING, "Property "+key+" in configuration file is not configurable");
    }
    
    /**
     * Transforms and injects a property value
     *
     * @param value the string property value to be transformed and injected
     * @param method the setter identified for the property in question
     * @throws za.co.turton.eyeburst.config.ConfigurationException
     */
    private static void transformAndSet(String value, Method method) throws ConfigurationException {
        
        Object arg = null;
        Class type = method.getParameterTypes()[0];
        
        try {
            PropertyTypeAdapter adapter = adapters.get(type);
            
            if (adapter == null)
                throw new ConfigurationException("No adapter registered for type "+type);
            
            arg = adapter.transform(value);
            method.invoke(null, arg);
        } catch (Exception e) {
            throw new ConfigurationException("Exception calling "+method.getName(), e);
        }
        
        logger.log(Level.CONFIG, method.getName()+": "+arg);
    }
    
    /**
     * Transforms and injects a property value
     *
     * @param name the name of the property to transformAndSet into
     * @param value the string property value to be transformed and injected
     * @throws za.co.turton.eyeburst.config.ConfigurationException if an error occurs while trying to set a property
     */
    public static void transformAndSet(String name, String value) throws ConfigurationException {
        
        String setterName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
        
        //@todo: fix me
        Method[] methods = Configuration.class.getDeclaredMethods();
        int i;
        
        for (i = 0; i < methods.length; i++)
            if (methods[i].getName().equals(setterName))
                break;
        
        if (i == methods.length)
            throw new ConfigurationException("Setter "+setterName+" not found");
        
        transformAndSet(value, methods[i]);
    }
    
    private static void loadAdapters() {
        // @todo: Find a better way to load adapters?
        
        adapters = new HashMap<Class, PropertyTypeAdapter>();
        adapters.put(String.class, new StringAdapter());
        adapters.put(Integer.class, new IntegerAdapter());
        adapters.put(Class.class, new ClassAdapter());
        adapters.put(InetSocketAddress.class, new InetSocketAddressAdapter());
        adapters.put(Properties.class, new PropertiesAdapter());
        adapters.put(Level.class, new LoggerLevelAdapter());
    }
    
    @Configure
            public static void setAppTitle(String aAppTitle) {
        appTitle = aAppTitle;
    }
    
    @Configure
            public static void setSignalLowerBound(Integer aSignalLowerBound) {
        signalLowerBound = aSignalLowerBound;
    }
    
    @Configure
            public static void setChartTitle(String aChartTitle) {
        chartTitle = aChartTitle;
    }
    
    @Configure
            public static void setXAxisTitle(String aXAxisTitle) {
        xAxisTitle = aXAxisTitle;
    }
    
    @Configure
            public static void setYAxisTitle(String aYAxisTitle) {
        yAxisTitle = aYAxisTitle;
    }
    
    @Configure
            public static void setUtdDebugMarker(String aUtdDebugMarker) {
        utdDebugMarker = aUtdDebugMarker;
    }
    
    @Configure
            public static void setAlignedCode(String aAlignedCode) {
        alignedCode = aAlignedCode;
    }
    
    @Configure
            public static void setDataCode(String aDataCode) {
        dataCode = aDataCode;
    }
    
    @Configure
            public static void setLineProvider(Class aLineProvider) {
        lineProvider = aLineProvider;
    }
    
    @Configure
            public static void setWriterSleep(Integer aWriterSleep) {
        writerSleep = aWriterSleep;
    }
    
    @Configure
            public static void setResourcePath(String aResourcePath) {
        resourcePath = aResourcePath;
    }
    
    @Configure
            public static void setConnectTimeout(Integer aConnectTimeout) {
        connectTimeout = aConnectTimeout;
    }
    
    @Configure
            public static void setReadTimeout(Integer aReadTimeout) {
        readTimeout = aReadTimeout;
    }
    
    @Configure
            public static void setUtdCurrentTower(String aUtdCurrentTower) {
        utdCurrentTower = aUtdCurrentTower;
    }
    
    @Configure
            public static void setUtdDebugOn(String aUtdDebugOn) {
        utdDebugOn = aUtdDebugOn;
    }
    
    @Configure
            public static void setUtdDebugOff(String aUtdDebugOff) {
        utdDebugOff = aUtdDebugOff;
    }
    
    @Configure
            public static void setUtdIPAddress(InetSocketAddress aUtdIPAddress) {
        utdIPAddress = aUtdIPAddress;
    }
    
    @Configure
            public static void setChartDataExpiry(Integer aChartDataExpiry) {
        chartDataExpiry = aChartDataExpiry;
    }
    
    @Configure
            public static void setFileLineProviderInterval(Integer aFileLineProviderInterval) {
        fileLineProviderInterval = aFileLineProviderInterval;
    }
    
    @Configure
            public static void setLoggerLevel(Level aLoggerLevel) {
        logger.setLevel(aLoggerLevel);
    }
    
    @Configure
            public static void setTowerNames(Properties aTowerNames) {
        towerNames = aTowerNames;
    }
    
    public static Logger getLogger() {
        return logger;
    }
    
    public static String getAppTitle() {
        return appTitle;
    }
    
    public static int getSignalLowerBound() {
        return signalLowerBound;
    }
    
    public static String getChartTitle() {
        return chartTitle;
    }
    
    public static String getXAxisTitle() {
        return xAxisTitle;
    }
    
    public static String getYAxisTitle() {
        return yAxisTitle;
    }
    
    public static String getUtdDebugMarker() {
        return utdDebugMarker;
    }
    
    public static String getAlignedCode() {
        return alignedCode;
    }
    
    public static String getDataCode() {
        return dataCode;
    }
    
    public static Class getLineProvider() {
        return lineProvider;
    }
    
    public static int getWriterSleep() {
        return writerSleep;
    }
    
    public static String getResourcePath() {
        return resourcePath;
    }
    
    public static int getConnectTimeout() {
        return connectTimeout;
    }
    
    public static int getReadTimeout() {
        return readTimeout;
    }
    
    public static String getUtdCurrentTower() {
        return utdCurrentTower;
    }
    
    public static String getUtdDebugOn() {
        return utdDebugOn;
    }
    
    public static String getUtdDebugOff() {
        return utdDebugOff;
    }
    
    public static InetSocketAddress getUtdIPAddress() {
        return utdIPAddress;
    }
    
    public static int getChartDataExpiry() {
        return chartDataExpiry;
    }
    
    public static int getFileLineProviderInterval() {
        return fileLineProviderInterval;
    }
    
    public static Properties getTowerNames() {
        return towerNames;
    }
}