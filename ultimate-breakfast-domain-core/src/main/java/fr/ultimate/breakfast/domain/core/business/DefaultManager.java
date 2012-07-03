package fr.ultimate.breakfast.domain.core.business;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Service générique implémentant les opérations redondantes (CRUD) communes à tous les
 * <code>Manager</code>.
 * 
 * @author lguerin
 */
public interface DefaultManager<T, PK extends Serializable>
{
    /**
     * Créé une nouvelle instance.
     * 
     * @param <T> Type de l'objet a creer
     * @param newInstance Instance a créer
     */
    T create(T t);

    /**
     * Met a jour l'objet transmis.
     * 
     * @param <T> Type de l'objet a mettre a jour
     * @param t Objet a mettre a jour
     * @return l'objet persiste
     */
    T update(T t);

    /**
     * Supprime l'objet identifie par la cle primaire.
     * 
     * @param <T>
     * @param <PK>
     * @param type
     *            , entity class type
     * @param id
     */
    void delete(PK id);

    /**
     * Retrouve un objet par cle primaire.
     * 
     * @param <T> Type de l'objet retourné
     * @param <PK> Identifiant de l'objet
     * @param type Type de l'objet a rechercher
     * @param id Clé primaire
     * @return the object
     */
    T find(PK id);

    /**
     * Recupere un nombre maximum de resultats, a partir d'une requete nommee et parametres.
     * 
     * @param <T> Type des objets a retourner
     * @param queryName Identifiant de la requere nommee
     * @param startIndex Index de départ
     * @param endIndex Index de fin
     * @param params Parametres de la requete
     * @return
     */
    List<T> findRangeResultsWithNamedQuery(String queryName, int startIndex, int endIndex, Map<String, Object> params);

    /**
     * Dénombre le nombre d'entités de type <code>T</code>.
     * 
     * @return le nombre d'entités de type <code>T</code>
     */
    long count();

    /**
     * Dénombre le nombre d'entités de type <code>T</code> répondant aux critères
     * <code>params</code>.
     * 
     * @param queryName Requête nommée
     * @param params Paramètres de la requête
     * @return le nombre d'entités de type <code>T</code>
     */
    long count(String queryName, Map<String, Object> params);
}
