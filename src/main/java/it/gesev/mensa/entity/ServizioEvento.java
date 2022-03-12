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

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="SERVIZIO_EVENTO")
@Getter
@Setter
public class ServizioEvento 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_SERVIZIO_EVENTO")
	private Integer idServizioEvento;
	
	@Column(name="DATA_SERVIZIO_EVENTO")
	private Date dataServizioEvento;
	
	@Column(name="DESCRIZIONE_SERVIZIO_EVENTO")
	private String descrizioneServizioEvento;
	
	@ManyToOne
	@JoinColumn(name="MENSA_FK")
	private Mensa mensa;
}
