package com.autobots.automanager.hateoas;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ClienteControle;
import com.autobots.automanager.dto.response.ClienteResponseDTO;

@Component
public class AdicionadorLinkCliente implements AdicionadorLink<ClienteResponseDTO> {

    @Autowired
    private AdicionadorLinkDocumento adicionadorLinkDocumento;

    @Autowired
    private AdicionadorLinkEndereco adicionadorLinkEndereco;

    @Autowired
    private AdicionadorLinkTelefone adicionadorLinkTelefone;

    @Override
    public void adicionarLink(List<ClienteResponseDTO> lista) {
        for (ClienteResponseDTO cliente : lista) {
            adicionarLinkProprio(cliente);
            adicionarLinksRelacionados(cliente);
        }
    }

    @Override
    public void adicionarLink(ClienteResponseDTO objeto) {
        adicionarLinkProprio(objeto);
        adicionarLinksRelacionados(objeto);

        objeto.add(linkTo(methodOn(ClienteControle.class).obterClientes())
                .withRel("clientes"));

        objeto.add(linkTo(methodOn(ClienteControle.class).atualizarCliente(objeto.getId(), null))
                .withRel("atualizar"));

        objeto.add(linkTo(methodOn(ClienteControle.class).excluirCliente(objeto.getId()))
                .withRel("excluir"));
    }

    private void adicionarLinkProprio(ClienteResponseDTO cliente) {
        cliente.add(linkTo(methodOn(ClienteControle.class).obterCliente(cliente.getId()))
                .withSelfRel());
    }

    /** Links dos recursos aninhados (documentos, endereço e telefones). */
    private void adicionarLinksRelacionados(ClienteResponseDTO cliente) {
        if (cliente.getDocumentos() != null) {
            adicionadorLinkDocumento.adicionarLink(cliente.getDocumentos());
        }
        if (cliente.getEndereco() != null) {
            adicionadorLinkEndereco.adicionarLink(List.of(cliente.getEndereco()));
        }
        if (cliente.getTelefones() != null) {
            adicionadorLinkTelefone.adicionarLink(cliente.getTelefones());
        }
    }
}