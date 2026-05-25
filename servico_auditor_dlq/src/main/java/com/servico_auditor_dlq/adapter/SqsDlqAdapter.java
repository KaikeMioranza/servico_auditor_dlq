package com.servico_auditor_dlq.adapter;

import com.servico_auditor_dlq.dto.PedidoDlqDTO;
import com.servico_auditor_dlq.port.AuditoriaServicePort;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SqsDlqAdapter {

    private final AuditoriaServicePort servicePort;

    @SqsListener("${queue.order-events}")
    public void listen(PedidoDlqDTO dto) {

        System.out.println(
                "Mensagem recebida: "
                        + dto.getCustomerId()
        );

        servicePort.processar(dto);

        System.out.println(
                "Mensagem salva no banco."
        );
    }
}