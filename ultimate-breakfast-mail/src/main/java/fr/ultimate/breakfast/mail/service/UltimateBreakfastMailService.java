package fr.ultimate.breakfast.mail.service;

import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.core.io.Resource;

public interface UltimateBreakfastMailService
{

    /**
     * Envoyer un email simple a un destinataire
     * 
     * @param from Expediteur
     * @param to Destinataire
     * @param subject Sujet
     * @param texte Corps du mail
     */
    void sendEmail(String from, String to, String subject, String text);

    /**
     * Envoyer un email au format MIME a partir d'un modele
     * 
     * @param from Expediteur
     * @param to Destinataire
     * @param subject Sujet
     * @param template Le template du mail a envoyer
     * @param model Le modele utilise pour alimenter le template
     * @throws MessagingException en cas de pb lors de la construction du message
     */
    void sendTemplatedEmail(String from, String to, String subject, String template, Map<String, String> model) throws MessagingException;

    /**
     * Envoyer un email au format MIME a partir d'un template et d'un modele
     * 
     * @param to Destinataire
     * @param subject Sujet
     * @param template Le template du mail a envoyer
     * @param model Le modele utilise pour alimenter le template
     * @throws MessagingException en cas de pb lors de la construction du message
     */
    void sendTemplatedEmail(String to, String subject, String template, Map<String, String> model) throws MessagingException;

    /**
     * Envoyer un email au format MIME a partir d'un template et d'un modele, plus une piece jointe
     * 
     * @param from Expediteur
     * @param to Destinataire
     * @param subject Sujet
     * @param template Le template du mail a envoyer
     * @param model Le modele utilise pour alimenter le template
     * @param attachment La piece jointe
     * @throws MessagingException en cas de pb lors de la construction du message
     */
    void sendTemplatedEmailWithAttachment(String from, String to, String subject, String template, Map<String, String> model, Resource attachment)
            throws MessagingException;

    /**
     * Send a remind mail about team account
     * 
     * @param to the email to
     * @param login the login
     * @param password the password
     */
    void sendAccountMail(String to, String login, String password) throws MessagingException;

    /**
     * Send a request mail
     * 
     * @param emails the receivers
     * @param cc the carbon copy recipient
     * @param customMsg a custom message to add to the mail
     * @param teamName the name of the team requesting the breakfast
     */
    void sendBreakfastRequest(List<String> emails, String cc, String teamName, String customMsg) throws MessagingException;

    /**
     * Send a call for Breakfast mail to all the team
     * 
     * @param emails the emails to warn
     * @param cc the carbon copy recipient, i.e le manager of the team
     * @param teamName the name of the team calling for breakfast
     * @param customMsg a custom message to add to the mail
     * @throws MessagingException si le mail ne peut etre envoyé
     */
    void callTeamForBreakfast(List<String> emails, String teamName, String customMsg) throws MessagingException;
}
