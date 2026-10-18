package com.autobots.automanager.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.dto.request.TelefoneRequestDTO;
import com.autobots.automanager.dto.response.TelefoneResponseDTO;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.excecoes.TelefoneNaoEncontradoException;
import com.autobots.automanager.mapper.TelefoneMapper;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.TelefoneRepositorio;

@Service
public class TelefoneService {

    @Autowired
    private TelefoneRepositorio repositorio;

    @Autowired
    private TelefoneMapper mapper;

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    public TelefoneResponseDTO cadastrarTelefone(
            TelefoneRequestDTO dto) {
        Telefone telefone = mapper.paraEntidade(dto);

        telefone = repositorio.save(telefone);

        return mapper.paraResponseDTO(telefone);
    }

    public List<TelefoneResponseDTO> obterTelefones() {
        List<Telefone> telefones = repositorio.findAll();

        List<TelefoneResponseDTO> dtos = new ArrayList<>();

        for (Telefone telefone : telefones) {
            dtos.add(mapper.paraResponseDTO(telefone));
        }

        return dtos;
    }

    public TelefoneResponseDTO obterTelefone(Long id) {
        Telefone telefone = repositorio.findById(id)
                .orElseThrow(() -> new TelefoneNaoEncontradoException(id));

        return mapper.paraResponseDTO(telefone);
    }

    public TelefoneResponseDTO atualizarTelefone(
            Long id,
            TelefoneRequestDTO dto) {
        Telefone telefone = repositorio.findById(id)
                .orElseThrow(() -> new TelefoneNaoEncontradoException(id));

        mapper.atualizarEntidade(telefone, dto);

        telefone = repositorio.save(telefone);

        return mapper.paraResponseDTO(telefone);
    }

    public void excluirTelefone(Long id) {
        Telefone telefone = repositorio.findById(id)
                .orElseThrow(() -> new TelefoneNaoEncontradoException(id));

        clienteRepositorio.findByTelefones_Id(id)
                .ifPresentOrElse(
                        cliente -> {
                            cliente.getTelefones().remove(telefone);
                            clienteRepositorio.save(cliente);
                        },
                        () -> repositorio.delete(telefone));
    }
}