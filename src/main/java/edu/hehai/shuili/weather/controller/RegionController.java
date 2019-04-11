package edu.hehai.shuili.weather.controller;

import edu.hehai.shuili.weather.intercepter.Permission;
import edu.hehai.shuili.weather.pojo.City;
import edu.hehai.shuili.weather.pojo.Privilege;
import edu.hehai.shuili.weather.pojo.Province;
import edu.hehai.shuili.weather.service.RegionService;
import edu.hehai.shuili.weather.api.ApiStatus;
import edu.hehai.shuili.weather.util.MappingURL;
import edu.hehai.shuili.weather.api.ReturnWithResultApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by yangyue
 *
 * @Date: 27/10/2017
 * @Time: 2:24 PM
 * @package_name: edu.hehai.shuili.weather.controller
 * @Description: region相关api
 */
@RestController
@RequestMapping(value = MappingURL.REGION_BASE_URL)
public class RegionController {

    @Autowired
    private RegionService regionService;

    /**
     * 根据区域id查找区域
     * @param regionId 区域id
     * @return 区域详细信息
     */
    @Permission(value = {Privilege.ADMIN})
    @RequestMapping(value = MappingURL.REGION_FIND, method = RequestMethod.GET)
    public ReturnWithResultApi<City> findRegion(
            @PathVariable int regionId
    ){
        ReturnWithResultApi<City> returnWithResultApi = new ReturnWithResultApi<City>();
        City region = regionService.findCityById(regionId);
        if (region != null){
            returnWithResultApi.setApiStatus(ApiStatus.SUCCESS);
        }else {
            returnWithResultApi.setApiStatus(ApiStatus.EMPTY);
        }
        returnWithResultApi.setResult(region);
        return returnWithResultApi;
    }
    /**
     * 查询所有的省份列表
     * @return 所有的省份列表
     */
    @Permission(value = {Privilege.ADMIN})
    @RequestMapping(value = MappingURL.REGION_FIND_ALL_PROVINCE_URL, method = RequestMethod.GET)
    public ReturnWithResultApi<List <Province>> findAllProvince(){
        ReturnWithResultApi<List <Province>> returnWithResultApi = new ReturnWithResultApi<List <Province>>();
        returnWithResultApi.setApiStatus(ApiStatus.SUCCESS);
        returnWithResultApi.setResult(regionService.findAllProvince());
        return returnWithResultApi;
    }


    /**
     * 获取省份下的所有城市
     * @param provinceId 省份
     * @return 省份下的所有城市
     */
    @Permission(value = {Privilege.ADMIN})
    @RequestMapping(value = MappingURL.ENGION_FIND_ALL_CITY_OF_PROVINCE_URL, method = RequestMethod.GET)
    public ReturnWithResultApi<List <City>> findAllCityOfProvince(
            @PathVariable int provinceId
    ){
        ReturnWithResultApi<List <City>> returnWithResultApi = new ReturnWithResultApi<List <City>>();
        List<City> regionList = regionService.findAllCityOfProvince(provinceId);
        System.out.println(regionList.isEmpty());
        if (!regionList.isEmpty()){
            returnWithResultApi.setApiStatus(ApiStatus.SUCCESS);
        }else {
            returnWithResultApi.setApiStatus(ApiStatus.EMPTY);
        }
        returnWithResultApi.setResult(regionList);
        return returnWithResultApi;
    }
}
