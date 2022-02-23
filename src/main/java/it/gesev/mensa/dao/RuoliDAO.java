package it.gesev.mensa.dao;

import java.util.List;

import it.gesev.mensa.entity.AssDipendenteRuolo;
import it.gesev.mensa.entity.Dipendente;

public interface RuoliDAO 
{
	public List<Dipendente> getListaDipendenti();
	public List<AssDipendenteRuolo> getListaDipendenteRuolo();
}
