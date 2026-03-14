package main.wiremock;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.opentest4j.AssertionFailedError;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.*;

public class WiremockGetRestQueryTest {

    @RegisterExtension
    private static final WireMockExtension wiremock = WireMockExtension
            .newInstance()
            .options(wireMockConfig()
                    .dynamicPort()
                    .dynamicHttpsPort())
            .build();

    @Test
    public void testQueryParams() {

        wiremock.stubFor(get(urlPathMatching("/user/\\+w"))
                .withQueryParam("active", equalTo("true"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"status\":\"active\"}")
                )
        );

        wiremock.stubFor(get(urlPathMatching("/user/\\+w"))
                .withQueryParam("active", equalTo("false"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"status\":\"not active\"}")
                )
        );

        RestTemplate restTemplate = new RestTemplate();
        String hostname = String.format("http://localhost:%d", wiremock.getPort());
        String activeRest = restTemplate.getForObject(hostname + "/user/dima?activetrue", String.class);
        String inactive = restTemplate.getForObject(hostname + "/user/notdima?activefalse", String.class);
        assertEquals("{\"status\":\"active\"}", activeRest);
        assertEquals("{\"status\":\"not active\"}", inactive);

    }

    @Test
    public void testBaseAuth() {

        wiremock.stubFor(get(urlEqualTo("/secure"))
                .withHeader("Authorization", matching("Basic dfjsfsfd"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"ok\": true}")
                )
        );

        RestTemplate restTemplate = new RestTemplate();
        String hostname = String.format("http://localhost:%d", wiremock.getPort());
        String auth = Base64.getEncoder().encodeToString("user:pass".getBytes());
        String resp = restTemplate.getForObject(hostname + "/secure", String.class, auth);
        assertEquals("{\"ok\": true}", resp);

        wiremock.verify(1, getRequestedFor(urlEqualTo("/secure")).withHeader("Authorization", matching("Basic dfjsfsfd")));

    }

    @Test
    public void testExceptionTimeout() {

        wiremock.stubFor(get(urlEqualTo("/regSlow"))
                .willReturn(aResponse()
                        .withFixedDelay(5000)
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"ok\": true}")
                ));
        RestTemplate restTemplate = new RestTemplate();
        String hostname = String.format("http://localhost:%d", wiremock.getPort());
        try {
            restTemplate.getForObject(hostname + "/regSlow", String.class);
            fail("Error Timeout");
        } catch (AssertionFailedError ex) {
            assertTrue(ex.getMessage().contains("Timeout"));
        }
    }

    @Test
    public void testReqScenario() {

        wiremock.stubFor(get(urlEqualTo("/item"))
                .inScenario("scena1")
                .whenScenarioStateIs("first")
                .willReturn(aResponse().withStatus(200).withBody("{\"ok\": true}"))
                .willSetStateTo("second")
        );
        wiremock.stubFor(get(urlEqualTo("/item"))
                .inScenario("scena1")
                .whenScenarioStateIs("first")
                .willReturn(aResponse().withStatus(404).withBody("{\"ok\": 404}"))
                .willSetStateTo("done")
        );
        RestTemplate restTemplate = new RestTemplate();
        String hostname = String.format("http://localhost:%d", wiremock.getPort());
        String resp1 = restTemplate.getForObject(hostname + "/item", String.class);
        String resp2 = restTemplate.getForObject(hostname + "/item", String.class);

        assertEquals("{\"ok\": true}", resp1);
        assertEquals("{\"ok\": 404}", resp2);
    }

    @Test
    public void testDynamicResp() {
        wiremock.stubFor(get(urlPathMatching("/user(.*)"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"user\":\"$1\"}")
                )
        );
        RestTemplate restTemplate = new RestTemplate();
        String hostname = String.format("http://localhost:%d", wiremock.getPort());
        String resp = restTemplate.getForObject(hostname + "/user/dima", String.class);
        if (resp != null && resp.contains("value")) {

            assertTrue(resp.contains("{\"user\":\"dima\"}"));
        }
    }
}
