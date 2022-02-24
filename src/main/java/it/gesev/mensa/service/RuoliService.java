package it.gesev.mensa.service;

import java.util.List;

import it.gesev.mensa.dto.DettaglioRuoloDTO;
import it.gesev.mensa.dto.OrganoDirettivoDTO;
import it.gesev.mensa.dto.RuoloDTO;

public interface RuoliService 
{
	public DettaglioRuoloDTO getDettaglioRuoli();
	public List<OrganoDirettivoDTO> getListaOrganiDirettivi();
	public List<RuoloDTO> getRuoliByIdOrdineDirettivo(Integer idOrganoDirettivo);
}
