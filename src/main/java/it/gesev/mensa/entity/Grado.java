package it.gesev.mensa.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="GRADO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Grado 
{
	@Id
	@Column(name = "shsgra_cod_uid_pk")
	private String shsgraCodUidPk;
	
	@Column(name = "shsor_cod_uid_pk")
	private String shsorCodUidPk;
	
	@Column(name = "shcmc_cod_uid_pk")
	private String shcmcCodUidPk;
	
	@Column(name = "descb_grado")
	private String descbGrado;
	
	@Column(name = "descr_grado")
	private String descrGrado;
	
	@Column(name = "num_ordine")
	private String numOrdine;
	
	@Column(name = "data_inizio")
	private Date dataInizio;
	
	@Column(name = "data_fine")
	private Date dataFine;
	
	@ManyToOne
	@JoinColumn(name = "tipo_grado_fk")
	private TipoGrado tipoGrado;
	
	@OneToMany(mappedBy = "grado")
	private List<Prenotazione> listaPrenotazioni;
	
}
