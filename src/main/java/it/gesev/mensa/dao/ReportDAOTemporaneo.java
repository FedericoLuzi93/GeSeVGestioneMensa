package it.gesev.mensa.dao;

import java.text.ParseException;
import java.util.List;

import it.gesev.mensa.dto.DC4RichiestaDTO;
import it.gesev.mensa.dto.FirmeDC4;
import it.gesev.mensa.jasper.ReportGeSeV3Jasper;

public interface ReportDAOTemporaneo
{

	List<ReportGeSeV3Jasper> downloadReportGesev3(DC4RichiestaDTO dc4RichiestaDTO) throws ParseException;
	List<FirmeDC4> listaFirmeReportGesev3(DC4RichiestaDTO dc4RichiestaDTO);

}
