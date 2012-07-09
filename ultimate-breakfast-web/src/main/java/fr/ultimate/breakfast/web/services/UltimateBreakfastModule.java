package fr.ultimate.breakfast.web.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.RequestExceptionHandler;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.services.ResponseRenderer;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;

import fr.exanpe.t5.lib.services.ExanpeLibraryModule;
import fr.ultimate.breakfast.common.constants.BuildProfilesEnum;
import fr.ultimate.breakfast.web.services.exceptionHandler.ExceptionHandlerService;
import fr.ultimate.breakfast.web.services.exceptionHandler.UltimateBreakfastRequestExceptionHandler;

/**
 * This module is automatically included as part of the Tapestry IoC Registry, it's a good place to
 * configure and extend Tapestry, or to place your own service definitions.
 */
@SubModule(
{ ExanpeLibraryModule.class })
public class UltimateBreakfastModule
{

    public static void bind(ServiceBinder binder)
    {
        binder.bind(ApplicationListener.class).eagerLoad();
        binder.bind(UltimateBreakfastApplicationScope.class);
        binder.bind(ExceptionHandlerService.class);

        // Make bind() calls on the binder object to define most IoC services.
        // Use service builder methods (example below) when the implementation
        // is provided inline, or requires more initialization than simply
        // invoking the constructor.
    }

    public static void contributeApplicationDefaults(MappedConfiguration<String, String> configuration, @InjectService("applicationContext")
    ApplicationContext applicationContext)
    {
        // This code is a bridge to Spring profiles
        boolean productionMode = true;

        String[] profiles = applicationContext.getEnvironment().getActiveProfiles();

        if (profiles != null)
        {
            for (String s : profiles)
            {
                if (s.equals(BuildProfilesEnum.EMBEDDED.getValue()))
                {
                    productionMode = false;
                    break;
                }
            }
        }

        // Contributions to ApplicationDefaults will override any contributions to
        // FactoryDefaults (with the same key). Here we're restricting the supported
        // locales to just "en" (English). As you add localised message catalogs and other assets,
        // you can extend this list of locales (it's a comma separated series of locale names;
        // the first locale name is the default when there's no reasonable match).

        configuration.add(SymbolConstants.SUPPORTED_LOCALES, "en,fr");

        // The factory default is true but during the early stages of an application
        // overriding to false is a good idea. In addition, this is often overridden
        // on the command line as -Dtapestry.production-mode=false
        configuration.add(SymbolConstants.PRODUCTION_MODE, Boolean.toString(productionMode));

        // The application version number is incorprated into URLs for some
        // assets. Web browsers will cache assets because of the far future expires
        // header. If existing assets are changed, the version number should also
        // change, to force the browser to download new versions.
        configuration.add(SymbolConstants.APPLICATION_VERSION, "1.0.1-SNAPSHOT");

    }

    /**
     * Tell Tapestry how to handle classpath URLs - we provide a converter to handle JBoss 5.
     * See http://wiki.apache.org/tapestry/HowToRunTapestry5OnJBoss5 .
     */
    @SuppressWarnings("rawtypes")
    public static void contributeServiceOverride(MappedConfiguration<Class, Object> configuration)
    {
        // Si JBoss
        // configuration.add(ClasspathURLConverter.class, new ClasspathURLConverterJBoss5());
    }

    public RequestExceptionHandler decorateRequestExceptionHandler(final Logger logger, final ResponseRenderer renderer, final ComponentSource componentSource,
            @Symbol(SymbolConstants.PRODUCTION_MODE)
            boolean productionMode, Object service, ExceptionHandlerService exceptionService, RequestGlobals request)
    {
        return new UltimateBreakfastRequestExceptionHandler(exceptionService, componentSource, renderer, request, productionMode);
    }

    public static void contributeIgnoredPathsFilter(Configuration<String> configuration)
    {
        configuration.add("/h2.*");
    }
}
