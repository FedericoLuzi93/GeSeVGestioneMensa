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

import lombok.Data;

@Entity
@Table(name="firma_quotidiana_dc4")
@Data
public class FirmaQuodidiana 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_firma")
	private Integer idFirma;
	
	@Column(name="data_firma")
	private Date dataFirma;
	
	@Column(name="id_operatore")
	private Integer idOperatore;
	
	@ManyToOne
	@JoinColumn(name = "ente_fk")
	private Ente ente;

}
