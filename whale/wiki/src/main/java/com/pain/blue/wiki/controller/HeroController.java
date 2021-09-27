package com.pain.blue.wiki.controller;

import com.pain.blue.wiki.domain.pojo.Hero;
import com.pain.blue.wiki.service.HeroService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/heroes")
@RestController
public class HeroController {

    private final HeroService heroService;

    @GetMapping("")
    public List<Hero> list() {
        return heroService.list();
    }
}
