package it.gesev.mensa.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="IDENTIFICATIVO_SISTEMA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdentificativoSistema
{
	@Id
	@Column(name="id_sistema")
	private String idSistema;
	
	@Column(name = "descrizione_sistema")
	private String descrizioneSistema;
	
	@OneToMany(mappedBy = "identificativoSistema", fetch = FetchType.LAZY)
	List<Prenotazione> listaPrenotazioni;
}
