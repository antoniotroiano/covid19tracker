package com.statistics.corona.model.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.statistics.corona.model.DistrictDto;

import java.util.List;

public class DataObjectDistrict {

    @JsonProperty("data")
    List<DistrictDto> districtDtoList;

    public List<DistrictDto> getDistrictDtoList() {
        return districtDtoList;
    }

    public void setDistrictDtoList(List<DistrictDto> districtDtoList) {
        this.districtDtoList = districtDtoList;
    }

    @Override
    public String toString() {
        return "DataObjectDistrict{" +
                "districtDtoList=" + districtDtoList +
                '}';
    }
}