/*
 * ConfigurationChangedListener.java
 *
 * Created on August 25, 2006, 10:33 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package za.co.turton.eyeburst.config;

import java.util.EventListener;

/**
 *
 * @author james
 */
public interface ConfigurationChangedListener extends EventListener {
    public void configurationChanged();
}
