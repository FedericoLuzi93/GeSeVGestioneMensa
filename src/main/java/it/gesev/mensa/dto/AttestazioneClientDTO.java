package it.gesev.mensa.dto;

import java.util.List;

public class AttestazioneClientDTO {
	
	private String codiceAttestazione;
	private String macAddress;
	private Integer idMensa;
	private List<TipoDietaDTO> tipiDieta;
	private List<TipoPastoDTO> tipiPasto;
	private List<OperatoreDTO> elencoOperatori;
	
	public AttestazioneClientDTO() {
		super();
	}

	public AttestazioneClientDTO(String codiceAttestazione, String macAddress, Integer idMensa,
			List<TipoDietaDTO> tipiDieta, List<TipoPastoDTO> tipiPasto, List<OperatoreDTO> elencoOperatori) {
		super();
		this.codiceAttestazione = codiceAttestazione;
		this.macAddress = macAddress;
		this.idMensa = idMensa;
		this.tipiDieta = tipiDieta;
		this.tipiPasto = tipiPasto;
		this.elencoOperatori = elencoOperatori;
	}

	public String getCodiceAttestazione() {
		return codiceAttestazione;
	}

	public void setCodiceAttestazione(String codiceAttestazione) {
		this.codiceAttestazione = codiceAttestazione;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public Integer getIdMensa() {
		return idMensa;
	}

	public void setIdMensa(Integer idMensa) {
		this.idMensa = idMensa;
	}

	public List<TipoDietaDTO> getTipiDieta() {
		return tipiDieta;
	}

	public void setTipiDieta(List<TipoDietaDTO> tipiDieta) {
		this.tipiDieta = tipiDieta;
	}

	public List<TipoPastoDTO> getTipiPasto() {
		return tipiPasto;
	}

	public void setTipiPasto(List<TipoPastoDTO> tipiPasto) {
		this.tipiPasto = tipiPasto;
	}

	public List<OperatoreDTO> getElencoOperatori() {
		return elencoOperatori;
	}

	public void setElencoOperatori(List<OperatoreDTO> elencoOperatori) {
		this.elencoOperatori = elencoOperatori;
	}
	
}
