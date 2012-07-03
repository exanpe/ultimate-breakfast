package fr.ultimate.breakfast.web.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

/**
 * Wrapper for Bootstrap Thumbnail
 * 
 * @see http://twitter.github.com/bootstrap/components.html#thumbnails
 * @author lguerin
 */
public class BootstrapThumbnail
{
    /**
     * The title of the Thumbnail caption.
     */
    @SuppressWarnings("unused")
    @Property
    @Parameter(defaultPrefix = BindingConstants.MESSAGE)
    private String title;
}
