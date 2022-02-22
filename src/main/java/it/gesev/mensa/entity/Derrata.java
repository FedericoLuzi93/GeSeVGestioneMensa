package it.gesev.mensa.entity;


import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="DERRATA")
@Getter
@Setter
public class Derrata 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="DERRATA_ID")
	private Integer derrataId;
	
	@Column(name="DESCRIZIONE_DERRATA")
	private String descrizioneDerrata;
	
	@Column(name="UNITA_MISURA")
	private String unitaMisura;
	
	@Column(name="PREZZO")
	private double prezzo;
	
	@Column(name="GIACENZA")
	private double giacenza;
	
	@Column(name="DATA_AGGIORNAMENTO_GIACENZA")
	private Date dataAggiornamentoGiacenza;
	
	@Column(name="QUANTITA_MIMINA")
	private double quantitaMinima;
	
	@Column(name="CODICE_MENSA")
	private int codiceMensa;
	
	@ManyToOne(	cascade={CascadeType.PERSIST, CascadeType.DETACH,
		 	CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name="TIPO_DERRATA")
	private TipoDerrata tipoDerrata;
	
	@OneToOne(mappedBy="derrata")
	private DettaglioPrelevamento dettaglioPrelevamento;
	
	@OneToOne(mappedBy="derrata")
	private DettaglioMovimento dettaglioMovimento;
	
	public Derrata()
	{
		
	}

	public Derrata(Integer derrataId, String descrizioneDerrata, String unitaMisura, double prezzo, int giagenza,
			Date dataAggiornamentoGiacenza, int quantitaMinima, int codiceMensa) 
	{
		this.derrataId = derrataId;
		this.descrizioneDerrata = descrizioneDerrata;
		this.unitaMisura = unitaMisura;
		this.prezzo = prezzo;
		this.giacenza = giagenza;
		this.dataAggiornamentoGiacenza = dataAggiornamentoGiacenza;
		this.quantitaMinima = quantitaMinima;
		this.codiceMensa = codiceMensa;
	}
}
