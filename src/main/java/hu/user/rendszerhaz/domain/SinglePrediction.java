package hu.user.rendszerhaz.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class SinglePrediction {

    private int predictionValue;
    private double[] parameters = new double[2];

    public int getPredictionValue() {
        return predictionValue;
    }

    public void setPredictionValue(int predictionValue) {
        this.predictionValue = predictionValue;
    }

    public double[] getParameters() {
        return parameters;
    }

    public void setParameters(double[] parameters) {
        this.parameters = parameters;
    }
}
