package data;

import java.util.ArrayList;
import java.util.List;

public class CityObject {

    List cityName = new ArrayList();
    List State = new ArrayList();
    List zipcode = new ArrayList();


    public List getCityName() {
        return cityName;
    }

    public void setCityName(List cityName) {
        this.cityName = cityName;
    }

    public List getState() {
        return State;
    }

    public void setState(List state) {
        State = state;
    }

    public List getZipcode() {
        return zipcode;
    }

    public void setZipcode(List zipcode) {
        this.zipcode = zipcode;
    }
}
