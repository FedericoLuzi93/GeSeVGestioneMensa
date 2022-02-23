package it.gesev.mensa.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "REPORT_INVIATO")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportInviato 
{
	@Id
	@Column(name = "CODICE_REPORT_INVIATO")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer codiceReportInviato;
	
	@Column(name = "DATA_CREAZIONE")
	private Date dataCreazione;
	
	@Column(name="DOCUMENTO_INVIATO")
	private byte[] documentoInviato;
	
	@Column(name="DOCUMENTO_FIRMATO")
	private byte[] documentoFirmato;
	
	@Column(name = "GIORNO_REPORT")
	private Integer giornoReport;
	
	@Column(name = "MESE_REPORT")
	private Integer meseReport;
	
	@Column(name = "SETTIMANA_REPORT")
	private Integer settimanaReport;
	
	@Column(name = "ANNO_REPORT")
	private Integer annoReport;
	
	@ManyToOne
	@JoinColumn(name = "REPORT_FK")
	private Report report;
	
	@ManyToOne
	@JoinColumn(name = "TIPO_FREQUENZA_REPORT_FK")
	private TipoFrequenzaReport tipoFrequenzaReport;
	
	@OneToMany(mappedBy = "reportInviato")
	private List<AssRepInviatiDipStatoRep> listaAssRepInviatiDipStatoRep;
	
}
