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
@Table(name="DETTAGLIO_PRELEVAMENTO")
@Getter
@Setter
public class DettaglioPrelevamento 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="DETTAGLIO_PRELEVAMENTO_ID")
	private Integer dettaglioPrelevamentoId;

	@Column(name="DATA_DETTAGLIO_PRELEVAMENTO")
	private Date date;
	
	@Column(name="QUANTITA")
	private int quantita;
	
	
	@ManyToOne(	cascade={CascadeType.PERSIST, CascadeType.DETACH,
		 	CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name="NUMERO_BUONO")
	private PrelevamentoMensa prelevamentoMensa;
	
	@OneToOne	
	@JoinColumn(name="DERRATA")
	private Derrata derrata;
	
	public DettaglioPrelevamento()
	{
		
	}

	public DettaglioPrelevamento(Integer dettaglioPrelevamentoId, Date date, int quantita, int numeroBuono) 
	{
		this.dettaglioPrelevamentoId = dettaglioPrelevamentoId;
		this.date = date;
		this.quantita = quantita;

	}
}
