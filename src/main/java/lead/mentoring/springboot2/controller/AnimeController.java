package lead.mentoring.springboot2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lead.mentoring.springboot2.domain.Anime;
import lead.mentoring.springboot2.requests.AnimePostRequestBody;
import lead.mentoring.springboot2.requests.AnimePutRequestBody;
import lead.mentoring.springboot2.service.AnimeService;
import lead.mentoring.springboot2.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    @Operation(summary = "List all animes pageable", description = "Default return page 0 with 5 animes",
            tags = "Anime List All")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Successful operation")
    })
    public ResponseEntity<Page<Anime>> listAll(@ParameterObject Pageable page){
        log.info(dateUtil.formaLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        //Padrão do page é size = 20
        return new ResponseEntity<>(animeService.listAll(page), HttpStatus.OK);
    }

    @GetMapping(path = "/all")
    @Operation(summary = "List all animes no pageable", description = "Return array with all animes",
            tags = "Anime List All")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Successful operation")
    })
    public ResponseEntity<List<Anime>> listAllNoPageable(){
        log.info(dateUtil.formaLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return new ResponseEntity<>(animeService.listAllNoPageable(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Successful operation"),
            @ApiResponse(responseCode = "400",description = "Anime not found")
    })
    public ResponseEntity<Anime> findById(@PathVariable long id){
        log.info(dateUtil.formaLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return new ResponseEntity(animeService.findByIdOrThrowBadRequestException(id), HttpStatus.OK);
    }

    @GetMapping(path = "/admin/authenticationPrincipal/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Successful operation"),
            @ApiResponse(responseCode = "400",description = "Anime not found"),
            @ApiResponse(responseCode = "401",description = "User not authenticated"),
            @ApiResponse(responseCode = "403",description = "User unauthorized"),
    })
    public ResponseEntity<Anime> findByIdAuthenticationPrincipal(@PathVariable long id,
                                                                 @AuthenticationPrincipal UserDetails userDetails){
        //@AuthenticationPrincipal recebendo os dados do usuário que vem da requisição
        log.info(dateUtil.formaLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        log.info(userDetails);
        return new ResponseEntity(animeService.findByIdOrThrowBadRequestException(id), HttpStatus.OK);
    }

    @GetMapping(path = "/param")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Successful operation"),
    })
    public ResponseEntity<List<Anime>> findByNameParam(@RequestParam(required = true) String nome){
        log.info(dateUtil.formaLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        return new ResponseEntity(animeService.findByNome(nome), HttpStatus.OK);
    }

    @PostMapping(path = "/admin")
    @Operation(summary = "Create anime when sucessfull", description = "Return new anime created",
            tags = "Anime CRUD")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",description = "Successful operation"),
            @ApiResponse(responseCode = "401",description = "User not authenticated"),
            @ApiResponse(responseCode = "403",description = "User unauthorized"),
    })
    //Antes de executar o método realiza a verificação do nível de acesso do usuário, nesse caso tem que ser ADMIN
    //Será habilitado nas configurações de segurança com @Enable
    //@Valid -> indicando ao spring que precisa usar a validação de campos
    public  ResponseEntity<Anime> save(@RequestBody @Valid AnimePostRequestBody animePostRequestBody){
        log.info(dateUtil.formaLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        log.info(animePostRequestBody);
        return new ResponseEntity(animeService.save(animePostRequestBody),HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/admin/{id}")
    @Operation(summary = "Delete one anime when successfully", description = "Return empty",
            tags = "Anime CRUD")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",description = "Successful operation"),
            @ApiResponse(responseCode = "401",description = "User not authenticated"),
            @ApiResponse(responseCode = "403",description = "User unauthorized"),
    })
    public ResponseEntity<Void> delete(@PathVariable long id){
        log.info(dateUtil.formaLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        animeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(path = "/admin")
    @Operation(summary = "Replace one anime when successfully", description = "Return empty",
            tags = "Anime CRUD")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",description = "Successful operation"),
            @ApiResponse(responseCode = "401",description = "User not authenticated"),
            @ApiResponse(responseCode = "403",description = "User unauthorized"),
    })
    public ResponseEntity<Void> replace(@RequestBody @Valid AnimePutRequestBody animePutRequestBody){
        log.info(dateUtil.formaLocalDateTimeToDatabaseStyle(LocalDateTime.now()));
        animeService.replace(animePutRequestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
