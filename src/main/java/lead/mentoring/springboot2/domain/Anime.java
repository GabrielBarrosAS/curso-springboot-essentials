package lead.mentoring.springboot2.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
    private String nome;

}
