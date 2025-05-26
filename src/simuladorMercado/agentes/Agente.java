package simuladorMercado.agentes;

import simuladorMercado.estrategia.EstrategiaOperacion;
import simuladorMercado.mercado.Mercado;
import simuladorMercado.mercado.Operacion;
import simuladorMercado.mercado.OperacionConAgente;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Agente implements Runnable {
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

    public synchronized void comprar(String simbolo, int cantidad, double precioUnitario) {
        if (cantidad <= 0 || precioUnitario <= 0) {
            return; 
        }
        double costo = cantidad * precioUnitario;
        if (dineroDisponible >= costo) {
            dineroDisponible -= costo;
            portafolio.merge(simbolo, cantidad, Integer::sum);
            mercado.registrarOperacion(new OperacionConAgente(new Operacion(Operacion.Tipo.COMPRA, simbolo, precioUnitario, cantidad), this.nombre));
        }
    }

    public synchronized void vender(String simbolo, int cantidad, double precioUnitario) {
        if (cantidad <= 0 || precioUnitario <= 0) {
            return; 
        }
        int actuales = portafolio.getOrDefault(simbolo, 0);
        if (actuales >= cantidad) {
            portafolio.put(simbolo, actuales - cantidad);
            dineroDisponible += cantidad * precioUnitario;
            mercado.registrarOperacion(new OperacionConAgente(new Operacion(Operacion.Tipo.VENTA, simbolo, precioUnitario, cantidad), this.nombre));
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
        while (mercado.estaActivo() || !Thread.currentThread().isInterrupted()) { // Continuar si el mercado está activo o si se está interrumpiendo
            try {
                estrategia.ejecutar(mercado, this);
                Thread.sleep(1000 + new Random().nextInt(2000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); 
                System.out.println(nombre + " ha sido interrumpido y está terminando.");
                break; 
            }
        }
        System.out.println(nombre + " ha terminado su ejecución.");
    }
}