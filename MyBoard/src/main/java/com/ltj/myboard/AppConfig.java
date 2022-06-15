package com.ltj.myboard;

import com.ltj.myboard.service.BoardService;
import com.ltj.myboard.service.BoardServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public BoardService boardService(){
        return new BoardServiceImpl();
    }
}
