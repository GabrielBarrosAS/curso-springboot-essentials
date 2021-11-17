package lead.mentoring.springboot2.handler;

import lead.mentoring.springboot2.exception.BadRequestException;
import lead.mentoring.springboot2.exception.BadRequestExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
//Interceptador de excessões geradas pelos componentes controllers
//Avisar ao controller que aqui tem informações que ele precisa utilizar
public class RestExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    //Dado uma determinada execessão lançada, caso corresponda ao tipo passo, vai executar a função a seguir

    public ResponseEntity<BadRequestExceptionDetails> handlerBadRequestException(BadRequestException badRequestException){
        return new ResponseEntity<>(
                BadRequestExceptionDetails.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .title("Bad Request Exception, Check the documentation")
                        .details(badRequestException.getMessage())
                        .developerMessage(badRequestException.getClass().getName())
                        .build(),HttpStatus.BAD_REQUEST
        );
    }

}
