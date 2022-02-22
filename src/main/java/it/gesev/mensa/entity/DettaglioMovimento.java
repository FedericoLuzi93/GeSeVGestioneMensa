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
@Table(name="DETTAGLIO_MOVIMENTO")
@Getter
@Setter
public class DettaglioMovimento 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_DETTAGLIO_MOVIMENTO ")
	private Integer idDettaglioMovimento;
	
	@Column(name="DATA_DETTAGLIO_MOVIMENTO")
	private Date date;
	
	@Column(name="QUANTITA_RICHIESTA")
	private double quantitaRichiesta;
	
	@Column(name="QUANTITA_EFFETTIVA")
	private double quantitaEffettiva;
	
	@Column(name="PREZZO_UNITARIO")
	private double prezzoUnitario;
	
	@Column(name="TOTALE_VALORE")
	private double totaleValore;
	
	@OneToOne(cascade={CascadeType.PERSIST, CascadeType.DETACH,
		 	CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name="DERRATA")
	private Derrata derrata;

	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.DETACH,
		 	CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name="NUM_PROGRESSIVO")
	private TestataMovimento testataMovimento;
	
	public DettaglioMovimento()
	{
		
	}

	public DettaglioMovimento(Integer idDettaglioMovimento, Date date, double quantitaRichiesta, double quantitaEffettiva,
			double prezzoUnitario, double totaleValore) 
	{
		this.idDettaglioMovimento = idDettaglioMovimento;
		this.date = date;
		this.quantitaRichiesta = quantitaRichiesta;
		this.quantitaEffettiva = quantitaEffettiva;
		this.prezzoUnitario = prezzoUnitario;
		this.totaleValore = totaleValore;
	}
	
	
	
}
