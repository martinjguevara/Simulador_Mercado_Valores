package simuladorMercado.agentes;

import simuladorMercado.estrategia.EstrategiaOperacion;
import simuladorMercado.mercado.Mercado;
import simuladorMercado.mercado.Operacion;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Agente implements Runnable {
    private final String nombre;
    private double dineroDisponible;
    private final Map<String, Integer> portafolio;
    private final EstrategiaOperacion estrategia;
    private final Mercado mercado;
    private volatile Operacion ultimaOperacion;

    public Agente(String nombre, double dineroInicial, EstrategiaOperacion estrategia, Mercado mercado) {
        this.nombre = nombre;
        this.dineroDisponible = dineroInicial;
        this.estrategia = estrategia;
        this.mercado = mercado;
        this.portafolio = new HashMap<>();
    }

    public synchronized void comprar(String simbolo, int cantidad, double precioUnitario) {
        if (cantidad <= 0 || precioUnitario <= 0) {
            ultimaOperacion = null;
            return;
        }
        double costo = cantidad * precioUnitario;
        if (dineroDisponible >= costo) { 
            dineroDisponible -= costo; 
            portafolio.merge(simbolo, cantidad, Integer::sum);
            setUltimaOperacion(new Operacion(Operacion.Tipo.COMPRA, simbolo, precioUnitario, cantidad)); // [cite: 64]
        } else {
            setUltimaOperacion(null); 
        }
    }

    public synchronized void vender(String simbolo, int cantidad, double precioUnitario) {
        if (cantidad <= 0 || precioUnitario <= 0) {
            ultimaOperacion = null;
            return;
        }
        int actuales = portafolio.getOrDefault(simbolo, 0); 
        if (actuales >= cantidad) { 
            portafolio.put(simbolo, actuales - cantidad); 
            dineroDisponible += cantidad * precioUnitario; 
            setUltimaOperacion(new Operacion(Operacion.Tipo.VENTA, simbolo, precioUnitario, cantidad)); // [cite: 72]
        } else {
            setUltimaOperacion(null);
        }
    }

    public int getCantidad(String simbolo) {
        return portafolio.getOrDefault(simbolo, 0);
    }

    public Map<String, Integer> getPortafolio() {
        return portafolio;
    }

    public String getNombre() {
        return nombre;
    }

    public double getDineroDisponible() {
        return dineroDisponible;
    }

    public Operacion getUltimaOperacion() {
        return ultimaOperacion;
    }

    public void setUltimaOperacion(Operacion operacion) {
        this.ultimaOperacion = operacion;
    }

    @Override
    public void run() {
        while (mercado.estaActivo()) {
            estrategia.ejecutar(mercado, this);
            try {
                Thread.sleep(1000 + new Random().nextInt(2000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}