package hu.user.rendszerhaz.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Values {

    private List<Integer> predict = new ArrayList<>();
    private List<Value> parameters = new ArrayList<>();

    public List<Integer> getPredict() {
        return predict;
    }

    public void setPredict(List<Integer> predict) {
        this.predict = predict;
    }

    public List<Value> getParameters() {
        return parameters;
    }

    public void setParameters(List<Value> parameters) {
        this.parameters = parameters;
    }
}
