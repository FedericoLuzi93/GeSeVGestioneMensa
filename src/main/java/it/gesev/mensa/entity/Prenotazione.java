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
@Table(name="PRENOTAZIONE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prenotazione 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_prenotazione")
	private Integer idPrenotazione;
	
	@Column(name = "data_prenotazione")
	private Date dataPrenotazione;
	
	@Column(name = "codice_fiscale")
	private String codiceFiscale;
	
	@Column(name = "flag_cestino")
	private String flagCestino;
	
	@ManyToOne
	@JoinColumn(name = "identificativo_sistema_fk")
	private IdentificativoSistema identificativoSistema;
	
	@ManyToOne
	@JoinColumn(name = "ente_fk")
	private Ente ente;
	
	@ManyToOne
	@JoinColumn(name = "tipo_pasto_fk")
	private TipoPasto tipoPasto;
	
	@ManyToOne
	@JoinColumn(name = "tipo_dieta_fk")
	private TipoDieta tipoDieta;
	
	@ManyToOne
	@JoinColumn(name = "tipo_razione_fk")
	private TipoRazione tipoRazione;
}
