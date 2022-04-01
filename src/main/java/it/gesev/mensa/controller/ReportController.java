package it.gesev.mensa.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.gesev.mensa.dto.EsitoDTO;
import it.gesev.mensa.dto.PastiConsumatiDTO;
import it.gesev.mensa.exc.GesevException;
import it.gesev.mensa.service.ReportService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/report")
public class ReportController 
{
	@Autowired
	private ReportService reportService;
	
	private static final Logger logger = LoggerFactory.getLogger(ReportController.class);
	private final String MESSAGGIO_ERRORE_INTERNO = "Si e' verificato un errore interno";
	
	/* Carica pasti consumati CSV*/
	@PostMapping(value = "/caricaPastiConsumatiCSV", consumes = {"multipart/form-data"})
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> caricaPastiConsumatiCSV(@RequestPart( name = "file", required = true) MultipartFile multipartFile)
	{
		logger.info("Accesso al servizio caricaPastiConsumatiCSV");
		EsitoDTO esito = new EsitoDTO();
		try
		{	
			//Controllo che il file sia una CSV
			String name = multipartFile.getOriginalFilename();
			boolean controlloCSV = name.endsWith(".csv");
			if(controlloCSV == false)
				throw new GesevException("Impossibile caricare i pasti consumati, il file non è di tipo CSV", HttpStatus.BAD_REQUEST);
			
			reportService.caricaPastiConsumatiCSV(multipartFile);
			esito.setStatus(HttpStatus.OK.value());
			esito.setMessaggio("INSERIMENTO AVVENUTO CON SUCCESSO");
		}
		catch(GesevException gex)   
		{
			logger.info("Si e' verificata un'eccezione", gex);
			esito.setStatus(gex.getStatus().value());
			esito.setMessaggio(gex.getMessage());
		}
		catch(Exception ex)
		{
			logger.info("Si e' verificata un'eccezione interna", ex);
			esito.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			esito.setMessaggio(MESSAGGIO_ERRORE_INTERNO);
		}
		return ResponseEntity.status(esito.getStatus()).body(esito);
	}
	
	
	/* Carica pasti consumati JSON*/
	@PostMapping(value = "/caricaPastiConsumatiJson")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> caricaPastiConsumatiJson(@RequestBody List<PastiConsumatiDTO> listaPastiConsumatiCSV)
	{
		logger.info("Accesso al servizio caricaPastiConsumatiJson");
		EsitoDTO esito = new EsitoDTO();
		try
		{	
			reportService.caricaPastiConsumatiJson(listaPastiConsumatiCSV);
			esito.setStatus(HttpStatus.OK.value());
			esito.setMessaggio("INSERIMENTO AVVENUTO CON SUCCESSO");
		}
		catch(GesevException gex)   
		{
			logger.info("Si e' verificata un'eccezione", gex);
			esito.setStatus(gex.getStatus().value());
			esito.setMessaggio(gex.getMessage());
		}
		catch(Exception ex)
		{
			logger.info("Si e' verificata un'eccezione interna", ex);
			esito.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			esito.setMessaggio(MESSAGGIO_ERRORE_INTERNO);
		}
		return ResponseEntity.status(esito.getStatus()).body(esito);
	}

}
