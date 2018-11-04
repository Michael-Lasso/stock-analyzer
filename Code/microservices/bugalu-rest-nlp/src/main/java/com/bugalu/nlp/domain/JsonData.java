package com.bugalu.nlp.domain;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JsonData {

private List<Business> businesses = null;
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

public List<Business> getBusinesses() {
return businesses;
}

public void setBusinesses(List<Business> businesses) {
this.businesses = businesses;
}

public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
}
}



