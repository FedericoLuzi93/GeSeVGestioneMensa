package it.gesev.mensa.service;

import java.text.ParseException;
import java.util.List;

import it.gesev.mensa.dto.AssDipendenteRuoloDTO;
import it.gesev.mensa.dto.DettaglioRuoloDTO;
import it.gesev.mensa.dto.OrganoDirettivoDTO;
import it.gesev.mensa.dto.RicercaColonnaDTO;
import it.gesev.mensa.dto.RuoloDTO;
import it.gesev.mensa.entity.AssDipendenteRuolo;

public interface RuoliService 
{
	public DettaglioRuoloDTO getDettaglioRuoli();
	public List<OrganoDirettivoDTO> getListaOrganiDirettivi();
	public List<RuoloDTO> getRuoliByIdOrdineDirettivo(String tipoRuolo);
	public void aggiungiRuoloDipendente(AssDipendenteRuoloDTO associazione) throws ParseException;
	public DettaglioRuoloDTO ricercaDipendenti(List<RicercaColonnaDTO> listaColonne, Integer idMensa);
	public DettaglioRuoloDTO updateRuoloDipendente(AssDipendenteRuoloDTO associazione);
	public DettaglioRuoloDTO cancellaRuolo(Integer idRuoloDipendente, Integer idMensa);

	public int creaNuovoOrganoDirettivo(OrganoDirettivoDTO organoDirettivoDTO);
	public int modificaOrganoDirettivo(OrganoDirettivoDTO organoDirettivoDTO, int idOrganoDirettivo);
	public int cancellaOrganoDirettivo(int idOrganoDirettivo);
	
	public DettaglioRuoloDTO findDipendenteByIdEnte(Integer idMensa);
	public void aggiungiRuoloDipendenteEsterno(AssDipendenteRuoloDTO adrd) throws ParseException;
	public List<AssDipendenteRuoloDTO> findRuoliDipendentiEsterni(Integer codiceMensa);


}
