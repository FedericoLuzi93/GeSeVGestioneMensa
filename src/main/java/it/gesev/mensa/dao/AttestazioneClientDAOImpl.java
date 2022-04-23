package it.gesev.mensa.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.gesev.mensa.repository.CodiceOTPRepository;

public class AttestazioneClientDAOImpl implements AttestazioneClientDAO
{
	private static final Logger logger = LoggerFactory.getLogger(AttestazioneClientDAOImpl.class);
	
	private CodiceOTPRepository codiceRepository;
	
	@Override
	public String generaCodiceOPT(Integer idMensa) 
	{
		logger.info("Generazione codice OTP per mensa con ID = " + idMensa);
		return null;
	}

}
