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

import com.autobots.automanager.dto.request.DocumentoRequestDTO;
import com.autobots.automanager.dto.response.DocumentoResponseDTO;
import com.autobots.automanager.hateoas.AdicionadorLinkDocumento;
import com.autobots.automanager.service.DocumentoService;

@RestController
@RequestMapping("/documentos")
public class DocumentoControle {

    @Autowired
    private DocumentoService service;

    @Autowired
    private AdicionadorLinkDocumento adicionadorLink;

    @GetMapping
    public ResponseEntity<CollectionModel<DocumentoResponseDTO>> obterDocumentos() {
        List<DocumentoResponseDTO> lista = service.obterDocumentos();
        adicionadorLink.adicionarLink(lista);

        CollectionModel<DocumentoResponseDTO> colecao = CollectionModel.of(lista);
        colecao.add(linkTo(methodOn(DocumentoControle.class).obterDocumentos()).withSelfRel());
        colecao.add(linkTo(methodOn(DocumentoControle.class).cadastrarDocumento(null)).withRel("cadastrar"));

        return ResponseEntity.ok(colecao);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentoResponseDTO> obterDocumento(@PathVariable Long id) {
        DocumentoResponseDTO resposta = service.obterDocumento(id);
        adicionadorLink.adicionarLink(resposta);

        return ResponseEntity.ok(resposta);
    }

    @PostMapping
    public ResponseEntity<DocumentoResponseDTO> cadastrarDocumento(
            @Valid @RequestBody DocumentoRequestDTO dto) {

        DocumentoResponseDTO resposta = service.cadastrarDocumento(dto);
        adicionadorLink.adicionarLink(resposta);

        return ResponseEntity
                .created(linkTo(methodOn(DocumentoControle.class).obterDocumento(resposta.getId())).toUri())
                .body(resposta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentoResponseDTO> atualizarDocumento(
            @PathVariable Long id,
            @Valid @RequestBody DocumentoRequestDTO dto) {

        DocumentoResponseDTO resposta = service.atualizarDocumento(id, dto);
        adicionadorLink.adicionarLink(resposta);

        return ResponseEntity.ok(resposta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirDocumento(@PathVariable Long id) {
        service.excluirDocumento(id);

        return ResponseEntity.noContent().build();
    }
}