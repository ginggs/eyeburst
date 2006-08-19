/*
 * TowerNameService.java
 *
 * Created on August 16, 2006, 4:56 PM
 *
 */

package za.co.turton.eyeburst;

import java.util.Properties;
import za.co.turton.eyeburst.config.Inject;
import za.co.turton.eyeburst.config.InjectionConstructor;
import za.co.turton.eyeburst.config.Singleton;

/**
 *
 * @author james
 */

public @Singleton class TowerNameService {
    
    private Properties towerNames;        
    
    public @InjectionConstructor TowerNameService(
            @Inject("towerNames") Properties towerNames) {
        
        this.towerNames = towerNames;
    }
    
    public String getTowerName(String towerCode) {
        String towerName = towerNames.getProperty(towerCode);
        
        if (towerName == null)
            towerName = towerCode + " (Unknown)";
        
        return towerName;
    }       
}
