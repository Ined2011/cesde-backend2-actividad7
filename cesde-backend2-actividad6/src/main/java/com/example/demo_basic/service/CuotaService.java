package com.example.demo_basic.service;

import com.example.demo_basic.model.entity.Cliente;
import com.example.demo_basic.model.entity.Prestamo;
import com.example.demo_basic.model.entity.Cuota;
import com.example.demo_basic.model.enums.EstadoPrestamo;
import com.example.demo_basic.model.enums.EstadoCuota;
import com.example.demo_basic.model.enums.CapacidadPrestamo;
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
    private PrestamoService mascotaService;

    @Autowired
    private ClienteService adoptanteService;

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
        Prestamo prestamo = mascotaService.findEntity(request.getMascota().getId());
        Cliente cliente = adoptanteService.findEntity(request.getCliente().getId());

        validarMascotaDisponible(prestamo);
        validarMayorEdad(cliente);
        validarMaximoSolicitudesActivas(cliente);
        validarCapacidadEndeudamiento(prestamo, cliente);

        request.setMascota(prestamo);
        request.setCliente(cliente);
        request.setEstado(EstadoCuota.PENDIENTE);

        prestamo.setEstado(EstadoPrestamo.EN_PROCESO);
        mascotaService.save(prestamo);

        return cuotaRepository.save(request);
    }

    @Transactional
    public Cuota cambiarEstado(Long id, EstadoCuota nuevoEstado) {
        Cuota cuota = findEntity(id);
        Prestamo mascota = mascotaService.findEntity(cuota.getMascota().getId());

        cuota.setEstado(nuevoEstado);

        if (nuevoEstado == EstadoCuota.APROBADA) {
            mascota.setEstado(EstadoPrestamo.ADOPTADO);
            mascotaService.save(mascota);
        }

        if (nuevoEstado == EstadoCuota.RECHAZADA) {
            mascota.setEstado(EstadoPrestamo.DISPONIBLE);
            mascotaService.save(mascota);
        }

        return cuotaRepository.save(cuota);
    }

    @Transactional
    public void delete(Long id) {
        findEntity(id);
        cuotaRepository.deleteById(id);
    }

    private void validarMascotaDisponible(Prestamo mascota) {
        if (mascota.getEstado() != EstadoPrestamo.DISPONIBLE) {
            throw new IllegalArgumentException("La mascota no está disponible para adopción.");
        }
    }

    private void validarMayorEdad(Cliente cliente) {
        if (cliente.getEdad() == null || cliente.getEdad() <= 18) {
            throw new IllegalArgumentException("El cliente debe ser mayor de 18 años.");
        }
    }

    private void validarMaximoSolicitudesActivas(Cliente cliente) {
        long activas = cuotaRepository.countByClienteIdAndEstado(cliente.getId(), EstadoCuota.PENDIENTE);
        if (activas >= 2) {
            throw new IllegalArgumentException("El cliente ya tiene 2 cuotas pendientes.");
        }
    }

    private void validarCapacidadEndeudamiento(Prestamo prestamo, Cliente cliente) {
        if (prestamo.getTamano() == CapacidadPrestamo.GRANDE && !Boolean.TRUE.equals(cliente.getCapacidadDeEndeudamiento())) {
            throw new IllegalArgumentException("Para adoptar una mascota grande el cliente debe tener capacidad de endeudamiento.");
        }
    }

    private Cuota findEntity(Long id) {
        return cuotaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cuota no encontrada con id: " + id));
    }
}
