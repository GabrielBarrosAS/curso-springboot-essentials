package lead.mentoring.springboot2.client;

import lead.mentoring.springboot2.domain.Anime;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Log4j2
public class SpringClient {
    public static void main(String[] args) {
        ResponseEntity<Anime> anime = new RestTemplate().getForEntity("http://localhost:8080/animes", Anime.class);

        log.info(anime);

        Anime object = new RestTemplate().getForObject("http://localhost:8080/animes/1", Anime.class);

        log.info(object);
    }
}
