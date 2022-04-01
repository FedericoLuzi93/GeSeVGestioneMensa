package it.gesev.mensa.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import it.gesev.mensa.dto.PastiConsumatiDTO;
import it.gesev.mensa.entity.Mensa;
import it.gesev.mensa.entity.PastiConsumati;
import it.gesev.mensa.entity.TipoPagamento;
import it.gesev.mensa.entity.TipoPasto;
import it.gesev.mensa.exc.GesevException;
import it.gesev.mensa.repository.MensaRepository;
import it.gesev.mensa.repository.PastiConsumatiRepository;
import it.gesev.mensa.repository.TipoPagamentoRepository;
import it.gesev.mensa.repository.TipoPastoRepository;
import it.gesev.mensa.utils.ControlloData;

@Repository
@Component
public class ReportDAOImpl implements ReportDAO
{
	private static Logger logger = LoggerFactory.getLogger(ReportDAOImpl.class);
	
	@Value("${gesev.data.format}")
	private String dateFormat;
	
	@Autowired
	private MensaRepository mensaRepository;
	
	@Autowired
	private TipoPastoRepository tipoPastoRepository;
	
	@Autowired
	private TipoPagamentoRepository tipoPagamentoRepository;
	
	@Autowired
	private PastiConsumatiRepository pastiConsumatiRepository;
	
	/* Carica pasti consumati CSV*/
	@Override
	public void caricaPastiConsumati(List<PastiConsumatiDTO> listaPastiConsumatiCSV) throws ParseException, org.apache.el.parser.ParseException 
	{
		logger.info("Accesso a caricaPastiConsumati, classe ReportDAOImpl");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		
		for(PastiConsumatiDTO pcCSV : listaPastiConsumatiCSV)
		{
			PastiConsumati pc = new PastiConsumati();
			
			Optional<Mensa> optionalMensa = mensaRepository.findByCodiceMensa(pcCSV.getMensa());
			if(!optionalMensa.isPresent())
				throw new GesevException("Impossibile caricare i pasti consumati, mensa non valida", HttpStatus.BAD_REQUEST);
			pc.setMensa(optionalMensa.get());
			
			pc.setDataPasto(simpleDateFormat.parse(pcCSV.getDataPasto()));
			
		
			pc.setNome(pcCSV.getNome());
			pc.setCognome(pcCSV.getCognome());
			pc.setCodiceFiscale(pcCSV.getCodiceFiscale());
			
			if(!StringUtils.isBlank(pcCSV.getCmd()))
				pc.setCmd(pcCSV.getCmd());
			
			if(!StringUtils.isBlank(pcCSV.getTipoPersonale()))
				pc.setTipoPersonale(pcCSV.getTipoPersonale());
			
			Optional<TipoPagamento> optionalPagamento = tipoPagamentoRepository.findByIdTipoPagamento(pcCSV.getTipoPagamento());
			if(!optionalPagamento.isPresent())
				throw new GesevException("Impossibile caricare i pasti consumati, tipo pagamento non valido", HttpStatus.BAD_REQUEST);
			pc.setTipoPagamento(optionalPagamento.get());
			
			Optional<TipoPasto> optionalTipoPasto = tipoPastoRepository.findById(pcCSV.getTipoPasto());
			if(!optionalTipoPasto.isPresent())
				throw new GesevException("Impossibile caricare i pasti consumati, tipo pasto non valido", HttpStatus.BAD_REQUEST);
			pc.setTipoPasto(optionalTipoPasto.get());
			
			pc.setOraIngresso(ControlloData.controlloTempo(pcCSV.getOraIngresso()));
			
			pastiConsumatiRepository.save(pc);
			
		}
		
		
	}

}
