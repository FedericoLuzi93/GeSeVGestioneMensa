package it.gesev.mensa.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.gesev.mensa.dto.AssDipendenteRuoloDTO;
import it.gesev.mensa.dto.DettaglioRuoloDTO;
import it.gesev.mensa.dto.EsitoDTO;
import it.gesev.mensa.dto.OrganoDirettivoDTO;
import it.gesev.mensa.dto.RicercaColonnaDTO;
import it.gesev.mensa.exc.GesevException;
import it.gesev.mensa.service.RuoliService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/ruoli")
public class RuoliController 
{
	@Autowired
	private RuoliService ruoliService;
	
	private static final Logger logger = LoggerFactory.getLogger(RuoliController.class);
	private final String MESSAGGIO_ERRORE_INTERNO = "Si e' verificato un errore interno";
	
	@GetMapping("/dettagliRuoli")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> getDettagliRuoli()
	{
		logger.info("Accesso al servizio getDettagliRuoli");
		EsitoDTO esito = new EsitoDTO();
		HttpStatus status = null;
		
		try
		{
			DettaglioRuoloDTO dettaglio = ruoliService.getDettaglioRuoli();
			esito.setBody(dettaglio);
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
	
	@GetMapping("/elencoOrganiDirettivo")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> getElencoOrgani()
	{
		logger.info("Accesso al servizio getElencoOrgani");
		EsitoDTO esito = new EsitoDTO();
		HttpStatus status = null;
		
		try
		{
			esito.setBody(ruoliService.getListaOrganiDirettivi());
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
	
	@GetMapping("/elencoRuoli")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> getElencoRuoli()
	{
		logger.info("Accesso al servizio getElencoRuoli");
		EsitoDTO esito = new EsitoDTO();
		HttpStatus status = null;
		
		try
		{
			esito.setBody(ruoliService.getRuoliByIdOrdineDirettivo());
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
	
	@PostMapping("/inserisciRuolo")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> inserisciRuolo(@RequestBody AssDipendenteRuoloDTO associazione)
	{
		logger.info("Accesso al servizio inserisciRuolo");
		EsitoDTO esito = new EsitoDTO();
		HttpStatus status = null;
		
		try
		{
			ruoliService.aggiungiRuoloDipendente(associazione);
			esito.setBody(ruoliService.getDettaglioRuoli());
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
	
	@PostMapping("/ricercaDipendenti")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> ricercaDipendenti(@RequestBody List<RicercaColonnaDTO> listaColonne)
	{
		logger.info("Accesso al servizio inserisciRuolo");
		EsitoDTO esito = new EsitoDTO();
		HttpStatus status = null;
		
		try
		{
			esito.setBody(ruoliService.ricercaDipendenti(listaColonne));
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
	
	@PostMapping("/updateRuolo")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> updateRuolo(@RequestBody AssDipendenteRuoloDTO associazione)
	{
		logger.info("Accesso al servizio updateRuolo");
		EsitoDTO esito = new EsitoDTO();
		HttpStatus status = null;
		
		try
		{
			esito.setBody(ruoliService.updateRuoloDipendente(associazione));
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
	
	@DeleteMapping("/cancellaRuolo/{idRuoloDipendente}")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> cancellaRuolo(@PathVariable("idRuoloDipendente") Integer idRuoloDipendente)
	{
		logger.info("Accesso al servizio cancellaRuolo");
		EsitoDTO esito = new EsitoDTO();
		HttpStatus status = null;
		
		try
		{
			esito.setBody(ruoliService.cancellaRuolo(idRuoloDipendente));
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
	
	/* Crea nuovo Organo direttivo */
	@PostMapping("/creaNuovoOrganoDirettivo")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> creaNuovoOrganoDirettivo(@RequestBody OrganoDirettivoDTO organoDirettivoDTO)
	{
		logger.info("Accesso al servizio creaNuovoOrganoDirettivo");
		EsitoDTO esito = new EsitoDTO();	
		try
		{
			ruoliService.creaNuovoOrganoDirettivo(organoDirettivoDTO);
			esito.setStatus(HttpStatus.OK.value());
			esito.setMessaggio("INSERIMENTO AVVENUTO CON SUCCESSO");
			esito.setBody(ruoliService.getListaOrganiDirettivi());
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
	
	/* Modifica Organo direttivo */
	@PutMapping("/modificaOrganoDirettivo/{idOrganoDirettivo}")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> modificaOrganoDirettivo(@RequestBody OrganoDirettivoDTO organoDirettivoDTO, @PathVariable int idOrganoDirettivo)
	{
		logger.info("Accesso al servizio modificaOrganoDirettivo");
		EsitoDTO esito = new EsitoDTO();	
		try
		{
			ruoliService.modificaOrganoDirettivo(organoDirettivoDTO, idOrganoDirettivo);
			esito.setStatus(HttpStatus.OK.value());
			esito.setMessaggio("MODIFICA AVVENUTA CON SUCCESSO");
			esito.setBody(ruoliService.getListaOrganiDirettivi());
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
	
	/* Cancella Organo direttivo */
	@DeleteMapping("/cancellaOrganoDirettivo/{idOrganoDirettivo}")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> cancellaOrganoDirettivo(@PathVariable int idOrganoDirettivo)
	{
		logger.info("Accesso al servizio cancellaOrganoDirettivo");
		EsitoDTO esito = new EsitoDTO();	
		try
		{
			ruoliService.cancellaOrganoDirettivo(idOrganoDirettivo);
			esito.setStatus(HttpStatus.OK.value());
			esito.setMessaggio("CANCELLAZIONE AVVENUTA CON SUCCESSO");
			esito.setBody(ruoliService.getListaOrganiDirettivi());
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
	
	@GetMapping("/getDipendentiMensa/{idMensa}")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> getDipendentiMensa(@PathVariable("idMensa") Integer idMensa)
	{
		logger.info("Accesso al servizio getDipendentiMensa");
		EsitoDTO esito = new EsitoDTO();
		HttpStatus status = null;
		
		try
		{
			esito.setBody(ruoliService.findDipendenteByIdEnte(idMensa));
			status = HttpStatus.OK;
		}
		
		catch(GesevException gex)   
		{
			logger.info("Si e' verificata un'eccezione", gex);
			status = gex.getStatus();
			esito.setMessaggio(gex.getMessage());
		}
		catch(Exception ex)
		{
			logger.info("Si e' verificata un'eccezione interna", ex);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			esito.setMessaggio(MESSAGGIO_ERRORE_INTERNO);
		}
		
		esito.setStatus(status.value());
		return ResponseEntity.status(status).headers(new HttpHeaders()).body(esito);
	}
}
