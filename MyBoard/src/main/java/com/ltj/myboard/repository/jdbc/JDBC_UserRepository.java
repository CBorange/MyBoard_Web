package com.ltj.myboard.repository.jdbc;
import com.ltj.myboard.domain.User;
import com.ltj.myboard.repository.UserRepository;
import com.ltj.myboard.util.MyResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class JDBC_UserRepository implements UserRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final String findUserByID_SQL;
    private final String findUserByGrade_SQL;

    @Autowired
    public JDBC_UserRepository(DataSource dataSource){
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        // 쿼리파일 Load
        String daoName = "JDBC_UserRepository";
        findUserByID_SQL = MyResourceLoader.loadProductionQuery(daoName, "findUserByID.sql");
        findUserByGrade_SQL = MyResourceLoader.loadProductionQuery(daoName, "findUserByGrade.sql");
    }

    @Override
    public Optional<User> findUserByID(String ID) {
        MapSqlParameterSource namedParameter = new MapSqlParameterSource();
        namedParameter.addValue("id", ID);

        Optional<User> ret = Optional.of((User)jdbcTemplate.queryForObject(findUserByID_SQL, namedParameter,
                new BeanPropertyRowMapper(User.class)));
        return ret;
    }

    @Override
    public List<User> findUserByGrade(int grade) {
        MapSqlParameterSource namedParameter = new MapSqlParameterSource();
        namedParameter.addValue("grade", grade);

        List<User> userList = jdbcTemplate.query(
                findUserByGrade_SQL,
                namedParameter,
                BeanPropertyRowMapper.newInstance(User.class)
        );
        return userList;
    }
}
