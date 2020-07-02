package hu.user.rendszerhaz.service;

import hu.user.rendszerhaz.domain.PredictionList;
import hu.user.rendszerhaz.domain.SinglePrediction;
import io.spring.guides.gs_producing_web_service.Request;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.Collections.singletonList;

@Service
public class WatsonClient {

    RestTemplate restTemplate;

    public WatsonClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }


    private final static String ML_INSTANCE_ID = "0668c595-6215-48a2-a5f7-f12214b4fe66";
    private final static String IAM_TOKEN = "Bearer" + "eyJraWQiOiIyMDIwMDYyNDE4MzAiLCJhbGciOiJSUzI1NiJ9.eyJpYW1faWQiOiJpYW0tU2VydmljZUlkLTRiZTE5OTUyLTNhM2ItNDlmMy1iNDUzLTYwOGRmNjQ3YzYxYyIsImlkIjoiaWFtLVNlcnZpY2VJZC00YmUxOTk1Mi0zYTNiLTQ5ZjMtYjQ1My02MDhkZjY0N2M2MWMiLCJyZWFsbWlkIjoiaWFtIiwiaWRlbnRpZmllciI6IlNlcnZpY2VJZC00YmUxOTk1Mi0zYTNiLTQ5ZjMtYjQ1My02MDhkZjY0N2M2MWMiLCJuYW1lIjoiVGFtw6FzIEjDqXJpbmciLCJzdWIiOiJTZXJ2aWNlSWQtNGJlMTk5NTItM2EzYi00OWYzLWI0NTMtNjA4ZGY2NDdjNjFjIiwic3ViX3R5cGUiOiJTZXJ2aWNlSWQiLCJhY2NvdW50Ijp7InZhbGlkIjp0cnVlLCJic3MiOiIxNGUxOTE3Mzc2YTgzZDg1MzE0MmIzZjY2OWNlYzFmYyJ9LCJpYXQiOjE1OTM2OTI5OTYsImV4cCI6MTU5MzY5NjU5NiwiaXNzIjoiaHR0cHM6Ly9pYW0ubmcuYmx1ZW1peC5uZXQvb2lkYy90b2tlbiIsImdyYW50X3R5cGUiOiJ1cm46aWJtOnBhcmFtczpvYXV0aDpncmFudC10eXBlOmFwaWtleSIsInNjb3BlIjoiaWJtIG9wZW5pZCIsImNsaWVudF9pZCI6ImJ4IiwiYWNyIjoxLCJhbXIiOlsicHdkIl19.EaocijukGWtyNhIcES64GwGEFq_4ycd1ux3zP1x8Vg0n26JjJJuJwEY_6mHzirxMQkyzW9tbB0KZ5-806vNVUgDfiJGax_ib1SWr6n6Qwic2EHT2OGpmsDrcunrQxrye0gLb1B1PCmVgZaVMJNbJzBuIc0-zK0Zj5YEA91MNCOyZFUBb_rDX-a_i6h4t9d9C1beuk7CjDYjuhe07tN7MEU4pa9ZSmWoOF8juvGYOv6zbqKmjb5ZVbN0hsu3A3k7IA1P_P3e2kyN8-bJ7x79GDtHxhIIU-eAWZFWGMvzWR6wxybTmdW2jSEjmpdOUvWiE883KM6SiHA3-HGxDJjoCPg";
    private final static String URL = "https://eu-de.ml.cloud.ibm.com/v4/deployments/65c47765-c4c3-4e6a-b9c0-9fad9634b7ba/predictions";

    private static String inputDataJson = "{\"input_data\": [{\n" +
            "                                   \"values\": [[0, -1.359807134, 0.072781173, 2.536346738, 1.378155224, -0.33832077,\n" +
            "                                               0.462387778, 0.239598554, 0.098697901, 0.36378697, 0.090794172,\n" +
            "                                               -0.551599533, -0.617800856, -0.991389847, -0.311169354, 1.468176972,\n" +
            "                                               -0.470400525, 0.207971242, 0.02579058, 0.40399296, 0.251412098,\n" +
            "                                               -0.018306778, 0.277837576, -0.11047391, 0.066928075, 0.128539358,\n" +
            "                                               -0.189114844, 0.133558377, -0.021053053, 149.62]," +
            "[9064, -3.499107537, 0.258555161, -4.489558073, 4.853894351, -6.974521545, 3.628382091, 5.431270921, -1.946733711,\n" +
            "     -0.775680093, -1.987773188, 4.690395666, -6.998042432, 1.454011986, -3.738023334, 0.317742063, -2.013542681,\n" +
            "     -5.136135103, -1.183822117, 1.663394014, -3.042625757, -1.052368256, 0.204816874, -2.11900744, 0.170278608,\n" +
            "     -0.393844118, 0.296367194, 1.985913218,\n" +
            "     -0.900451638, 1809.68]]}]}";

    @Async
    public CompletableFuture<String> predict(List<Request> requests) {
        System.out.println("Simple Rest");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(singletonList(MediaType.APPLICATION_XML));
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("Authorization", IAM_TOKEN);
        httpHeaders.add("ML-Instance-ID", ML_INSTANCE_ID);
        httpHeaders.setAccept(singletonList(MediaType.APPLICATION_JSON_UTF8));
        HttpEntity<String> httpEntity = new HttpEntity<>(inputDataJson, httpHeaders);
//        Values predictions = restTemplate.postForObject(URL, httpEntity, Values.class);
        String response = restTemplate.postForObject(URL, httpEntity, String.class);
//        ResponseEntity<PredictionList> responseEntity = restTemplate.exchange(URL,HttpMethod.POST, httpEntity, PredictionList.class);
//        PredictionList predictionList = responseEntity.getBody();
//        List<SinglePrediction> predictions = predictionList.getValues();
        ResponseEntity<String> responseEntity = restTemplate.exchange(URL, HttpMethod.POST, httpEntity, String.class);
//        PredictionList predictionList = responseEntity.getBody();
        return CompletableFuture.completedFuture(responseEntity.getBody());
    }
}