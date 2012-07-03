package fr.ultimate.breakfast.domain.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Index;

/**
 * Breakfast Eater
 * 
 * @author jmaupoux
 */
@Entity
@NamedQueries(
{ @NamedQuery(name = Eater.GET_NEXT_MAX_POSITION, query = "SELECT max(position)+1 FROM Eater WHERE team = :team"),
        @NamedQuery(name = Eater.FIND_BY_TEAM_AND_NAME, query = "SELECT e FROM Eater e WHERE team = :team and e.name = :username") })
public class Eater implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 4840317372659084101L;

    public static final String GET_NEXT_MAX_POSITION = "getNextMaxPosition";
    public static final String FIND_BY_TEAM_AND_NAME = "findByTeamAndName";

    /**
     * Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     * Name as Sopra email style
     */
    @Column(nullable = false)
    private String name;

    /**
     * Email of the eater
     */
    private String email;

    /**
     * Position to make breakfast. Low position have to do it FIRST !
     */
    @Column(nullable = false)
    private Integer position;

    /**
     * Last time the update have been performed for that user (last breakfast commit)
     */
    @Temporal(TemporalType.DATE)
    private Date lastUpdate;

    /**
     * Team
     */
    @ManyToOne
    @Index(name = "index_eater_team")
    private Team team;

    @PrePersist
    void prePersist()
    {
        if (position == null)
        {
            position = 0;
        }
    }

    /**
     * @return the id
     */
    public Integer getId()
    {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id)
    {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return the position
     */
    public Integer getPosition()
    {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(Integer position)
    {
        this.position = position;
    }

    /**
     * @return the lastUpdate
     */
    public Date getLastUpdate()
    {
        return lastUpdate;
    }

    /**
     * @param lastUpdate the lastUpdate to set
     */
    public void setLastUpdate(Date lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }

    /**
     * @return the team
     */
    public Team getTeam()
    {
        return team;
    }

    /**
     * @param team the team to set
     */
    public void setTeam(Team team)
    {
        this.team = team;
    }

    /**
     * @return the email
     */
    public String getEmail()
    {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email)
    {
        this.email = email;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) { return false; }
        if (id == null) { return false; }
        return id.equals(((Eater) obj).getId());
    }

    @Override
    public int hashCode()
    {
        return (id + "").hashCode();
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
