package it.gesev.mensa.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ASS_REPORT_RUOLO_MENSA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssReportRuoloMensa 
{
	@Id
	@Column(name = "ASS_REPORT_RUOLO_MENSA_ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer assReportRuoloMensaId;
	
	@Column(name = "DATA_INIZIO")
	private Date dataInizio;
	
	@Column(name = "DATA_FINE")
	private Date dataFine;
	
	@Column(name = "ORDINE_FIRMA")
	private Integer ordineFirma;
	
	@ManyToOne
	@JoinColumn(name = "REPORT_FK")
	private Report report;
	
	@ManyToOne
	@JoinColumn(name = "RUOLO_FK")
	private RuoloMensa ruoloMensa;
	

}
