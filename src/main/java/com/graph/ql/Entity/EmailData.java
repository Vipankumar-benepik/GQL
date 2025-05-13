package com.graph.ql.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "tbl_V2email_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "attachment", length = 255)
    private String attachment;

    @Column(name = "bcc", length = 255)
    private String bcc;

    @Column(name = "cc", length = 255)
    private String cc;

    @Column(name = "createDate", columnDefinition = "DATETIME(6)")
    private Date createDate;

    @Column(name = "senderFrom", length = 255)
    private String senderFrom;

    @Column(name = "subject", length = 255)
    private String subject;

    @Column(name = "recipient", length = 255)
    private String recipient;

    @Column(name = "ErrorMessage", length = 255)
    private String errorMessage;

    @Column(name = "MessageId", length = 255)
    private String messageId;

    @Column(name = "status", length = 255)
    private String status;

    @Column(name = "ipAddress", length = 255)
    private String ipAddress;

    @Column(name="projectId", length = 255)
    private String projectId;
}


