package it.gesev.mensa.dao;

import java.text.ParseException;
import java.util.List;

import it.gesev.mensa.dto.PastiConsumatiDTO;

public interface ReportDAO 
{

	public void caricaPastiConsumati(List<PastiConsumatiDTO> listaPastiConsumatiCSV) throws ParseException, org.apache.el.parser.ParseException;


}
