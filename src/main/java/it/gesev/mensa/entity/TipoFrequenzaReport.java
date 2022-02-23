package it.gesev.mensa.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TIPO_FREQUENZA_REPORT")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TipoFrequenzaReport 
{
	@Id
	@Column(name = "CODICE_TIPO_FREQUENZA_REPORT")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer codiceTipoFrequenzaReport;
	
	@Column(name = "DESCRIZIONE_TIPO_FREQUENZA_REPORT")
	private String descrizioneTipoFrequenzaReport;
	
	@OneToMany(mappedBy = "tipoFrequenzaReport")
	private List<ReportInviato> listaReportInviato;
}
