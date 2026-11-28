package com.example.tiendaPandora.schedule;

import com.example.tiendaPandora.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduleToken {

    private final TokenService tokenService;

    @Scheduled(fixedRate = 3600000)
    public void limpiarTokens() {
        tokenService.limpiarTokens();
    }

}
