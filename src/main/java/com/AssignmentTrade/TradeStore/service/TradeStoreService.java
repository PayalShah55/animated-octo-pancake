package com.AssignmentTrade.TradeStore.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.AssignmentTrade.TradeStore.Exception.LowerVersionTradeException;
import com.AssignmentTrade.TradeStore.dto.TradeTransmissionDto;
import com.AssignmentTrade.TradeStore.model.Trade;
import com.AssignmentTrade.TradeStore.repository.TradeStoreRepository;




// this class performs business layer validations,if valid calls repository methods to save the trade in trade store

@Service
@Transactional
public class TradeStoreService {
	
	@Autowired
	private TradeStoreRepository tradeStoreRepository;
	
	 private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	// validator method if any one of validation methods returns true the trade is not stored in trade store
	public boolean validateTradeTranmission(TradeTransmissionDto tradeTransmissionDto) throws Exception
	{
		LOGGER.info("inside validateTradeTranmission method of service");
		
		boolean result=true;
		try
		{
			if(checkGreaterVersionExitsTrade(tradeTransmissionDto))
			{
				result=false;
				throw new LowerVersionTradeException("ERROR", "Lower version trade Rejected");
			}
			if(checkForMaturityDateLessThanTodaysDate(tradeTransmissionDto.getMaturityDate()))
				result=false;
		}
		catch(Exception e)
		{
			LOGGER.info("Rejecting Trade "+tradeTransmissionDto.getTradeId()+": "+e.getMessage());
		}
		
		return result;
	}

	
	
	//checks if maturity date is less than Today's Date if yes sets true
	public boolean checkForMaturityDateLessThanTodaysDate(LocalDate maturityDate) {
		// TODO Auto-generated method stub
		LOGGER.info("inside checkForMaturityDate method of service");
		
		if(maturityDate.isBefore(LocalDate.now()))
			return true;
		
		return false;
		
	}


	// checks if any trade with version greater than the sent trade exists in Db if Yes sets true
	public boolean checkGreaterVersionExitsTrade(TradeTransmissionDto tradeTransmissionDto) {
		// TODO Auto-generated method stub
		
		LOGGER.info("inside checkGreaterVersionExitsTrade method of service");
		
		boolean result=false;
		
		Trade trade=tradeStoreRepository.findByTradeIdAndCounterPartyIdAndBookIdAndMaturityDateAndVersionGreaterThan(tradeTransmissionDto.getTradeId(),
				tradeTransmissionDto.getCounterPartyId(),tradeTransmissionDto.getBookId(),tradeTransmissionDto.getMaturityDate(),tradeTransmissionDto.getVersion());
		
		if(trade!=null)
			result= true;
	
		return result;
		
	}


	
	// use to store the trade transmissions in db by calling validateTradeTranmission method
	public void tradeTransmissionDetails(List<TradeTransmissionDto> tradeTransmissionDto) throws Exception
	{
		LOGGER.info("inside tradeTransmissionDetails method of service");
		
		try
		{
			for(TradeTransmissionDto dto: tradeTransmissionDto)
			{
				if(validateTradeTranmission(dto))
				{
					
					Trade trade=tradeStoreRepository.findByTradeIdAndCounterPartyIdAndBookIdAndMaturityDateAndVersionEquals(dto.getTradeId(), dto.getCounterPartyId(),dto.getBookId(),
							dto.getMaturityDate(),dto.getVersion());
					
					if(trade!=null)
					{
						trade.setCreatedDate(dto.getCreatedDate());
						trade.setExpired(dto.getExpired());
						tradeStoreRepository.save(trade);
					}
					
					else
					{
					
					trade=new Trade();
					trade.setTradeId(dto.getTradeId());
					trade.setVersion(dto.getVersion());
					trade.setCounterPartyId(dto.getCounterPartyId());
					trade.setBookId(dto.getBookId());
					trade.setMaturityDate(dto.getMaturityDate());
					trade.setCreatedDate(dto.getCreatedDate());
					trade.setExpired(dto.getExpired());
					
					tradeStoreRepository.save(trade);
					}
				}
					
			}
		}
		catch(Exception e)
		{
			throw e;
		}
	}
	
	
	//updates the Expiry flag of all trades in the store after every 2 minutes
	
	@Scheduled(cron = "0 0/2 * * * ?")
	public void runSchedulerToUpdateExpiryFlag() {
		
		LOGGER.info("inside scheduler for updating Expiry flag");
		
		List<Trade> tradeList=tradeStoreRepository.findAll();
		tradeList.forEach(trade->
		{
			if(trade.getMaturityDate().isBefore(LocalDate.now()))
			{
				trade.setExpired("Y");
				tradeStoreRepository.save(trade);
			}
		});
		
	}
}
