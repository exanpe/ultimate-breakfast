/**
 * 
 */
package fr.ultimate.breakfast.domain.business;

import java.util.List;
import java.util.Set;

import fr.ultimate.breakfast.domain.core.business.DefaultManager;
import fr.ultimate.breakfast.domain.model.Eater;
import fr.ultimate.breakfast.domain.model.Team;

public interface BreakfastManager extends DefaultManager<Team, Integer>
{

    Team findByName(String name);

    List<Eater> findEatersByTeam(Team team);

    void deleteEater(Integer id);

    void updateEater(Eater eater);

    void addEater(Team team, Eater eater);

    int getNextMaxPosition(Team team);

    void notifyEaters(Team team, Set<String> emails, String customMsg);

    void incrementEaters(List<Eater> eaters);

    void topEaters(Team p, List<Eater> eaters);

    void commit(Team p, List<Eater> providers, List<Eater> absents);

    void resetPassword(String teamName);

    Eater findEaterByName(Team team, String username);

    void callTeamForBreakfast(Team team, String customMsg);

    long countEaters();

    boolean hasCommittedToday(Team team);

    void updateProfile(Team team, String password);
}
