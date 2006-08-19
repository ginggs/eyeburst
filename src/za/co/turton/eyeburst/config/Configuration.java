/*
 * Configuration.java
 *
 * Created on July 4, 2006, 12:24 PM
 *
 */

package za.co.turton.eyeburst.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import za.co.turton.eyeburst.TowerDataThread;
import za.co.turton.eyeburst.TowerNameService;
import za.co.turton.eyeburst.TowerPublisher;
import za.co.turton.eyeburst.io.FileMonitorLineProvider;
import za.co.turton.eyeburst.monitor.ChartCanvas;
import za.co.turton.eyeburst.monitor.MonitorFrame;
import za.co.turton.eyeburst.monitor.TowerTableModel;

public abstract class Configuration {
    
    private static final Logger logger = Logger.getLogger("eyeBurst");
    
    private static Map<Class, Map<String, Object>> dependencies;
    
    private static Map<Class, PropertyTypeAdapter> adapters;
    
    private static Properties config;
    
    private static Map<Class, Object> globals;
    
    private static final String CONFIG_PROPERTIES = "conf/config.properties";
    
    private static final String DEV_CONFIG_PROPERTIES = "conf/dev.config.properties";
    
    private static Map<Class, Map<String, Object>> loadDependencies() throws ConfigurationException {
        Map<Class, Map<String, Object>> dependencies = new HashMap<Class, Map<String, Object>>();
        Map<String, Object> classDeps;
        
        classDeps = new HashMap<String, Object>();
        dependencies.put(MonitorFrame.class, classDeps);
        
        classDeps.put("towerDataThread", TowerDataThread.class);
        classDeps.put("towerTableModel", TowerTableModel.class);
        classDeps.put("chartCanvas", ChartCanvas.class);
        classDeps.put("towerPublisher", TowerPublisher.class);
        classDeps.put("towerNameService", TowerNameService.class);
        classDeps.put("logger", logger);
        
        classDeps = new HashMap<String, Object>();
        dependencies.put(TowerDataThread.class, classDeps);
        
        classDeps.put("lineProvider", FileMonitorLineProvider.class);
        classDeps.put("towerPublisher", TowerPublisher.class);
        classDeps.put("logger", logger);
        
        classDeps = new HashMap<String, Object>();
        dependencies.put(FileMonitorLineProvider.class, classDeps);
        
        classDeps.put("logger", logger);
        
        classDeps = new HashMap<String, Object>();
        dependencies.put(TowerPublisher.class, classDeps);
        
        classDeps.put("towerNameService", TowerNameService.class);
        
        classDeps = new HashMap<String, Object>();
        dependencies.put(TowerNameService.class, classDeps);
        
        classDeps = new HashMap<String, Object>();
        dependencies.put(TowerTableModel.class, classDeps);
                
        classDeps = new HashMap<String, Object>();
        dependencies.put(ChartCanvas.class, classDeps);
        
        classDeps.put("towerNameService", TowerNameService.class);
        
        return dependencies;
    }
    
    public static void initialise() throws ConfigurationException {
        
        logger.getParent().getHandlers()[0].setLevel(Level.FINEST);
        logger.setLevel(Level.CONFIG);
        
        dependencies = loadDependencies();
        adapters = loadAdapters();
        config = loadConfig();
        globals = new HashMap<Class, Object>();
        
        logger.setLevel(Level.parse(config.getProperty("loggerLevel")));
    }
    
    private static Map<Class, PropertyTypeAdapter> loadAdapters() {
        Map<Class, PropertyTypeAdapter> adapters = new HashMap<Class, PropertyTypeAdapter>();
        
        adapters.put(String.class, new StringAdapter());
        adapters.put(Integer.class, new IntegerAdapter());
        adapters.put(Integer.TYPE, new IntegerAdapter());
        adapters.put(Class.class, new ClassAdapter());
        adapters.put(InetSocketAddress.class, new InetSocketAddressAdapter());
        adapters.put(Properties.class, new PropertiesAdapter());
        adapters.put(Level.class, new LoggerLevelAdapter());
        
        return adapters;
    }
    
    private static Properties loadConfig() throws ConfigurationException {
        Properties config = new Properties();
        InputStream in = null;
        
        try {
            in = new FileInputStream(CONFIG_PROPERTIES);
            config.load(in);
            
            in = new FileInputStream(DEV_CONFIG_PROPERTIES);
            config.load(in);
            
            return config;
            
        } catch (FileNotFoundException e) {
            throw new ConfigurationException("Could load configuration proeprties from disk", e);
        } catch (IOException e) {
            throw new ConfigurationException("Could not read configuration properties from "+in, e);
        }
    }
    
    public static Object configure(Class clazz) throws ConfigurationException {
        return configure(clazz, new HashSet<Class>());
    }
    
    private static Object configure(Class clazz, Set<Class> alreadySeen) throws ConfigurationException {
        
//        if (alreadySeen.contains(clazz))
//            throw new ConfigurationException("Traversed a cycle in the dependency graph");
        
        alreadySeen.add(clazz);
        
        if (clazz.isAnnotationPresent(Singleton.class)) {
            Object instance = globals.get(clazz);
            
            if (instance != null)
                return instance;
        }
        
        try {
            Constructor constructor = getConstructorFrom(clazz);
            Annotation[][] allAnnotations = constructor.getParameterAnnotations();
            Class[] argTypes = constructor.getParameterTypes();
            Object[] args = new Object[argTypes.length];
            Map<String, Object> classDeps = dependencies.get(clazz);
            
            if (classDeps == null)
                throw new ConfigurationException("Class "+clazz+" not registered");
                                            
            for (int i = 0; i < argTypes.length; i++) {
                String depName = getDependencyName(allAnnotations[i]);                                                
                Object dependency = classDeps.get(depName);
                
                if (dependency != null) {
                    if (dependency instanceof Class)
                        args[i] = configure((Class) dependency, alreadySeen);
                    else
                        args[i] = dependency;
                    
                } else {
                    String strValue = config.getProperty(depName);
                    
                    if (strValue == null)
                        throw new ConfigurationException("No configuration definition for "+depName+" in "+clazz);
                    
                    try {
                        PropertyTypeAdapter adapter = adapters.get(argTypes[i]);
                        
                        if (adapter == null)
                            throw new ConfigurationException("No adapter defined for type "+argTypes[i]);
                        
                        args[i] = adapter.transform(strValue);
                        
                    } catch (AdapterException e) {
                        throw new ConfigurationException("Could not transform "+strValue+" for dependency "+depName);
                    }
                }
            }
            
            Object instance = constructor.newInstance(args);
            
            if (clazz.isAnnotationPresent(Singleton.class))
                globals.put(clazz, instance);
            
            return instance;
            
            
        } catch (IllegalAccessException e) {
            throw new ConfigurationException("Could not configure "+clazz, e);
        } catch (InstantiationException e) {
            throw new ConfigurationException("Could not configure "+clazz, e);
        } catch (InvocationTargetException e) {
            throw new ConfigurationException("Could not configure "+clazz, e);
        }
    }
    
    private static Method getSetter(Class clazz, String fieldName) throws NoSuchMethodException {
        StringBuffer setterName = new StringBuffer("set");
        setterName.append(Character.toUpperCase(fieldName.charAt(0)));
        setterName.append(fieldName.substring(1));
        return clazz.getMethod(setterName.toString());
    }
    
    private static Constructor getConstructorFrom(Class clazz) throws ConfigurationException {
        Constructor[] constructors = clazz.getConstructors();
        
        for (int i = 0; i < constructors.length; i++) {
            if (constructors[i].isAnnotationPresent(InjectionConstructor.class))
                return constructors[i];
            
        }
        
        throw new ConfigurationException("No constructor annotated with InjectionConstructor found in "+clazz);
    }
    
    private static String getDependencyName(Annotation[] annotations) throws ConfigurationException {
        for (Annotation annotation : annotations)
            if (Inject.class.isAssignableFrom(annotation.annotationType()))
                return ((Inject) annotation).value();
        
        throw new ConfigurationException("No Inject annotation found");
    }
}
