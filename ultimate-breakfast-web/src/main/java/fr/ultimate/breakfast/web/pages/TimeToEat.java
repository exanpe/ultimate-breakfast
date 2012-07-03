package fr.ultimate.breakfast.web.pages;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import fr.ultimate.breakfast.common.exception.TechnicalException;
import fr.ultimate.breakfast.domain.business.BreakfastManager;
import fr.ultimate.breakfast.domain.model.Eater;
import fr.ultimate.breakfast.domain.model.Team;
import fr.ultimate.breakfast.domain.security.UltimateBreakfastSecurityContext;
import fr.ultimate.breakfast.web.services.encoder.SecureEaterEncoder;

/**
 * Timetoeat page
 */
@Import(stylesheet = "${exanpe.asset-base}/css/exanpe-t5-lib-skin.css")
public class TimeToEat
{
    @Property
    private int currentIndex;

    @Property
    private Eater currentEater;

    @Property
    @Persist
    private Set<String> selected;

    @Property
    @Persist
    private ValueEncoder<Eater> valueEncoder;

    @Property
    @Persist
    private List<Eater> eaters;

    @Property
    @Persist
    private Date lastNotif;

    @Property
    @Persist(PersistenceConstants.FLASH)
    private String customMsgCall;

    @Property
    @Persist(PersistenceConstants.FLASH)
    private String customMsgSend;

    @Inject
    private BreakfastManager breakfastManager;

    @Inject
    private UltimateBreakfastSecurityContext securityContext;

    void onActivate()
    {
        // always need a fresh copy
        Team p = breakfastManager.find(securityContext.getTeam().getId());
        lastNotif = p.getLastNotification();
        eaters = p.getEaters();

        if (CollectionUtils.isNotEmpty(eaters))
        {
            valueEncoder = new SecureEaterEncoder(eaters);
        }

        if (selected == null)
        {
            selected = new HashSet<String>();
        }
    }

    @OnEvent(value = EventConstants.SUBMIT, component = "callForBreakfast")
    Object callForBreakfast()
    {
        try
        {
            breakfastManager.callTeamForBreakfast(securityContext.getTeam(), customMsgCall);
        }
        catch (TechnicalException e)
        {
            return sendFailure;
        }

        return sendOk;
    }

    public boolean getEaterChecked()
    {
        return selected.contains(eaters.get(currentIndex).getEmail());
    }

    public void setEaterChecked(boolean checked)
    {
        Eater e = eaters.get(currentIndex);
        if (e.getEmail() != null)
        {
            if (checked)
            {
                selected.add(e.getEmail());
            }
            else
            {
                selected.remove(e.getEmail());
            }
        }
    }

    @Inject
    private Block sendOk;

    @Inject
    private Block sendFailure;

    @Inject
    private Block sendEmpty;

    @OnEvent(value = "send")
    Object send()
    {
        if (CollectionUtils.isNotEmpty(selected))
        {

            try
            {
                breakfastManager.notifyEaters(securityContext.getTeam(), selected, customMsgSend);
            }
            catch (TechnicalException e)
            {
                return sendFailure;
            }
            return sendOk;
        }

        return sendEmpty;
    }

    /**
     * @return the currentIndex
     */
    public int getDisplayCurrentIndex()
    {
        return currentIndex + 1;
    }

    public boolean hasNoMail()
    {
        return StringUtils.isEmpty(currentEater.getEmail());
    }

}
