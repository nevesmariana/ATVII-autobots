package com.autobots.automanager.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.dto.request.EnderecoRequestDTO;
import com.autobots.automanager.dto.response.EnderecoResponseDTO;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.excecoes.EnderecoNaoEncontradoException;
import com.autobots.automanager.mapper.EnderecoMapper;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.EnderecoRepositorio;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepositorio repositorio;

    @Autowired
    private EnderecoMapper mapper;

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    public EnderecoResponseDTO cadastrarEndereco(
            EnderecoRequestDTO dto) {
        Endereco endereco = mapper.paraEntidade(dto);

        endereco = repositorio.save(endereco);

        return mapper.paraResponseDTO(endereco);
    }

    public List<EnderecoResponseDTO> obterEnderecos() {
        List<Endereco> enderecos = repositorio.findAll();

        List<EnderecoResponseDTO> dtos = new ArrayList<>();

        for (Endereco endereco : enderecos) {
            dtos.add(mapper.paraResponseDTO(endereco));
        }

        return dtos;
    }

    public EnderecoResponseDTO obterEndereco(Long id) {
        Endereco endereco = repositorio.findById(id)
                .orElseThrow(() -> new EnderecoNaoEncontradoException(id));

        return mapper.paraResponseDTO(endereco);
    }

    public EnderecoResponseDTO atualizarEndereco(
            Long id,
            EnderecoRequestDTO dto) {
        Endereco endereco = repositorio.findById(id)
                .orElseThrow(() -> new EnderecoNaoEncontradoException(id));

        mapper.atualizarEntidade(endereco, dto);

        endereco = repositorio.save(endereco);

        return mapper.paraResponseDTO(endereco);
    }

    public void excluirEndereco(Long id) {
        Endereco endereco = repositorio.findById(id)
                .orElseThrow(() -> new EnderecoNaoEncontradoException(id));

        clienteRepositorio.findByEndereco_Id(id)
                .ifPresentOrElse(
                        cliente -> {
                            cliente.setEndereco(null);
                            clienteRepositorio.save(cliente);
                        },
                        () -> repositorio.delete(endereco));
    }
}