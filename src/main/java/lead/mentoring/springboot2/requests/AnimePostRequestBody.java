package lead.mentoring.springboot2.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnimePostRequestBody {
    @NotEmpty(message = "The name cannot be empty post")
    //Indicando que um campo não pode ser vazio
    @NotNull(message = "The name connot be null")
    //não pode ser null
    @Schema(description = "This is the name of the anime that will be created",example = "Anime Name",required = true)
    //Adicionando uma breve descrição do que esse atributo representa e um exemplo na documentação
    private String nome;
}
