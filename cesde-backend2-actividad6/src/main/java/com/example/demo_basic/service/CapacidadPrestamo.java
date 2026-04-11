package com.example.demo_basic.service;

public class CapacidadPrestamo {

    public static boolean evaluarCapacidadPrestamo(double ingresosMensuales, double montoPrestamo, int plazoMeses) {
        double cuotaMensual = montoPrestamo / plazoMeses;
        double porcentajeEndeudamiento = (cuotaMensual / ingresosMensuales) * 100;

        // Se considera que el cliente tiene capacidad de pago si el porcentaje de endeudamiento es menor al 30%
        return porcentajeEndeudamiento < 30;
    }
}
