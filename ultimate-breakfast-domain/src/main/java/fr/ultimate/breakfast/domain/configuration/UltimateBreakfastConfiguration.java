package fr.ultimate.breakfast.domain.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Give access through Java to configuration properties
 * 
 * @author jmaupoux
 */
@Component
public class UltimateBreakfastConfiguration
{

    @Value("${mail.enabled}")
    private boolean mailEnabled;

    /**
     * @return the mailEnabled
     */
    public boolean isMailEnabled()
    {
        return mailEnabled;
    }

}
