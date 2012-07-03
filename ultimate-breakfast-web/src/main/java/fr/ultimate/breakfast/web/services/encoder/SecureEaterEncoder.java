package fr.ultimate.breakfast.web.services.encoder;

import java.util.List;

import org.apache.tapestry5.ValueEncoder;

import fr.ultimate.breakfast.domain.model.Eater;

/**
 * Ensure that the id send has not been corrupted
 * 
 * @author jmaupoux
 */
public class SecureEaterEncoder implements ValueEncoder<Eater>
{
    private List<Eater> restrictList;

    public SecureEaterEncoder(List<Eater> restrictList)
    {
        this.restrictList = restrictList;
    }

    @Override
    public String toClient(Eater value)
    {
        return value.getId() + "";
    }

    @Override
    public Eater toValue(String clientValue)
    {
        for (Eater e : restrictList)
        {
            if (e.getId().toString().equals(clientValue)) { return e; }
        }
        return null;
    }
}
