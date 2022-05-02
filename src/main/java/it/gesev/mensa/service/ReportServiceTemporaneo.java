package it.gesev.mensa.service;

import java.io.FileNotFoundException;
import java.text.ParseException;

import it.gesev.mensa.dto.DC4RichiestaDTO;
import it.gesev.mensa.dto.FileDC4DTO;

public interface ReportServiceTemporaneo {

	FileDC4DTO downloadReportGesev3(DC4RichiestaDTO dc4RichiestaDTO) throws FileNotFoundException, ParseException;

}
