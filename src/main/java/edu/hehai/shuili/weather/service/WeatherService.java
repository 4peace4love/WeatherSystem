package edu.hehai.shuili.weather.service;

import edu.hehai.shuili.weather.dao.WeatherDao;
import edu.hehai.shuili.weather.pojo.Weather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by yangyue
 *
 * @Date: 27/10/2017
 * @Time: 9:51 AM
 * @package_name: edu.hehai.shuili.weather.service
 * @Description:
 */
@Service
public class WeatherService {

    @Autowired
    private WeatherDao weatherDao;


    /**
     * 根据日期范围查找天气记录
     * @param cityName 城市名
     * @return 范围内的天气记录
     */
    public List<Weather> find(String cityName){
        return weatherDao.find(cityName);
    }


    public List<Weather> findall(){
        return weatherDao.findall();
    }

    /**
     * 添加一条天气记录
     * @param weather 天气记录
     * @return 是否添加成功
     */
    public boolean add(Weather weather){
        return weatherDao.add(weather);
    }
}
