package it.gesev.mensa.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.gesev.mensa.dto.AttestazioneClientDTO;
import it.gesev.mensa.dto.AttestazioneClientInputDTO;
import it.gesev.mensa.dto.CodiceOTPDTO;
import it.gesev.mensa.dto.EsitoDTO;
import it.gesev.mensa.exc.GesevException;
import it.gesev.mensa.service.AttestazioneClientService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/attestazioneClient")
public class AttestazioneClientController {
	
	@Autowired
	private AttestazioneClientService attestazioneClientService;
	
	private static final Logger logger = LoggerFactory.getLogger(AttestazioneClientController.class);
	private final String MESSAGGIO_ERRORE_INTERNO = "Si e' verificato un errore interno";

	@PostMapping("/generaCodiceAttestazione")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> generaCOdiceOtp(@RequestBody(required = true) CodiceOTPDTO codiceOTPDTO ){
		logger.info("Accesso al servizio generaCOdiceOtp");
		EsitoDTO esito = new EsitoDTO();
		HttpStatus status = null;
		
		try
		{
			CodiceOTPDTO codiceOTPDTO2 = attestazioneClientService.getCodiceOtp(codiceOTPDTO.getCodiceMensa());
			esito.setBody(codiceOTPDTO2);
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
	
	@PostMapping("/eseguiAttestazione")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> generaAttestazioneMensa(@RequestBody(required = true) AttestazioneClientInputDTO attestazioneClientInputDTO ){
		logger.info("Accesso al servizio generaCOdiceOtp");
		EsitoDTO esito = new EsitoDTO();
		HttpStatus status = null;
		
		try
		{
			AttestazioneClientDTO attestazioneClientDTO = attestazioneClientService.eseguiAttestazione(attestazioneClientInputDTO.fromClientInputDtoToClientDto());
			esito.setBody(attestazioneClientDTO);
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
}
