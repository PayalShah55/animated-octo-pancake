package com.AssignmentTrade.TradeStore;

import static org.hamcrest.CoreMatchers.allOf;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.AssignmentTrade.TradeStore.dto.TradeTransmissionDto;
import com.AssignmentTrade.TradeStore.service.TradeStoreService;



@SpringBootApplication
@EnableScheduling
public class TradeStoreApplication implements CommandLineRunner{

	@Autowired
	private TradeStoreService tradeStoreService;
	
	public static void main(String[] args) {
		SpringApplication.run(TradeStoreApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		
		// setting data for trades transmission from here
		List<TradeTransmissionDto> tradeList=new ArrayList<TradeTransmissionDto>();
		
		TradeTransmissionDto tradeTransmissionDto1=new TradeTransmissionDto("T1", 1,"CP-1","B1",LocalDate.of(2020,5,20),LocalDate.of(2021,4,21),"N");
		TradeTransmissionDto tradeTransmissionDto2=new TradeTransmissionDto("T2", 2,"CP-2","B1",LocalDate.of(2021,5,20),LocalDate.of(2021,4,21),"N");
		TradeTransmissionDto tradeTransmissionDto3=new TradeTransmissionDto("T2", 1,"CP-1","B1",LocalDate.of(2021,5,20),LocalDate.of(2015,3,14),"N");
		TradeTransmissionDto tradeTransmissionDto4=new TradeTransmissionDto("T3", 3,"CP-3","B2",LocalDate.of(2014,5,20),LocalDate.of(2021,4,21),"Y");
		
		tradeList.add(tradeTransmissionDto1);
		tradeList.add(tradeTransmissionDto2);
		tradeList.add(tradeTransmissionDto3);
		tradeList.add(tradeTransmissionDto4);
		
		tradeStoreService.tradeTransmissionDetails(tradeList);
	
	}

}
