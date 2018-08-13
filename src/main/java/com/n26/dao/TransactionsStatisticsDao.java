package com.n26.dao;

import com.n26.model.entity.TransactionsStatisticsEntity;

import java.util.List;

public interface TransactionsStatisticsDao {

    boolean createNewTransaction(TransactionsStatisticsEntity transactionsStatisticsEntity);

    void deleteAllTransactions(String eventId);

    List<TransactionsStatisticsEntity> getTransactionsStatistics();
}
