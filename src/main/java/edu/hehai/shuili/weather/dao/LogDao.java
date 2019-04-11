package edu.hehai.shuili.weather.dao;

import edu.hehai.shuili.weather.pojo.Log;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

@Repository
public class LogDao {
    private final JdbcTemplate jdbcTemplate;

    public LogDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final static String SQL_ADD_LOG = "insert into log(success,error,error_msg,date) values(?,?,?,now())";


    public void add(Log log){
        Object[] params = new Object[]{log.getSuccess(),log.getError(),log.getError_msg()};
        int[] types = new int[]{Types.INTEGER, Types.INTEGER, Types.VARCHAR};
        this.jdbcTemplate.update(SQL_ADD_LOG, params, types);
    }

    class LogRowMapper implements RowMapper<Log>{
        @Override
        public Log mapRow(ResultSet resultSet, int i) throws SQLException {
            Log log = new Log();
            log.setId(resultSet.getInt("id"));
            log.setSuccess(resultSet.getInt("success"));
            log.setError(resultSet.getInt("error"));
            log.setError_msg(resultSet.getString("error_msg"));
            log.setDate(resultSet.getDate("date"));
            return log;
        }
    }
}

