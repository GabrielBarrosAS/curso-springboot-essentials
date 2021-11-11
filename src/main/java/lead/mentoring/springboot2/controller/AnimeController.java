package lead.mentoring.springboot2.controller;

import lead.mentoring.springboot2.domain.Anime;
import lead.mentoring.springboot2.service.AnimeService;
import lead.mentoring.springboot2.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("animes")
@Log4j2
@RequiredArgsConstructor
public class AnimeController {
    private final DateUtil dateUtil;
    private final AnimeService animeService;

    @GetMapping
    public List<Anime> list(){
        log.info(dateUtil.formaLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return animeService.listAll();
    }

    @GetMapping(path = "olamundo")
    public String hello(){
        return "Ola Mundo";
    }

    @GetMapping(path = "olamundo2")
    public String hello2(){
        return "Ola Mundooooo";
    }
}
