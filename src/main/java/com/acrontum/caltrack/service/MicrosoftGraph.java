package com.acrontum.caltrack.service;

import com.acrontum.caltrack.util.PlainTokenProvider;
import com.microsoft.graph.models.Event;
import com.microsoft.graph.requests.EventCollectionPage;
import com.microsoft.graph.requests.GraphServiceClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@Log4j2
public class MicrosoftGraph {

    private static final String AUTHORIZATION_URL = "https://login.microsoftonline.com/common/oauth2/token";
    private static final String GRANT_TYPE = "client_credentials";

    public static String getAccessToken(String clientId, String clientSecret) {
        WebClient webClient = WebClient.builder().baseUrl(AUTHORIZATION_URL).build();
        String body = "grant_type=" + GRANT_TYPE + "&client_id=" + clientId + "&client_secret=" + clientSecret;
        return webClient.post()
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(body)
                .exchange()
                .flatMap(response -> response.bodyToMono(String.class))
                .block();
    }

    public Flux<String> getCalendarEvents(String userId, String accessToken) {
        PlainTokenProvider plainTokenProvider = new PlainTokenProvider();
        GraphServiceClient g = GraphServiceClient.builder().authenticationProvider(plainTokenProvider).buildClient();
        StringBuilder b = new StringBuilder();

        EventCollectionPage eventCollectionPage = g.me().calendar().events().buildRequest().get();
        for (Event event : eventCollectionPage.getCurrentPage()) {
            log.info("Event Subject {}", event.subject);
            log.info("Event Start {}", event.start.dateTime);
            log.info("Event End {}", event.end.dateTime);

            LocalDateTime start = LocalDateTime.parse(event.start.dateTime);
            LocalDateTime end = LocalDateTime.parse(event.end.dateTime);
            Duration duration = Duration.between(start, end);
            long hours = duration.toHours();
            long minutes = duration.toMinutes() % 60;
            log.info("Event Total Time {}h and {}m", hours, minutes);

        }

        //graph.me().teams().calls().buildRequest().get();

        g.me().calendars().buildRequest().get().getCurrentPage().forEach(calendar -> {
            log.info("Event: {}", calendar);
        });
        return Flux.just(b.toString());
//        WebClient webClient = WebClient.builder().build();
//        return webClient
//                .get()
//                .uri("https://graph.microsoft.com/v1.0/users/{userId}/calendar/events", userId)
//                .header("Authorization", "Bearer " + accessToken)
//                .retrieve()
//                .bodyToFlux(String.class);
    }


}
