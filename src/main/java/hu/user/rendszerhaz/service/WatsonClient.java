package hu.user.rendszerhaz.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import hu.user.rendszerhaz.domain.SinglePrediction;
import io.spring.guides.gs_producing_web_service.Request;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class WatsonClient {

    RestTemplate restTemplate;

    public WatsonClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }


    private final static String ML_INSTANCE_ID = "0668c595-6215-48a2-a5f7-f12214b4fe66";
    private final static String IAM_TOKEN = "Bearer" + "eyJraWQiOiIyMDIwMDYyNDE4MzAiLCJhbGciOiJSUzI1NiJ9.eyJpYW1faWQiOiJpYW0tU2VydmljZUlkLTRiZTE5OTUyLTNhM2ItNDlmMy1iNDUzLTYwOGRmNjQ3YzYxYyIsImlkIjoiaWFtLVNlcnZpY2VJZC00YmUxOTk1Mi0zYTNiLTQ5ZjMtYjQ1My02MDhkZjY0N2M2MWMiLCJyZWFsbWlkIjoiaWFtIiwiaWRlbnRpZmllciI6IlNlcnZpY2VJZC00YmUxOTk1Mi0zYTNiLTQ5ZjMtYjQ1My02MDhkZjY0N2M2MWMiLCJuYW1lIjoiVGFtw6FzIEjDqXJpbmciLCJzdWIiOiJTZXJ2aWNlSWQtNGJlMTk5NTItM2EzYi00OWYzLWI0NTMtNjA4ZGY2NDdjNjFjIiwic3ViX3R5cGUiOiJTZXJ2aWNlSWQiLCJhY2NvdW50Ijp7InZhbGlkIjp0cnVlLCJic3MiOiIxNGUxOTE3Mzc2YTgzZDg1MzE0MmIzZjY2OWNlYzFmYyJ9LCJpYXQiOjE1OTQzMDA0NzYsImV4cCI6MTU5NDMwNDA3NiwiaXNzIjoiaHR0cHM6Ly9pYW0ubmcuYmx1ZW1peC5uZXQvb2lkYy90b2tlbiIsImdyYW50X3R5cGUiOiJ1cm46aWJtOnBhcmFtczpvYXV0aDpncmFudC10eXBlOmFwaWtleSIsInNjb3BlIjoiaWJtIG9wZW5pZCIsImNsaWVudF9pZCI6ImJ4IiwiYWNyIjoxLCJhbXIiOlsicHdkIl19.I_Nfh9YV5_XNvll6hqRLd6vHhvUEG6qwH5gmfLFSYh6E-6ZjmID9OFpL1L03H6shtUurNeSqkQQwx9kNGJ_DBBeu5QbbdKv-kOvMw3jxAi9i7LjH6M4M6mjxmr8eoGWDpQdJw0ag-hS748WbFxQhV-OGEtgnUQE1Cb3T3mhFtuR5muvphEpetkPu2lcyiFmXZ-sKP1WTQ9KyBBlMfxDTiYV2iOX9LKeLOdz9Kwgco3KxJ8u25Y-cMyI8kYBc1VB0OG2VdaYeFIMw1fu1IoIHHK_VOTxF1rak7rxaVhJfQTil5VWyV7A-EmZuV3LT10-8XR_sR3A5XyR0aR0FNpR7eA";
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
        httpHeaders.add("Authorization", IAM_TOKEN);
        httpHeaders.add("ML-Instance-ID", ML_INSTANCE_ID);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        ObjectMapper objectMapper = new ObjectMapper();
        HttpEntity<String> httpEntity = new HttpEntity<>(convertJavaToJson(requests), httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, httpEntity, String.class);
        return CompletableFuture.completedFuture(response.getBody());
    }

    public List<SinglePrediction> predictAndConvertFromJsonStringToPredictionList(List<Request> requests) {
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        CompletableFuture<String> asyncResponse = predict(requests);
        JsonNode fullNode = null;
        try {
            fullNode = objectMapper.readTree(asyncResponse.get());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        JsonNode rootNode = fullNode.get("predictions").get(0).get("values");
        ArrayNode arrayNode = (ArrayNode) rootNode;
        List<SinglePrediction> predictions = new ArrayList<>();
        for (int i = 0; i < arrayNode.size(); i++) {
            System.out.println("Array node: " + arrayNode.get(i));
            ArrayNode singleResultArrayNode = (ArrayNode) arrayNode.get(i);
            SinglePrediction singlePrediction = new SinglePrediction();
            singlePrediction.setPredictionValue(singleResultArrayNode.get(0).asInt());
            for (int j = 0; j < singleResultArrayNode.size(); j++) {
                singlePrediction.getParameters().add(singleResultArrayNode.get(1).get(0).asDouble());
                singlePrediction.getParameters().add(singleResultArrayNode.get(1).get(1).asDouble());
            }
            predictions.add(singlePrediction);
        }
        return predictions;
    }

    private String convertJavaToJson(List<Request> requests) {
        Map<String, Object> inputDataAsBuildJsonObject = new HashMap<>();
        List<Object> inputDataAsJsonArray = new ArrayList<>();
        Map<String, List<List<Double>>> requestMap = new HashMap<>();
        List<List<Double>> outerList = new ArrayList<>();
        for (Request request : requests) {
            List<Double> singleRequestValues = new ArrayList<>();
            singleRequestValues.add(Double.valueOf(request.getTime()));
            singleRequestValues.add(request.getV1());
            singleRequestValues.add(request.getV2());
            singleRequestValues.add(request.getV3());
            singleRequestValues.add(request.getV4());
            singleRequestValues.add(request.getV5());
            singleRequestValues.add(request.getV6());
            singleRequestValues.add(request.getV7());
            singleRequestValues.add(request.getV8());
            singleRequestValues.add(request.getV9());
            singleRequestValues.add(request.getV10());
            singleRequestValues.add(request.getV11());
            singleRequestValues.add(request.getV12());
            singleRequestValues.add(request.getV13());
            singleRequestValues.add(request.getV14());
            singleRequestValues.add(request.getV15());
            singleRequestValues.add(request.getV16());
            singleRequestValues.add(request.getV17());
            singleRequestValues.add(request.getV18());
            singleRequestValues.add(request.getV19());
            singleRequestValues.add(request.getV20());
            singleRequestValues.add(request.getV21());
            singleRequestValues.add(request.getV22());
            singleRequestValues.add(request.getV23());
            singleRequestValues.add(request.getV24());
            singleRequestValues.add(request.getV25());
            singleRequestValues.add(request.getV26());
            singleRequestValues.add(request.getV27());
            singleRequestValues.add(request.getV28());
            singleRequestValues.add(request.getAmount());
            outerList.add(singleRequestValues);
        }
        requestMap.put("values", outerList);
        inputDataAsJsonArray.add(requestMap);
        inputDataAsBuildJsonObject.put("input_data",inputDataAsJsonArray);
        ObjectMapper objectMapper = new ObjectMapper();
        String result = null;
        try {
            result = objectMapper.writeValueAsString(inputDataAsBuildJsonObject);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }
}