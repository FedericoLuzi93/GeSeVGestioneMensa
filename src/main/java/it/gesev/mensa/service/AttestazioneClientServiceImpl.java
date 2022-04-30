package it.gesev.mensa.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import it.gesev.mensa.dao.AttestazioneClientDAO;
import it.gesev.mensa.dto.AttestazioneClientDTO;
import it.gesev.mensa.dto.CodiceOTPDTO;
import it.gesev.mensa.dto.OperatoreDTO;
import it.gesev.mensa.dto.TipoDietaDTO;
import it.gesev.mensa.dto.TipoPastoDTO;
import it.gesev.mensa.entity.AssDipendenteRuolo;
import it.gesev.mensa.entity.AssMensaTipoDieta;
import it.gesev.mensa.entity.AssTipoPastoMensa;
import it.gesev.mensa.entity.AttestazioneClient;
import it.gesev.mensa.entity.CodiceOTP;
import it.gesev.mensa.entity.Grado;
import it.gesev.mensa.entity.StatoClient;
import it.gesev.mensa.enums.RuoloMensaEnum;
import it.gesev.mensa.enums.StatoClientEnum;
import it.gesev.mensa.exc.GesevException;
import it.gesev.mensa.repository.AssMensaTipoDietaRepository;
import it.gesev.mensa.repository.AssRuoloDipendenteRepository;
import it.gesev.mensa.repository.AssTipoPastoMensaRepository;
import it.gesev.mensa.repository.CodiceOTPRepository;
import it.gesev.mensa.repository.StatoClientRepository;

@Service
public class AttestazioneClientServiceImpl implements AttestazioneClientService {
	
	@Value("${gesev.data.format}")
	private String dateFormat;
	
	private static Logger logger = LoggerFactory.getLogger(AttestazioneClientServiceImpl.class);
	
	@Autowired
	private AttestazioneClientDAO attestazioneClientDAO;
	
	@Autowired
	private CodiceOTPRepository codiceOTPRepository;
	
	@Autowired
	private StatoClientRepository statoClientRepository;
	
	@Autowired
	private AssMensaTipoDietaRepository assMensaTipoDietaRepository;
	
	@Autowired
	private AssTipoPastoMensaRepository assTipoPastoMensaRepository;
	
	@Autowired
	private AssRuoloDipendenteRepository assRuoloDipendenteRepository;

	@Override
	public CodiceOTPDTO getCodiceOtp(Integer idMensa) throws GesevException {
		logger.info("Inizio in getCodiceOtp");
		String codiceOTP = attestazioneClientDAO.generaCodiceOTP(idMensa);
		logger.info("Fine getCodiceOtp");
		return new CodiceOTPDTO(codiceOTP, idMensa);
	}
	
	/**
	 * @param AttestazioneClientDTO
	 * @return AttestazioneClientDTO
	 * 
	 * Prende in ingresso il DTO del client, in cui è presente solo il codice attestazione ed il mac address.
	 * Si carica l'entity CodiceOTP e memorizza su DB l'attestazione. 
	 * A quel punto esegue le query per il caricamento le liste dei dati di risposta (TipoDietaDTO,
	 * TipoPastoDTO e OperatoreDTO), chiama il metodo per la creazione del DTO di risposta e lo ritorna. 
	 */
	@Override
	@Transactional
	public AttestazioneClientDTO eseguiAttestazione(AttestazioneClientDTO attestazioneClientDto) throws GesevException {
		logger.info("Inizio in eseguiAttestazione");
		AttestazioneClientDTO attestazioneToReturn = null;
		AttestazioneClient attestazioneClient = new AttestazioneClient();
		try {
			// validare il campo macaddress con regex
			if (!codiceOTPRepository.existsById(attestazioneClientDto.getCodiceAttestazione())) {
				logger.error("Errore nell'attestazione del client: codice attestazione inesistente " + attestazioneClientDto.getCodiceAttestazione());
				throw new GesevException("Errore nell'attestazione del client: codice attestazione inesistente " + attestazioneClientDto.getCodiceAttestazione(), HttpStatus.BAD_REQUEST);
			}
			CodiceOTP codiceOTP = codiceOTPRepository.getById(attestazioneClientDto.getCodiceAttestazione());
			StatoClientEnum statoClientEnum = StatoClientEnum.ATTESTATO;
			StatoClient statoClient = statoClientRepository.findByDesc(statoClientEnum.getStatoClient());
			attestazioneClient.setCodiceOtp(codiceOTP);
			attestazioneClient.setMacAddress(attestazioneClientDto.getMacAddress());
			SimpleDateFormat formatter = new SimpleDateFormat(this.dateFormat);
			attestazioneClient.setDataUltimaAttivita(LocalDate.now());
			attestazioneClient.setStatoClient(statoClient);
			
			attestazioneClientDAO.eseguiAttestazioneClient(attestazioneClient);
			
			List<AssMensaTipoDieta> assMensaTipoDietas = assMensaTipoDietaRepository.cercaPerMensa(codiceOTP.getMensa().getCodiceMensa());
			
			List<AssTipoPastoMensa> assTipoPastoMensas = assTipoPastoMensaRepository.cercaPerMensa(codiceOTP.getMensa().getCodiceMensa());
			
			RuoloMensaEnum ruoloMensaEnum = RuoloMensaEnum.OPERATORE;
			List<AssDipendenteRuolo> assDipendenteRuolos = assRuoloDipendenteRepository.findDipendentiByRuoloAndMensa(ruoloMensaEnum.getTipoRuolo(), codiceOTP.getMensa().getCodiceMensa());
			
			attestazioneToReturn = creaAttestazioneClientDTO(codiceOTP, assMensaTipoDietas, assTipoPastoMensas, assDipendenteRuolos, attestazioneClientDto.getMacAddress());
		} catch (GesevException ges) {
			throw ges;
		} catch (Exception ex) {
			logger.error("Errore nell'attestazione del client: " + attestazioneClient.toString());
			throw new GesevException("Errore nell'attestazione del client", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		logger.info("Fine eseguiAttestazione");
		return attestazioneToReturn;
	}
	
	/**
	 * Prende come argomento codiceOTP e le liste associative per formare un AttestazioneClientDTO. 
	 * Cicla ogni lista di entity per creare le liste di DTO da memorizzare nell'oggetto da restituire 
	 */
	private AttestazioneClientDTO creaAttestazioneClientDTO(CodiceOTP codiceOTP, List<AssMensaTipoDieta> assMensaTipoDietas
			, List<AssTipoPastoMensa> assTipoPastoMensas, List<AssDipendenteRuolo> assDipendenteRuolos, String macAddress) {
		AttestazioneClientDTO attestazioneClientDTO = new AttestazioneClientDTO();
		List<TipoDietaDTO> tipoDietaDTOs = new ArrayList<>();
		List<TipoPastoDTO> tipoPastoDTOs = new ArrayList<>();
		List<OperatoreDTO> operatoreDTOs = new ArrayList<>();
		
		attestazioneClientDTO.setIdMensa(codiceOTP.getMensa().getCodiceMensa());
		Iterator<AssMensaTipoDieta> iteratorTipoDieta = assMensaTipoDietas.iterator();
		while(iteratorTipoDieta.hasNext()) {
			TipoDietaDTO tipoDietaDTO = new TipoDietaDTO();
			tipoDietaDTOs.add(tipoDietaDTO.fromEntityToDTO(iteratorTipoDieta.next().getTipoDieta()));
		}
		attestazioneClientDTO.setTipiDieta(tipoDietaDTOs);
		
		Iterator<AssTipoPastoMensa> iteratorTipoPasto = assTipoPastoMensas.iterator();
		while(iteratorTipoPasto.hasNext()) {
			TipoPastoDTO tipoPastoDTO = new TipoPastoDTO();
			AssTipoPastoMensa assTipoPastoMensa = iteratorTipoPasto.next();
			tipoPastoDTO = tipoPastoDTO.fromEntityToDTO(assTipoPastoMensa.getTipoPasto());
			tipoPastoDTO.setOrarioDal(assTipoPastoMensa.getOrarioDal().toString());
			tipoPastoDTO.setOrarioAl(assTipoPastoMensa.getOrarioAl().toString());
			tipoPastoDTOs.add(tipoPastoDTO);
		}
		attestazioneClientDTO.setTipiPasto(tipoPastoDTOs);
		
		Iterator<AssDipendenteRuolo> iteratorDipendente = assDipendenteRuolos.iterator();
		while(iteratorDipendente.hasNext()) {
			OperatoreDTO operatoreDTO = new OperatoreDTO();
			AssDipendenteRuolo assDipendenteRuolo = iteratorDipendente.next();
			
			if(assDipendenteRuolo.getDipendente() != null)
			{
				operatoreDTO.setNome(assDipendenteRuolo.getDipendente().getNome());
				operatoreDTO.setCognome(assDipendenteRuolo.getDipendente().getCognome());
				operatoreDTO.setCodiceFiscale(assDipendenteRuolo.getDipendente().getCodiceFiscale());
				operatoreDTO.setEmail(assDipendenteRuolo.getDipendente().getEmail());
				
				if(assDipendenteRuolo.getDipendente().getGrado() != null)
				{
					Grado grado = attestazioneClientDAO.getGrado(assDipendenteRuolo.getDipendente().getGrado());
					if(grado != null)
						operatoreDTO.setGrado(grado.getDescbGrado());
				}
				
				operatoreDTO.setNumeroCMD(assDipendenteRuolo.getDipendente().getCmd());
				operatoreDTO.setTipoPersonale(assDipendenteRuolo.getDipendente().getTipoPersonale());
			}
			
			else
			{
				operatoreDTO.setNome(assDipendenteRuolo.getDipendenteEsterno().getNomeDipendenteEsterno());
				operatoreDTO.setCognome(assDipendenteRuolo.getDipendenteEsterno().getCognomeDipendenteEsterno());
				operatoreDTO.setCodiceFiscale(assDipendenteRuolo.getDipendenteEsterno().getCodiceFiscale());
				operatoreDTO.setEmail(assDipendenteRuolo.getDipendenteEsterno().getEmailDipendenteEsterno());
				
			}
			
			operatoreDTOs.add(operatoreDTO);
		}
		attestazioneClientDTO.setElencoOperatori(operatoreDTOs);
		attestazioneClientDTO.setMacAddress(macAddress);
		attestazioneClientDTO.setCodiceAttestazione(codiceOTP.getCodice());
		
		return attestazioneClientDTO;
	}

}
