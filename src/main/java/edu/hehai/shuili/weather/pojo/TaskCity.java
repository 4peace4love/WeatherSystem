package edu.hehai.shuili.weather.pojo;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by yangyue
 *
 * @Date: 26/10/2017
 * @Time: 2:37 PM
 * @package_name: edu.hehai.shuili.weather.pojo
 * @Description: 要采集的城市列表
 */
public class TaskCity {
    //城市id
    private int id;
    //城市所在省份
    private String province;
    //城市名称
    private String cityName;
    //添加城市日期
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date insertDate;
    //区域id
    private int cityId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
