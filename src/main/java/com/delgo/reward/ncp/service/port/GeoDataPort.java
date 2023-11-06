package com.delgo.reward.ncp.service.port;

import com.delgo.reward.domain.common.GeoData;


public interface GeoDataPort {
    GeoData getGeoData(String address);
    String getReverseGeoData(String latitude, String longitude);
}
