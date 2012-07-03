package fr.ultimate.breakfast.web.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

/**
 * Wrapper for Bootstrap Page Header
 * 
 * @see http://twitter.github.com/bootstrap/components.html#thumbnails
 * @author lguerin
 */
public class BootstrapTitle
{
    /**
     * Display a Bootstrap header with this title.
     */
    @SuppressWarnings("unused")
    @Property
    @Parameter(defaultPrefix = BindingConstants.MESSAGE)
    private String title;
}
