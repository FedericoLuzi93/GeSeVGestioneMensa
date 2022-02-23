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
@Table(name = "ASS_MENSA_TIPO_LOCALE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssMensaTipoLocale
{
	@Id
	@Column(name = "ASS_MENSA_TIPO_LOCALE_ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer assMensaTipoLocaleId;
	
	@Column(name = "DATA_INIZIO")
	private Date dataInizio;
	
	@Column(name = "DATA_FINE")
	private Date dataFine;
	
	@Column(name = "SUPERFICIE")
	private Integer superficie;
	
	@Column(name = "NUMERO_LOCALI")
	private Integer numeroLocali;
	
	@Column(name = "NOTE")
	private String note;
	
	@ManyToOne
	@JoinColumn(name="CODICE_MENSA_FK")
	private Mensa mensa;
	
	@ManyToOne
	@JoinColumn(name="CODICE_TIPO_LOCALE_FK")
	private TipoLocale tipoLocale;
	

}
