package simuladorMercado.agentes;

import simuladorMercado.mercado.Mercado;
import simuladorMercado.estrategia.EstrategiaOperacion;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Agente implements Runnable{
    private final String nombre;
    private double dineroDisponible;
    private final Map<String, Integer> portafolio;
    private final EstrategiaOperacion estrategia;
    private final Mercado mercado;

    public Agente(String nombre, double dineroInicial, EstrategiaOperacion estrategia, Mercado mercado) {
        this.nombre = nombre;
        this.dineroDisponible = dineroInicial;
        this.estrategia = estrategia;
        this.mercado = mercado;
        this.portafolio = new HashMap<>();
    }

    public void comprar(String simbolo, int cantidad, double precioUnitario) {
        dineroDisponible -= cantidad * precioUnitario;
        portafolio.merge(simbolo, cantidad, Integer::sum);
    }

    public void vender(String simbolo, int cantidad, double precioUnitario) {
        int actuales = portafolio.getOrDefault(simbolo, 0);
        if (actuales >= cantidad) {
            portafolio.put(simbolo, actuales - cantidad);
            dineroDisponible += cantidad * precioUnitario;
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
