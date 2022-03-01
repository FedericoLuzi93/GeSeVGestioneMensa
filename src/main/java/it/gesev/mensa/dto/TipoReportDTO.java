package it.gesev.mensa.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoReportDTO implements Serializable
{
	private static final long serialVersionUID = 866045953525553857L;
	private Integer codiceTipoReport;
	private String descrizioneTipoRecord;
}
