package it.gesev.mensa.dao;

import java.util.List;

import it.gesev.mensa.entity.Mensa;


public interface MensaDAO 
{
	public List<Mensa> getAllMense();
	public int createMensa(Mensa mensa);
	public int updateMensa(Mensa mensa, int idMensa);
}
