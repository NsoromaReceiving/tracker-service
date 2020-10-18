package com.nsoroma.trackermonitoring.datasourceclient.server1api.client;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.nsoroma.trackermonitoring.datasourceclient.server1api.model.LatestLocation;
import com.nsoroma.trackermonitoring.datasourceclient.server1api.model.Unit;
import com.nsoroma.trackermonitoring.exceptions.DataSourceClientResponseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface UnitManager {

    public List<Unit> getUnits(ArrayList<String> uids) throws DataSourceClientResponseException, UnirestException, IOException;

    public List<LatestLocation> getLatestLocation(ArrayList<String> uids) throws UnirestException, IOException, DataSourceClientResponseException;

    public ArrayList<String> getUnitsStringChunks() throws DataSourceClientResponseException, UnirestException, IOException;

}
