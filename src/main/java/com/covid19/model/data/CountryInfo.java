package com.covid19.model.data;

public class CountryInfo {

    private int _id;
    private String iso2;
    private String iso3;
    private String flag;

    public CountryInfo() {

    }

    public CountryInfo(final CountryInfo countryInfo) {
        this._id = countryInfo.getId();
        this.iso2 = countryInfo.getIso2();
        this.iso3 = countryInfo.getIso3();
        this.flag = countryInfo.getFlag();
    }

    public int getId() {
        return _id;
    }

    public void setId(final int _id) {
        this._id = _id;
    }

    public String getIso2() {
        return iso2 != null ? iso2 : "NO";
    }

    public void setIso2(final String iso2) {
        this.iso2 = iso2;
    }

    public String getIso3() {
        return iso3;
    }

    public void setIso3(final String iso3) {
        this.iso3 = iso3;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(final String flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "CountryInfo{" +
                "_id=" + _id +
                ", iso2='" + iso2 + '\'' +
                ", iso3='" + iso3 + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }
}