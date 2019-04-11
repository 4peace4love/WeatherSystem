package edu.hehai.shuili.weather.service;

import edu.hehai.shuili.weather.dao.TaskCityDao;
import edu.hehai.shuili.weather.pojo.City;
import edu.hehai.shuili.weather.pojo.TaskCity;
import edu.hehai.shuili.weather.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yangyue
 *
 * @Date: 27/10/2017
 * @Time: 12:46 AM
 * @package_name: edu.hehai.shuili.weather.service
 * @Description:
 */
@Service
public class TaskCityService {

    @Autowired
    private TaskCityDao taskCityDao;
    @Autowired
    private RegionService regionService;


    /**
     * 返回所有城市信息
     * @return 所有需要采集的城市
     */
    public List<TaskCity> findAll(){
        return taskCityDao.findAll();
    }


    /**
     * 判断城市是否存在
     * @param regionId
     * @return
     */
    public boolean isExist(int regionId){
        return taskCityDao.isExist(regionId);
    }
    /**
     * 分页返回城市信息
     * @param pageNum 页码
     * @return 分页城市信息
     */
    public Page<TaskCity> findPage(int pageNum){
        return taskCityDao.findPage(pageNum);
    }

    /**
     * 增加城市
     * @param cityId 区域id
     * @return 是否成功
     */
    public boolean add(int cityId){
        City city = regionService.findCityById(cityId);
        TaskCity taskCity = new TaskCity();
        if (city != null){
            taskCity.setCityId(city.getCityid());
            taskCity.setCityName(city.getCity());
            taskCity.setProvince(regionService.findProvinceByID(city.getProvinceid()).getProvince());
            return taskCityDao.add(taskCity);
        }
        return false;
    }

    /**
     * 根据城市id删除城市
     * @param regionId 城市id
     * @return 是否成功
     */
    public boolean delete(int regionId){
        return taskCityDao.delete(regionId);
    }



}
