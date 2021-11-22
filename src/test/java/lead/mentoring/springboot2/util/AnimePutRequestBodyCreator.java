package lead.mentoring.springboot2.util;

import lead.mentoring.springboot2.requests.AnimePutRequestBody;

public class AnimePutRequestBodyCreator {

    public static AnimePutRequestBody createdAnimePutRequestBody(){
        return AnimePutRequestBody.builder()
                .nome(AnimeCreator.generateValidUpdateAnime().getNome())
                .id(AnimeCreator.generateValidUpdateAnime().getId())
                .build();
    }
}
