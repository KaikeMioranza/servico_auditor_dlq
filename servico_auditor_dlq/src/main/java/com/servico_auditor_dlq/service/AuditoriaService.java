package com.servico_auditor_dlq.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.servico_auditor_dlq.dto.OrderItemDTO;
import com.servico_auditor_dlq.dto.PedidoDlqDTO;
import com.servico_auditor_dlq.entity.LogDlq;
import com.servico_auditor_dlq.entity.enums.Severity;
import com.servico_auditor_dlq.entity.enums.Status;
import com.servico_auditor_dlq.exception.AuditoriaException;
import com.servico_auditor_dlq.port.AuditoriaServicePort;
import com.servico_auditor_dlq.repository.LogDlqRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuditoriaService
        implements AuditoriaServicePort {

    private final LogDlqRepository repository;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Override
    @Transactional
    public void processar(PedidoDlqDTO dto) {

        try {

            int totalItens = dto.getOrderItems()
                    .stream()
                    .mapToInt(OrderItemDTO::getAmount)
                    .sum();

            Severity severity = calcularSeverity(totalItens);

            LogDlq entity = LogDlq.builder()
                    .queueName("T03N_KAIKE_MIORANZA")
                    .payload(converterPayload(dto))
                    .timestamp(Instant.now())
                    .status(Status.PENDING_ANALYSIS)
                    .severity(severity)
                    .build();

            repository.save(entity);

        } catch (Exception e) {

            throw new AuditoriaException(
                    "Erro ao salvar mensagem da DLQ",
                    e
            );
        }
    }

    private Severity calcularSeverity(int totalItens) {

        if (totalItens > 100) {
            return Severity.HIGH;
        }

        if (totalItens >= 50) {
            return Severity.MEDIUM;
        }

        return Severity.LOW;
    }

    private String converterPayload(PedidoDlqDTO dto) {

        try {

            return objectMapper.writeValueAsString(dto);

        } catch (JsonProcessingException e) {

            throw new AuditoriaException(
                    "Erro ao converter payload para JSON",
                    e
            );
        }
    }
}