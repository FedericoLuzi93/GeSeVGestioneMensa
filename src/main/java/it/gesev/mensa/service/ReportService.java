package it.gesev.mensa.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import it.gesev.mensa.dto.DC4RichiestaDTO;
import it.gesev.mensa.dto.DC4TabellaAllegatoCDTO;
import it.gesev.mensa.dto.DC4TabellaDTO;
import it.gesev.mensa.dto.FileDC4DTO;
import it.gesev.mensa.dto.FirmaQuotidianaDC4DTO;
import it.gesev.mensa.dto.IdentificativoSistemaDTO;
import it.gesev.mensa.dto.PastiConsumatiDTO;

public interface ReportService 
{

	public void caricaPastiConsumatiCSV(MultipartFile multipartFile) throws IllegalStateException, FileNotFoundException, IOException, ParseException, org.apache.el.parser.ParseException;
	public int caricaPastiConsumatiJson(List<PastiConsumatiDTO> listaPastiConsumatiCSV) throws ParseException, org.apache.el.parser.ParseException;

	public List<DC4TabellaDTO> richiestaDocumentoDC4(DC4RichiestaDTO dc4RichiestaDTO) throws ParseException;
	public FileDC4DTO downloadDC4(DC4RichiestaDTO dc4RichiestaDTO) throws ParseException, FileNotFoundException;
	
	public List<DC4TabellaAllegatoCDTO> richiestaDocumentoDC4AllegatoC(DC4RichiestaDTO dc4RichiestaDTO) throws ParseException;
	public FileDC4DTO downloadDC4AllegatoC(DC4RichiestaDTO dc4RichiestaDTO) throws ParseException, FileNotFoundException;
	
	public List<IdentificativoSistemaDTO> getAllIdentificativiSistema();
	
	public int createNuovaFirma(FirmaQuotidianaDC4DTO firmaQuotidianaDC4DTO) throws ParseException;
	public int deleteFirma(FirmaQuotidianaDC4DTO firmaQuotidianaDC4DTO) throws ParseException;
}
