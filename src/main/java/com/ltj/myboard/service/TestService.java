package com.ltj.myboard.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Slf4j
@Service
public class TestService {
    public void DoTest(){
        throw new NoSuchElementException("test no such");
    }
}
