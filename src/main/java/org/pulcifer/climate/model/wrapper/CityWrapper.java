package org.pulcifer.climate.model.wrapper;

import org.pulcifer.climate.model.City;

/**
 * Wrapper, as given by the source service.
 * It serves no purpose in this set of applications,
 * so is simply discarded upon insertion into the db.
 * As it's not used beyond the parsing of the source
 * material, it's not shared via the model project.
 */
public class CityWrapper {
    City city;

    public CityWrapper() {}

    public City getCity() {return this.city;}

    public void setCity(City city) {this.city = city; }

}
