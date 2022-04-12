package it.gesev.mensa.controller;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.gesev.mensa.dto.DC4RichiestaDTO;
import it.gesev.mensa.dto.DC4TabellaAllegatoCDTO;
import it.gesev.mensa.dto.DC4TabellaDTO;
import it.gesev.mensa.dto.EsitoDTO;
import it.gesev.mensa.dto.FileDC4DTO;
import it.gesev.mensa.dto.FirmaQuotidianaDC4DTO;
import it.gesev.mensa.dto.IdentificativoSistemaDTO;
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
	
	/* Carica pasti consumati CSV */
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
	
	
	/* Carica pasti consumati JSON */
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
	
	/* Richiesta documento DC4 */
	@PostMapping(value = "/richiestaDocumentoDC4")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> richiestaDocumentoDC4(@RequestBody DC4RichiestaDTO dc4RichiestaDTO)
	{
		logger.info("Accesso al servizio richiestaDocumentoDC4");
		EsitoDTO esito = new EsitoDTO();
		try
		{	
			List<DC4TabellaDTO> listaDC4TabellaDTO = new ArrayList<>();
			listaDC4TabellaDTO = reportService.richiestaDocumentoDC4(dc4RichiestaDTO);
			esito.setStatus(HttpStatus.OK.value());
			esito.setMessaggio("DOCUMENTO CREATO CON SUCCESSO");
			esito.setBody(listaDC4TabellaDTO);
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
	
	/* Download File DC4 */
	@PostMapping(value = "/downloadDC4")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Richiesta download DC4 andata a buon fine"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<Resource> downloadFileDC4(@RequestBody DC4RichiestaDTO dc4RichiestaDTO) throws ParseException, FileNotFoundException
	{
		logger.info("Accesso al servizio downloadDC4");
		HttpHeaders headers = new HttpHeaders();

		/* Invio FIle */
		FileDC4DTO fileDC4DTO = reportService.downloadDC4(dc4RichiestaDTO);

		headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileDC4DTO.getNomeFile());
		headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);

		return ResponseEntity.ok().headers(headers).contentLength(fileDC4DTO.getFileDC4().length)
				.contentType(MediaType.APPLICATION_OCTET_STREAM).body(new ByteArrayResource(fileDC4DTO.getFileDC4()));

	}
	
	/* Richiesta documento DC4 */
	@PostMapping(value = "/richiestaDocumentoDC4AllegatoC")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> richiestaDocumentoDC4AllegatoC(@RequestBody DC4RichiestaDTO dc4RichiestaDTO)
	{
		logger.info("Accesso al servizio richiestaDocumentoDC4");
		EsitoDTO esito = new EsitoDTO();
		try
		{	
			List<DC4TabellaAllegatoCDTO> listaDC4TabellaAllegatoC = new ArrayList<>();
			listaDC4TabellaAllegatoC = reportService.richiestaDocumentoDC4AllegatoC(dc4RichiestaDTO);
			esito.setStatus(HttpStatus.OK.value());
			esito.setMessaggio("DOCUMENTO CREATO CON SUCCESSO");
			esito.setBody(listaDC4TabellaAllegatoC);
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
	
	/* Download File DC4 AlleegatoC */
	@PostMapping(value = "/downloadDC4AllegatoC")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Richiesta download DC4 andata a buon fine"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<Resource> downloadDC4AllegatoC(@RequestBody DC4RichiestaDTO dc4RichiestaDTO) throws ParseException, FileNotFoundException
	{
		logger.info("Accesso al servizio downloadDC4");
		HttpHeaders headers = new HttpHeaders();

		/* Invio FIle */
		FileDC4DTO fileDC4DTO = reportService.downloadDC4AllegatoC(dc4RichiestaDTO);

		headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileDC4DTO.getNomeFile());
		headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);

		return ResponseEntity.ok().headers(headers).contentLength(fileDC4DTO.getFileDC4().length)
				.contentType(MediaType.APPLICATION_OCTET_STREAM).body(new ByteArrayResource(fileDC4DTO.getFileDC4()));

	}
	
	/* Leggi tutti identificativi Sistema */
	@GetMapping("/getAllIdentificativiSistema")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> getAllMensa()
	{
		logger.info("Accesso al servizio getAllIdentificativiSistema");
		EsitoDTO esito = new EsitoDTO();
		HttpStatus status = null;
		try
		{
			List<IdentificativoSistemaDTO> listaIdentificativoSistema = reportService.getAllIdentificativiSistema();
			esito.setBody(listaIdentificativoSistema);
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
	
	/* Aggiungi una nuova Firma */
	@PutMapping("/createNuovaFirma")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> createNuovaFirma(@RequestBody FirmaQuotidianaDC4DTO firmaQuotidianaDC4DTO)
	{
		logger.info("Accesso al servizio createNuovaFirma");
		EsitoDTO esito = new EsitoDTO();
		HttpStatus status = null;
		try
		{
			reportService.createNuovaFirma(firmaQuotidianaDC4DTO);
			//esito.setBody(listaIdentificativoSistema);
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
	
	/* Cancella un Firma */
	@DeleteMapping("/deleteFirma")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<EsitoDTO> deleteFirma(@RequestBody FirmaQuotidianaDC4DTO firmaQuotidianaDC4DTO)
	{
		logger.info("Accesso al servizio deleteFirma");
		EsitoDTO esito = new EsitoDTO();
		HttpStatus status = null;
		try
		{
			reportService.deleteFirma(firmaQuotidianaDC4DTO);
			//esito.setBody(listaIdentificativoSistema);
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
