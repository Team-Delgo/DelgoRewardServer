package com.delgo.reward.certification.service.port;

import com.delgo.reward.domain.common.Location;

public interface GeoDataPort {
    Location getGeoData(String address);
    Location getReverseGeoData(String latitude, String longitude);
}
