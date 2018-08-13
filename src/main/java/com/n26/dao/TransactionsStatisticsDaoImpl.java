package com.n26.dao;

import com.n26.model.entity.TransactionsStatisticsEntity;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionsStatisticsDaoImpl implements TransactionsStatisticsDao {

    private List<TransactionsStatisticsEntity> transactionsStatistics;

    public List<TransactionsStatisticsEntity> getTransactionsStatistics() {
        return transactionsStatistics;
    }

    @PostConstruct
    public void init() {
        transactionsStatistics = new ArrayList<>();
    }

    @Override
    public void createNewTransaction(TransactionsStatisticsEntity transactionsStatisticsEntity) {
        transactionsStatistics.add(transactionsStatisticsEntity);
    }

    @Override
    public void deleteAllTransactions() {
        transactionsStatistics.clear();
    }
}
