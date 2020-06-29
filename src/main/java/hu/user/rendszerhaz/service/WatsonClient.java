package hu.user.rendszerhaz.service;

import io.spring.guides.gs_producing_web_service.Request;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

import static java.util.Collections.singletonList;

@Service
public class WatsonClient {

    RestTemplate restTemplate;

    public WatsonClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }


    private final static String ML_INSTANCE_ID = "0668c595-6215-48a2-a5f7-f12214b4fe66";
    private final static String IAM_TOKEN = "Bearer" + "eyJraWQiOiIyMDIwMDYyNDE4MzAiLCJhbGciOiJSUzI1NiJ9.eyJpYW1faWQiOiJpYW0tU2VydmljZUlkLTRiZTE5OTUyLTNhM2ItNDlmMy1iNDUzLTYwOGRmNjQ3YzYxYyIsImlkIjoiaWFtLVNlcnZpY2VJZC00YmUxOTk1Mi0zYTNiLTQ5ZjMtYjQ1My02MDhkZjY0N2M2MWMiLCJyZWFsbWlkIjoiaWFtIiwiaWRlbnRpZmllciI6IlNlcnZpY2VJZC00YmUxOTk1Mi0zYTNiLTQ5ZjMtYjQ1My02MDhkZjY0N2M2MWMiLCJuYW1lIjoiVGFtw6FzIEjDqXJpbmciLCJzdWIiOiJTZXJ2aWNlSWQtNGJlMTk5NTItM2EzYi00OWYzLWI0NTMtNjA4ZGY2NDdjNjFjIiwic3ViX3R5cGUiOiJTZXJ2aWNlSWQiLCJhY2NvdW50Ijp7InZhbGlkIjp0cnVlLCJic3MiOiIxNGUxOTE3Mzc2YTgzZDg1MzE0MmIzZjY2OWNlYzFmYyJ9LCJpYXQiOjE1OTM0MzY3MjUsImV4cCI6MTU5MzQ0MDMyNSwiaXNzIjoiaHR0cHM6Ly9pYW0ubmcuYmx1ZW1peC5uZXQvb2lkYy90b2tlbiIsImdyYW50X3R5cGUiOiJ1cm46aWJtOnBhcmFtczpvYXV0aDpncmFudC10eXBlOmFwaWtleSIsInNjb3BlIjoiaWJtIG9wZW5pZCIsImNsaWVudF9pZCI6ImJ4IiwiYWNyIjoxLCJhbXIiOlsicHdkIl19.mzCGKaaAHHfPk8wNY9I06lKGTsyJTWEwKLcWgIFYf6MNqzOtnFxMcCRJcotQfmAVT2AfNfd9bWZ2NeRbbFS0lSmkrjcu1nOSdYj-P_2R5k18AZQNdnHfKjaksy8PI656U1OiUBMpYH20HhYduv_KLIbX6rnDE1xpBYPq5KNzB2316DJ8hPjgQQ6x_m6txvpraW4xyuZ8YDxUdQgHUTZKC3VW5n4EwwNRlz2ihKwrRSgdRUbaUBsd5kcqq2Og8ee4y0gTMfhs6MgD4gLoj4Hs_suuQ5WTo9XiEyjIEoSq-3XVqf08A7xLdWnu-Ef7_OUUh4UNvqdSFeu5y1AdHIbg0A";
    private final static String URL = "https://eu-de.ml.cloud.ibm.com/v4/deployments/65c47765-c4c3-4e6a-b9c0-9fad9634b7ba/predictions";

    private static String inputDataJson = "{\"input_data\": [{\n" +
            "                                   \"values\": [[0, -1.359807134, 0.072781173, 2.536346738, 1.378155224, -0.33832077,\n" +
            "                                               0.462387778, 0.239598554, 0.098697901, 0.36378697, 0.090794172,\n" +
            "                                               -0.551599533, -0.617800856, -0.991389847, -0.311169354, 1.468176972,\n" +
            "                                               -0.470400525, 0.207971242, 0.02579058, 0.40399296, 0.251412098,\n" +
            "                                               -0.018306778, 0.277837576, -0.11047391, 0.066928075, 0.128539358,\n" +
            "                                               -0.189114844, 0.133558377, -0.021053053, 149.62]]}]}";

    @Async
    public CompletableFuture<String> predict(Request request) {
        System.out.println("Simple Rest");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(singletonList(MediaType.APPLICATION_XML));
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("Authorization", IAM_TOKEN);
        httpHeaders.add("ML-Instance-ID", ML_INSTANCE_ID);
        httpHeaders.setAccept(singletonList(MediaType.APPLICATION_JSON_UTF8));
        HttpEntity<String> httpEntity = new HttpEntity<>(inputDataJson, httpHeaders);
        String response = restTemplate.postForObject(URL, httpEntity, String.class);
        return CompletableFuture.completedFuture(response
        );
    }
}