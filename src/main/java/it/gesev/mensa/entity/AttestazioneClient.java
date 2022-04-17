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
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "attestazione_client")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttestazioneClient 
{
	@Id
	@Column(name = "id_attestazione_client")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idAttestazioneClient;
	
	@Column(name = "codice_otp")
	private String codiceOtp;
	
	@Column(name = "mac_address")
	private String macAddress;
	
	@Column(name = "data_ultima_attivita")
	private Date dataUltimaAttivita;
	
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.DETACH,
		 	CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "mensa_fk")
	private Mensa mensa;
	
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.DETACH,
		 	CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "dipendente_fk")
	private Dipendente dipendente;
	
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.DETACH,
		 	CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "dipendente_esterno_fk")
	private DipendenteEsterno dipendenteEsterno;
	
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.DETACH,
		 	CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "stato_client_fk")
	private StatoClient statoClient;
	
}