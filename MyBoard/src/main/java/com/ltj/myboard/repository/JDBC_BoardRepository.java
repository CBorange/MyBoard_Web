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
import java.util.stream.Collectors;

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
        // 쿼리 실행
        String sql = "SELECT * FROM board;";
        List<Board> allBoards = jdbcTemplate.query(
                sql,
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
