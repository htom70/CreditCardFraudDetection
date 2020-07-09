package hu.user.rendszerhaz.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleRequest {

    private Map<String, List<Double>> values = new HashMap<>();

    public Map<String, List<Double>> getValues() {
        return values;
    }

    public void setValues(Map<String, List<Double>> values) {
        this.values = values;
    }
}
