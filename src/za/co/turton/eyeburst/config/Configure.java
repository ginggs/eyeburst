/*
 * Configure.java
 *
 * Created on July 15, 2006, 3:50 PM
 *
 */

package za.co.turton.eyeburst.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * When present on a one-argument public setter method in the <code>Configuration</code> class,
 * indicates that the annotated property holds configuration data and should be configured
 * @author james
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Configure {
}
