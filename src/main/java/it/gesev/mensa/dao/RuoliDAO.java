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
	public List<AssDipendenteRuolo> getListaDipendenteRuolo(Integer idEnte);
	
	public List<OrganoDirettivo> getListaOrganiDirettivi();
	public List<RuoloMensa> getRuoliByIdOrdineDirettivo();
	public void aggiungiRuoloDipendente(Integer idDipendente, Integer idRuolo, Integer idOrganoDirettivo, Integer idMensa) throws ParseException;
	public List<Dipendente> ricercaDipendenti(List<RicercaColonnaDTO> listaColonne, Integer idEnte);
	public void updateRuoloDipendente(Integer idRuoloDipendente, Integer idRuolo, Integer idDipendente, Integer idOrganoDirettivo, Integer idMensa);
	public void cancellaRuolo(Integer idRuoloDipendente);
	
	public int creaOrganoDirettivo(OrganoDirettivo organoDirettivo);
	public int modificaOrganoDirettivo(OrganoDirettivo organoDirettivo, int idOrganoDirettivo);
	public int cancellaOrganoDirettivo(int idOrganoDirettivo);
	
	public List<Dipendente> findDipendenteByIdEnte(Integer idMensa);
}
