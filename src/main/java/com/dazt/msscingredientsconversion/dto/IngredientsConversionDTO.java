package com.dazt.msscingredientsconversion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IngredientsConversionDTO {

    private String from;

    private String to;

    private BigDecimal quantity;

    private BigDecimal conversionFactor;

    private BigDecimal totalCalculatedAmount;

    private String hostPort;

    private OffsetDateTime lastUpdatedDate;

    private OffsetDateTime creationDate;
}
