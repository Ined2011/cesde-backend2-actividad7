package com.example.demo_basic.controller;

import com.example.demo_basic.model.entity.Cliente;
import com.example.demo_basic.service.ClienteService;
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
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "Gestión de clientes")
public class ClienteController {

    @Autowired
    private ClienteService clientesService;

    @Operation(summary = "Obtener todos los clientes")
    @GetMapping
    public List<Cliente> getAll() {
        return clientesService.findAll();
    }

    @Operation(summary = "Obtener un cliente por ID")
    @GetMapping("/{id}")
    public Cliente getById(@PathVariable Long id) {
        return clientesService.findById(id);
    }

    @Operation(summary = "Crear un cliente")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente create(@RequestBody Cliente request) {
        return clientesService.save(request);
    }

    @Operation(summary = "Actualizar un cliente")
    @PutMapping("/{id}")
    public Cliente update(@PathVariable Long id, @RequestBody Cliente request) {
        return clientesService.update(id, request);
    }

    @Operation(summary = "Eliminar un cliente")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        clientesService.delete(id);
    }
}
