package lead.mentoring.springboot2.requests;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class AnimePutRequestBody {
    @Min(value = 1,message = "ID deve ser maior ou igual a 1")
    private long id;
    @NotEmpty(message = "Name cannot empty put")//engloba o null
    @NotNull(message = "Name cannot null")
    private String nome;

}
