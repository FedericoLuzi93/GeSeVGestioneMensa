package it.gesev.mensa.service;

import java.text.ParseException;
import java.util.List;

import it.gesev.mensa.dto.AssDipendenteRuoloDTO;
import it.gesev.mensa.dto.DettaglioRuoloDTO;
import it.gesev.mensa.dto.OrganoDirettivoDTO;
import it.gesev.mensa.dto.RicercaColonnaDTO;
import it.gesev.mensa.dto.RuoloDTO;

public interface RuoliService 
{
	public DettaglioRuoloDTO getDettaglioRuoli();
	public List<OrganoDirettivoDTO> getListaOrganiDirettivi();
	public List<RuoloDTO> getRuoliByIdOrdineDirettivo();
	public void aggiungiRuoloDipendente(AssDipendenteRuoloDTO associazione) throws ParseException;
	public DettaglioRuoloDTO ricercaDipendenti(List<RicercaColonnaDTO> listaColonne);
	public DettaglioRuoloDTO updateRuoloDipendente(AssDipendenteRuoloDTO associazione);
}
