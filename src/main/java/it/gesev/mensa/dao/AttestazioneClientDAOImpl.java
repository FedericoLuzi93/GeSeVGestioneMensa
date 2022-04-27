package it.gesev.mensa.dao;

import java.util.Objects;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.stereotype.Component;

import it.gesev.mensa.entity.CodiceOTP;
import it.gesev.mensa.entity.Mensa;
import it.gesev.mensa.exc.GesevException;
import it.gesev.mensa.repository.CodiceOTPRepository;
import it.gesev.mensa.repository.MensaRepository;

@Component
public class AttestazioneClientDAOImpl implements AttestazioneClientDAO
{
	private static final Logger logger = LoggerFactory.getLogger(AttestazioneClientDAOImpl.class);
	
	@Autowired
	private CodiceOTPRepository codiceRepository;
	@Autowired
	private MensaRepository mensaRepository;
	
	/**
	 * @param Integer idMensa
	 * @return String codiceOTP generato. 
	 * @throws GesevException Nel caso l'id della mensa non fosse valido o ci fossero
	 * problemi nella memorizzazione su DB lancia l'eccezione con i rispettivi messaggi
	 * Riceve in argomento l'id della mensa della quale generare l'OTP. Se la mensa esiste, dopo
	 * aver generato la stringa alfanumerica, la memorizza nella tabella codice_otp
	 */
	@Override
	public String generaCodiceOTP(Integer idMensa) throws GesevException
	{
		Mensa mensa = null;
		try {
			logger.info("Caricamento mensa con ID = " + idMensa);
			if (mensaRepository.existsById(idMensa)) {
				mensa = mensaRepository.getById(idMensa);
			}
		} catch (JpaObjectRetrievalFailureException ex) {
			logger.error("Nessuna mensa trovata con id specificato: " + idMensa);
			throw new GesevException("Nessuna mensa trovata con codice mensa: " + idMensa, HttpStatus.BAD_REQUEST);
		} catch (Exception ex2) {
			logger.error("Errore connessione al DB");
			throw new GesevException("Errore connessione al DB", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (Objects.isNull(mensa)) {
			logger.error("Nessuna mensa trovata con id specificato: " + idMensa);
			throw new GesevException("Nessuna mensa trovata con codice mensa: " + idMensa, HttpStatus.BAD_REQUEST);
		}
		boolean unique = false;
		String generatedString = "";
		CodiceOTP nuovoCodice = new CodiceOTP();
		logger.info("Generazione codice OTP per mensa con ID = " + idMensa);
		try {
			while(!unique) {
				generatedString = RandomStringUtils.random(6, true, true);
				if (codiceRepository.getCodiceExist(generatedString) == 0) {
					unique = true;
				}
			}
			
			nuovoCodice.setCodice(generatedString);
			nuovoCodice.setMensa(mensa);
			logger.info("Memorizzazione codice generato (" + generatedString + ") con codice mensa = " + idMensa);
			if (Objects.isNull(codiceRepository.save(nuovoCodice))) {
				logger.error("Errore nella memorizzazione del codice OTP sul db - codice: " + generatedString
						+ ", idMensa: " + idMensa);
				throw new GesevException("Errore nella memorizzazione del codice OTP sul db - codice: " + generatedString
						+ ", idMensa: " + idMensa, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception ex2) {
			logger.error("Errore nella memorizzazione del codice OTP sul db - codice: " + generatedString
					+ ", idMensa: " + idMensa);
			throw new GesevException("Errore nella memorizzazione del codice OTP sul db - codice: " + generatedString
					+ ", idMensa: " + idMensa, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return generatedString;
	}

}
