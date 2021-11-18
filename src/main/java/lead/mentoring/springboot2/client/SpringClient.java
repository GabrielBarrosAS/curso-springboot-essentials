package lead.mentoring.springboot2.client;

import lead.mentoring.springboot2.domain.Anime;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Log4j2
public class SpringClient {
    public static void main(String[] args) {
        ResponseEntity<Anime> anime = new RestTemplate().getForEntity("http://localhost:8080/animes/1", Anime.class);

        log.info(anime);

        Anime object = new RestTemplate().getForObject("http://localhost:8080/animes/1", Anime.class);

        log.info(object);

        Anime[] animes = new RestTemplate().getForObject("http://localhost:8080/animes/all", Anime[].class);

        log.info(Arrays.toString(animes));

        ResponseEntity<List<Anime>> exchange = new RestTemplate().exchange("http://localhost:8080/animes/all", HttpMethod.GET,null,
                new ParameterizedTypeReference<List<Anime>>(){});

        log.info(exchange.getBody());

        Anime newAnimeForObject = Anime.builder()
                .nome("newAnimeForObject")
                .build();

        Anime newAnimeSaved = new RestTemplate()
                .postForObject(
                "http://localhost:8080/animes/",
                newAnimeForObject,
                Anime.class);

        log.info(newAnimeSaved);

        Anime newAnimeExchange = Anime.builder()
                .nome("newAnimeExchance")
                .build();

        ResponseEntity<Anime> newAnimeExchangeSaved = new RestTemplate().exchange(
                "http://localhost:8080/animes/",
                HttpMethod.POST,
                new HttpEntity<>(newAnimeExchange), //Vantagem que podemos passar elementos http (cabeçalhos, token etc)
                Anime.class);

        log.info(newAnimeExchangeSaved);
    }
}
