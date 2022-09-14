package com.delgo.reward.comm.ncp.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GeoResponseDTO {
    String status;
    GeoAddressDTO geoAddressDTO;
}
