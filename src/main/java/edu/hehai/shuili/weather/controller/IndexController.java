package edu.hehai.shuili.weather.controller;

import edu.hehai.shuili.weather.intercepter.Permission;
import edu.hehai.shuili.weather.pojo.Privilege;
import edu.hehai.shuili.weather.pojo.Weather;
import edu.hehai.shuili.weather.service.TaskCityService;
import edu.hehai.shuili.weather.service.WeatherService;
import edu.hehai.shuili.weather.util.MappingURL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by yangyue
 *
 * @Date: 30/10/2017
 * @Time: 10:02 AM
 * @package_name: edu.hehai.shuili.weather.controller
 * @Description:
 */
@Controller
public class IndexController {

    @Autowired
    TaskCityService taskCityService;

    @Autowired
    WeatherService weatherService;
    /**
     * API welcome page
     * @return
     */
    @RequestMapping(value = {MappingURL.LOGIN_PAGE})
    public String login(){
        return "login";
    }


    @Permission(value = {Privilege.ADMIN, Privilege.USER})
    @RequestMapping(value = {MappingURL.LOGOUT_PAGE})
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:"+MappingURL.LOGIN_PAGE;
    }

    @Permission(value = {Privilege.ADMIN, Privilege.USER})
    @RequestMapping(value = {MappingURL.ROOT_PAGE, MappingURL.INDEX_PAGE, MappingURL.CITY_LIST_PAGE})
    public ModelAndView cityList(ModelAndView mv){
        mv.addObject("city_list", taskCityService.findAll());
        mv.setViewName("city_list");
        return mv;
    }

    @Permission(value = {Privilege.ADMIN, Privilege.USER})
    @RequestMapping(value=MappingURL.WEATHER_DOWNLOAD_SINGLE_PAGE, method = RequestMethod.GET)
    public void exportSingleCity(HttpServletRequest request, HttpServletResponse response,
                                @RequestParam(value = "cityName") String cityName) throws IOException {

        String reportName = "city_data.csv";
        response.reset();
        response.setContentType("application/octet-stream; charset=UTF-8");
        response.setHeader("Content-Encoding", "UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + reportName);

        OutputStream outputStream = null;
        outputStream = response.getOutputStream();
        outputStream.write(0xEF);   // 1st byte of BOM
        outputStream.write(0xBB);
        outputStream.write(0xBF);   // last byte of BOM
        // now get a PrintWriter to stream the chars.

        List<Weather> weatherList = null;
        if (cityName.equals("all")){
            weatherList = weatherService.findall();
        }else {
            weatherList= weatherService.find(cityName);
        }
        PrintWriter os = new PrintWriter(new OutputStreamWriter(outputStream,"UTF-8"));
        StringBuffer sb = new StringBuffer();
        sb.append("id,cityName,date,weather_1,weather_2,weather_3,weather_4,weather_5,weather_6,weather_7," +
                "max_temp_1,max_temp_2,max_temp_3,max_temp_4,max_temp_5,max_temp_6,max_temp_7," +
                "min_temp_1,min_temp_2,min_temp_3,min_temp_4,min_temp_5,min_temp_6,min_temp_7," +
                "wind_1,wind_2,wind_3,wind_4,wind_5,wind_6,wind_7");
        for(Weather weather : weatherList ){
            System.out.println(weather.getWind1());
            sb.append("\n");sb.append(weather.getWeatherId() + "," + weather.getCityName() + "," + weather.getInsertDate()
                    + "," + weather.getWeather1() + "," + weather.getWeather2()+ "," + weather.getWeather3()+ "," + weather.getWeather4()+ "," + weather.getWeather5()+ "," + weather.getWeather6()+ "," + weather.getWeather7()
                    + "," + weather.getMaxTemp1() + "," + weather.getMaxTemp2()+ "," + weather.getMaxTemp3()+ "," + weather.getMaxTemp4()+ "," + weather.getMaxTemp5()+ "," + weather.getMaxTemp6()+ "," + weather.getMaxTemp7()
                    + "," + weather.getMinTemp1() + "," + weather.getMinTemp2()+ "," + weather.getMinTemp3()+ "," + weather.getMinTemp4()+ "," + weather.getMinTemp5()+ "," + weather.getMinTemp6()+ "," + weather.getMinTemp7()
                    + ",[" + weather.getWind1() + "],[" + weather.getWind2() + "],[" + weather.getWind3()+ "],[" + weather.getWind4()+ "],[" + weather.getWind5()+ "],[" + weather.getWind6()+ "],[" + weather.getWind7()+"]");
        }
        os.print(sb);
        os.flush();
        os.close();
    }
}
