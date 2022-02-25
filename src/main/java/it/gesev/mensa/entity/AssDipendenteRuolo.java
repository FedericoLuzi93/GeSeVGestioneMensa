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
@Table(name = "ASS_DIPENDENTE_RUOLO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssDipendenteRuolo 
{
	@Id
	@Column(name = "ASS_DIPENDENTE_RUOLO_ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer assDipendenteRuoloId;
	
	@ManyToOne
	@JoinColumn(name = "DIPENDENTE_FK")
	private Dipendente dipendente;
	
	@ManyToOne
	@JoinColumn(name = "RUOLO_FK")
	private RuoloMensa ruolo;
	
	@ManyToOne
	@JoinColumn(name = "ORGANO_DIRETTIVO_FK")
	private OrganoDirettivo organoDirettivo;
	
	@Column(name = "DATA_INIZIO_RUOLO")
	private Date dataInizioRuolo;
	
	@Column(name = "DATA_FINE_RUOLO")
	private Date dataFineRuolo;
	
	
	
}
