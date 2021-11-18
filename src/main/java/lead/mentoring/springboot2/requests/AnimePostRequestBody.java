package lead.mentoring.springboot2.requests;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class AnimePostRequestBody {
    @NotEmpty(message = "The name cannot be empty")
    //Indicando que um campo não pode ser vazio
    @NotNull(message = "The name connot be null")
    //não pode ser null
    private String nome;
}
