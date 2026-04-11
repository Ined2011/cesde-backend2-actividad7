package com.example.demo_basic.controller;

import com.example.demo_basic.model.entity.Prestamo;
import com.example.demo_basic.service.PrestamoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
@Tag(name = "Prestamos", description = "Gestión de prestamos")
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;

    @Operation(summary = "Obtener todos los prestamos")
    @GetMapping
    public List<Prestamo> getAll() {
        return prestamoService.findAll();
    }

    @Operation(summary = "Obtener un prestamo por ID")
    @GetMapping("/{id}")
    public Prestamo getById(@PathVariable Long id) {
        return prestamoService.findById(id);
    }

    @Operation(summary = "Crear un prestamo")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Prestamo create(@RequestBody Prestamo request) {
        return prestamoService.save(request);
    }

    @Operation(summary = "Actualizar un prestamo")
    @PutMapping("/{id}")
    public Prestamo update(@PathVariable Long id, @RequestBody Prestamo request) {
        return prestamoService.update(id, request);
    }

    @Operation(summary = "Eliminar un prestamo")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        prestamoService.delete(id);
    }
}
