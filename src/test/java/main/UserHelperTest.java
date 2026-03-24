package main;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import helpers.UserHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserHelperTest {

    @RegisterExtension
    private static final WireMockExtension wiremock = WireMockExtension
            .newInstance().options(wireMockConfig()
                    .dynamicPort().dynamicHttpsPort()).build();

    @Test
    public void testGetUserById() {
        wiremock.stubFor(get(urlPathMatching("/user/get/[0-9]+"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"name\": \"Test user\", \"score\": 78}")
                )
        );

        UserHelper userHelper = new UserHelper(new RestTemplate(), "http://wiremockt:%d" + wiremock.getPort());
        String userJson = userHelper.getUserById(123);
        assertEquals("{\"name\": \"Test user\", \"score\": 78}", userJson);
    }

    // Тест для получения всех пользователей
    @Test
    public void testGetAllUsers() {
        wiremock.stubFor(get(urlEqualTo("/user/get/all"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"name\": \"Test user\", \"course\": \"QA\", \"email\": \"test@test.test\", \"age\": 23}]")
                )
        );
        UserHelper userHelper = new UserHelper(new RestTemplate(), "http://wiremock:%d" + wiremock.getPort());
        String usersJson = userHelper.getAllUsers();
        assertEquals("[{\"name\": \"Test user\", \"course\": \"QA\", \"email\": \"test@test.test\", \"age\": 23}]", usersJson);
    }
}
