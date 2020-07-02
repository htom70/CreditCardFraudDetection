package hu.user.rendszerhaz.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class PredictionList {

    private List<SinglePrediction[]> values = new ArrayList<>();

    public List<SinglePrediction[]> getValues() {
        return values;
    }

    public void setValues(List<SinglePrediction[]> values) {
        this.values = values;
    }
}
