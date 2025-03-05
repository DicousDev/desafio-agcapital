package br.com.agsistemas.controller;

import br.com.agsistemas.controlleradvice.ApiResponseError;
import br.com.agsistemas.dto.in.CadastrarClienteDTO;
import br.com.agsistemas.dto.out.ClienteOutDTO;
import br.com.agsistemas.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
@Slf4j
public class ClienteController {

  private final ClienteService service;

  @Operation(
      summary = "Consulta todos clientes.",
      responses = {
          @ApiResponse(
              description = "Clientes consultados com sucesso.",
              responseCode = "200",
              content = {
                  @Content(
                      schema = @Schema(implementation = ClienteOutDTO.class),
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
  public Collection<ClienteOutDTO> buscarAll() {
    return service.buscarAll();
  }

  @Operation(
      summary = "Cadastra cliente.",
      responses = {
          @ApiResponse(
              description = "Cliente cadastrado com sucesso.",
              responseCode = "201",
              content = {
                  @Content(
                      schema = @Schema(implementation = ClienteOutDTO.class),
                      mediaType = MediaType.APPLICATION_JSON_VALUE)
              }
          ),
          @ApiResponse(
              description = "Dados enviados inválidos.",
              responseCode = "400",
              content = {
                  @Content(
                      schema = @Schema(implementation = ApiResponseError.class),
                      mediaType = MediaType.APPLICATION_JSON_VALUE)
              }
          ),
          @ApiResponse(
              description = "Não foi possível cadastrar cliente.",
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
  public ClienteOutDTO cadastrar(@RequestBody @Valid CadastrarClienteDTO cliente) {
    ClienteOutDTO resultado = service.cadastrar(cliente);
    log.info("Cliente cadastrado com sucesso.");
    return resultado;
  }
}
