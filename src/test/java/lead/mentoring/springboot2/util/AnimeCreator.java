package lead.mentoring.springboot2.util;

import lead.mentoring.springboot2.domain.Anime;

public class AnimeCreator {

    public static Anime generateAnimeToSave(){
        return Anime.builder().nome("Anime para teste").build();
    }

    public static Anime generateAnimeValid(){
        return Anime.builder()
                .nome("Anime para teste 2")
                .id(1l)
                .build();
    }
}
