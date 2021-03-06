package edu.hehai.shuili.weather.sdk;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import edu.hehai.shuili.weather.pojo.Weather;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yangyue
 *
 * @Date: 26/10/2017
 * @Time: 7:18 PM
 * @package_name: edu.hehai.shuili.weather.api
 * @Description: 和天气jd万象api
 */

public class HeWeatherJdSDK {

    //和天气接口
    private final static String apiUrl = "https://way.jd.com/he/freeweather";
    //接口参数
    private final static String apiParam = "city";
    //apikey
    private final static String appKey = "8e7129d9e81c06d25ff81c30679f4c9d";
    //返回的api结果版本
    private final static String apiVersion = "HeWeather5";

    private static Logger logger = Logger.getLogger(HeWeatherJdSDK.class);

    /**
     *
     * 百度api通用调用方法
     * @param httpUrl 请求接口
     * @param httpArg 参数串，不包含？
     * @return 返回结果
     */
    private static String request(String httpUrl, String httpArg) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" + httpArg;

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据城市名返回天气，调用和天气api
     * @param city 城市名
     * @return weather
     */
    public static Weather getWeather(String city) {
        System.out.println(city);
        Weather weather = new Weather();

        String resString = request(apiUrl, apiParam + "=" + city + "&appkey=" + appKey);
        System.out.println(resString);
        JSONObject resJson = JSON.parseObject(resString);

        if (!resJson.getString("code").equals("10000")){
            logger.error("api接口异常");
            return null;
        }

        JSONObject content = resJson.getJSONObject("result").getJSONArray(apiVersion).getJSONObject(0);

        //城市基本信息
        JSONObject contentBasic = content.getJSONObject("basic");
        //包含未来七天的天气预报
        JSONArray contentDailyForecast = content.getJSONArray("daily_forecast");

        //日期
        java.sql.Date sqlDate=new java.sql.Date(contentDailyForecast.getJSONObject(0).getDate("date").getTime());

        weather.setInsertDate(sqlDate);
        //城市
        weather.setCityName(city);

        //未来七天的最高温度
        weather.setMaxTemp1(contentDailyForecast.getJSONObject(0).getJSONObject("tmp").getInteger("max"));
        weather.setMaxTemp2(contentDailyForecast.getJSONObject(1).getJSONObject("tmp").getInteger("max"));
        weather.setMaxTemp3(contentDailyForecast.getJSONObject(2).getJSONObject("tmp").getInteger("max"));
        weather.setMaxTemp4(contentDailyForecast.getJSONObject(3).getJSONObject("tmp").getInteger("max"));
        weather.setMaxTemp5(contentDailyForecast.getJSONObject(4).getJSONObject("tmp").getInteger("max"));
        weather.setMaxTemp6(contentDailyForecast.getJSONObject(5).getJSONObject("tmp").getInteger("max"));
        weather.setMaxTemp7(contentDailyForecast.getJSONObject(6).getJSONObject("tmp").getInteger("max"));

        //未来七天的最低温度
        weather.setMinTemp1(contentDailyForecast.getJSONObject(0).getJSONObject("tmp").getInteger("min"));
        weather.setMinTemp2(contentDailyForecast.getJSONObject(1).getJSONObject("tmp").getInteger("min"));
        weather.setMinTemp3(contentDailyForecast.getJSONObject(2).getJSONObject("tmp").getInteger("min"));
        weather.setMinTemp4(contentDailyForecast.getJSONObject(3).getJSONObject("tmp").getInteger("min"));
        weather.setMinTemp5(contentDailyForecast.getJSONObject(4).getJSONObject("tmp").getInteger("min"));
        weather.setMinTemp6(contentDailyForecast.getJSONObject(5).getJSONObject("tmp").getInteger("min"));
        weather.setMinTemp7(contentDailyForecast.getJSONObject(6).getJSONObject("tmp").getInteger("min"));

        //未来七天的风力
        weather.setWind1(contentDailyForecast.getJSONObject(0).getJSONObject("wind").getString("sc"));
        weather.setWind2(contentDailyForecast.getJSONObject(1).getJSONObject("wind").getString("sc"));
        weather.setWind3(contentDailyForecast.getJSONObject(2).getJSONObject("wind").getString("sc"));
        weather.setWind4(contentDailyForecast.getJSONObject(3).getJSONObject("wind").getString("sc"));
        weather.setWind5(contentDailyForecast.getJSONObject(4).getJSONObject("wind").getString("sc"));
        weather.setWind6(contentDailyForecast.getJSONObject(5).getJSONObject("wind").getString("sc"));
        weather.setWind7(contentDailyForecast.getJSONObject(6).getJSONObject("wind").getString("sc"));

        //未来七天的天气简介
        weather.setWeather1(processWeatherField(contentDailyForecast.getJSONObject(0).getJSONObject("cond")));
        weather.setWeather2(processWeatherField(contentDailyForecast.getJSONObject(1).getJSONObject("cond")));
        weather.setWeather3(processWeatherField(contentDailyForecast.getJSONObject(2).getJSONObject("cond")));
        weather.setWeather4(processWeatherField(contentDailyForecast.getJSONObject(3).getJSONObject("cond")));
        weather.setWeather5(processWeatherField(contentDailyForecast.getJSONObject(4).getJSONObject("cond")));
        weather.setWeather6(processWeatherField(contentDailyForecast.getJSONObject(5).getJSONObject("cond")));
        weather.setWeather7(processWeatherField(contentDailyForecast.getJSONObject(6).getJSONObject("cond")));

        return weather;
    }

    /**
     * 将api返回结果中白天天气和晚上天气整合
     * @param cond 天气简介对象
     * @return 天气情况整合
     */
    private static String processWeatherField(JSONObject cond){
        String txt_d = cond.getString("txt_d");
        String txt_n = cond.getString("txt_n");
        return txt_d.equals(txt_n)
                ? txt_d
                : (txt_d + "转" + txt_n);
    }

    public static void main(String[] args) {
       getWeather("北京");
    }
}
