package it.gesev.mensa.dto;

public class AttestazioneClientInputDTO {

	private String codiceAttestazione;
	private String macAddress;
	
	public AttestazioneClientInputDTO() {
		super();
	}

	public AttestazioneClientInputDTO(String codiceAttestazione, String macAddress) {
		super();
		this.codiceAttestazione = codiceAttestazione;
		this.macAddress = macAddress;
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
	
	public AttestazioneClientDTO fromClientInputDtoToClientDto() {
		AttestazioneClientDTO attestazioneClientDTO = new AttestazioneClientDTO();
		attestazioneClientDTO.setCodiceAttestazione(this.getCodiceAttestazione());
		attestazioneClientDTO.setMacAddress(this.getMacAddress());
		return attestazioneClientDTO;
	}

}
