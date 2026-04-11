package com.example.demo_basic.repository;

import com.example.demo_basic.model.entity.Cuota;
import com.example.demo_basic.model.enums.EstadoCuota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuotaRepository extends JpaRepository<Cuota, Long> {
    long countByClienteIdAndEstado(Long clienteId, EstadoCuota estado);

    List<Cuota> findByClienteId(Long clienteId);

    List<Cuota> findByEstado(EstadoCuota estado);
}
