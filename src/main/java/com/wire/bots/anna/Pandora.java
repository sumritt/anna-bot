package com.wire.bots.anna;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.wire.bots.sdk.Logger;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.JerseyClientBuilder;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;

public class Pandora {
    public static String PANDORA_URL = "https://aiaas.pandorabots.com";

    private final Client client;
    private final String appId;
    private final String userKey;
    private final String pandoraBot;

    public Pandora(AnnaConfig config) {
        this.appId = config.getPandora().getAppId();
        this.userKey = config.getPandora().getUserKey();
        this.pandoraBot = config.getPandora().getBot();

        ClientConfig cfg = new ClientConfig(JacksonJsonProvider.class);
        cfg.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
        client = JerseyClientBuilder.createClient(cfg);
    }

    public ArrayList<String> talk(String clientName, String text) throws IOException {
        Response response = client.target(PANDORA_URL)
                .path("talk")
                .path(appId)
                .path(pandoraBot)
                .queryParam("user_key", userKey)
                .queryParam("client_name", clientName)
                .queryParam("input", text)
                .queryParam("recent", true)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(null, MediaType.APPLICATION_JSON));

        if (response.getStatus() > 300) {
            String msg = String.format("Pandora.talk: msg: %s, status: %d",
                    response.readEntity(String.class),
                    response.getStatus());
            Logger.warning(msg);
            throw new IOException(msg);
        }

        return response.readEntity(PandoraResponse.class).responses;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PandoraResponse {
        public ArrayList<String> responses;
    }
}