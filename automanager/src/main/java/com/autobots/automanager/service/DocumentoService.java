package com.autobots.automanager.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.dto.request.DocumentoRequestDTO;
import com.autobots.automanager.dto.response.DocumentoResponseDTO;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.excecoes.DocumentoDuplicadoException;
import com.autobots.automanager.excecoes.DocumentoNaoEncontradoException;
import com.autobots.automanager.mapper.DocumentoMapper;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.DocumentoRepositorio;

@Service
public class DocumentoService {

    @Autowired
    private DocumentoRepositorio repositorio;

    @Autowired
    private DocumentoMapper mapper;

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    public DocumentoResponseDTO cadastrarDocumento(
            DocumentoRequestDTO dto) {
        if (repositorio.existsByNumero(dto.getNumero())) {
            throw new DocumentoDuplicadoException(dto.getNumero());
        }

        Documento documento = mapper.paraEntidade(dto);

        documento = repositorio.save(documento);

        return mapper.paraResponseDTO(documento);
    }

    public List<DocumentoResponseDTO> obterDocumentos() {
        List<Documento> documentos = repositorio.findAll();

        List<DocumentoResponseDTO> dtos = new ArrayList<>();

        for (Documento documento : documentos) {
            dtos.add(mapper.paraResponseDTO(documento));
        }

        return dtos;
    }

    public DocumentoResponseDTO obterDocumento(Long id) {
        Documento documento = repositorio.findById(id)
                .orElseThrow(() -> new DocumentoNaoEncontradoException(id));

        return mapper.paraResponseDTO(documento);
    }

    public DocumentoResponseDTO atualizarDocumento(
            Long id,
            DocumentoRequestDTO dto) {

        Documento documento = repositorio.findById(id)
                .orElseThrow(() -> new DocumentoNaoEncontradoException(id));

        if (repositorio.existsByNumeroAndIdNot(dto.getNumero(), id)) {
            throw new DocumentoDuplicadoException(dto.getNumero());
        }

        mapper.atualizarEntidade(documento, dto);

        documento = repositorio.save(documento);

        return mapper.paraResponseDTO(documento);
    }

    public void excluirDocumento(Long id) {
        Documento documento = repositorio.findById(id)
                .orElseThrow(() -> new DocumentoNaoEncontradoException(id));

            clienteRepositorio.findByDocumentos_Id(id)
                .ifPresentOrElse(
                    cliente -> {
                        cliente.getDocumentos().remove(documento);
                            clienteRepositorio.save(cliente);
                    },
                () -> repositorio.delete(documento));
    }
}