package com.example.demo_basic.service;

import com.example.demo_basic.model.entity.Prestamo;
import com.example.demo_basic.repository.PrestamoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PrestamoService {

    @Autowired
    private PrestamoRepository prestamoRepository;

    public List<Prestamo> findAll() {
        return prestamoRepository.findAll();
    }

    public Prestamo findById(Long id) {
        return findEntity(id);
    }

    @Transactional
    public Prestamo save(Prestamo request) {
        return prestamoRepository.save(request);
    }

    @Transactional
    public Prestamo update(Long id, Prestamo request) {
        Prestamo prestamo = findEntity(id);
        prestamo.setNombre(request.getNombre());
        prestamo.setEspecie(request.getEspecie());
        prestamo.setEdad(request.getEdad());
        prestamo.setTamano(request.getTamano());
        prestamo.setEstado(request.getEstado());
        return prestamoRepository.save(prestamo);
    }

    @Transactional
    public void delete(Long id) {
        findEntity(id);
        prestamoRepository.deleteById(id);
    }

    public Prestamo findEntity(Long id) {
        return prestamoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Préstamo no aprobado con id: " + id));
    }
}
