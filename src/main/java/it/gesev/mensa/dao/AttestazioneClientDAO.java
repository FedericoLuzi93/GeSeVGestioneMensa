package it.gesev.mensa.dao;

import it.gesev.mensa.entity.AttestazioneClient;
import it.gesev.mensa.entity.Grado;
import it.gesev.mensa.exc.GesevException;

public interface AttestazioneClientDAO 
{
	public String generaCodiceOTP(Integer idMensa) throws GesevException;
	public void eseguiAttestazioneClient(AttestazioneClient attestazioneClient) throws GesevException;
	public Grado getGrado(String codiceGrado);
}
