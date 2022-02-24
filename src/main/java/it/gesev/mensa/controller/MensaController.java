package it.gesev.mensa.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.gesev.mensa.dto.EsitoDTO;
import it.gesev.mensa.dto.MensaDTO;
import it.gesev.mensa.exc.GesevException;
import it.gesev.mensa.service.MensaService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/mensa")
public class MensaController 
{
	@Autowired
	private MensaService mensaService;
	
	private static final Logger logger = LoggerFactory.getLogger(MensaController.class);
	private final String MESSAGGIO_ERRORE_INTERNO = "Si e' verificato un errore interno";
	
	/* Leggi tutte le Mense */
	@GetMapping("/leggiTutteLeMense")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> getAllMensa()
	{
		logger.info("Accesso al servizio getAllMensa");
		EsitoDTO esito = new EsitoDTO();
		HttpStatus status = null;
		try
		{
			List<MensaDTO> listaMensaDTO = mensaService.getAllMense();
			esito.setBody(listaMensaDTO);
			status = HttpStatus.OK;
		}
		catch(GesevException gex)
		{
			logger.info("Si e' verificata un'eccezione", gex);
			esito.setMessaggio(gex.getMessage());
			status = gex.getStatus();
		}
		catch(Exception ex)
		{
			logger.info("Si e' verificata un'eccezione interna", ex);
			esito.setMessaggio(MESSAGGIO_ERRORE_INTERNO);
			status = HttpStatus.INTERNAL_SERVER_ERROR;	
		}
		esito.setStatus(status.value());
		return ResponseEntity.status(status).headers(new HttpHeaders()).body(esito);
	}
	
	/* Crea una Mensa */
	@PostMapping("/creaMensa")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> createMensa(@RequestBody MensaDTO mensaDTO)
	{
		logger.info("Accesso al servizio createMensa");
		EsitoDTO esito = new EsitoDTO();
		try
		{
			mensaService.createMensa(mensaDTO);
			esito.setStatus(HttpStatus.OK.value());
			esito.setMessaggio("INSERIMENTO AVVENUTO CON SUCCESSO");
			esito.setBody(mensaService.getAllMense());
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
	
	/* Aggiorna una Mensa */
	@PutMapping("/aggiornaMensa/{idMensa}")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> updateMensa(@RequestBody MensaDTO mensaDTO, @PathVariable int idMensa)
	{
		logger.info("Accesso al servizio updateMensa");
		EsitoDTO esito = new EsitoDTO();
		try
		{
			mensaService.updateMensa(mensaDTO, idMensa);
			esito.setStatus(HttpStatus.OK.value());
			esito.setMessaggio("AGGIORNAMENTO AVVENUTO CON SUCCESSO");
			esito.setBody(mensaService.getAllMense());
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
