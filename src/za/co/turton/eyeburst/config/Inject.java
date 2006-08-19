/*
 * Inject.java
 *
 * Created on August 16, 2006, 2:12 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package za.co.turton.eyeburst.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author james
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface Inject {
    String value();
}
