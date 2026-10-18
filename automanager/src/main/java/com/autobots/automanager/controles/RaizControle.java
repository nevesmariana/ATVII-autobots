package com.autobots.automanager.controles;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RaizControle {

    public static class Raiz extends RepresentationModel<Raiz> {
    }

    @GetMapping("/")
    public ResponseEntity<Raiz> raiz() {
        Raiz raiz = new Raiz();

        raiz.add(linkTo(methodOn(RaizControle.class).raiz()).withSelfRel());
        raiz.add(linkTo(methodOn(ClienteControle.class).obterClientes()).withRel("clientes"));
        raiz.add(linkTo(methodOn(DocumentoControle.class).obterDocumentos()).withRel("documentos"));
        raiz.add(linkTo(methodOn(EnderecoControle.class).obterEnderecos()).withRel("enderecos"));
        raiz.add(linkTo(methodOn(TelefoneControle.class).obterTelefones()).withRel("telefones"));

        return ResponseEntity.ok(raiz);
    }
}