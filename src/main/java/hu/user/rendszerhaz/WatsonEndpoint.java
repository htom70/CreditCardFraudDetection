package hu.user.rendszerhaz;

import hu.user.rendszerhaz.service.WatsonClient;
import io.spring.guides.gs_producing_web_service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

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
        Request request = detectionRequest.getRequest();
        CompletableFuture<String> responseFromWatson = watsonClient.predict(detectionRequest.getRequest());
        try {
            String result = responseFromWatson.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Response response = new Response();
        response.setPrediction(1.0);
        GetDetectionResponse getDetectionResponse = new ObjectFactory().createGetDetectionResponse();
        getDetectionResponse.setResponse(response);
        return getDetectionResponse;
    }
}
