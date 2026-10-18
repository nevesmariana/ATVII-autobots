package com.autobots.automanager.hateoas;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.TelefoneControle;
import com.autobots.automanager.dto.response.TelefoneResponseDTO;

@Component
public class AdicionadorLinkTelefone implements AdicionadorLink<TelefoneResponseDTO> {

    @Override
    public void adicionarLink(List<TelefoneResponseDTO> lista) {
        for (TelefoneResponseDTO dto : lista) {
            adicionarLinkProprio(dto);
        }
    }

    @Override
    public void adicionarLink(TelefoneResponseDTO objeto) {
        adicionarLinkProprio(objeto);

        objeto.add(linkTo(methodOn(TelefoneControle.class).obterTelefones())
                .withRel("telefones"));

        objeto.add(linkTo(methodOn(TelefoneControle.class).atualizarTelefone(objeto.getId(), null))
                .withRel("atualizar"));

        objeto.add(linkTo(methodOn(TelefoneControle.class).excluirTelefone(objeto.getId()))
                .withRel("excluir"));
    }

    private void adicionarLinkProprio(TelefoneResponseDTO dto) {
        dto.add(linkTo(methodOn(TelefoneControle.class).obterTelefone(dto.getId()))
                .withSelfRel());
    }
}