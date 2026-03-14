package main.wiremock;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WiremockSpecRestTest {

    @RegisterExtension
    private static final WireMockExtension wiremock = WireMockExtension
            .newInstance()
            .options(wireMockConfig()
                    .dynamicPort()
                    .dynamicHttpsPort())
            .build();

    @Test
    public void testGetRest() {

        wiremock.stubFor(get(urlEqualTo("/resource"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("{\"get\":true}")
                )
        );

        wiremock.stubFor(post(urlEqualTo("/resource"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withBody("{\"post\":true}")
                )
        );
        RestTemplate restTemplate = new RestTemplate();
        String hostname = String.format("http://localhost:%d", wiremock.getPort());

        String getRest = restTemplate.getForObject(hostname + "/resource", String.class);
        assertEquals("{\"get\":true}", getRest);

        String postRest = restTemplate.postForObject(hostname + "/resource", "", String.class);
        assertEquals("{\"post\":true}", postRest);
    }
}
