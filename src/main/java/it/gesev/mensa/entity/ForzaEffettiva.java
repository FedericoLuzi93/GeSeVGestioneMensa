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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "FORZA_EFFETTIVA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForzaEffettiva 
{
	@Id
	@Column(name = "id_forza_effettiva")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idForzaEffettiva;
	
	@Column(name = "data_riferimento")
	private Date dataRiferimento;
	
	@Column(name = "num_dipendenti")
	private Integer numDipendenti;
	
	@ManyToOne
	@JoinColumn(name="ente_fk")
	private Ente ente;
}
