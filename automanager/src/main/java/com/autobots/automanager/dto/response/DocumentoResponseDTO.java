package com.autobots.automanager.dto.response;

import org.springframework.hateoas.RepresentationModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class DocumentoResponseDTO extends RepresentationModel<DocumentoResponseDTO> {

    private Long id;
    private String tipo;
    private String numero;
}