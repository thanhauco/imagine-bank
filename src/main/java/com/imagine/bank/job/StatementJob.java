package com.imagine.bank.job;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Component
public class StatementJob {
    @Scheduled(cron = ""0 0 0 1 * ?"")
    public void generateStatements() {
        // Logic
    }
}
