package fr.ultimate.breakfast.domain.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Index;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Team entity
 * 
 * @author jmaupoux
 */
@Entity
@NamedQueries(@NamedQuery(name = Team.FIND_BY_NAME, query = "FROM Team WHERE lower(name) = lower(:name)"))
public class Team implements UserDetails, Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -2554881336855548282L;

    public static final String FIND_BY_NAME = "findByName";

    /**
     * Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     * Name of the team. Also used to log in
     */
    @Column(nullable = false, unique = true)
    @Index(name = "index_team_name")
    private String name;

    /**
     * Password to access the team
     */
    @Column(nullable = false)
    private String password;

    /**
     * Email of the team
     */
    private String email;

    /**
     * Last notification of the providers
     */
    @Temporal(TemporalType.DATE)
    private Date lastNotification;

    /**
     * Eaters
     */
    @OrderBy("position,lastUpdate")
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Eater> eaters = new ArrayList<Eater>();

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
     * @return the password
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password)
    {
        this.password = password;
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

    /**
     * @return the eaters
     */
    public List<Eater> getEaters()
    {
        return eaters;
    }

    /**
     * @param eaters the eaters to set
     */
    public void setEaters(List<Eater> eaters)
    {
        this.eaters = eaters;
    }

    /**
     * @return the lastNotification
     */
    public Date getLastNotification()
    {
        return lastNotification;
    }

    /**
     * @param lastNotification the lastNotification to set
     */
    public void setLastNotification(Date lastNotification)
    {
        this.lastNotification = lastNotification;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();

        auths.add(new SimpleGrantedAuthority("ROLE_TEAM"));

        return auths;
    }

    @Override
    public String getUsername()
    {
        return name;
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }

}
