package com.ltj.myboard.repository.jdbc;
import com.ltj.myboard.domain.UserGrade;
import com.ltj.myboard.repository.UserGradeRepository;
import com.ltj.myboard.util.MyResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.Optional;

public class JDBC_UserGradeRepository implements UserGradeRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final String findGradeByLevel_SQL;
    private final String findGradeByCaption_SQL;

    @Autowired
    public JDBC_UserGradeRepository(DataSource dataSource){
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        // 쿼리파일 Load
        String daoName = "JDBC_UserGradeRepository";
        findGradeByLevel_SQL = MyResourceLoader.loadProductionQuery(daoName, "findGradeByLevel.sql");
        findGradeByCaption_SQL = MyResourceLoader.loadProductionQuery(daoName, "findGradeByCaption.sql");
    }

    @Override
    public Optional<UserGrade> findGradeByLevel(int gradeLevel) {
        return Optional.empty();
    }

    @Override
    public Optional<UserGrade> findGradeByCaption(String caption) {
        return Optional.empty();
    }
}
