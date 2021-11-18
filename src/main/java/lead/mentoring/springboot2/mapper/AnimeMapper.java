package lead.mentoring.springboot2.mapper;

import lead.mentoring.springboot2.domain.Anime;
import lead.mentoring.springboot2.requests.AnimePostRequestBody;
import lead.mentoring.springboot2.requests.AnimePutRequestBody;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring") // -> injeção de depenência caso precise
//permite definir qual atributo da classe sera mapeado para outro com nome diferente se diferente
public abstract class AnimeMapper {
    public static final AnimeMapper INSTANCE = Mappers.getMapper(AnimeMapper.class);
    //instância para chamar os métodos de conversão, tbm pode ser feito por injeção de dependencia

    public abstract Anime toAnime(AnimePostRequestBody animePostRequestBody);

    public abstract Anime toAnime(AnimePutRequestBody animePutRequestBody);
    //conversão de valores que o nome é igual para um obj anime
}
