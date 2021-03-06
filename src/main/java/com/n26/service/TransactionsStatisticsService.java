package com.n26.service;

import com.n26.model.Statistics;
import com.n26.model.dto.TransactionsStatisticsDto;
import com.n26.model.entity.TransactionsStatisticsEntity;

public interface TransactionsStatisticsService {

    TransactionsStatisticsEntity convertDtoToEntity(TransactionsStatisticsDto transStatDto, String eventId);

    boolean createNewTransaction(TransactionsStatisticsEntity transactionsStatisticsEntity, String eventId);

    Statistics getTransactionsAndStatistics();

    void deleteAllTransactions(String eventId);

    void transactionsStatisticsScheduler();
}
