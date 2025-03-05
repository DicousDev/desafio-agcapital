package br.com.agsistemas.controller;

import br.com.agsistemas.controlleradvice.ApiResponseError;
import br.com.agsistemas.dto.in.AdicionarAtividadeDTO;
import br.com.agsistemas.dto.in.AlterarProjetoDTO;
import br.com.agsistemas.dto.in.CadastrarProjetoDTO;
import br.com.agsistemas.dto.in.RemoverAtividadeDTO;
import br.com.agsistemas.dto.in.VincularClienteNoProjetoDTO;
import br.com.agsistemas.dto.out.AtividadeOutDTO;
import br.com.agsistemas.dto.out.ProjetoFetchOutDTO;
import br.com.agsistemas.dto.out.ProjetoOutDTO;
import br.com.agsistemas.service.ProjetoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projetos")
@RequiredArgsConstructor
@Slf4j
public class ProjetoController {

  private final ProjetoService service;

  @Operation(
      summary = "Consulta todos projetos de um cliente.",
      responses = {
          @ApiResponse(
              description = "Projetos consultados com sucesso.",
              responseCode = "200",
              content = {
                  @Content(
                      schema = @Schema(implementation = ProjetoOutDTO.class),
                      mediaType = MediaType.APPLICATION_JSON_VALUE)
              }
          ),
          @ApiResponse(
              description = "Erro no servidor. Consulte alguém responsável pelo sistema.",
              responseCode = "500",
              content = {
                  @Content(
                      schema = @Schema(implementation = ApiResponseError.class),
                      mediaType = MediaType.APPLICATION_JSON_VALUE)
              }
          )
      }
  )
  @GetMapping
  public Page<ProjetoOutDTO> buscarAllPorCliente(@RequestParam("idExternoCliente") UUID idExternoCliente,
      @RequestParam(defaultValue = "0") Integer pagina) {
    return service.buscarTodosPorCliente(idExternoCliente, pagina);
  }

  @Operation(
      summary = "Consulta um projeto especifico.",
      responses = {
          @ApiResponse(
              description = "Projeto consultado com sucesso.",
              responseCode = "200",
              content = {
                  @Content(
                      schema = @Schema(implementation = ProjetoFetchOutDTO.class),
                      mediaType = MediaType.APPLICATION_JSON_VALUE)
              }
          ),
          @ApiResponse(
              description = "Erro no servidor. Consulte alguém responsável pelo sistema.",
              responseCode = "500",
              content = {
                  @Content(
                      schema = @Schema(implementation = ApiResponseError.class),
                      mediaType = MediaType.APPLICATION_JSON_VALUE)
              }
          )
      }
  )
  @GetMapping("/{idExterno}")
  public ProjetoFetchOutDTO buscarByIdExterno(@PathVariable UUID idExterno) {
    return service.buscarByIdExterno(idExterno);
  }

  @Operation(
      summary = "Cadastra um projeto por vez.",
      responses = {
          @ApiResponse(
              description = "Projeto cadastrado com sucesso.",
              responseCode = "201",
              content = {
                  @Content(
                      schema = @Schema(implementation = ProjetoOutDTO.class),
                      mediaType = MediaType.APPLICATION_JSON_VALUE)
              }
          ),
          @ApiResponse(
              description = "Dado informado inválido.",
              responseCode = "400",
              content = {
                  @Content(
                      schema = @Schema(implementation = ApiResponseError.class),
                      mediaType = MediaType.APPLICATION_JSON_VALUE)
              }
          ),
          @ApiResponse(
              description = "Cliente não encontrado.",
              responseCode = "404",
              content = {
                  @Content(
                      schema = @Schema(implementation = ApiResponseError.class),
                      mediaType = MediaType.APPLICATION_JSON_VALUE)
              }
          ),
          @ApiResponse(
              description = "Não foi possível cadastrar projeto.",
              responseCode = "422",
              content = {
                  @Content(
                      schema = @Schema(implementation = ApiResponseError.class),
                      mediaType = MediaType.APPLICATION_JSON_VALUE)
              }
          ),
          @ApiResponse(
              description = "Erro no servidor. Consulte alguém responsável pelo sistema.",
              responseCode = "500",
              content = {
                  @Content(
                      schema = @Schema(implementation = ApiResponseError.class),
                      mediaType = MediaType.APPLICATION_JSON_VALUE)
              }
          )
      }
  )
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ProjetoOutDTO cadastrar(@RequestBody @Valid CadastrarProjetoDTO projeto) {
    ProjetoOutDTO projetoCadastrado = service.cadastrar(projeto);
    log.info("Projeto cadastrado com sucesso.");
    return projetoCadastrado;
  }

  @Operation(
      summary = "Adiciona atividade a um projeto especifico.",
      responses = {
          @ApiResponse(
              description = "Atividade adicionada ao projeto com sucesso.",
              responseCode = "201",
              content = {
                  @Content(
                      schema = @Schema(implementation = AtividadeOutDTO.class),
                      mediaType = MediaType.APPLICATION_JSON_VALUE)
              }
          ),
          @ApiResponse(
              description = "Dado informado inválido.",
              responseCode = "400",
              content = {
                  @Content(
                      schema = @Schema(implementation = ApiResponseError.class),
                      mediaType = MediaType.APPLICATION_JSON_VALUE)
              }
          ),
          @ApiResponse(
              description = "Cliente não encontrado.",
              responseCode = "404",
              content = {
                  @Content(
                      schema = @Schema(implementation = ApiResponseError.class),
                      mediaType = MediaType.APPLICATION_JSON_VALUE)
              }
          ),
          @ApiResponse(
              description = "Não foi possível adicionar atividade ao projeto.",
              responseCode = "422",
              content = {
                  @Content(
                      schema = @Schema(implementation = ApiResponseError.class),
                      mediaType = MediaType.APPLICATION_JSON_VALUE)
              }
          ),
          @ApiResponse(
              description = "Erro no servidor. Consulte alguém responsável pelo sistema.",
              responseCode = "500",
              content = {
                  @Content(
                      schema = @Schema(implementation = ApiResponseError.class),
                      mediaType = MediaType.APPLICATION_JSON_VALUE)
              }
          )
      }
  )
  @PostMapping("/atividades")
  @ResponseStatus(HttpStatus.CREATED)
  public AtividadeOutDTO adicionarAtividade(@RequestBody @Valid AdicionarAtividadeDTO atividade) {
    AtividadeOutDTO atividadeInserida = service.adicionarAtividade(atividade);
    log.info("Atividade [%s] adicionada no projeto [%s] com sucesso.".formatted(atividadeInserida.getIdExterno(), atividade.getProjetoIdExterno()));
    return atividadeInserida;
  }

  @Operation(
      summary = "Remove atividade de um projeto especifico.",
      responses = {
          @ApiResponse(
              description = "Atividade removida do projeto com sucesso.",
              responseCode = "200"
          ),
          @ApiResponse(
              description = "Dado informado inválido.",
              responseCode = "400",
              content = {
                  @Content(
                      schema = @Schema(implementation = ApiResponseError.class),
                      mediaType = MediaType.APPLICATION_JSON_VALUE)
              }
          ),
          @ApiResponse(
              description = "Cliente, projeto ou atividade não encontrado.",
              responseCode = "404",
              content = {
                  @Content(
                      schema = @Schema(implementation = ApiResponseError.class),
                      mediaType = MediaType.APPLICATION_JSON_VALUE)
              }
          ),
          @ApiResponse(
              description = "Não foi possível remover atividade do projeto.",
              responseCode = "422",
              content = {
                  @Content(
                      schema = @Schema(implementation = ApiResponseError.class),
                      mediaType = MediaType.APPLICATION_JSON_VALUE)
              }
          ),
          @ApiResponse(
              description = "Erro no servidor. Consulte alguém responsável pelo sistema.",
              responseCode = "500",
              content = {
                  @Content(
                      schema = @Schema(implementation = ApiResponseError.class),
                      mediaType = MediaType.APPLICATION_JSON_VALUE)
              }
          )
      }
  )
  @DeleteMapping("/atividades")
  public void removerAtividade(@RequestBody @Valid RemoverAtividadeDTO atividade) {
    service.removerAtividade(atividade);
    log.info("Atividade [%s] removida do projeto [%s] com sucesso.".formatted(atividade.getAtividadeIdExterno(), atividade.getProjetoIdExterno()));
  }

  @Operation(
      summary = "Vincula cliente a um projeto.",
      responses = {
          @ApiResponse(
              description = "Cliente vinculado com sucesso.",
              responseCode = "200",
              content = {
                  @Content(
                      schema = @Schema(implementation = ProjetoOutDTO.class),
                      mediaType = MediaType.APPLICATION_JSON_VALUE)
              }
          ),
          @ApiResponse(
              description = "Dado informado inválido.",
              responseCode = "400",
              content = {
                  @Content(
                      schema = @Schema(implementation = ApiResponseError.class),
                      mediaType = MediaType.APPLICATION_JSON_VALUE)
              }
          ),
          @ApiResponse(
              description = "Cliente ou projeto não encontrado.",
              responseCode = "404",
              content = {
                  @Content(
                      schema = @Schema(implementation = ApiResponseError.class),
                      mediaType = MediaType.APPLICATION_JSON_VALUE)
              }
          ),
          @ApiResponse(
              description = "Não foi possível vincular cliente ao projeto.",
              responseCode = "422",
              content = {
                  @Content(
                      schema = @Schema(implementation = ApiResponseError.class),
                      mediaType = MediaType.APPLICATION_JSON_VALUE)
              }
          ),
          @ApiResponse(
              description = "Erro no servidor. Consulte alguém responsável pelo sistema.",
              responseCode = "500",
              content = {
                  @Content(
                      schema = @Schema(implementation = ApiResponseError.class),
                      mediaType = MediaType.APPLICATION_JSON_VALUE)
              }
          )
      }
  )
  @PostMapping("/clientes")
  public void vincularCliente(@RequestBody @Valid VincularClienteNoProjetoDTO cliente) {
    service.vincularCliente(cliente);
    log.info("Cliente [%s] vinculado ao projeto [%s] com sucesso.".formatted(cliente.getClienteIdExterno(), cliente.getProjetoIdExterno()));
  }

  @Operation(
      summary = "Modifica dados de um projeto.",
      responses = {
          @ApiResponse(
              description = "Projeto modificado com sucesso.",
              responseCode = "200",
              content = {
                  @Content(
                      schema = @Schema(implementation = ProjetoOutDTO.class),
                      mediaType = MediaType.APPLICATION_JSON_VALUE)
              }
          ),
          @ApiResponse(
              description = "Dado informado inválido.",
              responseCode = "400",
              content = {
                  @Content(
                      schema = @Schema(implementation = ApiResponseError.class),
                      mediaType = MediaType.APPLICATION_JSON_VALUE)
              }
          ),
          @ApiResponse(
              description = "Projeto não encontrado.",
              responseCode = "404",
              content = {
                  @Content(
                      schema = @Schema(implementation = ApiResponseError.class),
                      mediaType = MediaType.APPLICATION_JSON_VALUE)
              }
          ),
          @ApiResponse(
              description = "Não foi possível modificar os dados do projeto.",
              responseCode = "422",
              content = {
                  @Content(
                      schema = @Schema(implementation = ApiResponseError.class),
                      mediaType = MediaType.APPLICATION_JSON_VALUE)
              }
          ),
          @ApiResponse(
              description = "Erro no servidor. Consulte alguém responsável pelo sistema.",
              responseCode = "500",
              content = {
                  @Content(
                      schema = @Schema(implementation = ApiResponseError.class),
                      mediaType = MediaType.APPLICATION_JSON_VALUE)
              }
          )
      }
  )
  @PatchMapping
  public ProjetoOutDTO alterar(@RequestBody @Valid AlterarProjetoDTO projeto) {
    ProjetoOutDTO projetoAlterado = service.alterar(projeto);
    log.info("Projeto [%s] atualizado com sucesso.".formatted(projeto.getIdExternoProjeto()));
    return projetoAlterado;
  }
}
