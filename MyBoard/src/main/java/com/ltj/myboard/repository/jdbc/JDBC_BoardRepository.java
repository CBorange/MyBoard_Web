package com.ltj.myboard.repository.jdbc;

import com.ltj.myboard.domain.Board;
import com.ltj.myboard.repository.BoardRepository;
import com.ltj.myboard.util.MyResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JDBC_BoardRepository implements BoardRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final String findBoardByID_SQL;
    private final String getAllBoards_SQL;

    @Autowired
    public JDBC_BoardRepository(DataSource dataSource){
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        String daoName = "JDBC_BoardRepository";
        findBoardByID_SQL = MyResourceLoader.loadProductionQuery(daoName, "findBoardByID.sql");
        getAllBoards_SQL = MyResourceLoader.loadProductionQuery(daoName, "getAllBoards.sql");
    }

    @Override
    public Optional<Board> findBoardByID(int id) {
        MapSqlParameterSource namedParameter = new MapSqlParameterSource();
        namedParameter.addValue("id", id);

        Optional<Board> ret = Optional.<Board>of((Board)jdbcTemplate.queryForObject(findBoardByID_SQL, namedParameter, new BeanPropertyRowMapper(Board.class)));
        return ret;
    }

    @Override
    public List<Board> getAllBoards() {
        // 쿼리 실행
        List<Board> allBoards = jdbcTemplate.query(
                getAllBoards_SQL,
                BeanPropertyRowMapper.newInstance(Board.class)
        );

        // 자식 Board 객체 맵핑
        for(Board board : allBoards) {
            if(board.getParentBoardID() == 0){  // 루트 Board만 해당
                List<Board> childBoards = allBoards.stream().filter(item -> {
                    if(item.getParentBoardID() == board.getID())
                        return true;
                    return false;
                }).collect(Collectors.toList());

                board.addChildBoard(childBoards);
            }
        }

        return allBoards;
    }

    @Override
    public List<Board> getAllRootBoards() {
        List<Board> allBoards = getAllBoards();
        List<Board> rootBoards = allBoards.stream().filter(board -> {
            if(board.getParentBoardID() == 0)
                return true;
            return false;
        }).collect(Collectors.toList());
        return rootBoards;
    }
}
