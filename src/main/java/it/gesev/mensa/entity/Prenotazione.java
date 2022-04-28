package it.gesev.mensa.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
	
	@ManyToOne
	@JoinColumn(name = "identificativo_sistema_fk")
	private IdentificativoSistema identificativoSistema;
	
	@ManyToOne
	@JoinColumn(name = "identificativo_mensa_fk")
	private Mensa mensa;
	
	@Column(name = "data_prenotazione")
	private Date dataPrenotazione;
	
	@Column(name = "codice_fiscale")
	private String codiceFiscale;
	
	@Column(name = "nome")
	private String nome;
	
	@Column(name = "cognome")
	private String cognome;
	
	@Column(name = "tipo_personale")
	private String tipoPersonale;
	
	@ManyToOne
	@JoinColumn(name = "grado_fk")
	private Grado grado;
	
	@ManyToOne
	@JoinColumn(name = "tipo_grado_fk")
	private TipoGrado tipoGrado;
	
	@ManyToOne
	@JoinColumn(name = "struttura_organizzativa_fk")
	private StrutturaOrganizzativa strutturaOrganizzativa;
	
	@Column(name = "denominazione_unita_funzionale")
	private String denominazioneUnitaFunzionale;
	
	@Column(name = "commensale_esterno")
	private String commensaleEsterno;
	
	@ManyToOne
	@JoinColumn(name = "tipo_pagamento_fk")
	private TipoPagamento tipoPagamento;
	
	@ManyToOne
	@JoinColumn(name = "tipo_pasto_fk")
	private TipoPasto tipoPasto;
	
	@Column(name = "flag_cestino")
	private String flagCestino;
	
	@ManyToOne
	@JoinColumn(name = "tipo_dieta_fk")
	private TipoDieta tipoDieta;
	
	@ManyToOne
	@JoinColumn(name = "tipo_razione_fk")
	private TipoRazione tipoRazione;
	
	@Column(name = "specchio_flag")
	private String specchioFlag;
	
	@Column(name = "col_obbligatoria_flag")
	private String colObbligatoriaFlag;
}
