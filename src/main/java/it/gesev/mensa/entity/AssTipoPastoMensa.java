package it.gesev.mensa.entity;

import java.time.LocalTime;

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
@Table(name="MENSA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssTipoPastoMensa 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ass_tipo_pasto_mensa_id")
	private Integer assTipoPastoMensaId;
	
	@Column(name="ORARIO_DAL")
	private LocalTime orarioDal;
	
	@Column(name="ORARIO_AL")
	private LocalTime orarioAl;
	
	@Column(name="ORA_FINE_PRENOTAZIONE")
	private LocalTime oraFinePrenotazione;
	
	@ManyToOne
	@JoinColumn(name="codice_mensa_fk")
	private Mensa mensa;
	
	@ManyToOne
	@JoinColumn(name="codice_tipo_pasto_fk")
	private TipoPasto tipoPasto;

}
