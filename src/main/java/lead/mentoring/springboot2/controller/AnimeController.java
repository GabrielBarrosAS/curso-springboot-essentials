package lead.mentoring.springboot2.controller;

import lead.mentoring.springboot2.domain.Anime;
import lead.mentoring.springboot2.requests.AnimePostRequestBody;
import lead.mentoring.springboot2.requests.AnimePutRequestBody;
import lead.mentoring.springboot2.service.AnimeService;
import lead.mentoring.springboot2.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("animes")
@Log4j2
@RequiredArgsConstructor
public class AnimeController {
    private final DateUtil dateUtil;
    private final AnimeService animeService;

    @GetMapping
    public ResponseEntity<Page<Anime>> listAll(Pageable page){
        log.info(dateUtil.formaLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        //Padrão do page é size = 20
        return new ResponseEntity<>(animeService.listAll(page), HttpStatus.OK);
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<Anime>> listAllNoPageable(){
        log.info(dateUtil.formaLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return new ResponseEntity<>(animeService.listAllNoPageable(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Anime> findById(@PathVariable long id){
        log.info(dateUtil.formaLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return new ResponseEntity(animeService.findByIdOrThrowBadRequestException(id), HttpStatus.OK);
    }

    @GetMapping(path = "/param")
    public ResponseEntity<List<Anime>> findByNameParam(@RequestParam(required = true) String nome){
        log.info(dateUtil.formaLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return new ResponseEntity(animeService.findByNome(nome), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    //Antes de executar o método realiza a verificação do nível de acesso do usuário, nesse caso tem que ser ADMIN
    //Será habilitado nas configurações de segurança com @Enable
    //@Valid -> indicando ao spring que precisa usar a validação de campos
    public  ResponseEntity<Anime> save(@RequestBody @Valid AnimePostRequestBody animePostRequestBody){
        log.info(dateUtil.formaLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return new ResponseEntity(animeService.save(animePostRequestBody),HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable long id){
        log.info(dateUtil.formaLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        animeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> replace(@RequestBody @Valid AnimePutRequestBody animePutRequestBody){
        log.info(dateUtil.formaLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        animeService.replace(animePutRequestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
