package hu.user.rendszerhaz.domain;

import com.fasterxml.jackson.databind.JsonNode;

public class SimplePrediction {

    private int prediction;

    public SimplePrediction(int prediction) {
        this.prediction = prediction;
    }

    public int getPrediction() {
        return prediction;
    }

    public void setPrediction(int prediction) {
        this.prediction = prediction;
    }
}
