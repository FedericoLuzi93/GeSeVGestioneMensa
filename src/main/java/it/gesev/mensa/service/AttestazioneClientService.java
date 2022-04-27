package it.gesev.mensa.service;

import it.gesev.mensa.dto.CodiceOTPDTO;
import it.gesev.mensa.exc.GesevException;

public interface AttestazioneClientService {
	public CodiceOTPDTO getCodiceOtp(Integer idMensa) throws GesevException;
}
