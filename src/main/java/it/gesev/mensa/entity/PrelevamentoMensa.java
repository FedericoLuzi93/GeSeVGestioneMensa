package it.gesev.mensa.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="PRELEVAMENTO_MENSA")
@Getter
@Setter
public class PrelevamentoMensa 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="NUMERO_BUONO")
	private Integer numeroBuono;
	
	@Column(name="DATA_PRELEVAMENTO_MENSA")
	private Date date;
	
	//Nome Entita nell'altra tabella
	//mappedyBy NON ha la FR
	@OneToMany(mappedBy="prelevamentoMensa")
	private List<DettaglioPrelevamento> dettaglioPrelevamento;
	
	public PrelevamentoMensa()
	{
		
	}

	public PrelevamentoMensa(Integer numeroBuono, Date date) 
	{
		this.numeroBuono = numeroBuono;
		this.date = date;
	}
}
