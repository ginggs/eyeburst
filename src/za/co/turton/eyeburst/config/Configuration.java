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
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import za.co.turton.eyeburst.TowerDataThread;
import za.co.turton.eyeburst.TowerNameService;
import za.co.turton.eyeburst.TowerPublisher;
import za.co.turton.eyeburst.config.ConfigurationChangedListener;
import za.co.turton.eyeburst.io.FileMonitorLineProvider;
import za.co.turton.eyeburst.io.SocketMonitorLineProvider;
import za.co.turton.eyeburst.io.SocketMonitorLineProvider;
import za.co.turton.eyeburst.monitor.MonitorFrame;
import za.co.turton.eyeburst.monitor.TowerTableModel;
import za.co.turton.eyeburst.sample.SampleFrame;
import za.co.turton.eyeburst.sample.SampleGroup;
import za.co.turton.eyeburst.sample.SampleGroupPanel;
import za.co.turton.eyeburst.sample.TowerTransferHandler;

public abstract class Configuration {
    
    private static Logger logger;
    
    private static Map<Class, Map<String, Object>> dependencies;
    
    private static Map<Class, PropertyTypeAdapter> adapters;
    
    private static Properties config;
    
    private static Map<Class, Object> globals;
    
    private static Set<WeakReference<Object>> instantiateds;
    
    private static final String CONFIG_PROPERTIES = "conf/config.properties";
    
    private static final String DEV_CONFIG_PROPERTIES = "conf/dev.config.properties";
    
    private static void loadDependencies() throws ConfigurationException {
        dependencies = new HashMap<Class, Map<String, Object>>();
        Map classDeps;
        
        classDeps = getDependencyMapFor(MonitorFrame.class);
        classDeps.put("towerDataThread", TowerDataThread.class);
        classDeps.put("towerTableModel", TowerTableModel.class);
        classDeps.put("chartPanel", za.co.turton.eyeburst.monitor.ChartPanel.class);
        classDeps.put("towerPublisher", TowerPublisher.class);
        classDeps.put("towerNameService", TowerNameService.class);
        classDeps.put("logger", logger);
        
        classDeps = getDependencyMapFor(TowerDataThread.class);
        classDeps.put("lineProvider", SocketMonitorLineProvider.class);
        classDeps.put("towerPublisher", TowerPublisher.class);
        classDeps.put("logger", logger);
        
        classDeps = getDependencyMapFor(SocketMonitorLineProvider.class);
        classDeps.put("logger", logger);
        
        classDeps = getDependencyMapFor(TowerPublisher.class);
        classDeps.put("towerNameService", TowerNameService.class);
        
        classDeps = getDependencyMapFor(TowerNameService.class);
        
        classDeps = getDependencyMapFor(TowerTableModel.class);
        
        classDeps = getDependencyMapFor(za.co.turton.eyeburst.monitor.ChartPanel.class);
        classDeps.put("towerNameService", TowerNameService.class);
        
        classDeps = getDependencyMapFor(SampleFrame.class);
        classDeps.put("chartPanel", za.co.turton.eyeburst.sample.ChartPanel.class);
        
        classDeps = getDependencyMapFor(za.co.turton.eyeburst.sample.ChartPanel.class);
        
        classDeps = getDependencyMapFor(SampleGroup.class);
        classDeps.put("towerPublisher", TowerPublisher.class);
        
        classDeps = getDependencyMapFor(SampleGroupPanel.class);
        classDeps.put("towerPublisher", TowerPublisher.class);
        classDeps.put("towerTransferHandler", TowerTransferHandler.class);
        
        classDeps = getDependencyMapFor(TowerTransferHandler.class);
        classDeps.put("logger", logger);
        
        logger.config("Loaded dependency graph");
    }
    
    public static void initialise() throws ConfigurationException {
        
        logger = Logger.getLogger("Bootstrap");
        logger.getParent().getHandlers()[0].setLevel(Level.FINEST);
        
        loadDependencies();
        loadAdapters();
        loadConfig();
        flushGlobals();
        
        instantiateds = new HashSet<WeakReference<Object>>();        
    }
    
    private static void loadAdapters() {
        
        adapters = new HashMap<Class, PropertyTypeAdapter>();
        
        adapters.put(String.class, new StringAdapter());
        adapters.put(Integer.class, new IntegerAdapter());
        adapters.put(Integer.TYPE, new IntegerAdapter());
        adapters.put(Class.class, new ClassAdapter());
        adapters.put(InetSocketAddress.class, new InetSocketAddressAdapter());
        adapters.put(Properties.class, new PropertiesAdapter());
        adapters.put(Level.class, new LoggerLevelAdapter());
        
        logger.config("Loaded adapters");
    }
    
    private static void loadConfig() throws ConfigurationException {
        InputStream in = null;
        config = new Properties();
        
        try {
            in = new FileInputStream(CONFIG_PROPERTIES);
            config.load(in);
            
            in = new FileInputStream(DEV_CONFIG_PROPERTIES);
            config.load(in);
            
            logger.config("Loaded config properties files");
            
        } catch (FileNotFoundException e) {
            throw new ConfigurationException("Could load configuration properties from disk", e);
        } catch (IOException e) {
            throw new ConfigurationException("Could not read configuration properties from "+in, e);
        }
    }
    
    public static String getProperty(String propertyName) {
        return config.getProperty(propertyName);
    }
    
    public static <T> T getTransformedProperty(Class<T> targetType, String propertyName) throws ConfigurationException{
        try {
            return (T) adapters.get(targetType).transform(config.getProperty(propertyName));
        } catch (AdapterException e) {
            throw new ConfigurationException("Could not transform property "+propertyName, e);
        }
    }
    
    public static void setProperty(String propertyName, String propertyValue) {
        config.setProperty(propertyName, propertyValue);
    }
    
    public static <T> T configure(Class<T> clazz) throws ConfigurationException {
        return configure(clazz, new LinkedList<Class>());
    }
    
    private static <T> T configure(Class<T> clazz, List<Class> classesAbove) throws ConfigurationException {
        
        if (classesAbove.contains(clazz))
            throw new ConfigurationException("Traversed a cycle in the dependency graph, node = "+clazz);
        
        classesAbove.add(clazz);
        
        if (clazz.isAnnotationPresent(Singleton.class)) {
            T instance = (T) globals.get(clazz);
            
            if (instance != null) {
                classesAbove.remove(clazz);
                return instance;
            }
        }
        
        try {
            Constructor<T> constructor = getConstructorFrom(clazz);
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
                        args[i] = configure((Class) dependency, classesAbove);
                    else
                        args[i] = dependency;
                    
                } else {
                    String strValue = config.getProperty(depName);
                    
                    if (strValue == null)
                        throw new ConfigurationException("No dependency or configuration definition for "+depName+" in "+clazz);
                    
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
            
            T instance = constructor.newInstance(args);
            instantiateds.add(new WeakReference(instance));
            
            if (clazz.isAnnotationPresent(Singleton.class))
                globals.put(clazz, instance);
            
            classesAbove.remove(clazz);
            logger.fine("Configured "+instance);
            return instance;
            
        } catch (IllegalAccessException e) {
            throw new ConfigurationException("Could not configure "+clazz, e);
        } catch (InstantiationException e) {
            throw new ConfigurationException("Could not configure "+clazz, e);
        } catch (InvocationTargetException e) {
            throw new ConfigurationException("Could not configure "+clazz, e);
        }
    }
    
    private static <T> Constructor<T> getConstructorFrom(Class<T> clazz) throws ConfigurationException {
        Constructor<T>[] constructors = clazz.getConstructors();
        
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
    
    private static Map<String, ?> getDependencyMapFor(Class clazz) {
        Map classDeps = new HashMap();
        dependencies.put(clazz, classDeps);
        return classDeps;
    }
    
    public static void flushGlobals() {
        
        if (globals == null)
            globals = new HashMap<Class, Object>();
        else
            globals.clear();
        
        logger = Logger.getLogger("eyeBurst");
        logger.setLevel(Level.parse(config.getProperty("loggerLevel")));
    }
    
    static void fireConfigurationChanged() {
        
        flushGlobals();
        
        Set<WeakReference<Object>> deadRefs = new HashSet<WeakReference<Object>>();
        
        for (WeakReference<Object> ref : instantiateds) {
            Object instance = ref.get();
            
            if (instance != null) {
                if (instance instanceof ConfigurationChangedListener)
                    ((ConfigurationChangedListener) instance).configurationChanged();
            } else
                deadRefs.add(ref);
        }
        
        for (WeakReference<Object> deadRef : deadRefs)
            instantiateds.remove(deadRef);
    }
}
