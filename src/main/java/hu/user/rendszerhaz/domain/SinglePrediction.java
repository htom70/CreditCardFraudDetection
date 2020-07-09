package hu.user.rendszerhaz.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class SinglePrediction {

    private int predictionValue;
    private List<Double> parameters = new ArrayList<>();

    public int getPredictionValue() {
        return predictionValue;
    }

    public void setPredictionValue(int predictionValue) {
        this.predictionValue = predictionValue;
    }

    public List<Double> getParameters() {
        return parameters;
    }

    public void setParameters(List<Double> parameters) {
        this.parameters = parameters;
    }
}
