package com.pain.blue.wiki.service;

import com.pain.blue.wiki.domain.pojo.Hero;
import com.pain.blue.wiki.mapper.HeroMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class HeroService {

    private final HeroMapper heroMapper;

    public List<Hero> list() {
        return heroMapper.selectByExample(null);
    }
}
