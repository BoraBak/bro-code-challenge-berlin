package com.n26.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
public class TransactionsStatisticsEntity {

    private BigDecimal amount;

    private Instant transactionTime;
}
