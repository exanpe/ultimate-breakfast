package fr.ultimate.breakfast.web.components;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;

public class Help
{
    private static final String PAGE_HELP_MESSAGE = "help-message";

    @Inject
    private ComponentResources componentResources;

    @SetupRender
    boolean init()
    {
        return componentResources.getPage().getComponentResources().getMessages().contains(PAGE_HELP_MESSAGE);
    }

    public String getHelpMessage()
    {
        return componentResources.getPage().getComponentResources().getMessages().get(PAGE_HELP_MESSAGE);
    }
}
