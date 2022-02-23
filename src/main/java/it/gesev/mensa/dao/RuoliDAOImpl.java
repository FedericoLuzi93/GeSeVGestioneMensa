package it.gesev.mensa.dao;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.gesev.mensa.entity.AssDipendenteRuolo;
import it.gesev.mensa.entity.Dipendente;
import it.gesev.mensa.repository.AssRuoloDipendenteRepository;
import it.gesev.mensa.repository.DipendenteRepository;

@Component
public class RuoliDAOImpl implements RuoliDAO 
{

	private static Logger logger = LoggerFactory.getLogger(RuoliDAOImpl.class);
	
	@Autowired
	private DipendenteRepository dipendenteRepository;
	
	@Autowired
	private AssRuoloDipendenteRepository assRuoloDipendenteRepository;
	
	@Override
	public List<Dipendente> getListaDipendenti() 
	{
		logger.info("Ricerca di dipendenti...");
		List<Dipendente> listaDipendenti = dipendenteRepository.findAll();
		logger.info("Trovati " + listaDipendenti.size() + " dipendenti.");
		
		return listaDipendenti;
	}

	@Override
	public List<AssDipendenteRuolo> getListaDipendenteRuolo() 
	{
		logger.info("Ricerca di dipendenti/ruoli...");
		List<AssDipendenteRuolo> listaDipendentiRuolo = assRuoloDipendenteRepository.findAll();
		logger.info("Trovati " + listaDipendentiRuolo.size() + " elementi.");
		
		return listaDipendentiRuolo;
		
	}

}
