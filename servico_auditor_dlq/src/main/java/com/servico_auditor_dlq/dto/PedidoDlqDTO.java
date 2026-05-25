package com.servico_auditor_dlq.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class PedidoDlqDTO {

    private String zipCode;

    private Long customerId;

    private List<OrderItemDTO> orderItems;

    private String origin;

    private Instant occurredAt;
}