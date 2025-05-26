package simuladorMercado.mercado;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Mercado {
    private final Map<String, Accion> acciones;
    private boolean activo = true;

    public Mercado(Map<String, Accion> acciones) {
        this.acciones = new ConcurrentHashMap<>();
        acciones.put("APPL", new Accion("APPL", 150.0));
        acciones.put("GOOG", new Accion("GOOG", 2800.0));
    }

    public void fluctuacion() {
        Random rand = new Random();
        for (Accion accion: acciones.values()) {
            double variacion = (rand.nextDouble() - 0.5) * 10; // +-5%
            double nuevoPrecio = Math.max(1, accion.getPrecioActual() + variacion);
            accion.actualizarPrecio(nuevoPrecio);
        }
    }

    public Accion getAccion(String simbolo) {
        return acciones.get(simbolo);
    }

    public Set<String> obtenerSimbolos() {
        return acciones.keySet();
    }

    public boolean estaActivo() {
        return activo;
    }

    public void cerrarMercado() {
        activo = false;
    }
}
