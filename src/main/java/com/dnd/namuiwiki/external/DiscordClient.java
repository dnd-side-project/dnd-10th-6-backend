package com.dnd.namuiwiki.external;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class DiscordClient {
    @Value("${discord.webhook-url}")
    private String discordWebHookUrl;

    public void sendMessage(String message) {
        String body = String.format("{\"content\":\"%s\"}", message);

        RestClient.create().post()
                .uri(discordWebHookUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve();
    }

}
