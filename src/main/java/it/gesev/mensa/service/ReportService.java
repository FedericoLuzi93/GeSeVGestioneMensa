package it.gesev.mensa.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import it.gesev.mensa.dto.CaricamentoPastiConsumatiDTO;
import it.gesev.mensa.dto.DC4RichiestaDTO;
import it.gesev.mensa.dto.DC4TabellaDTO;
import it.gesev.mensa.dto.FEDC1Nominativo;
import it.gesev.mensa.dto.FileDC4DTO;
import it.gesev.mensa.dto.FirmaQuotidianaDC4DTO;
import it.gesev.mensa.dto.Ges2ComunicazionePastiDTO;
import it.gesev.mensa.dto.IdentificativoSistemaDTO;
import it.gesev.mensa.dto.MenuDTO;
import it.gesev.mensa.dto.MenuLeggeroDTO;
import it.gesev.mensa.dto.PastiConsumatiDTO;
import it.gesev.mensa.dto.PastiPrenotatiDTO;
import it.gesev.mensa.dto.SendListPastiDC4AllegatoC;
import it.gesev.mensa.dto.SendListaDC1Prenotati;
import it.gesev.mensa.jasper.DC1MilitariJasper;

public interface ReportService 
{

	//Caricamento File
	public void caricaPastiConsumatiCSV(MultipartFile multipartFile) throws IllegalStateException, FileNotFoundException, IOException, ParseException, org.apache.el.parser.ParseException;
	public int caricaPastiConsumatiJson(List<CaricamentoPastiConsumatiDTO> listaPastiConsumatiCSV) throws ParseException, org.apache.el.parser.ParseException;

	//DC4
	public List<DC4TabellaDTO> richiestaDocumentoDC4(DC4RichiestaDTO dc4RichiestaDTO) throws ParseException;
	public FileDC4DTO downloadDC4(DC4RichiestaDTO dc4RichiestaDTO) throws ParseException, FileNotFoundException;

	public SendListPastiDC4AllegatoC richiestaDocumentoDC4AllegatoC(DC4RichiestaDTO dc4RichiestaDTO, SendListPastiDC4AllegatoC sendObjList) throws ParseException;
	public FileDC4DTO downloadDC4AllegatoC(DC4RichiestaDTO dc4RichiestaDTO) throws ParseException, FileNotFoundException;
	public FileDC4DTO downloadDC4AllegatoCUfficiali(DC4RichiestaDTO dc4RichiestaDTO) throws FileNotFoundException;
	public FileDC4DTO downloadDC4AllegatoCGraduati(DC4RichiestaDTO dc4RichiestaDTO) throws FileNotFoundException;
	
	//Firme
	public int createNuovaFirma(FirmaQuotidianaDC4DTO firmaQuotidianaDC4DTO) throws ParseException;
	public int deleteFirma(FirmaQuotidianaDC4DTO firmaQuotidianaDC4DTO) throws ParseException;
	
	//DC1
	public List<DC1MilitariJasper> richiestaDocumentoDC1Prenotati(DC4RichiestaDTO dc4RichiestaDTO,
			SendListaDC1Prenotati sendObjList) throws ParseException;
	FileDC4DTO downloadDC1Prenotati(DC4RichiestaDTO dc4RichiestaDTO) throws ParseException, FileNotFoundException;
	
	public List<DC1MilitariJasper> richiestaDocumentoDC1Consumati(DC4RichiestaDTO dc4RichiestaDTO,
			SendListaDC1Prenotati sendObjList) throws ParseException;
	public FileDC4DTO downloadDC1Consumati(DC4RichiestaDTO dc4RichiestaDTO) throws ParseException, FileNotFoundException;
	
	public FEDC1Nominativo richiestaDocumentoDC1Nominativo(DC4RichiestaDTO dc4RichiestaDTO) throws ParseException;
	public FileDC4DTO downloadDC1Nominativo(DC4RichiestaDTO dc4RichiestaDTO) throws ParseException, FileNotFoundException;
	
	//Menu del Giorno
	public MenuDTO richiestaMenuDelGiorno(MenuDTO menuDTO) throws ParseException;
	public FileDC4DTO downloadMenuDelGiorno(MenuLeggeroDTO menuLeggeroDTO) throws ParseException, FileNotFoundException;
	
	//Chiamate
	public List<PastiConsumatiDTO> getListaPastiConsumatiFiltrata(DC4RichiestaDTO dc4RichiestaDTO) throws ParseException;
	public List<IdentificativoSistemaDTO> getAllIdentificativiSistema();
	public List<PastiPrenotatiDTO> getListaPastiPrenotatiFiltrata(DC4RichiestaDTO dc4RichiestaDTO) throws ParseException;
	
	// Pasti giornalieri
	public Ges2ComunicazionePastiDTO comunicazionePastiDaApprontare(DC4RichiestaDTO dc4RichiestaDTO) throws ParseException;

}
