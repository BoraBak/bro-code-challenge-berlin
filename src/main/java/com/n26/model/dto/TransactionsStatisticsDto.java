package com.n26.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionsStatisticsDto {

    private String amount;

    private String timestamp;
}
