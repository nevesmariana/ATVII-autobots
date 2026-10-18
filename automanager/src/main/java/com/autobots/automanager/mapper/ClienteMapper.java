package com.autobots.automanager.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.autobots.automanager.dto.request.ClienteRequestDTO;
import com.autobots.automanager.dto.request.DocumentoRequestDTO;
import com.autobots.automanager.dto.request.EnderecoRequestDTO;
import com.autobots.automanager.dto.request.TelefoneRequestDTO;

import com.autobots.automanager.dto.response.ClienteResponseDTO;
import com.autobots.automanager.dto.response.DocumentoResponseDTO;
import com.autobots.automanager.dto.response.EnderecoResponseDTO;
import com.autobots.automanager.dto.response.TelefoneResponseDTO;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Telefone;

@Component
public class ClienteMapper {

    public Cliente paraEntidade(ClienteRequestDTO dto) {

        Cliente cliente = new Cliente();

        cliente.setNome(dto.getNome());
        cliente.setNomeSocial(dto.getNomeSocial());
        cliente.setDataNascimento(dto.getDataNascimento());
        cliente.setDataCadastro(dto.getDataCadastro());

        if (dto.getDocumentos() != null) {

            for (DocumentoRequestDTO dtoDocumento : dto.getDocumentos()) {

                Documento documento = new Documento();

                documento.setTipo(dtoDocumento.getTipo());
                documento.setNumero(dtoDocumento.getNumero());

                cliente.getDocumentos().add(documento);
            }
        }

        if (dto.getEndereco() != null) {

            EnderecoRequestDTO dtoEndereco = dto.getEndereco();

            Endereco endereco = new Endereco();

            endereco.setEstado(dtoEndereco.getEstado());
            endereco.setCidade(dtoEndereco.getCidade());
            endereco.setBairro(dtoEndereco.getBairro());
            endereco.setRua(dtoEndereco.getRua());
            endereco.setNumero(dtoEndereco.getNumero());
            endereco.setCodigoPostal(dtoEndereco.getCodigoPostal());
            endereco.setInformacoesAdicionais(
                dtoEndereco.getInformacoesAdicionais()
            );

            cliente.setEndereco(endereco);
        }

        if (dto.getTelefones() != null) {

            for (TelefoneRequestDTO dtoTelefone : dto.getTelefones()) {

                Telefone telefone = new Telefone();

                telefone.setDdd(dtoTelefone.getDdd());
                telefone.setNumero(dtoTelefone.getNumero());

                cliente.getTelefones().add(telefone);
            }
        }

        return cliente;
    }

    public ClienteResponseDTO paraResponseDTO(Cliente cliente) {

        ClienteResponseDTO dto = new ClienteResponseDTO();

        dto.setId(cliente.getId());
        dto.setNome(cliente.getNome());
        dto.setNomeSocial(cliente.getNomeSocial());
        dto.setDataNascimento(cliente.getDataNascimento());
        dto.setDataCadastro(cliente.getDataCadastro());

        if (cliente.getDocumentos() != null) {

            List<DocumentoResponseDTO> documentos = new ArrayList<>();

            for (Documento documento : cliente.getDocumentos()) {

                DocumentoResponseDTO dtoDocumento = new DocumentoResponseDTO();

                dtoDocumento.setId(documento.getId());
                dtoDocumento.setTipo(documento.getTipo());
                dtoDocumento.setNumero(documento.getNumero());

                documentos.add(dtoDocumento);
            }

            dto.setDocumentos(documentos);
        }

        if (cliente.getEndereco() != null) {

            Endereco endereco = cliente.getEndereco();

            EnderecoResponseDTO dtoEndereco = new EnderecoResponseDTO();

            dtoEndereco.setId(endereco.getId());
            dtoEndereco.setEstado(endereco.getEstado());
            dtoEndereco.setCidade(endereco.getCidade());
            dtoEndereco.setBairro(endereco.getBairro());
            dtoEndereco.setRua(endereco.getRua());
            dtoEndereco.setNumero(endereco.getNumero());
            dtoEndereco.setCodigoPostal(endereco.getCodigoPostal());
            dtoEndereco.setInformacoesAdicionais(
                endereco.getInformacoesAdicionais()
            );

            dto.setEndereco(dtoEndereco);
        }

        if (cliente.getTelefones() != null) {

            List<TelefoneResponseDTO> telefones = new ArrayList<>();

            for (Telefone telefone : cliente.getTelefones()) {

                TelefoneResponseDTO dtoTelefone = new TelefoneResponseDTO();

                dtoTelefone.setId(telefone.getId());
                dtoTelefone.setDdd(telefone.getDdd());
                dtoTelefone.setNumero(telefone.getNumero());

                telefones.add(dtoTelefone);
            }

            dto.setTelefones(telefones);
        }

        return dto;
    }

    public void atualizarEntidade(Cliente cliente, ClienteRequestDTO dto) {

        cliente.setNome(dto.getNome());
        cliente.setNomeSocial(dto.getNomeSocial());
        cliente.setDataNascimento(dto.getDataNascimento());
        cliente.setDataCadastro(dto.getDataCadastro());

        if (dto.getDocumentos() != null) {

            List<String> numerosRecebidos = new ArrayList<>();

            for (DocumentoRequestDTO dtoDocumento : dto.getDocumentos()) {

                numerosRecebidos.add(dtoDocumento.getNumero());

                // procura um documento que o cliente já tem com esse número
                Documento existente = null;
                for (Documento atual : cliente.getDocumentos()) {
                    if (atual.getNumero().equals(dtoDocumento.getNumero())) {
                        existente = atual;
                        break;
                    }
                }

                if (existente != null) {
                    // já existe: só atualiza o tipo, sem inserir de novo
                    existente.setTipo(dtoDocumento.getTipo());
                } else {
                    Documento documento = new Documento();

                    documento.setTipo(dtoDocumento.getTipo());
                    documento.setNumero(dtoDocumento.getNumero());

                    cliente.getDocumentos().add(documento);
                }
            }

            // remove os documentos que não vieram na atualização
            cliente.getDocumentos().removeIf(
                    documento -> !numerosRecebidos.contains(documento.getNumero()));
        }

        if (dto.getEndereco() != null) {

            EnderecoRequestDTO dtoEndereco = dto.getEndereco();

            Endereco endereco = cliente.getEndereco();
            if (endereco == null) {
                endereco = new Endereco();
                cliente.setEndereco(endereco);
            }

            endereco.setEstado(dtoEndereco.getEstado());
            endereco.setCidade(dtoEndereco.getCidade());
            endereco.setBairro(dtoEndereco.getBairro());
            endereco.setRua(dtoEndereco.getRua());
            endereco.setNumero(dtoEndereco.getNumero());
            endereco.setCodigoPostal(dtoEndereco.getCodigoPostal());
            endereco.setInformacoesAdicionais(
                dtoEndereco.getInformacoesAdicionais()
            );
        }

        if (dto.getTelefones() != null) {

            cliente.getTelefones().clear();

            for (TelefoneRequestDTO dtoTelefone : dto.getTelefones()) {

                Telefone telefone = new Telefone();

                telefone.setDdd(dtoTelefone.getDdd());
                telefone.setNumero(dtoTelefone.getNumero());

                cliente.getTelefones().add(telefone);
            }
        }
    }
}