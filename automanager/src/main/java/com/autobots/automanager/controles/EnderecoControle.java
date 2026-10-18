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

import com.autobots.automanager.dto.request.EnderecoRequestDTO;
import com.autobots.automanager.dto.response.EnderecoResponseDTO;
import com.autobots.automanager.hateoas.AdicionadorLinkEndereco;
import com.autobots.automanager.service.EnderecoService;

@RestController
@RequestMapping("/enderecos")
public class EnderecoControle {

    @Autowired
    private EnderecoService service;

    @Autowired
    private AdicionadorLinkEndereco adicionadorLink;

    @GetMapping
    public ResponseEntity<CollectionModel<EnderecoResponseDTO>> obterEnderecos() {
        List<EnderecoResponseDTO> lista = service.obterEnderecos();
        adicionadorLink.adicionarLink(lista);

        CollectionModel<EnderecoResponseDTO> colecao = CollectionModel.of(lista);
        colecao.add(linkTo(methodOn(EnderecoControle.class).obterEnderecos()).withSelfRel());
        colecao.add(linkTo(methodOn(EnderecoControle.class).cadastrarEndereco(null)).withRel("cadastrar"));

        return ResponseEntity.ok(colecao);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnderecoResponseDTO> obterEndereco(@PathVariable Long id) {
        EnderecoResponseDTO resposta = service.obterEndereco(id);
        adicionadorLink.adicionarLink(resposta);

        return ResponseEntity.ok(resposta);
    }

    @PostMapping
    public ResponseEntity<EnderecoResponseDTO> cadastrarEndereco(
            @Valid @RequestBody EnderecoRequestDTO dto) {

        EnderecoResponseDTO resposta = service.cadastrarEndereco(dto);
        adicionadorLink.adicionarLink(resposta);

        return ResponseEntity
                .created(linkTo(methodOn(EnderecoControle.class).obterEndereco(resposta.getId())).toUri())
                .body(resposta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnderecoResponseDTO> atualizarEndereco(
            @PathVariable Long id,
            @Valid @RequestBody EnderecoRequestDTO dto) {

        EnderecoResponseDTO resposta = service.atualizarEndereco(id, dto);
        adicionadorLink.adicionarLink(resposta);

        return ResponseEntity.ok(resposta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirEndereco(@PathVariable Long id) {
        service.excluirEndereco(id);

        return ResponseEntity.noContent().build();
    }
}