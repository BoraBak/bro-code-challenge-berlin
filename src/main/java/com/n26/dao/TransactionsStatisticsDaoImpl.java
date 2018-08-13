package com.n26.dao;

import com.n26.model.entity.TransactionsStatisticsEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Slf4j
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
    public boolean createNewTransaction(TransactionsStatisticsEntity transactionsStatisticsEntity) {
        return transactionsStatistics.add(transactionsStatisticsEntity);
    }

    @Override
    public void deleteAllTransactions(String eventId) {
        log.debug("Removing {} transactions records. EventId: {}", transactionsStatistics.size(), eventId);
        transactionsStatistics.clear();
    }
}
