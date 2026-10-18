package com.autobots.automanager.dto.response;

import java.util.Date;
import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ClienteResponseDTO extends RepresentationModel<ClienteResponseDTO> {

    private Long id;
    private String nome;
    private String nomeSocial;
    private Date dataNascimento;
    private Date dataCadastro;

    private List<DocumentoResponseDTO> documentos;
    private EnderecoResponseDTO endereco;
    private List<TelefoneResponseDTO> telefones;
}