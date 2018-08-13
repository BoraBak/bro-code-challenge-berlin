package com.n26.dao;

import com.n26.model.entity.TransactionsStatisticsEntity;

import java.util.List;

public interface TransactionsStatisticsDao {

    void createNewTransaction(TransactionsStatisticsEntity transactionsStatisticsEntity);

    void deleteAllTransactions();

    List<TransactionsStatisticsEntity> getTransactionsStatistics();
}
