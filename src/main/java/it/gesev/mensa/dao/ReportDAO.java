package it.gesev.mensa.dao;

import java.text.ParseException;
import java.util.List;

import it.gesev.mensa.dto.DC4RichiestaDTO;
import it.gesev.mensa.dto.DC4TabellaAllegatoCDTO;
import it.gesev.mensa.dto.DC4TabellaDTO;
import it.gesev.mensa.dto.FEPastiDC4Graduati;
import it.gesev.mensa.dto.FEPastiDC4USC;
import it.gesev.mensa.dto.FirmaQuotidianaDC4DTO;
import it.gesev.mensa.dto.FirmeDC4;
import it.gesev.mensa.dto.MenuDTO;
import it.gesev.mensa.dto.MenuLeggeroDTO;
import it.gesev.mensa.dto.PastiConsumatiDTO;
import it.gesev.mensa.dto.SendListPastiDC4AllegatoC;
import it.gesev.mensa.dto.SendListaDC1Prenotati;
import it.gesev.mensa.entity.IdentificativoSistema;
import it.gesev.mensa.entity.Pietanza;
import it.gesev.mensa.jasper.DC1NomJasper;
import it.gesev.mensa.jasper.DC1NomNumericaJasper;

public interface ReportDAO 
{

	public void caricaPastiConsumati(List<PastiConsumatiDTO> listaPastiConsumatiCSV) throws ParseException, org.apache.el.parser.ParseException;
	
	public List<IdentificativoSistema> getAllIdentificativiSistema();
	public int createNuovaFirma(FirmaQuotidianaDC4DTO firmaQuotidianaDC4DTO) throws ParseException;
	public int deleteFirma(FirmaQuotidianaDC4DTO firmaQuotidianaDC4DTO) throws ParseException;
	
	public List<DC4TabellaDTO> richiestaDocumentoDC4(DC4RichiestaDTO dc4RichiestaDTO, boolean includiPrenotati, boolean includiConsumati,
			boolean includiForzaEffettiva) throws ParseException;
	public List<FirmeDC4> richiestaFirmeDC4(DC4RichiestaDTO dc4RichiestaDTO);
	
	public SendListPastiDC4AllegatoC richiestaDocumentoDC4AllegatoC(DC4RichiestaDTO dc4RichiestaDTO, 
			List<FEPastiDC4USC> listaPastiUFC, List<FEPastiDC4Graduati> listaPastiGraduati, SendListPastiDC4AllegatoC sendObjList);
	public  List<DC4TabellaAllegatoCDTO> downloadDocumentoDC4AllegatoC(DC4RichiestaDTO dc4RichiestaDTO);
	public List<DC4TabellaAllegatoCDTO> downloadDC4AllegatoCUfficiali(DC4RichiestaDTO dc4RichiestaDTO);
	public List<DC4TabellaAllegatoCDTO> downloadDC4AllegatoCGraduati(DC4RichiestaDTO dc4RichiestaDTO);

	public SendListaDC1Prenotati richiestaDocumentoDC1Prenotati(DC4RichiestaDTO dc4RichiestaDTO,
			SendListaDC1Prenotati sendObjList) throws ParseException;
	public List<FirmeDC4> richiestaFirmeDC1(DC4RichiestaDTO dc4RichiestaDTO);

	public SendListaDC1Prenotati richiestaDocumentoDC1Consumati(DC4RichiestaDTO dc4RichiestaDTO,
			SendListaDC1Prenotati sendObjList) throws ParseException;

	public DC1NomNumericaJasper richiestaDocumentoDC1NominativoNumerica(DC4RichiestaDTO dc4RichiestaDTO) throws ParseException;
	public List<DC1NomJasper> richiestaDocumentoDC1Nominativo(DC4RichiestaDTO dc4RichiestaDTO) throws ParseException;

	public List<Pietanza> richiestaMenuDelGiorno(MenuDTO menuDTO) throws ParseException;
	public List<Pietanza> richiestaTuttePietanze(MenuLeggeroDTO menuLeggeroDTO) throws ParseException;




	


	





}
