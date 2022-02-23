package it.gesev.mensa.entity;

import java.time.LocalTime;
import java.util.Date;
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
@Table(name="MENSA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Mensa 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="CODICE_MENSA")
	private Integer codiceMensa;
	
	@Column(name="DESCRIZIONE_MENSA")
	private String descrizioneMensa;
	
	@Column(name="ORARIO_DAL")
	private LocalTime orarioDal;
	
	@Column(name="ORARIO_AL")
	private LocalTime orarioAl;
	
	@Column(name="SERVIZIO_FESTIVO")
	private String servizioFestivo;
	
	@Column(name="AUTORIZZAZIONE_SANITARIA")
	private byte[] autorizzazioneSanitaria;
	
	@Column(name="NUMERO_AUTORIZZAZIONE_SANITARIA")
	private String numeroAutorizzazioneSanitaria;
	
	@Column(name="DATA_AUTORIZZAZIONE_SANITARIA")
	private Date dataAutorizzazioneSanitaria;
	
	@Column(name="AUT_SANITARAIA_RILASCIATA_DA")
	private String autSanitariaRilasciataDa;
	
	@Column(name="ORA_FINE_PRENOTAZIONE")
	private LocalTime oraFinePrenotazione;
	
	@Column(name="VIA")
	private String via;
	
	@Column(name="NUMERO_CIVICO")
	private Integer numeroCivico;
	
	@Column(name="CAP")
	private String cap;
	
	@Column(name="CITTA")
	private String citta;
	
	@OneToMany(mappedBy= "mensa")
	private List<AssMensaTipoLocale> listaAssDipendenteRuolo;
}
