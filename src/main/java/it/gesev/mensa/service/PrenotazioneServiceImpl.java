package it.gesev.mensa.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBeanBuilder;

import it.gesev.mensa.dao.PrenotazioneDAO;
import it.gesev.mensa.dto.CaricamentoPrenotazioniDTO;
import it.gesev.mensa.dto.PastiConsumatiDTO;
import it.gesev.mensa.dto.PrenotazioneDTO;

@Service
public class PrenotazioneServiceImpl implements PrenotazioneService {
	private static Logger logger = LoggerFactory.getLogger(PrenotazioneServiceImpl.class);
	
	@Value("${gesev.data.format}")
	private String dateFormat;
	
	@Autowired
	private PrenotazioneDAO prenotazioneDAO;
		
	@SuppressWarnings("unchecked")
	@Override
	public void insertPrenotazioni(MultipartFile file) throws IOException 
	{
		logger.info("Inserimento prenotazioni...");
		
		Reader reader = new InputStreamReader(file.getInputStream());
	    CSVReader csvReader = new CSVReaderBuilder(reader).build();
	    
	    List<CaricamentoPrenotazioniDTO> listaPrenotazioni = new CsvToBeanBuilder(csvReader)
                .withType(CaricamentoPrenotazioniDTO.class)
                .build()
                .parse();
	    
	    prenotazioneDAO.insertPrenotazione(listaPrenotazioni);
	    
	    logger.info("Fine inserimento prenotazioni...");
		
	}

	@Override
	public List<PrenotazioneDTO> getListaPrenotazioni() {
		return prenotazioneDAO.getListaPrenotazioni();
	}

}
