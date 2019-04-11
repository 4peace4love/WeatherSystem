package edu.hehai.shuili.weather.dao;

import edu.hehai.shuili.weather.pojo.Province;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

@Repository
public class ProvinceDao {

    private final JdbcTemplate jdbcTemplate;

    public ProvinceDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final static String QUERY_ALL_PROVINCE = "select * from province";
    private final static String QUERY_PROVINCE_BY_ID = "select * from province where provinceid=?";



    /**
     * find all province
     * @return
     */
    public List<Province> findAll(){
        return this.jdbcTemplate.query(QUERY_ALL_PROVINCE, new ProvinceRowMapper());
    }


    /**
     * find province by provinceID
     * @param provinceid
     * @return
     */
    public Province findProvinceById(int provinceid){
        Object[] params = new Object[]{provinceid};
        int[] types = new int[]{Types.INTEGER};
        List<Province> provinces = this.jdbcTemplate.query(QUERY_PROVINCE_BY_ID, params, types, new ProvinceRowMapper());
        if (provinces == null || provinces.size() == 0){
            return null;
        }else {
            return provinces.get(0);
        }
    }
    class ProvinceRowMapper implements RowMapper<Province>{
        @Override
        public Province mapRow(ResultSet resultSet, int i) throws SQLException {
            Province province = new Province();
            province.setId(resultSet.getInt("id"));
            province.setProvinceid(resultSet.getInt("provinceid"));
            province.setProvince(resultSet.getString("province"));
            return province;
        }
    }
}
