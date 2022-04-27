package it.gesev.mensa.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.gesev.mensa.dao.AttestazioneClientDAO;
import it.gesev.mensa.dto.CodiceOTPDTO;
import it.gesev.mensa.exc.GesevException;

@Service
public class AttestazioneClientServiceImpl implements AttestazioneClientService {
	
	private static Logger logger = LoggerFactory.getLogger(AttestazioneClientServiceImpl.class);
	
	@Autowired
	private AttestazioneClientDAO attestazioneClientDAO;

	@Override
	public CodiceOTPDTO getCodiceOtp(Integer idMensa) throws GesevException {
		String codiceOTP = attestazioneClientDAO.generaCodiceOTP(idMensa);
		return new CodiceOTPDTO(codiceOTP, idMensa);
	}

}
