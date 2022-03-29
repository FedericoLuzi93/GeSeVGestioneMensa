package it.gesev.mensa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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

}
