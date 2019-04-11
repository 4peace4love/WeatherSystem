package edu.hehai.shuili.weather.service;

import edu.hehai.shuili.weather.dao.LogDao;
import edu.hehai.shuili.weather.pojo.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogService{
    @Autowired
    private LogDao logDao;

    public void insert(Log log){
        logDao.add(log);
    }
}
