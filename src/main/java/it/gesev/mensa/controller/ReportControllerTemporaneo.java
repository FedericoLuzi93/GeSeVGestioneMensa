package it.gesev.mensa.controller;

import java.io.FileNotFoundException;
import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.gesev.mensa.dto.DC4RichiestaDTO;
import it.gesev.mensa.dto.FileDC4DTO;
import it.gesev.mensa.service.MensaService;
import it.gesev.mensa.service.ReportService;
import it.gesev.mensa.service.ReportServiceTemporaneo;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/reportTemporaneo ")
public class ReportControllerTemporaneo 
{
	@Autowired
	private ReportServiceTemporaneo reportServiceTemporaneo;

	@Autowired
	private MensaService mensaService;

	private static final Logger logger = LoggerFactory.getLogger(ReportControllerTemporaneo.class);
	private final String MESSAGGIO_ERRORE_INTERNO = "Si e' verificato un errore interno";
	
	/* Download File Report Gesev 3 */
	@GetMapping(value = "/downloadReportGesev3")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Richiesta download Report Gesev3 andata a buon fine"),
			@ApiResponse(code = 400, message = "Dati in ingresso non validi"),
			@ApiResponse(code = 500, message = "Errore interno") })
	public ResponseEntity<Resource> downloadReportGesev3(@RequestParam String anno, @RequestParam String mese, 
			@RequestParam int idEnte, @RequestParam int idOperatore, @RequestParam (required = false) String sistemaPersonale) throws ParseException, FileNotFoundException
	{
		logger.info("Accesso al servizio downloadReportGesev3");
		HttpHeaders headers = new HttpHeaders();
		DC4RichiestaDTO dc4RichiestaDTO = new DC4RichiestaDTO();

		try
		{			
			if(!StringUtils.isBlank(sistemaPersonale))
				dc4RichiestaDTO.setSistemaPersonale(sistemaPersonale);
			else
				dc4RichiestaDTO.setSistemaPersonale(null);

			dc4RichiestaDTO.setAnno(anno);
			dc4RichiestaDTO.setMese(mese);
			dc4RichiestaDTO.setIdEnte(idEnte);
			dc4RichiestaDTO.setIdOperatore(idOperatore);
		}
		catch(Exception e)
		{
			logger.info("Si e' verificata un'eccezione", e);
		}

		/* Invio FIle */
		FileDC4DTO fileDC4DTO = reportServiceTemporaneo.downloadReportGesev3(dc4RichiestaDTO);

		headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileDC4DTO.getNomeFile());
		headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);

		return ResponseEntity.ok().headers(headers).contentLength(fileDC4DTO.getFileDC4().length)
				.contentType(MediaType.APPLICATION_OCTET_STREAM).body(new ByteArrayResource(fileDC4DTO.getFileDC4()));
	}

}
