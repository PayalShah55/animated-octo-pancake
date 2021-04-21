package com.AssignmentTrade.TradeStore.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.AssignmentTrade.TradeStore.model.Trade;

public interface TradeStoreRepository extends JpaRepository<Trade,Long>{
	
	Trade findByTradeIdAndCounterPartyIdAndBookIdAndMaturityDateAndVersionGreaterThan(String tradeId,String counterPartyId,String bookId,LocalDate maturityDate,int value);
	Trade findByTradeIdAndCounterPartyIdAndBookIdAndMaturityDateAndVersionEquals(String tradeId,String counterPartyId,String bookId,LocalDate maturityDate,int value);
}
