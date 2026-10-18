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

import com.autobots.automanager.dto.request.ClienteRequestDTO;
import com.autobots.automanager.dto.response.ClienteResponseDTO;
import com.autobots.automanager.hateoas.AdicionadorLinkCliente;
import com.autobots.automanager.service.ClienteService;

@RestController
@RequestMapping("/clientes")
public class ClienteControle {

    @Autowired
    private ClienteService service;

    @Autowired
    private AdicionadorLinkCliente adicionadorLink;

    @GetMapping
    public ResponseEntity<CollectionModel<ClienteResponseDTO>> obterClientes() {
        List<ClienteResponseDTO> lista = service.obterClientes();
        adicionadorLink.adicionarLink(lista);

        CollectionModel<ClienteResponseDTO> colecao = CollectionModel.of(lista);
        colecao.add(linkTo(methodOn(ClienteControle.class).obterClientes()).withSelfRel());
        colecao.add(linkTo(methodOn(ClienteControle.class).cadastrarCliente(null)).withRel("cadastrar"));

        return ResponseEntity.ok(colecao);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> obterCliente(@PathVariable Long id) {
        ClienteResponseDTO resposta = service.obterCliente(id);
        adicionadorLink.adicionarLink(resposta);

        return ResponseEntity.ok(resposta);
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> cadastrarCliente(
            @Valid @RequestBody ClienteRequestDTO dto) {

        ClienteResponseDTO resposta = service.cadastrarCliente(dto);
        adicionadorLink.adicionarLink(resposta);

        return ResponseEntity
                .created(linkTo(methodOn(ClienteControle.class).obterCliente(resposta.getId())).toUri())
                .body(resposta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizarCliente(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequestDTO dto) {

        ClienteResponseDTO resposta = service.atualizarCliente(id, dto);
        adicionadorLink.adicionarLink(resposta);

        return ResponseEntity.ok(resposta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirCliente(@PathVariable Long id) {
        service.excluirCliente(id);

        return ResponseEntity.noContent().build();
    }
}