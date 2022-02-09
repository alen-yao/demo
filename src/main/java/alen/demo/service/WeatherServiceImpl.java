package alen.demo.service;

import alen.demo.entity.City;
import alen.demo.entity.Country;
import alen.demo.entity.Province;
import alen.demo.entity.WeatherInfo;
import alen.demo.util.HttpClientUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Alen
 * @Date: 2022/2/8
 */
@Slf4j
@Service("WeatherServiceImpl")
public class WeatherServiceImpl implements WeatherService{

    @Value("${api.province}")
    private String proPath;

    @Value("${api.city}")
    private String cityPath;

    @Value("${api.country}")
    private String countryPath;

    @Value("${api.weather}")
    private String weatherPath;

    private static final String URL_SUFFIX = ".html";

    @Override
    public List<Province> getProvinces() {
        String url = proPath;
        String proResult = HttpClientUtil.doGet(url);
        JSONObject list = JSONObject.parseObject(proResult);

        if(list.isEmpty()){
            return null;
        }

        List<Province> provinceList = new ArrayList<>();

        for(String key : list.keySet()){
            Province province = new Province();
            province.setProCode(key);
            province.setProName(list.getString(key));

            provinceList.add(province);
        }

        return provinceList;
    }

    @Override
    public List<City> getCities(String proCode) {
        String url = cityPath + proCode + URL_SUFFIX;
        String cityResult = HttpClientUtil.doGet(url);
        JSONObject list = JSONObject.parseObject(cityResult);

        if(list.isEmpty()){
            return null;
        }

        List<City> cityList = new ArrayList<>();

        for(String key : list.keySet()){
            City city = new City();
            city.setCityCode(key);
            city.setCityName(list.getString(key));

            cityList.add(city);
        }

        return cityList;
    }

    @Override
    public List<Country> getCountries(String proCode, String cityCode) {
        String url = countryPath + proCode + cityCode + URL_SUFFIX;
        String countryResult = HttpClientUtil.doGet(url);
        JSONObject list = JSONObject.parseObject(countryResult);

        if(list.isEmpty()){
            return null;
        }

        List<Country> countryList = new ArrayList<>();
        for(String key : list.keySet()){
            Country country = new Country();
            country.setCountryCode(key);
            country.setCountryName(list.getString(key));

            countryList.add(country);
        }

        return countryList;
    }

    @Override
    public WeatherInfo getWeatherInfo(String proCode, String cityCode, String countryCode) {
        String url = weatherPath + proCode + cityCode + countryCode + URL_SUFFIX;
        String weatherResult = HttpClientUtil.doGet(url);
        JSONObject object = JSONObject.parseObject(weatherResult);
        if(weatherResult == null){
            return null;
        }
        WeatherInfo weatherInfo = JSONObject.parseObject(object.getString("weatherinfo"), WeatherInfo.class);

        return weatherInfo;
    }
}
