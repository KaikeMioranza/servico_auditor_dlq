package com.servico_auditor_dlq.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDTO {

    private Long sku;

    private Integer amount;
}