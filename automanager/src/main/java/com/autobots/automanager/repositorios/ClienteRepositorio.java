package com.autobots.automanager.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autobots.automanager.entidades.Cliente;

public interface ClienteRepositorio extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByDocumentos_Id(Long documentoId);

    Optional<Cliente> findByTelefones_Id(Long telefoneId);

    Optional<Cliente> findByEndereco_Id(Long enderecoId);
}