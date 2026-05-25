package com.servico_auditor_dlq.repository;

import com.servico_auditor_dlq.entity.LogDlq;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LogDlqRepository extends JpaRepository<LogDlq, UUID>{

}
