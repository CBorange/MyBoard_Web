package com.ltj.myboard.repository.jdbc;

import com.ltj.myboard.domain.Board;
import com.ltj.myboard.repository.BoardRepository;
import com.ltj.myboard.util.MyResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JDBC_BoardRepository implements BoardRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private ResourceLoader resourceLoader;

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

        Optional<Board> ret = Optional.<Board>of((Board)jdbcTemplate.queryForObject(findBoardByID_SQL,
                                                                                    namedParameter,
                                                                                    new BoardRowMapper()));
        return ret;
    }

    @Override
    public List<Board> getAllBoards() {
        // 쿼리 실행
        List<Board> allBoards = jdbcTemplate.query(
                getAllBoards_SQL,
                new BoardRowMapper()
        );

        // 자식 Board 객체 맵핑
        for(Board board : allBoards) {
            if(board.getParent_board_id() == 0){  // 루트 Board만 해당
                List<Board> childBoards = allBoards.stream().filter(item -> {
                    if(item.getParent_board_id() == board.getId())
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
            if(board.getParent_board_id() == 0)
                return true;
            return false;
        }).collect(Collectors.toList());
        return rootBoards;
    }

    @Override
    public List<Board> getAllLeafBoards(){
        List<Board> allBoards = getAllBoards();
        List<Board> leafBoards = allBoards.stream().filter(board -> {
            if(board.getChildBoardCount() == 0)
                return true;
            return false;
        }).collect(Collectors.toList());
        return leafBoards;
    }

    public class BoardRowMapper implements RowMapper<Board>{
        @Override
        public Board mapRow(ResultSet rs, int rowNum) throws SQLException {
            Board result = new Board();
            result.setId(rs.getInt("id"));
            result.setBoard_name(rs.getString("board_name"));
            result.setBoard_owner_id(rs.getString("board_owner_id"));
            result.setParent_board_id(rs.getInt("parent_board_id"));
            result.setBoard_icon(rs.getString("board_icon"));
            result.setCreated_day(rs.getTimestamp("created_day").toLocalDateTime());
            result.setModify_day(rs.getTimestamp("modify_day").toLocalDateTime());
            Timestamp deleteDayTS = rs.getTimestamp("delete_day");
            if(deleteDayTS != null)
                result.setDelete_day(deleteDayTS.toLocalDateTime());
            return result;
        }
    }
}
