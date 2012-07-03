package fr.ultimate.breakfast.web.spring;

import java.io.IOException;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.ResourcePropertySource;

import fr.ultimate.breakfast.common.constants.BuildProfilesEnum;

/**
 * This hack is mandatory in order to boot Spring with a profile from a property file.<br/>
 * configuration in web.xml is prioritary on configuration given in argument (-D...)
 * As web.xml is not parsed by jetty before launch, can't ${} the web.xml...
 * And it is not possible to add -D on server boot for the client, so thats my solution!
 * 
 * @author jmaupoux
 */
public class PropertyApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>
{
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext)
    {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        try
        {
            environment.getPropertySources().addFirst(new ResourcePropertySource("classpath:spring-profile.properties"));
        }
        catch (IOException e)
        {
            environment.setActiveProfiles(BuildProfilesEnum.PROD.getValue());
        }
    }

}
