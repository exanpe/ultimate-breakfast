package fr.ultimate.breakfast.mail.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.unitils.UnitilsTestNG;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBean;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;

/**
 * @author lguerin
 */
@SpringApplicationContext("applicationContext-mail.xml")
public class UltimateBreakfastMailServiceTest extends UnitilsTestNG
{

    private static final String MAIL_TEST_FROM = "noreply@ultimate-breakfast.fr";

    private static final String MAIL_TEST_SUBJECT = "Test";

    private static final String MAIL_TEST_TO = "test@ultimate-breakfast.fr";

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UltimateBreakfastMailServiceTest.class);

    /**
     * Service de gestion des utilisateurs a tester
     */
    @SpringBean("ultimate-breakfastMailService")
    private UltimateBreakfastMailService mailService;

    /**
     * Port de Test
     */
    private static final int TEST_SMTP_PORT = 1025;

    /**
     * Server SMTP de Test
     */
    private SimpleSmtpServer smtpServer;

    @BeforeMethod
    public void startSmtpServer()
    {
        smtpServer = SimpleSmtpServer.start(TEST_SMTP_PORT);
    }

    @AfterMethod
    public void stopSmtpServer()
    {
        smtpServer.stop();
    }

    /**
     * Test method for
     * {@link fr.ultimate.breakfast.mail.service.UltimateBreakfastMailService#sendEmail(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}
     * .
     */
    @Test
    public void testSendEmail()
    {
        Assert.assertEquals(0, smtpServer.getReceivedEmailSize());
        mailService.sendEmail(MAIL_TEST_FROM, MAIL_TEST_TO, MAIL_TEST_SUBJECT, "Message de test");
        Assert.assertEquals(1, smtpServer.getReceivedEmailSize());

        SmtpMessage email = (SmtpMessage) smtpServer.getReceivedEmail().next();
        Assert.assertEquals(MAIL_TEST_SUBJECT, email.getHeaderValue("Subject"));
        Assert.assertEquals(MAIL_TEST_FROM, email.getHeaderValue("From"));
        Assert.assertEquals(MAIL_TEST_TO, email.getHeaderValue("To"));
        Assert.assertEquals("Message de test", email.getBody());
    }

    @Test
    public void testSendTemplatedEmail()
    {
        Map<String, String> model = this.getTemplateModel();
        Assert.assertEquals(0, smtpServer.getReceivedEmailSize());
        try
        {
            mailService.sendTemplatedEmail(MAIL_TEST_FROM, MAIL_TEST_TO, MAIL_TEST_SUBJECT, "emailTemplateTest.vm", model);
        }
        catch (MessagingException e)
        {
            LOGGER.error("Pb lors du préparation ou de l'envoi du mail: " + e);
            throw new RuntimeException(e);
        }
        Assert.assertEquals(1, smtpServer.getReceivedEmailSize());
        SmtpMessage email = (SmtpMessage) smtpServer.getReceivedEmail().next();
        Assert.assertEquals(MAIL_TEST_SUBJECT, email.getHeaderValue("Subject"));
        Assert.assertEquals(MAIL_TEST_FROM, email.getHeaderValue("From"));
        Assert.assertEquals(MAIL_TEST_TO, email.getHeaderValue("To"));
        Assert.assertTrue(email.getHeaderValue("Content-Type").contains("multipart/mixed"));
        Assert.assertTrue(email.getBody().contains(model.get("name")));
        Assert.assertTrue(email.getBody().contains(model.get("text")));
    }

    @Test
    public void testSendTemplatedEmailWithoutFrom()
    {
        Map<String, String> model = this.getTemplateModel();
        Assert.assertEquals(0, smtpServer.getReceivedEmailSize());
        try
        {
            mailService.sendTemplatedEmail(MAIL_TEST_TO, MAIL_TEST_SUBJECT, "emailTemplateTest.vm", model);
        }
        catch (MessagingException e)
        {
            LOGGER.error("Pb lors du préparation ou de l'envoi du mail: " + e);
            throw new RuntimeException(e);
        }
        Assert.assertEquals(1, smtpServer.getReceivedEmailSize());
        SmtpMessage email = (SmtpMessage) smtpServer.getReceivedEmail().next();
        Assert.assertEquals(MAIL_TEST_FROM, email.getHeaderValue("From"));
    }

    @Test
    public void testSendTemplatedEmailWithAttachment()
    {
        Map<String, String> model = this.getTemplateModel();
        Assert.assertEquals(0, smtpServer.getReceivedEmailSize());
        Resource resource = new ClassPathResource("tapestry.png");
        try
        {
            mailService.sendTemplatedEmailWithAttachment(MAIL_TEST_FROM, MAIL_TEST_TO, MAIL_TEST_SUBJECT, "emailTemplateTest.vm", model, resource);
        }
        catch (MessagingException e)
        {
            LOGGER.error("Pb lors du préparation ou de l'envoi du mail: " + e);
            throw new RuntimeException(e);
        }
        Assert.assertEquals(1, smtpServer.getReceivedEmailSize());
    }

    @Test
    public void testSendAccountMail()
    {
        Assert.assertEquals(0, smtpServer.getReceivedEmailSize());

        try
        {
            mailService.sendAccountMail(MAIL_TEST_TO, "Agefos", "age.fos");
        }
        catch (MessagingException e)
        {
            LOGGER.error("Pb lors du préparation ou de l'envoi du mail: " + e);
            throw new RuntimeException(e);
        }
        Assert.assertEquals(1, smtpServer.getReceivedEmailSize());
    }

    @Test
    public void testSendCallTeamForBreakfast()
    {
        Assert.assertEquals(0, smtpServer.getReceivedEmailSize());
        List<String> emails = new ArrayList<String>();
        emails.add(MAIL_TEST_TO);
        try
        {
            mailService.callTeamForBreakfast(emails, "Agefos", null);
        }
        catch (MessagingException e)
        {
            LOGGER.error("Pb lors du préparation ou de l'envoi du mail: " + e);
            throw new RuntimeException(e);
        }
        SmtpMessage email = (SmtpMessage) smtpServer.getReceivedEmail().next();
        Assert.assertEquals(MAIL_TEST_FROM, email.getHeaderValue("From"));
        Assert.assertEquals(MAIL_TEST_TO, email.getHeaderValue("To"));
        Assert.assertEquals(1, smtpServer.getReceivedEmailSize());
    }

    @Test
    public void testSendCallTeamForBreakfastWithCustomMessage()
    {
        Assert.assertEquals(0, smtpServer.getReceivedEmailSize());
        List<String> emails = new ArrayList<String>();
        emails.add(MAIL_TEST_TO);
        String customMsg = "Dans le bureau 6A22";
        try
        {
            mailService.callTeamForBreakfast(emails, "Agefos", customMsg);
        }
        catch (MessagingException e)
        {
            LOGGER.error("Pb lors du préparation ou de l'envoi du mail: " + e);
            throw new RuntimeException(e);
        }
        SmtpMessage email = (SmtpMessage) smtpServer.getReceivedEmail().next();
        Assert.assertTrue(email.getHeaderValue("Content-Type").contains("multipart/mixed"));
        Assert.assertTrue(email.getBody().contains(customMsg));
        Assert.assertEquals(1, smtpServer.getReceivedEmailSize());
    }

    @Test
    public void testSendBreakfastRequestWithCustomMessage()
    {
        Assert.assertEquals(0, smtpServer.getReceivedEmailSize());
        List<String> emails = new ArrayList<String>();
        emails.add(MAIL_TEST_TO);
        String customMsg = "Avec des fruits!!!";

        try
        {
            mailService.sendBreakfastRequest(emails, MAIL_TEST_FROM, "Agefos", customMsg);
        }
        catch (MessagingException e)
        {
            LOGGER.error("Pb lors du préparation ou de l'envoi du mail: " + e);
            throw new RuntimeException(e);
        }
        SmtpMessage email = (SmtpMessage) smtpServer.getReceivedEmail().next();
        Assert.assertEquals(MAIL_TEST_FROM, email.getHeaderValue("From"));
        Assert.assertEquals(MAIL_TEST_TO, email.getHeaderValue("To"));
        Assert.assertEquals(MAIL_TEST_FROM, email.getHeaderValue("Cc"));
        Assert.assertEquals(1, smtpServer.getReceivedEmailSize());
        Assert.assertTrue(email.getBody().contains(customMsg));
    }

    private Map<String, String> getTemplateModel()
    {
        Map<String, String> model = new HashMap<String, String>();
        String name = "ultimate-breakfast";
        String text = "Mail de l'application UltimateBreakfast";
        model.put("name", name);
        model.put("text", text);
        return model;
    }

}
