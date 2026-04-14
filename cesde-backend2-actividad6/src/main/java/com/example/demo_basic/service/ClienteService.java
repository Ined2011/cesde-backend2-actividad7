package com.example.demo_basic.service;

import com.example.demo_basic.model.entity.Cliente;
import com.example.demo_basic.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    public Cliente findById(Long id) {
        return findEntity(id);
    }

    @Transactional
    public Cliente save(Cliente request) {
        return clienteRepository.save(request);
    }

    @Transactional
    public Cliente update(Long id, Cliente request) {
        Cliente cliente = findEntity(id);
        cliente.setNombre(request.getNombre());
        cliente.setIdentificacion(request.getIdentificacion());
        cliente.setEdad(request.getEdad());
        cliente.setIngresoMensual(request.getIngresoMensual());
        cliente.setTieneGarantia(request.getTieneGarantia());
        cliente.setHistorialCrediticio(request.getHistorialCrediticio());
        return clienteRepository.save(cliente);
    }

    @Transactional
    public void delete(Long id) {
        findEntity(id);
        clienteRepository.deleteById(id);
    }

    public Cliente findEntity(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cliente no encontrado con id: " + id));
    }
}
