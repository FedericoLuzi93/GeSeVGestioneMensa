package it.gesev.mensa.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.gesev.mensa.dto.CreaMensaDTO;
import it.gesev.mensa.dto.EnteDTO;
import it.gesev.mensa.dto.EsitoDTO;
import it.gesev.mensa.dto.FELocaliDTO;
import it.gesev.mensa.dto.FEMensaCompletaDTO;
import it.gesev.mensa.dto.FEServizioMensaDTO;
import it.gesev.mensa.dto.FileDTO;
import it.gesev.mensa.dto.MensaDTO;
import it.gesev.mensa.dto.RicercaColonnaDTO;
import it.gesev.mensa.dto.ServizioEventoDTO;
import it.gesev.mensa.dto.TipoDietaDTO;
import it.gesev.mensa.dto.TipoFromaVettovagliamentoDTO;
import it.gesev.mensa.dto.TipoLocaleDTO;
import it.gesev.mensa.dto.TipoPastoDTO;
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
	@PostMapping(value = "/creaMensa", consumes = {"multipart/form-data"})
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> createMensa(@RequestPart( name = "file", required = false) MultipartFile multipartFile, @RequestParam("creaMensaDTO") String LCreaMensaDTO)
	{
		logger.info("Accesso al servizio createMensa");
		EsitoDTO esito = new EsitoDTO();
		try
		{
			int posizione = LCreaMensaDTO.indexOf("\"creaMensaDTO\":");
			String JSON = LCreaMensaDTO.substring(posizione + "\"creaMensaDTO\":".length(), LCreaMensaDTO.length() - 1);
//			String JSON = LCreaMensaDTO;
			CreaMensaDTO creaMensaDTO = new Gson().fromJson(JSON, CreaMensaDTO.class);			
			mensaService.createMensa(creaMensaDTO, multipartFile);
			esito.setStatus(HttpStatus.OK.value());
			esito.setMessaggio("INSERIMENTO AVVENUTO CON SUCCESSO");
			esito.setBody(mensaService.getMensaPerEnte(creaMensaDTO.getIdEnte()));
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
	@PutMapping(value = "/aggiornaMensa/{idMensa}", consumes = {"multipart/form-data"})
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> updateMensa(@RequestPart( name = "file", required = false) MultipartFile multipartFile, @RequestParam("creaMensaDTO") String LCreaMensaDTO, @PathVariable int idMensa)
	{
		logger.info("Accesso al servizio updateMensa");
		EsitoDTO esito = new EsitoDTO();
		try
		{
			int posizione = LCreaMensaDTO.indexOf("\"creaMensaDTO\":");
			String JSON = LCreaMensaDTO.substring(posizione + "\"creaMensaDTO\":".length(), LCreaMensaDTO.length() - 1);
			//String JSON = LCreaMensaDTO;
			CreaMensaDTO creaMensaDTO = new Gson().fromJson(JSON, CreaMensaDTO.class);	
			mensaService.updateMensa(creaMensaDTO, idMensa, multipartFile);
			esito.setStatus(HttpStatus.OK.value());
			esito.setMessaggio("AGGIORNAMENTO AVVENUTO CON SUCCESSO");
			esito.setBody(mensaService.getMensaPerEnte(creaMensaDTO.getIdEnte()));
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

	/* Disattiva una Mensa */
	@PutMapping("/DisattivaMensa/{idMensa}")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> disableMensa(@RequestBody MensaDTO mensaDTO, @PathVariable int idMensa)
	{
		logger.info("Accesso al servizio updateMensa");
		EsitoDTO esito = new EsitoDTO();
		try
		{
			mensaService.disableMensa(mensaDTO, idMensa);
			esito.setStatus(HttpStatus.OK.value());
			esito.setMessaggio("DISABILITAZIONE AVVENUTA CON SUCCESSO");
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

	/* Leggi singola mensa */
	@GetMapping("/getSingolaMensa/{idMensa}")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> getSingolaMensa(@PathVariable int idMensa)
	{
		logger.info("Accesso al servizio getSingolaMensa");
		EsitoDTO esito = new EsitoDTO();
		HttpStatus status = null;
		try
		{
			MensaDTO mensaDTO = mensaService.getSingolaMensa(idMensa);
			esito.setBody(mensaDTO);
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

	/* Invio del File */
	@GetMapping(value = "/downloadFileAutorizzazioneMensa/{idMensa}")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Richiesta download file Autorizzazione Mensa andata a buon fine"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<Resource> downloadFileControdeduzioni(@PathVariable int idMensa)
	{
		logger.info("Accesso al servizio downloadFileAutorizzazioneMensa");
		HttpHeaders headers = new HttpHeaders();

		/* Invio FIle */
		FileDTO fileDTO = mensaService.getFile(idMensa);

		headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileDTO.getNomeFile());
		headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);

		return ResponseEntity.ok().headers(headers).contentLength(fileDTO.getAutorizzazioneSanitaria().length)
				.contentType(MediaType.APPLICATION_OCTET_STREAM).body(new ByteArrayResource(fileDTO.getAutorizzazioneSanitaria()));

	}

	/* Cerca Mense per Id Ente */
	@GetMapping("/getMensaPerEnte/{idEnte}")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> getMensaPerEnte(@PathVariable int idEnte)
	{
		logger.info("Accesso al servizio getMensaPerEnte");
		EsitoDTO esito = new EsitoDTO();
		HttpStatus status = null;
		try
		{
			List<MensaDTO> listaMensaDTO = mensaService.getMensaPerEnte(idEnte);
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

	/* --------------------------------------------------------------------------------- */

	/* leggi Lista Locali */
	@GetMapping("/leggiListaLocali")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> getAllLista()
	{
		logger.info("Accesso al servizio getAllLista");
		EsitoDTO esito = new EsitoDTO();
		HttpStatus status = null;
		try
		{
			List<TipoLocaleDTO> listaTipoLocaleDTO = mensaService.getAllLocali();
			esito.setBody(listaTipoLocaleDTO);
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

	/* Leggi locali per mensa */
	@GetMapping("/leggiLocaliPerMensa/{idMensa}")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> getLocaliPerMensa(@PathVariable int idMensa)
	{
		logger.info("Accesso al servizio getLocaliPerMensa");
		EsitoDTO esito = new EsitoDTO();
		HttpStatus status = null;
		try
		{
			List<FELocaliDTO> listaFELocalIDTO = mensaService.getLocaliPerMensa(idMensa);
			esito.setBody(listaFELocalIDTO);
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

	/* leggi enti */
	@GetMapping("/leggiEnti")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> getAllEnti()
	{
		logger.info("Accesso al servizio getAllEnti");
		EsitoDTO esito = new EsitoDTO();
		HttpStatus status = null;
		try
		{
			List<EnteDTO> listaEntiDTO = mensaService.getAllEnti();
			esito.setBody(listaEntiDTO);
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

	/* leggi Tipo froma vettovagliamento */
	@GetMapping("/leggiTipoFormaVettovagliamento")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> getAllTipoFormaVettovagliamento()
	{
		logger.info("Accesso al servizio getAllTipoFormaVettovagliamento");
		EsitoDTO esito = new EsitoDTO();
		HttpStatus status = null;
		try
		{
			List<TipoFromaVettovagliamentoDTO> listaVettovagliamentoDTOs = mensaService.getAllTipoFormaVettovagliamento();
			esito.setBody(listaVettovagliamentoDTOs);
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

	/* leggi Tipo Pasto */
	@GetMapping("/leggiTipoPasto")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> getAllTipoPasto()
	{
		logger.info("Accesso al servizio getAllTipoPasto");
		EsitoDTO esito = new EsitoDTO();
		HttpStatus status = null;
		try
		{
			List<TipoPastoDTO> lisaTipoPastoDTO = mensaService.getAllTipoPasto();
			esito.setBody(lisaTipoPastoDTO);
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

	/* Get Servizi per idMensa */
	@GetMapping("/getServiziPerMensa/{idMensa}")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> getServiziPerMensa(@PathVariable int idMensa)
	{
		logger.info("Accesso al servizio getServiziPerMensa");
		EsitoDTO esito = new EsitoDTO();
		HttpStatus status = null;
		try
		{
			FEServizioMensaDTO serviziMensa = mensaService.getServiziPerMensa(idMensa);
			esito.setBody(serviziMensa);
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

	/* Lista Ente Filtrato Per Mensa */
	@GetMapping("/getEntiFiltratiPerMensa/{idMensa}")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> getEntiFiltratiPerMensa(@PathVariable int idMensa)
	{
		logger.info("Accesso al servizio getEntiFiltratiPerMensa");
		EsitoDTO esito = new EsitoDTO();
		HttpStatus status = null;
		try
		{
			List<EnteDTO> listaEnteDTO = mensaService.getEntiFiltratiPerMensa(idMensa);
			esito.setBody(listaEnteDTO);
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

	/* Servizio Evento per mensa */
	@GetMapping("/leggiServizioEventoPerMensa/{idMensa}")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> getServizioEventoPerMensa(@PathVariable int idMensa)
	{
		logger.info("Accesso al servizio getServizioEventoPerMensa");
		EsitoDTO esito = new EsitoDTO();
		HttpStatus status = null;
		try
		{
			List<ServizioEventoDTO> listaServizioEventoDTO = mensaService.getServizioEventoPerMensa(idMensa);
			esito.setBody(listaServizioEventoDTO);
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
	
	/* leggi Tipo Dieta */
	@GetMapping("/leggiTuttiTipoDieta")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> getAllTipoDieta()
	{
		logger.info("Accesso al servizio getAllTipoDieta");
		EsitoDTO esito = new EsitoDTO();
		HttpStatus status = null;
		try
		{
			List<TipoDietaDTO> listTipoDietaDTO = mensaService.getAllTipoDieta();
			esito.setBody(listTipoDietaDTO);
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
	
	/* leggi Tipo Dieta per idMensa */
	@GetMapping("/leggiTipoDietaPerMensa{idMensa}")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> getTipoDietaPerMensa(@PathVariable int idMensa)
	{
		logger.info("Accesso al servizio leggiTipoDietaPerMensa");
		EsitoDTO esito = new EsitoDTO();
		HttpStatus status = null;
		try
		{
			List<TipoDietaDTO> listTipoDietaDTO = mensaService.getTipoDietaPerMensa(idMensa);
			esito.setBody(listTipoDietaDTO);
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
	
	/* Leggi Singola Mensa completa */
	@GetMapping("/getSingolaMensaCompleta/{idMensa}")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> getSingolaMensaCompleta(@PathVariable int idMensa)
	{
		logger.info("Accesso al servizio getSingolaMensaCompleta");
		EsitoDTO esito = new EsitoDTO();
		HttpStatus status = null;
		try
		{
			FEMensaCompletaDTO feMensaCompletaDTO = mensaService.getSingolaMensaCompleta(idMensa);
			esito.setBody(feMensaCompletaDTO);
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
	
	/* Ricerca Mense */
	@PostMapping("/ricercaMensa/{IdEnte}")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> ricercaMense(@PathVariable int IdEnte, @RequestBody List<RicercaColonnaDTO> colonne)
	{
		logger.info("Accesso al servizio getAllMensa");
		EsitoDTO esito = new EsitoDTO();
		HttpStatus status = null;
		try
		{
			List<MensaDTO> listaMensaDTO = mensaService.ricercaMense(IdEnte, colonne);
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
	
}
