package it.gesev.mensa.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO implements Serializable
{
	private static final long serialVersionUID = -2108982679473026692L;
	private String codiceReport;
	private String descrizioneRecord;
}
