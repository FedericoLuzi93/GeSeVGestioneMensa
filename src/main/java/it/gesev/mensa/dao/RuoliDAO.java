package it.gesev.mensa.dao;

import java.text.ParseException;
import java.util.List;

import it.gesev.mensa.dto.RicercaColonnaDTO;
import it.gesev.mensa.entity.AssDipendenteRuolo;
import it.gesev.mensa.entity.Dipendente;
import it.gesev.mensa.entity.OrganoDirettivo;
import it.gesev.mensa.entity.RuoloMensa;

public interface RuoliDAO 
{
	public List<Dipendente> getListaDipendenti();
	public List<AssDipendenteRuolo> getListaDipendenteRuolo();
	public List<OrganoDirettivo> getListaOrganiDirettivi();
	public List<RuoloMensa> getRuoliByIdOrdineDirettivo();
	public void aggiungiRuoloDipendente(Integer idDipendente, Integer idRuolo, Integer idOrganoDirettivo) throws ParseException;
	public List<Dipendente> ricercaDipendenti(List<RicercaColonnaDTO> listaColonne);
	public void updateRuoloDipendente(Integer idRuoloDipendente, Integer idRuolo, Integer idDipendente, Integer idOrganoDirettivo);
	public void cancellaRuolo(Integer idRuoloDipendente);
	
	public OrganoDirettivo creaOrganoDirettivo(OrganoDirettivo organoDirettivo);
}
