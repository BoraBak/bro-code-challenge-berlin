package com.n26.service;

import com.n26.dao.TransactionsStatisticsDao;
import com.n26.exception.ApiBadRequestException;
import com.n26.exception.ApiNoContentRequestException;
import com.n26.exception.ApiUnprocessableEntityRequestException;
import com.n26.model.Statistics;
import com.n26.model.dto.TransactionsStatisticsDto;
import com.n26.model.entity.TransactionsStatisticsEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TransactionsStatisticsServiceImpl implements TransactionsStatisticsService {

    @Value("${transactions.statistics.scheduler.interval:60000}")
    private int SCHEDULER_INTERVAL;

    @Autowired
    private TransactionsStatisticsDao transactionsStatisticsDao;

    private Statistics transactionsStatistics;

    @PostConstruct
    public void init() {
        transactionsStatistics = new Statistics();
    }

    @Override
    public TransactionsStatisticsEntity convertDtoToEntity(TransactionsStatisticsDto transStatDto, String eventId) {
        BigDecimal amount;
        Instant transactionTime;
        try {
            amount = new BigDecimal(transStatDto.getAmount());
            transactionTime = Instant.parse(transStatDto.getTimestamp());
        } catch (ApiNoContentRequestException e) {
            log.error(e.getMessage() + ". EventId: {}", eventId);
            throw new ApiUnprocessableEntityRequestException(e.getMessage(), eventId);
        }
        return new TransactionsStatisticsEntity(amount, transactionTime);
    }

    @Override
    public void createNewTransaction(TransactionsStatisticsEntity transactionsStatisticsEntity, String eventId) {

        validateNotNullInputArgs(transactionsStatisticsEntity, eventId);

        validateCorrectnessInputArgs(transactionsStatisticsEntity, eventId);

        transactionsStatisticsDao.createNewTransaction(transactionsStatisticsEntity);

        //TODO: advice with Igor regards
        transactionsStatisticsScheduler();
    }

    @Override
    public Statistics getTransactionsAndStatistics() {
        return transactionsStatistics;
    }

    @Override
    public void deleteAllTransactions() {
        transactionsStatisticsDao.deleteAllTransactions();

        nullifyTransactionsStatistics();
    }

    @Override
    @Scheduled(fixedDelayString = "${transactions.statistics.scheduler.interval:60000}")
    public void transactionsStatisticsScheduler() {
        List<BigDecimal> statisticsList = transactionsStatisticsDao.getTransactionsStatistics().stream()
                .filter(transactionsStatisticsEntity -> (transactionsStatisticsEntity.getTransactionTime().plusMillis(SCHEDULER_INTERVAL)).compareTo(Instant.now()) > 0)
                .map(TransactionsStatisticsEntity::getAmount)
                .collect(Collectors.toList());

        Optional<BigDecimal> sum = statisticsList.stream().reduce((bigDecimal1, bigDecimal2)
                -> bigDecimal1.setScale(2, BigDecimal.ROUND_HALF_UP).add(bigDecimal2.setScale(2, BigDecimal.ROUND_HALF_UP)));
        Optional<BigDecimal> max = statisticsList.stream().max(Comparator.naturalOrder());
        Optional<BigDecimal> min = statisticsList.stream().min(Comparator.naturalOrder());

        transactionsStatistics.setSum(sum.map(bigDecimal -> bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP)).orElse(null));
        transactionsStatistics.setAvg(sum.map(bigDecimal -> bigDecimal.divide(new BigDecimal(statisticsList.size()), 2, BigDecimal.ROUND_HALF_UP)).orElse(null));
        transactionsStatistics.setMax(max.map(bigDecimal -> bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP)).orElse(null));
        transactionsStatistics.setMin(min.map(bigDecimal -> bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP)).orElse(null));
        transactionsStatistics.setCount(statisticsList.size());
    }

    private void nullifyTransactionsStatistics() {
        transactionsStatistics.setSum(null);
        transactionsStatistics.setAvg(null);
        transactionsStatistics.setMax(null);
        transactionsStatistics.setMin(null);
        transactionsStatistics.setCount(0);
    }

    private void validateNotNullInputArgs(TransactionsStatisticsEntity transactionsStatisticsEntity, String eventId) throws ApiBadRequestException {
        if (transactionsStatisticsEntity.getAmount() == null) {
            log.error("Amount is not valid. EventId: {}", eventId);
            throw new ApiBadRequestException("Amount is not valid.", eventId);
        }
        if (transactionsStatisticsEntity.getTransactionTime() == null) {
            log.error("TimeStamp is not valid. EventId: {}", eventId);
            throw new ApiBadRequestException("TimeStamp is not valid", eventId);
        }
    }

    private void validateCorrectnessInputArgs(TransactionsStatisticsEntity transactionsStatisticsEntity, String eventId) {
        if (transactionsStatisticsEntity.getTransactionTime().plusSeconds(60).isBefore(Instant.now())) {
            log.error("TransactionTime is older than 60 seconds. EventId: {}", eventId);
            throw new ApiNoContentRequestException("TransactionTime is older than 60 seconds", eventId);
        }
        if (transactionsStatisticsEntity.getTransactionTime().isAfter(Instant.now())) {
            log.error("TransactionTime is in the future. EventId: {}", eventId);
            throw new ApiUnprocessableEntityRequestException("TransactionTime is in the future", eventId);
        }
    }

}
