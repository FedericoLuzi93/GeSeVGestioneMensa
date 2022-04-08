package it.gesev.mensa.dao;

import java.text.ParseException;
import java.util.List;

import it.gesev.mensa.dto.DC4RichiestaDTO;
import it.gesev.mensa.dto.DC4TabellaAllegatoCDTO;
import it.gesev.mensa.dto.DC4TabellaDTO;
import it.gesev.mensa.dto.FirmeDC4;
import it.gesev.mensa.dto.PastiConsumatiDTO;
import it.gesev.mensa.entity.IdentificativoSistema;

public interface ReportDAO 
{

	public void caricaPastiConsumati(List<PastiConsumatiDTO> listaPastiConsumatiCSV) throws ParseException, org.apache.el.parser.ParseException;

	public List<DC4TabellaDTO> richiestaDocumentoDC4(DC4RichiestaDTO dc4RichiestaDTO, boolean includiPrenotati, boolean includiConsumati,
			boolean includiForzaEffettiva) throws ParseException;
	public List<FirmeDC4> richiestaFirmeDC4(DC4RichiestaDTO dc4RichiestaDTO);
	
	public List<DC4TabellaAllegatoCDTO> richiestaDocumentoDC4AllegatoC(DC4RichiestaDTO dc4RichiestaDTO);

	public List<IdentificativoSistema> getAllIdentificativiSistema();




}
