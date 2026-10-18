package com.autobots.automanager.hateoas;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.DocumentoControle;
import com.autobots.automanager.dto.response.DocumentoResponseDTO;

@Component
public class AdicionadorLinkDocumento implements AdicionadorLink<DocumentoResponseDTO> {

    @Override
    public void adicionarLink(List<DocumentoResponseDTO> lista) {
        for (DocumentoResponseDTO dto : lista) {
            adicionarLinkProprio(dto);
        }
    }

    @Override
    public void adicionarLink(DocumentoResponseDTO objeto) {
        adicionarLinkProprio(objeto);

        objeto.add(linkTo(methodOn(DocumentoControle.class).obterDocumentos())
                .withRel("documentos"));

        objeto.add(linkTo(methodOn(DocumentoControle.class).atualizarDocumento(objeto.getId(), null))
                .withRel("atualizar"));

        objeto.add(linkTo(methodOn(DocumentoControle.class).excluirDocumento(objeto.getId()))
                .withRel("excluir"));
    }

    private void adicionarLinkProprio(DocumentoResponseDTO dto) {
        dto.add(linkTo(methodOn(DocumentoControle.class).obterDocumento(dto.getId()))
                .withSelfRel());
    }
}