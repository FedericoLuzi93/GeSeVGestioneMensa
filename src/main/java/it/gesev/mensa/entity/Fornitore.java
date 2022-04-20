package it.gesev.mensa.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="FORNITORE")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Fornitore implements Serializable
{
	
	private static final long serialVersionUID = 3098607660112617547L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="CODICE")
	private Integer codice;
	
	@Column(name="DESCRIZIONE")
	private String descrizione;
	
	@OneToMany(mappedBy="fornitore", cascade={CascadeType.PERSIST, CascadeType.DETACH,
		 	CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
	private List<TestataMovimento> testataMovimento;
	
	@OneToMany(mappedBy="fornitore", cascade={CascadeType.PERSIST, CascadeType.DETACH,
		 	CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
	private List<AssDipendenteRuolo> listaAssDipendenteRuolo;
	
	
}
