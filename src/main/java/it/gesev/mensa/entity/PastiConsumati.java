package it.gesev.mensa.entity;

import java.time.LocalTime;
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
@Table(name="PASTI_CONSUMATI")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PastiConsumati 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_pasti_consumati")
	private Integer idPastiConsumati;
	
	@Column(name="data_pasto")
	private Date dataPasto;
	
	@Column(name="cognome")
	private String cognome;
	
	@Column(name="nome")
	private String nome;
	
	@Column(name="codice_fiscale")
	private String codiceFiscale;
	
	@Column(name="cmd")
	private String cmd;
	
	@Column(name="tipo_personale")
	private String tipoPersonale;
	
	@Column(name="ora_ingresso")
	private LocalTime oraIngresso;
	
	@ManyToOne
	@JoinColumn(name="tipo_pagamento_fk")
	private TipoPagamento tipoPagamento;
	
	@ManyToOne
	@JoinColumn(name="tipo_pasto_fk")
	private TipoPasto tipoPasto;
	
	@ManyToOne
	@JoinColumn(name="mensa_fk")
	private Mensa mensa;

}
