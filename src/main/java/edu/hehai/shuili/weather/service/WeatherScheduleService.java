package edu.hehai.shuili.weather.service;

import edu.hehai.shuili.weather.pojo.Log;
import edu.hehai.shuili.weather.pojo.TaskCity;
import edu.hehai.shuili.weather.sdk.HeWeatherJdSDK;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class WeatherScheduleService {

    @Autowired
    private TaskCityService taskCityService;
    @Autowired
    private WeatherService weatherService;

    @Autowired
    private LogService logService;

    Logger logger = Logger.getLogger(WeatherScheduleService.class);

    /**
     * 定时执行天气采集任务
     * 每天10点执行
     */
    //    @Scheduled(cron="0/60 * * * * ? ") //间隔5秒执行  测试
    @Scheduled(cron="0 0 10 * * ?") //每天10点定时执行
    public void init() {
        int success_count = 0;
        int error_count = 0;
        StringBuilder error_msg = new StringBuilder();
        for (TaskCity city : taskCityService.findAll()){
            try {
                weatherService.add(HeWeatherJdSDK.getWeather(city.getCityName()));
                success_count ++;
                logger.warn("天气采集成功: 【" + city.getCityName() + "】");
            } catch (Exception e) {
                logger.error("天气采集失败！城市名【" + city.getCityName() + "】 错误信息: " + e.getMessage());
                error_count ++;
                error_msg.append(city.getCityName()).append(",");
            }
        }
        Log log = new Log();
        log.setSuccess(success_count);
        log.setError(error_count);
        log.setError_msg(error_msg.toString());
        logService.insert(log);
    }

}