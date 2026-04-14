package com.example.demo_basic.service;

import com.example.demo_basic.model.entity.Cliente;
import com.example.demo_basic.model.entity.Prestamo;
import com.example.demo_basic.model.entity.Cuota;
import com.example.demo_basic.model.enums.EstadoCuota;
import com.example.demo_basic.repository.CuotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CuotaService {

    @Autowired
    private CuotaRepository cuotaRepository;

    @Autowired
    private PrestamoService prestamoService;

    @Autowired
    private ClienteService clienteService;

    public List<Cuota> findAll() {
        return cuotaRepository.findAll();
    }

    public Cuota findById(Long id) {
        return findEntity(id);
    }

    public List<Cuota> findByEstado(EstadoCuota estado) {
        return cuotaRepository.findByEstado(estado);
    }

    public List<Cuota> findByCliente(Long clienteId) {
        return cuotaRepository.findByClienteId(clienteId);
    }

    @Transactional
    public Cuota crearCuota(Cuota request) {
        if (request.getPrestamo() == null || request.getPrestamo().getId() == null) {
            throw new IllegalArgumentException("El id del préstamo es obligatorio para crear una cuota.");
        }

        Prestamo prestamo = prestamoService.findEntity(request.getPrestamo().getId());
        Cliente cliente = request.getCliente() != null
                ? clienteService.findEntity(request.getCliente().getId())
                : prestamo.getCliente();

        request.setPrestamo(prestamo);
        request.setCliente(cliente);
        request.setEstadoPago(EstadoCuota.PENDIENTE);

        return cuotaRepository.save(request);
    }

    @Transactional
    public Cuota cambiarEstado(Long id, EstadoCuota nuevoEstado) {
        Cuota cuota = findEntity(id);
        cuota.setEstadoPago(nuevoEstado);
        return cuotaRepository.save(cuota);
    }

    @Transactional
    public void delete(Long id) {
        findEntity(id);
        cuotaRepository.deleteById(id);
    }

    private Cuota findEntity(Long id) {
        return cuotaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cuota no encontrada con id: " + id));
    }
}
