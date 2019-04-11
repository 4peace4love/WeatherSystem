package edu.hehai.shuili.weather.service;

import edu.hehai.shuili.weather.dao.CityDao;
import edu.hehai.shuili.weather.dao.ProvinceDao;
import edu.hehai.shuili.weather.pojo.City;
import edu.hehai.shuili.weather.pojo.Province;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yangyue
 *
 * @Date: 26/10/2017
 * @Time: 9:37 PM
 * @package_name: edu.hehai.shuili.weather.service
 * @Description:
 */
@Service
public class RegionService {

    @Autowired
    private ProvinceDao provinceDao;
    @Autowired
    private CityDao cityDao;


    /**
     * 查询所有的省份列表
     * @return
     */
    public List<Province> findAllProvince(){
        return provinceDao.findAll();
    }

    /**
     * 查询某个省份的所有城市
     * @param provinceId
     * @return
     */
    public List<City> findAllCityOfProvince(int provinceId){
        return cityDao.findCitiesByProvinceid(provinceId);
    }

    /**
     * 根据id查询城市信息
     * @param cityId
     * @return
     */
    public City findCityById(int cityId){
        return cityDao.findCityById(cityId);
    }

    /**
     * 根据id查询省份
     * @param provinceID
     * @return
     */
    public Province findProvinceByID(int provinceID){
        return provinceDao.findProvinceById(provinceID);
    }
}
