package com.banking.transactionservice.entity;

/**
 * TransactionStatus lifecycle
 * pending -> processing -> completed (clean flow)
 * pending -> processing -> pending verifiction(suspicious flow) -> user
 * verified(otp/call) - >completed/flag(SAGA refund flow)
 * 
 */
public enum TransactionStatus {

    PENDING,
    COMPLETED,
    FAILED,
    FLAGGED,
    PROCESSING,
    PENDING_VERIFICATION,
}
