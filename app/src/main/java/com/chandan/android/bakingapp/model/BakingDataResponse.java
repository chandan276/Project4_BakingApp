package com.chandan.android.bakingapp.model;

import java.util.List;

public class BakingDataResponse {

    private List<BakingData> results;

    public BakingDataResponse() { }

    public BakingDataResponse(List<BakingData> results) { this.results = results; }

    public List<BakingData> getResults() { return results; }

    public void setResults(List<BakingData> results) { this.results = results; }
}
