package it.gesev.mensa.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService
{
	@Autowired
    private JavaMailSender emailSender;
	
	@Value("${gesev.mail.from}")
	private String mailFrom;
	
	private static Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);
	public static final String MESSAGGIO_OPERATORE_MENSA = "Gentile %s %s, \n le comunichiamo con la seguente email che l'assegnazione della sua persona al ruolo di " + 
			                                               "operatore di mensa e' andato a buon fine. Il processo deve essere completato con la configurazione al terminale " + 
			                                               "di mensa inserendo il codice OTP: %s. \n\nDistinti saluti.";
	
	@Override
	public String sendMailOperatoreMensa(String nome, String cognome, String email) 
	{
		logger.info("Invio della mail di conferma a " + email);
		
		SimpleMailMessage message = new SimpleMailMessage(); 
        message.setFrom(this.mailFrom);
        message.setTo(email); 
        message.setSubject("Conferma assegnazione ruolo operatore mensa"); 
        
        logger.info("Generazione codice OTP...");
        String codiceOTP = RandomStringUtils.random(6, true, true).toUpperCase();
        logger.info("Generato codice " + codiceOTP);
        
        message.setText(String.format(MESSAGGIO_OPERATORE_MENSA, nome, cognome, codiceOTP));
        emailSender.send(message);
        
        return codiceOTP;
	}

}
