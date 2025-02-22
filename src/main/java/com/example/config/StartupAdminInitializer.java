package com.example.config;

import com.example.service.AccountService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupAdminInitializer {
    private final AccountService accountService;

    public StartupAdminInitializer(AccountService accountService) {
        this.accountService = accountService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void createAdminIfNotExists() {
        accountService.createAdminAccount();
    }
}
