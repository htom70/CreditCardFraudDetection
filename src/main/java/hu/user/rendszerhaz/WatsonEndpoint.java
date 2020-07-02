package hu.user.rendszerhaz;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import hu.user.rendszerhaz.domain.PredictionList;
import hu.user.rendszerhaz.domain.SimplePrediction;
import hu.user.rendszerhaz.domain.SinglePrediction;
import hu.user.rendszerhaz.service.WatsonClient;
import io.spring.guides.gs_producing_web_service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Endpoint
public class WatsonEndpoint {
    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";


    private WatsonClient watsonClient;

    @Autowired
    public WatsonEndpoint(WatsonClient watsonClient) {
        this.watsonClient = watsonClient;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getDetectionRequest")
    @ResponsePayload
    public GetDetectionResponse getPrediction(@RequestPayload GetDetectionRequest detectionRequest) {
//        try {
////            String responseString = watsonHandler.handle(request.getRequest());
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        List<Request> request = detectionRequest.getRequest();
        List<SimplePrediction> simplePredictions = new ArrayList<>();
        CompletableFuture<String> responseFromWatson = watsonClient.predict(detectionRequest.getRequest());
        try {
            String responseAsString = responseFromWatson.get();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode fullNode = mapper.readTree(responseAsString);
            JsonNode resultNode= fullNode.get("predictions").get(0).get("values");
            ObjectReader reader = mapper.readerFor(new TypeReference<SimplePrediction>) {
            });
            Iterator<JsonNode> iterator = resultNode.elements();
            while (iterator.hasNext()) {
                JsonNode jsonNode = iterator.next();
                SimplePrediction simplePrediction = ;
            }

//            SinglePrediction[] singlePredictions = reader.readValue(resultNode);
//            System.out.println(predictionList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        List<Response> responses = new Response();
//        response.setPrediction(1.0);
        GetDetectionResponse getDetectionResponse = new ObjectFactory().createGetDetectionResponse();
//        getDetectionResponse.setResponse(response);
        return getDetectionResponse;
    }
}
