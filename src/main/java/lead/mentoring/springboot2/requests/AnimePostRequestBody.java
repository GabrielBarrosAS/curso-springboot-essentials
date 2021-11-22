package lead.mentoring.springboot2.requests;

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
    @NotEmpty(message = "The name cannot be empty")
    //Indicando que um campo não pode ser vazio
    @NotNull(message = "The name connot be null")
    //não pode ser null
    private String nome;
}
