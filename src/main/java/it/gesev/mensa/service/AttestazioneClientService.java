package it.gesev.mensa.service;

import it.gesev.mensa.dto.AttestazioneClientDTO;
import it.gesev.mensa.dto.CodiceOTPDTO;
import it.gesev.mensa.exc.GesevException;

public interface AttestazioneClientService {
	public CodiceOTPDTO getCodiceOtp(Integer idMensa) throws GesevException;
	public AttestazioneClientDTO eseguiAttestazione(AttestazioneClientDTO attestazioneClient) throws GesevException;
}
