package main;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import helpers.CourseHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CourseHelperTest {

    @RegisterExtension
    private static final WireMockExtension wiremock = WireMockExtension
            .newInstance().options(wireMockConfig()
                    .dynamicPort().dynamicHttpsPort()).build();

    @Test
    public void testGetAllCourses() {
        wiremock.stubFor(get(urlEqualTo("/course/get/all"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"name\": \"QA java\", \"price\": 15000}, {\"name\": \"Java\", \"price\": 12000}]")
                )
        );

        CourseHelper courseHelper = new CourseHelper(new RestTemplate(), "http://localhost:%d" + wiremock.getPort());
        String coursesJson = courseHelper.getAllCourses();
        assertEquals("[{\"name\": \"QA java\", \"price\": 15000}, {\"name\": \"Java\", \"price\": 12000}]", coursesJson);
    }
}
