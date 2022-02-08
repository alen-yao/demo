package alen.demo;

import alen.demo.service.WeatherService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @Author: Alen
 * @Date: 2022/2/8
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes={DemoApplication.class})
public class WeatherServiceTests{

    @Autowired
    private WeatherService weatherService;

    @Before
    public void init() {
        System.out.println("start test-----------------");
    }

    @After
    public void after() {
        System.out.println("end test-----------------");
    }

    @Test
    public void testGetCities() throws Exception {
        Assert.assertNotNull("not null", weatherService.getCities("10119"));
    }

    @Test
    public void testGetCountries() throws Exception {
        Assert.assertNotNull("not null", weatherService.getCountries("10119", "04"));
    }

    @Test
    public void testGetWeather(){
        Assert.assertNotNull("not null", weatherService.getWeatherInfo("10119", "04","01"));
    }

    @Test(expected = Exception.class)
    public void testGetCitiesFail() throws Exception {
        weatherService.getCities("101888819");
    }

    @Test(expected = Exception.class)
    public void testGetCountriesFail() throws Exception {
        weatherService.getCountries("1011111119","11111");
    }
}
