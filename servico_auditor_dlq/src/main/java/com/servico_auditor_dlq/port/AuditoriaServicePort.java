package com.servico_auditor_dlq.port;

import com.servico_auditor_dlq.dto.PedidoDlqDTO;

public interface AuditoriaServicePort {

    void processar(PedidoDlqDTO dto);
}