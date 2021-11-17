package lead.mentoring.springboot2.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
//Geração de códigos automátiocos com lombok
@Builder
//Instanciar objetos pela por uma função de build
public class BadRequestExceptionDetails {

    private String title;
    private int status;
    private String details;
    private String developerMessage;
    private LocalDateTime timestamp;

}
