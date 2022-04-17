package it.gesev.mensa.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
	
//	@Column(name="TIPO_DIETA")
//	private String tipoDieta = "Normale";

//	@Column(name="SERVIZIO_FESTIVO")
//	private String servizioFestivo;
	
	@Column(name="SERVIZIO_FESTIVO_SABATO")
	private String servizioFestivoSabato;
	
	@Column(name="SERVIZIO_FESTIVO_DOMENICA")
	private String servizioFestivoDomenica;
	
	@Column(name="AUTORIZZAZIONE_SANITARIA")
	private byte[] autorizzazioneSanitaria;
	
	@Column(name="NUMERO_AUTORIZZAZIONE_SANITARIA")
	private String numeroAutorizzazioneSanitaria;
	
	@Column(name="DATA_AUTORIZZAZIONE_SANITARIA")
	private Date dataAutorizzazioneSanitaria;
	
	@Column(name="AUT_SANITARAIA_RILASCIATA_DA")
	private String autSanitariaRilasciataDa;

	@Column(name="VIA")
	private String via;
	
	@Column(name="NUMERO_CIVICO")
	private Integer numeroCivico;
	
	@Column(name="CAP")
	private String cap;
	
	@Column(name="CITTA")
	private String citta;
	
	@Column(name="PROVINCIA")
	private String provincia;
	
	@Column(name="TELEFONO")
	private String telefono;
	
	@Column(name="FAX")
	private String fax;
	
	@Column(name="EMAIL")
	private String email;
	
	@Column(name="DATA_INIZIO_SERVIZIO")
	private Date dataInizioServizio;
	
	@Column(name="DATA_FINE_SERVIZIO")
	private Date dataFineServizio;
	
	@ManyToOne
	@JoinColumn(name="TIPO_FORMA_VETTOVAGLIAMENTO_FK")
	private TipoFormaVettovagliamento tipoFormaVettovagliamento;
	
	@OneToMany(mappedBy = "mensa", fetch = FetchType.LAZY)
	private List<AssMensaTipoLocale> listaAssDipendenteRuolo;
	
	@OneToMany(mappedBy = "mensa", fetch = FetchType.LAZY)
	private List<AssDipendenteRuolo> listaDipendentiRuoli;

	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.DETACH,
		 	CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name="ENTE_FK")
	private Ente ente;
	
	@OneToMany(mappedBy= "mensa", fetch = FetchType.LAZY)
	private List<AssTipoPastoMensa> assTipoPastoMensa;
	
	@OneToMany(mappedBy= "mensa", fetch = FetchType.LAZY)
	private List<ServizioEvento> listaServizioEvento;
	
	@OneToMany(mappedBy = "mensa", fetch = FetchType.LAZY)
	private List<AssMensaTipoDieta> listaAssMensaTipoDieta;
	
	@OneToMany(mappedBy= "mensa", fetch = FetchType.LAZY)
	private List<PastiConsumati> listaPastiConsumati;
	
	@OneToMany(mappedBy= "mensa", fetch = FetchType.LAZY)
	private List<Prenotazione> listaPrenotazioni;
	
	@OneToMany(mappedBy = "mensa", fetch = FetchType.LAZY)
	private List<AttestazioneClient> listaAttestazioni;
	
}
