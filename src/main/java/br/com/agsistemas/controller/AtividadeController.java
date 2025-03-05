package br.com.agsistemas.controller;

import br.com.agsistemas.controlleradvice.ApiResponseError;
import br.com.agsistemas.dto.in.AlterarAtividadeDTO;
import br.com.agsistemas.dto.out.AtividadeOutDTO;
import br.com.agsistemas.dto.out.ClienteOutDTO;
import br.com.agsistemas.service.AtividadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/atividades")
@RequiredArgsConstructor
public class AtividadeController {

  private final AtividadeService service;

  @Operation(
      summary = "Modifica dados de uma atividade.",
      responses = {
          @ApiResponse(
              description = "Dados modificados com sucesso.",
              responseCode = "200",
              content = {
                  @Content(
                      schema = @Schema(implementation = AtividadeOutDTO.class),
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
              description = "Não foi possível modificar dados da atividade.",
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
  public AtividadeOutDTO alterarAtividade(@RequestBody @Valid AlterarAtividadeDTO atividade) {
    return service.alterar(atividade);
  }
}
