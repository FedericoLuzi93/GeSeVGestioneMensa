package it.gesev.mensa.entity;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="TIPO_MOVIMENTO")
@Getter
@Setter
public class TipoMovimento 
{
	@Id
	@Column(name="CODICE")
	private Integer codice;
	
	@Column(name="TIPO_DESCRIZIONE")
	private String descrizione;
	
	@Column(name="SEGNO")
	private String segno;
	
	@OneToMany(mappedBy="tipoMovimento", fetch = FetchType.LAZY)
	private List<TestataMovimento> listaTestataMovimento;
	
	public TipoMovimento()
	{
		
	}

	public TipoMovimento(Integer codice, String descrizione, String segno) 
	{
		this.codice = codice;
		this.descrizione = descrizione;
		this.segno = segno;
	}
}
