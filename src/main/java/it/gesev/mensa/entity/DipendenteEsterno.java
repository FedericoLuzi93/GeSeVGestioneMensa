package it.gesev.mensa.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "DIPENDENTE_ESTERNO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DipendenteEsterno 
{
	@Id
	@Column(name = "id_dipendente_esterno")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idDipendenteEsterno;
	
	@Column(name = "nome_dipendente_esterno")
	private String nomeDipendenteEsterno;
	
	@Column(name = "cognome_dipendente_esterno")
	private String cognomeDipendenteEsterno;
	
	@Column(name = "email_dipendente_esterno")
	private String emailDipendenteEsterno;
	
	@Column(name = "cf_dipendente_esterno")
	private String codiceFiscale;
	
	@OneToMany(mappedBy = "dipendenteEsterno")
	private List<AssDipendenteRuolo> listaAssDipendenteRuolo;
		
}
