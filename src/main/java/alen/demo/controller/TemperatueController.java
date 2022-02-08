package alen.demo.controller;

import alen.demo.entity.City;
import alen.demo.entity.Country;
import alen.demo.entity.Province;
import alen.demo.entity.WeatherInfo;
import alen.demo.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author: Alen
 * @Date: 2022/2/8
 */
@RestController
@RequestMapping(value = "/api/demo/temperature")
public class TemperatueController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping(value = "/getTemperature")
    public Optional<Integer> getTemperature(@RequestParam String province, @RequestParam String city, @RequestParam String country) throws Exception {
        List<Province> provinceList = weatherService.getProvinces();
        if(provinceList == null || provinceList.isEmpty()){
            throw new Exception("No province result");
        }
        String provinceCode = getProCode(provinceList, province);
        if(StringUtils.isEmpty(provinceCode)){
            throw new Exception("Invalid province name");
        }

        List<City> cityList = weatherService.getCities(provinceCode);
        if(cityList == null || cityList.isEmpty()){
            throw new Exception("No city result");
        }
        String cityCode = getCityCode(cityList, city);
        if(StringUtils.isEmpty(cityCode)){
            throw new Exception("Invalid city name");
        }

        List<Country> countryList = weatherService.getCountries(provinceCode, cityCode);
        if(countryList == null || countryList.isEmpty()){
            throw new Exception("No country result");
        }
        String countryCode = getCountryCode(countryList, country);
        if(StringUtils.isEmpty(countryCode)){
            throw new Exception("Invalid country name");
        }

        WeatherInfo weatherInfo = weatherService.getWeatherInfo(provinceCode, cityCode, countryCode);
        if(weatherInfo == null || weatherInfo.getTemp() == null){
            throw new Exception("No weather result");
        }

        return Optional.of(Double.valueOf(weatherInfo.getTemp()).intValue());
    }

    private String getCountryCode(List<Country> countryList, String country) {
        return countryList.stream().collect(Collectors.toMap(Country::getCountryName, Country::getCountryCode)).get(country);
    }

    private String getCityCode(List<City> cityList, String city) {
        return cityList.stream().collect(Collectors.toMap(City::getCityName, City::getCityCode)).get(city);
    }

    private String getProCode(List<Province> provinceList, String province) {
        return provinceList.stream().collect(Collectors.toMap(Province::getProName, Province::getProCode)).get(province);
    }
}
