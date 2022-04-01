package it.gesev.mensa.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import it.gesev.mensa.dto.PastiConsumatiDTO;

public interface ReportService 
{

	public void caricaPastiConsumatiCSV(MultipartFile multipartFile) throws IllegalStateException, FileNotFoundException, IOException, ParseException, org.apache.el.parser.ParseException;

	public int caricaPastiConsumatiJson(List<PastiConsumatiDTO> listaPastiConsumatiCSV) throws ParseException, org.apache.el.parser.ParseException;

}
