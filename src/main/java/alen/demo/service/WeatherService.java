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

    List<Province> getProvinces() throws Exception;

    List<City> getCities(String proCode) throws Exception;

    List<Country> getCountries(String proCode, String cityCode) throws Exception;

    WeatherInfo getWeatherInfo(String proCode, String cityCode, String countryCode);
}
