package com.ltj.myboard.repository.jdbc;

import com.ltj.myboard.domain.Board;
import com.ltj.myboard.repository.BoardRepository;
import com.ltj.myboard.util.MyResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
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
            result.setID(rs.getInt("ID"));
            result.setBoardName(rs.getString("BoardName"));
            result.setBoardOwnerID(rs.getString("BoardOwnerID"));
            result.setParentBoardID(rs.getInt("ParentBoardID"));
            result.setBoardIcon(rs.getString("BoardIcon"));
            result.setCreatedDay(rs.getTimestamp("CreatedDay").toLocalDateTime());
            result.setModifyDay(rs.getTimestamp("ModifyDay").toLocalDateTime());
            Timestamp deleteDayTS = rs.getTimestamp("DeleteDay");
            if(deleteDayTS != null)
                result.setDeleteDay(deleteDayTS.toLocalDateTime());
            return result;
        }
    }
}
