package com.n26.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Statistics {

    private BigDecimal sum;

    private BigDecimal avg;

    private BigDecimal max;

    private BigDecimal min;

    private int count;
}
