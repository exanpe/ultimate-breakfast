package fr.ultimate.breakfast.domain.core.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Interface pour les operations CRUD realisees sur la base de donnees.
 * (DRY concept)
 * 
 * @author lguerin
 */
public interface CrudDAO
{
    /**
     * Cree une nouvelle entree en base pour le type donne.
     * Apres appel de cette methode, l'entite sera persistee en base de donnees puis rafraichie.
     * La Session courante sera egalement flushee.
     * 
     * @param <T> Type de l'objet a creer
     * @param t Objet a persister
     * @return l'objet persiste
     */
    <T> T create(T t);

    /**
     * Met a jour l'objet transmis.
     * 
     * @param <T> Type de l'objet a mettre a jour
     * @param t Objet a mettre a jour
     * @return l'objet persiste
     */
    <T> T update(T t);

    /**
     * Supprime l'objet identifie par la cle primaire.
     * 
     * @param <T>
     * @param <PK>
     * @param type
     *            , entity class type
     * @param id
     */
    <T, PK extends Serializable> void delete(Class<T> type, PK id);

    /**
     * Retrouve un objet par cle primaire.
     * 
     * @param <T> Type de l'objet retourné
     * @param <PK> Identifiant de l'objet
     * @param type Type de l'objet a rechercher
     * @param id Cl� primaire
     * @return the object
     */
    <T, PK extends Serializable> T find(Class<T> type, PK id);

    /**
     * Récupère une liste d'objets a partir d'une requete nommee
     * 
     * @param <T> Type des objets a retourner
     * @param queryName Identifiant de la requere nommee
     * @return returns a list of objects
     */
    <T> List<T> findWithNamedQuery(String queryName);

    /**
     * Recupere une liste d'objets a partir d'une requete nommee et d'un ensemble de parametres.
     * 
     * @param <T> Type des objets a retourner
     * @param queryName Identifiant de la requere nommee
     * @param params Parametres de la requete
     * @return resulting list
     */
    <T> List<T> findWithNamedQuery(String queryName, Map<String, Object> params);

    /**
     * Recupere un resultat unique, a partir d'une requete nommee, sans parametre.
     * 
     * @param <T> Type des objets a retourner
     * @param queryName Identifiant de la requere nommee
     * @return T object
     */
    <T> T findUniqueWithNamedQuery(String queryName);

    /**
     * Recupere un resultat unique, a partir d'une requete nommee et parametres.
     * 
     * @param <T> Type des objets a retourner
     * @param queryName Identifiant de la requere nommee
     * @param params Parametres de la requete
     * @return T object
     */
    <T> T findUniqueWithNamedQuery(String queryName, Map<String, Object> params);

    /**
     * Recupere un nombre maximum de resultats, a partir d'une requete nommee, sans parametres.
     * 
     * @param <T> Type des objets a retourner
     * @param queryName Identifiant de la requere nommee
     * @param range Nombre de resultats a retourner
     * @return liste resultat
     */
    <T> T findMaxResultsWithNamedQuery(String queryName, int range);

    /**
     * Recupere un nombre maximum de resultats, a partir d'une requete nommee et parametres.
     * 
     * @param <T> Type des objets a retourner
     * @param queryName Identifiant de la requere nommee
     * @param params Parametres de la requete
     * @param range Nombre de resultats a retourner
     * @return liste resultat
     */
    <T> List<T> findMaxResultsWithNamedQuery(String queryName, Map<String, Object> params, int range);

    /**
     * Récupère une tranche de résultats, à partir d'une requête nommée et de paramètres
     * 
     * @param <T> Type des objets a retourner
     * @param queryName Identifiant de la requere nommee
     * @param startIndex Index de départ de la tranche
     * @param endIndex Index de fin de la tranche
     * @param params Paramètres de la requête
     * @return liste resultat
     */
    <T> List<T> findRangeResultsWithNamedQuery(String queryName, int startIndex, int endIndex, Map<String, Object> params);

    /**
     * Dénombre le nombre d'entités de type <code>T</code>.
     * 
     * @return le nombre d'entités de type <code>T</code>
     */
    <T> long count(Class<T> type);

    /**
     * Dénombre le nombre d'entités de type <code>T</code> répondant aux critères
     * <code>params</code>.
     * 
     * @param queryName Requête nommée
     * @param params Paramètres de la requête
     * @return le nombre d'entités de type <code>T</code>
     */
    <T> long count(String queryName, Map<String, Object> params);
}
