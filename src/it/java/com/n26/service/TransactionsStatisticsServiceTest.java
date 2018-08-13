package com.n26.service;

import com.n26.model.dto.TransactionsStatisticsDto;
import com.n26.model.entity.TransactionsStatisticsEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.Instant;

@RunWith(SpringRunner.class)
@SpringBootTest
//@RunWith(MockitoJUnitRunner.class)
public class TransactionsStatisticsServiceTest {

    private final String eventId = "1a2b3c4d";

    @Autowired
    private TransactionsStatisticsService transactionsStatisticsService;

    @Before
    public void init() {

    }

    @Test
    public void convertDtoToEntity() {
        TransactionsStatisticsDto transactionsStatisticsDto = TransactionsStatisticsDto.builder()
                .amount("12.33435")
                .timestamp("2018-07-17T09:59:51.312Z")
                .build();

        TransactionsStatisticsEntity transactionsStatisticsEntity = transactionsStatisticsService.convertDtoToEntity(transactionsStatisticsDto, eventId);
        Assert.assertNotNull(transactionsStatisticsEntity);
    }

    @Test
    public void createNewTransaction() {
        TransactionsStatisticsEntity transactionsStatisticsEntity = TransactionsStatisticsEntity.builder()
                .amount(new BigDecimal("12.35"))
                .transactionTime(Instant.now().minusSeconds(10))
                .build();

        Assert.assertTrue(transactionsStatisticsService.createNewTransaction(transactionsStatisticsEntity, eventId));
    }

    @Test
    public void getTransactionsAndStatistics() {
        Assert.assertNotNull(transactionsStatisticsService.getTransactionsAndStatistics());
    }

    @Test
    public void deleteAllTransactions() {
        transactionsStatisticsService.deleteAllTransactions(eventId);
    }
}
