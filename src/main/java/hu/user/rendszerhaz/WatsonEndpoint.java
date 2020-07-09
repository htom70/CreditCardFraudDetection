package hu.user.rendszerhaz;

import hu.user.rendszerhaz.domain.SinglePrediction;
import hu.user.rendszerhaz.service.WatsonClient;
import io.spring.guides.gs_producing_web_service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

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
        ObjectFactory objectFactory = new ObjectFactory();
        List<Request> requests = detectionRequest.getRequest();
        List<SinglePrediction> predictions = watsonClient.predictAndConvertFromJsonStringToPredictionList(requests);
        GetDetectionResponse getDetectionResponse = objectFactory.createGetDetectionResponse();
        List<Response> responses = getDetectionResponse.getResponse();
        for (int i = 0; i < predictions.size(); i++) {
            Response response = new Response();
            SinglePrediction singlePrediction = predictions.get(i);
            response.setPrediction(singlePrediction.getPredictionValue());
            response.setNegativeProbability(singlePrediction.getParameters().get(0));
            response.setPositiveProbability(singlePrediction.getParameters().get(1));
            responses.add(response);
        }
        return getDetectionResponse;
    }
}
