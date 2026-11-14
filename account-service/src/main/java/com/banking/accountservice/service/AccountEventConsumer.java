package com.banking.accountservice.service;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountEventConsumer {

    private final AccountService accountService;
    private final ObjectMapper objectMapper;

    // Messages arrive as JSON strings (StringDeserializer), so we parse them here.
    @KafkaListener(topics = "transaction.completed")
    public void consumeTransactionCompleted(String message) {
        try {
            Map<?, ?> payload = objectMapper.readValue(message, Map.class);
            String receiverAccount = (String) payload.get("receiverAccount");
            // amount may be sent as a number or a string
            BigDecimal amount = new BigDecimal(String.valueOf(payload.get("amount")));
            log.info("Transaction completed event received for account: {} with amount: {}", receiverAccount,
                    amount);
            accountService.creditBalance(receiverAccount, amount);
        } catch (Exception e) {
            log.error("Error processing transaction.completed event: {}", message, e);
        }
    }

    @KafkaListener(topics = "fraud.detected")
    public void consumeFraudDetected(String message) {
        try {
            Map<?, ?> payload = objectMapper.readValue(message, Map.class);
            String accountNumber = (String) payload.get("accountNumber");
            log.info("Fraud detected event received for account: {}", accountNumber);
            accountService.blockAccount(accountNumber);
        } catch (Exception e) {
            log.error("Error processing fraud.detected event: {}", message, e);
        }
    }

}
