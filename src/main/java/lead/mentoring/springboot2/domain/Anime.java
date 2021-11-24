package lead.mentoring.springboot2.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
//Mapeamento de atributos e objetos para tabelas e dados
@Builder
//Lombok -> permite instanciar objetos de uma maneira mais organizada
public class Anime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "The name cannot be empty domain")
    private String nome;

}
