package lead.mentoring.springboot2.controller;

import lead.mentoring.springboot2.domain.Anime;
import lead.mentoring.springboot2.util.DateUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("anime")
@Log4j2
public class AnimeController {
    private DateUtil dateUtil;

    public AnimeController(DateUtil dateUtil) {
        this.dateUtil = dateUtil;
    }

    @GetMapping(path = "list")
    public List<Anime> list(){
        log.info(dateUtil.formaLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return List.of(new Anime("DBZZZZZZZZ"),new Anime("Segundo Anime"));
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
