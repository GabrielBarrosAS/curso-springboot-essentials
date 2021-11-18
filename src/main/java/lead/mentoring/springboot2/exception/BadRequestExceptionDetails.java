package lead.mentoring.springboot2.exception;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
//Geração de códigos automátiocos com somente getters
@SuperBuilder
//Usar o build da super classe
public class BadRequestExceptionDetails extends ExceptionDetails{

}
