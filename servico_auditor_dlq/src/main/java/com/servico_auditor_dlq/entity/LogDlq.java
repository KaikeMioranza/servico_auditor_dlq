package com.servico_auditor_dlq.entity;

import com.servico_auditor_dlq.entity.enums.Severity;
import com.servico_auditor_dlq.entity.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "log_dlq")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogDlq {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID errorId;

    private String queueName;

    @Column(columnDefinition = "TEXT")
    private String payload;

    private Instant timestamp;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Severity severity;
}