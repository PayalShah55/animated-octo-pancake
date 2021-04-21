package com.AssignmentTrade.TradeStore.service;


import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.AssignmentTrade.TradeStore.Exception.LowerVersionTradeException;
import com.AssignmentTrade.TradeStore.dto.TradeTransmissionDto;
import com.AssignmentTrade.TradeStore.model.Trade;
import com.AssignmentTrade.TradeStore.repository.TradeStoreRepository;


@RunWith(MockitoJUnitRunner.class)
public class TradeStoreServiceTest {
	
		
	@InjectMocks
	private TradeStoreService tradeStoreSerice;
	
	@Mock
	private TradeStoreRepository tradeStoreRepository;
	
	@Test
	public void checkForMaturityDateLessThanTodaysDateTrueTest() 
	{
		Boolean result=tradeStoreSerice.checkForMaturityDateLessThanTodaysDate(LocalDate.of(2021,4,15));
		assertTrue(result);
	}
	
	@Test
	public void checkForMaturityDateLessThanTodaysDateFalseTest() 
	{
		Boolean result=tradeStoreSerice.checkForMaturityDateLessThanTodaysDate(LocalDate.of(2021,4,30));
		assertFalse(result);
	}

	@Test
	public void checkGreaterVersionExitsTradeTrueTest()
	{
		
		Trade trade=new Trade();
		trade.setId(1l);
		trade.setTradeId("T1");
		trade.setBookId("B1");
		trade.setCounterPartyId("CP-1");
		trade.setCreatedDate(LocalDate.now());
		trade.setExpired("N");
		trade.setMaturityDate(LocalDate.of(2021,7,1));
		trade.setVersion(2);
		
		Mockito.when(tradeStoreRepository.findByTradeIdAndCounterPartyIdAndBookIdAndMaturityDateAndVersionGreaterThan(Mockito.anyString(), Mockito.anyString(),  Mockito.anyString(),
				Mockito.any(),Mockito.anyInt())).thenReturn(trade);
		
		TradeTransmissionDto  tradeTransmissionDto=new TradeTransmissionDto("T1",1, "CP-1", "B1", LocalDate.of(2021,7,1), LocalDate.now(),"N");
		Boolean result=tradeStoreSerice.checkGreaterVersionExitsTrade(tradeTransmissionDto);
		assertTrue(result);
		
	}
	
	
	@Test
	public void checkGreaterVersionExitsTradeFalseTest()
	{
		
		Mockito.when(tradeStoreRepository.findByTradeIdAndCounterPartyIdAndBookIdAndMaturityDateAndVersionGreaterThan(Mockito.anyString(), Mockito.anyString(),  Mockito.anyString(),
				Mockito.any(),Mockito.anyInt())).thenReturn(null);
		
		TradeTransmissionDto  tradeTransmissionDto=new TradeTransmissionDto("T1",1, "CP-1", "B1", LocalDate.of(2021,7,1), LocalDate.now(),"N");
		Boolean result=tradeStoreSerice.checkGreaterVersionExitsTrade(tradeTransmissionDto);
		assertFalse(result);
		
	}
	
	@Test
	public void validateTradeTranmissionTrueTest() throws Exception
	{
		TradeTransmissionDto  tradeTransmissionDto=new TradeTransmissionDto("T1",1, "CP-1", "B1", LocalDate.of(2021,7,1), LocalDate.now(),"N");
		
		TradeStoreService spyService=Mockito.spy(tradeStoreSerice);
		//Mockito.doReturn(false).when(spyService).checkGreaterVersionExitsTrade(tradeTransmissionDto);
		
		//Mockito.doReturn(false).when(spyService).checkForMaturityDateLessThanTodaysDate(Mockito.any());
		
		boolean result= tradeStoreSerice.validateTradeTranmission(tradeTransmissionDto);
		assertTrue(result);
	}
	
	//@Test(expected = LowerVersionTradeException.class)
	public void validateTradeTranmissionExceptionTest() throws Exception
	{

		TradeStoreService spyService=Mockito.spy(tradeStoreSerice);
		Mockito.doReturn(false).when(spyService).checkGreaterVersionExitsTrade(Mockito.any());
		
		TradeTransmissionDto  tradeTransmissionDto=new TradeTransmissionDto("T1",1, "CP-1", "B1", LocalDate.of(2021,7,1), LocalDate.now(),"N");
		boolean result= tradeStoreSerice.validateTradeTranmission(tradeTransmissionDto);
	}
	
	
	@Test
	public void validateTradeTranmissionFalseTest() throws Exception
	{
		
		TradeTransmissionDto  tradeTransmissionDto=new TradeTransmissionDto("T1",1, "CP-1", "B1", LocalDate.of(2019,2,1), LocalDate.now(),"N");
		TradeStoreService spyService=Mockito.spy(tradeStoreSerice);
		
		//Mockito.doReturn(true).when(spyService).checkGreaterVersionExitsTrade(tradeTransmissionDto);
		
		//Mockito.doReturn(true).when(spyService).checkForMaturityDateLessThanTodaysDate(Mockito.any());
		
		boolean result= tradeStoreSerice.validateTradeTranmission(tradeTransmissionDto);
		assertFalse(result);
	}
	
	@Test
	public void tradeTransmissionDetailsWithoutElseTest() throws Exception
	{
		TradeTransmissionDto  tradeTransmissionDto=new TradeTransmissionDto("T1",1, "CP-1", "B1", LocalDate.of(2021,7,1), LocalDate.now(),"N");
		List<TradeTransmissionDto> list=new ArrayList<TradeTransmissionDto>();
		list.add(tradeTransmissionDto);
		
		TradeStoreService spyService=Mockito.spy(tradeStoreSerice);
		
		//Mockito.doReturn(true).when(spyService).validateTradeTranmission(tradeTransmissionDto);
		
		Mockito.when(tradeStoreRepository.findByTradeIdAndCounterPartyIdAndBookIdAndMaturityDateAndVersionEquals(Mockito.anyString(),Mockito.anyString(),Mockito.anyString(),Mockito.any()
				,Mockito.anyInt())).thenReturn(null);
		
		//Mockito.when(tradeStoreRepository.save(Mockito.any())).thenReturn(tradeTransmissionDto);
		
		tradeStoreSerice.tradeTransmissionDetails(list);
		
		
	}
	
	@Test
	public void tradeTransmissionDetailsWithElseTest() throws Exception
	{
		TradeTransmissionDto  tradeTransmissionDto=new TradeTransmissionDto("T1",1, "CP-1", "B1", LocalDate.of(2021,7,1), LocalDate.now(),"N");
		List<TradeTransmissionDto> list=new ArrayList<TradeTransmissionDto>();
		list.add(tradeTransmissionDto);
		
		TradeStoreService spyService=Mockito.spy(tradeStoreSerice);
		
		Trade trade=new Trade();
		trade.setCreatedDate(LocalDate.of(2020, 4, 2));
		trade.setExpired("N");
		
		//Mockito.doReturn(true).when(spyService).validateTradeTranmission(tradeTransmissionDto);
		
		Mockito.when(tradeStoreRepository.findByTradeIdAndCounterPartyIdAndBookIdAndMaturityDateAndVersionEquals(Mockito.anyString(),Mockito.anyString(),Mockito.anyString(),Mockito.any()
				,Mockito.anyInt())).thenReturn(trade);
		
		//Mockito.when(tradeStoreRepository.save(Mockito.any())).thenReturn(tradeTransmissionDto);
		
		tradeStoreSerice.tradeTransmissionDetails(list);
		
		
	}
	
	
	@Test
	public void runSchedulerToUpdateExpiryFlagTest()
	{
		//TradeTransmissionDto  tradeTransmissionDto=new TradeTransmissionDto("T1",1, "CP-1", "B1", LocalDate.of(2021,4,1), LocalDate.now(),"N");
		

		Trade trade=new Trade();
		trade.setId(1l);
		trade.setTradeId("T1");
		trade.setBookId("B1");
		trade.setCounterPartyId("CP-1");
		trade.setCreatedDate(LocalDate.now());
		trade.setExpired("N");
		trade.setMaturityDate(LocalDate.of(2021,4,1));
		trade.setVersion(2);
		
		List<Trade> list=new ArrayList<Trade>();
		list.add(trade);
		
		Mockito.when(tradeStoreRepository.findAll()).thenReturn(list);
		
		tradeStoreSerice.runSchedulerToUpdateExpiryFlag();
		
	}
}
