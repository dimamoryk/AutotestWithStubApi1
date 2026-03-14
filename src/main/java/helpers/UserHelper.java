package helpers;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class UserHelper {

    private RestTemplate restTemplate;
    private String apiUrl;

    public UserHelper(RestTemplate restTemplate, String apiUrl) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
    }

    public String getUserById(long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(apiUrl + "/user/get/" + userId, HttpMethod.GET, entity, String.class)
                .getBody();
    }
    public String getAllUsers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(apiUrl + "/user/get/all", HttpMethod.GET, entity, String.class).getBody();
    }
}
