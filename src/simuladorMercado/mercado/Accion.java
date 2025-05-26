package simuladorMercado.mercado;

import java.util.LinkedList;
import java.util.Queue;

public class Accion {
    private final String simbolo;
    private double precioActual;
    private final Queue<Double> historialPrecios;
    private final int maxHistorial = 10;

    public Accion(String simbolo, double precioInicial) {
        this.simbolo = simbolo;
        this.precioActual = precioInicial;
        this.historialPrecios = new LinkedList<>();
        this.historialPrecios.add(precioInicial);
    }

    public String getSimbolo() {
        return simbolo;
    }

    public double getPrecioActual() {
        return precioActual;
    }

    public void actualizarPrecio(double nuevoPrecio) {
        this.precioActual = nuevoPrecio;
        if (historialPrecios.size() >= maxHistorial) {
            historialPrecios.poll();
        }
        historialPrecios.add(nuevoPrecio);
    }

    public Queue<Double> getHistorialPrecios() {
        return historialPrecios;
    }
}
