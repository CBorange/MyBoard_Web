package com.ltj.myboard.repository;

import com.ltj.myboard.domain.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

@Repository
public class JDBC_BoardRepository implements BoardRepository{

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public JDBC_BoardRepository(DataSource dataSource){
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Optional<Board> findBoardByID(int id) {
        return null;
    }

    @Override
    public List<Board> getAllBoards() {
        String sql = "SELECT * FROM board;";
        List<Board> boards = jdbcTemplate.query(
                "SELECT * FROM board;",
                BeanPropertyRowMapper.newInstance(Board.class)
        );
        return boards;
    }

    @Override
    public List<Board> getAllRootBoards() {

        return null;
    }
}
