/**
 * 
 */
package fr.ultimate.breakfast.domain.business.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.mail.MessagingException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import fr.ultimate.breakfast.common.exception.TechnicalException;
import fr.ultimate.breakfast.domain.business.BreakfastManager;
import fr.ultimate.breakfast.domain.business.util.BreakfastBusinessUtil;
import fr.ultimate.breakfast.domain.core.business.impl.DefaultManagerImpl;
import fr.ultimate.breakfast.domain.core.dao.CrudDAO;
import fr.ultimate.breakfast.domain.core.dao.QueryParameters;
import fr.ultimate.breakfast.domain.model.Eater;
import fr.ultimate.breakfast.domain.model.Team;
import fr.ultimate.breakfast.mail.service.UltimateBreakfastMailService;

/**
 * @author lguerin
 */
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
@Component("breakfastManager")
public class BreakfastManagerImpl extends DefaultManagerImpl<Team, Integer> implements BreakfastManager
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BreakfastManagerImpl.class);

    @Autowired
    private CrudDAO crudDAO;

    @Autowired
    private SaltSource saltSource;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UltimateBreakfastMailService mailService;

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Team create(Team team)
    {
        LOGGER.info("Creating team {}", team.getName());
        String pass = team.getPassword();
        encodePassword(team, pass);

        Team t = super.create(team);

        try
        {
            mailService.sendAccountMail(team.getEmail(), team.getName(), pass);
        }
        catch (MessagingException e)
        {
            throw new TechnicalException("Mail send impossible", e);
        }

        return t;
    }

    @Override
    public Team findByName(String name)
    {
        return crudDAO.findUniqueWithNamedQuery(Team.FIND_BY_NAME, QueryParameters.with("name", name).parameters());
    }

    @Override
    public List<Eater> findEatersByTeam(Team team)
    {
        // get a fresh copy
        // TODO faire un refresh
        Team p = find(team.getId());
        return p.getEaters();
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteEater(Integer id)
    {
        Eater e = crudDAO.find(Eater.class, id);

        e.getTeam().getEaters().remove(e);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateEater(Eater eater)
    {
        crudDAO.update(eater);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void addEater(Team team, Eater eater)
    {
        team = crudDAO.find(Team.class, team.getId());
        team.getEaters().add(eater);
        eater.setTeam(team);

        crudDAO.update(team);
    }

    @Override
    public int getNextMaxPosition(Team team)
    {
        Integer nextMax = crudDAO.findUniqueWithNamedQuery(Eater.GET_NEXT_MAX_POSITION, QueryParameters.with("team", team).parameters());
        return nextMax == null ? 0 : nextMax;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void notifyEaters(Team p, Set<String> emails, String customMsg)
    {
        p = find(p.getId());
        p.setLastNotification(new Date());

        update(p);

        try
        {
            mailService.sendBreakfastRequest(new ArrayList<String>(emails), p.getEmail(), p.getName(), customMsg);
        }
        catch (MessagingException e)
        {
            throw new TechnicalException("Mail send impossible", e);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void incrementEaters(List<Eater> eaters)
    {
        if (CollectionUtils.isNotEmpty(eaters))
        {
            for (Eater e : eaters)
            {
                e.setPosition(e.getPosition() + 1);
                crudDAO.update(e);
            }
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void topEaters(Team p, List<Eater> eaters)
    {
        if (CollectionUtils.isNotEmpty(eaters))
        {
            int maxPosition = getNextMaxPosition(p);

            for (Eater e : eaters)
            {
                e.setPosition(maxPosition);
                e.setLastUpdate(new Date());
                crudDAO.update(e);
            }
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void commit(Team p, List<Eater> providers, List<Eater> absents)
    {
        topEaters(p, providers);
        incrementEaters(absents);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void resetPassword(String teamName)
    {
        LOGGER.info("Reseting password for team {}", teamName);

        Team p = findByName(teamName);

        String password = BreakfastBusinessUtil.generatePassword();

        encodePassword(p, password);

        try
        {
            mailService.sendAccountMail(p.getEmail(), p.getName(), password);
        }
        catch (MessagingException e)
        {
            throw new TechnicalException("Mail send impossible", e);
        }
    }

    private void encodePassword(Team p, String password)
    {
        p.setPassword(this.passwordEncoder.encodePassword(password, this.saltSource.getSalt(p)));
    }

    @Override
    public Eater findEaterByName(Team team, String username)
    {
        return crudDAO.findUniqueWithNamedQuery(Eater.FIND_BY_TEAM_AND_NAME, QueryParameters.with("team", team).and("username", username).parameters());
    }

    @Override
    public void callTeamForBreakfast(Team team, String customMsg)
    {
        List<Eater> eaters = findEatersByTeam(team);

        if (CollectionUtils.isEmpty(eaters)) { return; }

        List<String> emails = new ArrayList<String>(eaters.size());

        for (Eater e : eaters)
        {
            if (StringUtils.isNotEmpty(e.getEmail()))
            {
                emails.add(e.getEmail());
            }
        }

        try
        {
            mailService.callTeamForBreakfast(emails, team.getName(), customMsg);
        }
        catch (MessagingException e1)
        {
            throw new TechnicalException("Mail send impossible", e1);
        }
    }

    @Override
    public long countEaters()
    {
        return crudDAO.count(Eater.class);
    }

}
