package com.autobots.automanager.dto.response;

import org.springframework.hateoas.RepresentationModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class TelefoneResponseDTO extends RepresentationModel<TelefoneResponseDTO> {

    private Long id;
    private String ddd;
    private String numero;
}