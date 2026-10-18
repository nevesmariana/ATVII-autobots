package com.autobots.automanager.hateoas;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.EnderecoControle;
import com.autobots.automanager.dto.response.EnderecoResponseDTO;

@Component
public class AdicionadorLinkEndereco implements AdicionadorLink<EnderecoResponseDTO> {

    @Override
    public void adicionarLink(List<EnderecoResponseDTO> lista) {
        for (EnderecoResponseDTO dto : lista) {
            adicionarLinkProprio(dto);
        }
    }

    @Override
    public void adicionarLink(EnderecoResponseDTO objeto) {
        adicionarLinkProprio(objeto);

        objeto.add(linkTo(methodOn(EnderecoControle.class).obterEnderecos())
                .withRel("enderecos"));

        objeto.add(linkTo(methodOn(EnderecoControle.class).atualizarEndereco(objeto.getId(), null))
                .withRel("atualizar"));

        objeto.add(linkTo(methodOn(EnderecoControle.class).excluirEndereco(objeto.getId()))
                .withRel("excluir"));
    }

    private void adicionarLinkProprio(EnderecoResponseDTO dto) {
        dto.add(linkTo(methodOn(EnderecoControle.class).obterEndereco(dto.getId()))
                .withSelfRel());
    }
}