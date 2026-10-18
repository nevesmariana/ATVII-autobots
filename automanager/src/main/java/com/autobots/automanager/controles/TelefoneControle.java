package com.autobots.automanager.controles;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.dto.request.TelefoneRequestDTO;
import com.autobots.automanager.dto.response.TelefoneResponseDTO;
import com.autobots.automanager.hateoas.AdicionadorLinkTelefone;
import com.autobots.automanager.service.TelefoneService;

@RestController
@RequestMapping("/telefones")
public class TelefoneControle {

    @Autowired
    private TelefoneService service;

    @Autowired
    private AdicionadorLinkTelefone adicionadorLink;

    @GetMapping
    public ResponseEntity<CollectionModel<TelefoneResponseDTO>> obterTelefones() {
        List<TelefoneResponseDTO> lista = service.obterTelefones();
        adicionadorLink.adicionarLink(lista);

        CollectionModel<TelefoneResponseDTO> colecao = CollectionModel.of(lista);
        colecao.add(linkTo(methodOn(TelefoneControle.class).obterTelefones()).withSelfRel());
        colecao.add(linkTo(methodOn(TelefoneControle.class).cadastrarTelefone(null)).withRel("cadastrar"));

        return ResponseEntity.ok(colecao);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TelefoneResponseDTO> obterTelefone(@PathVariable Long id) {
        TelefoneResponseDTO resposta = service.obterTelefone(id);
        adicionadorLink.adicionarLink(resposta);

        return ResponseEntity.ok(resposta);
    }

    @PostMapping
    public ResponseEntity<TelefoneResponseDTO> cadastrarTelefone(
            @Valid @RequestBody TelefoneRequestDTO dto) {

        TelefoneResponseDTO resposta = service.cadastrarTelefone(dto);
        adicionadorLink.adicionarLink(resposta);

        return ResponseEntity
                .created(linkTo(methodOn(TelefoneControle.class).obterTelefone(resposta.getId())).toUri())
                .body(resposta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TelefoneResponseDTO> atualizarTelefone(
            @PathVariable Long id,
            @Valid @RequestBody TelefoneRequestDTO dto) {

        TelefoneResponseDTO resposta = service.atualizarTelefone(id, dto);
        adicionadorLink.adicionarLink(resposta);

        return ResponseEntity.ok(resposta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirTelefone(@PathVariable Long id) {
        service.excluirTelefone(id);

        return ResponseEntity.noContent().build();
    }
}