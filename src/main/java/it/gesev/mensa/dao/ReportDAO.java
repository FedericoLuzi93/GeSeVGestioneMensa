package it.gesev.mensa.dao;

import java.text.ParseException;
import java.util.List;

import it.gesev.mensa.dto.DC4RichiestaDTO;
import it.gesev.mensa.dto.DC4TabellaDTO;
import it.gesev.mensa.dto.FirmaQuotidianaDC4DTO;
import it.gesev.mensa.dto.FirmeDC4;
import it.gesev.mensa.dto.PastiConsumatiDTO;
import it.gesev.mensa.dto.SendListPastiDC4AllegatoC;
import it.gesev.mensa.entity.IdentificativoSistema;
import it.gesev.mensa.jasper.NumeroPastiGraduatiJasper;
import it.gesev.mensa.jasper.NumeroPastiUFCJasper;

public interface ReportDAO 
{

	public void caricaPastiConsumati(List<PastiConsumatiDTO> listaPastiConsumatiCSV) throws ParseException, org.apache.el.parser.ParseException;

	public List<DC4TabellaDTO> richiestaDocumentoDC4(DC4RichiestaDTO dc4RichiestaDTO, boolean includiPrenotati, boolean includiConsumati,
			boolean includiForzaEffettiva) throws ParseException;
	public List<FirmeDC4> richiestaFirmeDC4(DC4RichiestaDTO dc4RichiestaDTO);
	
	public SendListPastiDC4AllegatoC richiestaDocumentoDC4AllegatoC(DC4RichiestaDTO dc4RichiestaDTO, 
			List<NumeroPastiUFCJasper> listaPastiUFC, List<NumeroPastiGraduatiJasper> listaPastiGraduati, SendListPastiDC4AllegatoC sendObjList);

	public List<IdentificativoSistema> getAllIdentificativiSistema();

	public int createNuovaFirma(FirmaQuotidianaDC4DTO firmaQuotidianaDC4DTO) throws ParseException;
	public int deleteFirma(FirmaQuotidianaDC4DTO firmaQuotidianaDC4DTO) throws ParseException;





}
