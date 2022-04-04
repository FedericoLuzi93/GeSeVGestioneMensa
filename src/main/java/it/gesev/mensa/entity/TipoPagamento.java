package it.gesev.mensa.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="TIPO_PAGAMENTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoPagamento 
{
	@Id
	@Column(name="id_tipo_pagamento")
	private String idTipoPagamento;
	
	@Column(name="descrizione_tipo_pagamento")
	private String descrizioneTipoPagamento;
	
	@OneToMany(mappedBy= "tipoPagamento")
	private List<PastiConsumati> listaPastiConsumati;
	
	@OneToMany(mappedBy = "tipoPagamento")
	private List<Prenotazione> listaPrenotazioni;

}
