package it.gesev.mensa.dao;

import it.gesev.mensa.exc.GesevException;

public interface AttestazioneClientDAO 
{
	public String generaCodiceOTP(Integer idMensa) throws GesevException;
}
