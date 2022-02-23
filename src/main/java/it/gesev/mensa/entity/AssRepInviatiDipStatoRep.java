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
@Table(name = "ASS_REP_INVIATI_DIP_STATO_REP")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AssRepInviatiDipStatoRep 
{
	@Id
	@Column(name = "ASS_REP_INVIATI_DIP_STATO_REP_ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer assRepInviatiDipStatoRepId;
	
	@Column(name = "DATA_FIRMA")
	private Date dataFirma;
	
	@ManyToOne
	@JoinColumn(name = "DIPENDENTE_FK")
	private Dipendente dipendente;
	
	@ManyToOne
	@JoinColumn(name = "REPORT_INVIATO_FK")
	private ReportInviato reportInviato;
	
	@ManyToOne
	@JoinColumn(name = "STATO_REPORT_FK")
	private StatoReport statoReport;
	
	

}
