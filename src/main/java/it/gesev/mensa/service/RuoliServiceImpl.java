package it.gesev.mensa.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.gesev.mensa.dao.RuoliDAO;
import it.gesev.mensa.dto.AssDipendenteRuoloDTO;
import it.gesev.mensa.dto.DettaglioRuoloDTO;
import it.gesev.mensa.dto.DipendenteDTO;
import it.gesev.mensa.dto.RuoloDTO;
import it.gesev.mensa.entity.AssDipendenteRuolo;
import it.gesev.mensa.entity.Dipendente;

@Service
public class RuoliServiceImpl implements RuoliService 
{
	private static Logger logger = LoggerFactory.getLogger(RuoliServiceImpl.class);
	
	@Autowired
	private RuoliDAO ruoliDAO;
	
	@Override
	public DettaglioRuoloDTO getDettaglioRuoli() 
	{
		logger.info("Servizio per la ricerca dei dettagli dei ruoli...");
		DettaglioRuoloDTO dettaglio = new DettaglioRuoloDTO();
		
		List<Dipendente> listaDipendenti = ruoliDAO.getListaDipendenti();
		List<AssDipendenteRuolo> listaDipendentiRuoli = ruoliDAO.getListaDipendenteRuolo();
		
		ModelMapper mapper = new ModelMapper();
		
		/* conversione dei dipendenti */
		if(listaDipendenti.size() > 0)
		{
			List<DipendenteDTO> dipendentiDTO = new ArrayList<>();
			for(Dipendente dipendente : listaDipendenti)
				dipendentiDTO.add(mapper.map(dipendente, DipendenteDTO.class));
			
			dettaglio.setListaDipendenti(dipendentiDTO);
		}
		
		/* conversioni delle associazioni dei ruoli */
		if(listaDipendentiRuoli.size() > 0)
		{
			List<AssDipendenteRuoloDTO> listaRuoliDTO = new ArrayList<AssDipendenteRuoloDTO>();
			for(AssDipendenteRuolo ruolo : listaDipendentiRuoli)
			{
				AssDipendenteRuoloDTO ruoloDTO = new AssDipendenteRuoloDTO();
				ruoloDTO.setDipendente(mapper.map(ruolo.getDipendente(), DipendenteDTO.class));
				ruoloDTO.setRuolo(mapper.map(ruolo.getRuolo(), RuoloDTO.class));
				ruoloDTO.setAssDipendenteRuoloId(ruolo.getAssDipendenteRuoloId());
				
				listaRuoliDTO.add(ruoloDTO);
			}
			
			dettaglio.setAssociazioniRuolo(listaRuoliDTO);
		}
		
		return dettaglio;
		
	}

}
