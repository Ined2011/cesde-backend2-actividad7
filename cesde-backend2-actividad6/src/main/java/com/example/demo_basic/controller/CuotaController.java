package com.example.demo_basic.controller;

import com.example.demo_basic.model.entity.Cuota;
import com.example.demo_basic.model.enums.EstadoCuota;
import com.example.demo_basic.service.CuotaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cuotas")
@Tag(name = "Cuotas", description = "Gestión de cuotas")
public class CuotaController {

    @Autowired
    private CuotaService cuotaService;

    @Operation(summary = "Obtener todas las cuotas")
    @GetMapping
    public List<Cuota> getAll() {
        return cuotaService.findAll();
    }

    @Operation(summary = "Obtener una cuota por ID")
    @GetMapping("/{id}")
    public Cuota getById(@PathVariable Long id) {
        return cuotaService.findById(id);
    }

    @Operation(summary = "Filtrar cuotas por estado")
    @GetMapping("/estado/{estado}")
    public List<Cuota> getByEstado(@PathVariable EstadoCuota estado) {
        return cuotaService.findByEstado(estado);
    }

    @Operation(summary = "Obtener cuotas por cliente")
    @GetMapping("/cliente/{clienteId}")
    public List<Cuota> getByCliente(@PathVariable Long clienteId) {
        return cuotaService.findByCliente(clienteId);
    }

    @Operation(summary = "Calcular una cuota", description = "Solo requiere prestamo.id y cliente.id en el body")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cuota create(@RequestBody Cuota request) {
        return cuotaService.crearCuota(request);
    }

    @Operation(summary = "Cambiar estado de una cuota")
    @PatchMapping("/{id}/estado/{estado}")
    public Cuota cambiarEstado(@PathVariable Long id, @PathVariable EstadoCuota estado) {
        return cuotaService.cambiarEstado(id, estado);
    }

    @Operation(summary = "Eliminar una cuota")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        cuotaService.delete(id);
    }
}
