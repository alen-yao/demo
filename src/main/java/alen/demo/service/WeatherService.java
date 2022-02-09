package alen.demo.service;

import alen.demo.entity.City;
import alen.demo.entity.Country;
import alen.demo.entity.Province;
import alen.demo.entity.WeatherInfo;

import java.util.List;

/**
 * @Author: Alen
 * @Date: 2022/2/8
 */
public interface WeatherService {

    List<Province> getProvinces();

    List<City> getCities(String proCode);

    List<Country> getCountries(String proCode, String cityCode);

    WeatherInfo getWeatherInfo(String proCode, String cityCode, String countryCode);
}
