package it.gesev.mensa.dao;

import java.text.ParseException;
import java.util.List;

import it.gesev.mensa.dto.AssDipendenteRuoloDTO;
import it.gesev.mensa.dto.RicercaColonnaDTO;
import it.gesev.mensa.entity.AssDipendenteRuolo;
import it.gesev.mensa.entity.Dipendente;
import it.gesev.mensa.entity.Mensa;
import it.gesev.mensa.entity.OrganoDirettivo;
import it.gesev.mensa.entity.RuoloMensa;

public interface RuoliDAO 
{
	public List<Dipendente> getListaDipendenti();
	public List<AssDipendenteRuolo> getListaDipendenteRuolo();
	public List<AssDipendenteRuolo> getListaDipendenteRuolo(Integer idEnte);
	
	public List<OrganoDirettivo> getListaOrganiDirettivi();
	public List<RuoloMensa> getRuoliByIdOrdineDirettivo(String tipoRuolo);
	public void aggiungiRuoloDipendente(Integer idDipendente, String emailDipendente, Integer idRuolo, Integer idOrganoDirettivo, Integer idMensa) throws ParseException;
	public void aggiungiRuoloDipendenteEsterno(String nome, String cognome, String email, Integer idRuolo, Integer idMensa) throws ParseException;
	public List<Dipendente> ricercaDipendenti(List<RicercaColonnaDTO> listaColonne, Integer idEnte);
	public void updateRuoloDipendente(Integer idRuoloDipendente, Integer idRuolo, Integer idDipendente, Integer idOrganoDirettivo, Integer idMensa);
	public void cancellaRuolo(Integer idRuoloDipendente);
	
	public int creaOrganoDirettivo(OrganoDirettivo organoDirettivo);
	public int modificaOrganoDirettivo(OrganoDirettivo organoDirettivo, int idOrganoDirettivo);
	public int cancellaOrganoDirettivo(int idOrganoDirettivo);
	
	public List<Dipendente> findDipendenteByIdEnte(Integer idMensa);
	public List<AssDipendenteRuolo> findRuoliDipendentiEsterni(Integer codiceMensa);
	public void updateRuoloDipendenteEsterno(AssDipendenteRuoloDTO associazione);
	public void inserisciDatiAttestazione(String codiceOtp, Mensa mensa, Integer idDipendente, boolean isDipendente);
}
