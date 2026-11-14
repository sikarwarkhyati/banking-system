package com.banking.transactionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {

    private String transactionId;
    private String senderAccountNumber;
    private String receiverAccountNumber;
    private String amount;
    private String transactionType;
    private String transactionStatus;
    private String description;
    private String failureReason;
    private String referenceNumber;
    private String createdAt;
    private String completedAt;

}
