package edu.hehai.shuili.weather.dao;

import edu.hehai.shuili.weather.pojo.City;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

@Repository
public class CityDao {

    private final JdbcTemplate jdbcTemplate;
    public CityDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final static String QUERY_ALL_CITIES = "select * from city where provinceid = ?";
    private final static String QUERY_CITY_BY_ID = "select * from city where cityid=?";
    /**
     * find all cities of one province
     * @param provinceid
     * @return
     */
    public List<City> findCitiesByProvinceid(int provinceid){
        Object[] params = new Object[]{provinceid};
        int[] types = new int[]{Types.INTEGER};
        return this.jdbcTemplate.query(QUERY_ALL_CITIES, params, types, new CityRowMapper());
    }

    /**
     * find city by id
     * @param cityId
     * @return
     */
    public City findCityById(int cityId){
        Object[] params = new Object[]{cityId};
        int[] types = new int[]{Types.INTEGER};
        List<City> cities = this.jdbcTemplate.query(QUERY_CITY_BY_ID, params, types, new CityRowMapper());
        if (cities == null || cities.size() == 0){
            return null;
        }else {
            return cities.get(0);
        }
    }

    class CityRowMapper implements RowMapper<City>{
        @Override
        public City mapRow(ResultSet resultSet, int i) throws SQLException {
            City city = new City();
            city.setId(resultSet.getInt("id"));
            city.setCityid(resultSet.getInt("cityid"));
            city.setCity(resultSet.getString("city"));
            city.setProvinceid(resultSet.getInt("provinceid"));
            return city;
        }
    }
}
