package simuladorMercado.mercado;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class Mercado {
    private final Map<String, Accion> acciones;
    private volatile boolean activo = true;
    private final ReentrantLock mercadoLock = new ReentrantLock();
    private final BlockingQueue<OperacionConAgente> operacionesPendientes = new LinkedBlockingQueue<>();

    public Mercado() {
        this.acciones = new ConcurrentHashMap<>();
        acciones.put("AAPL", new Accion("AAPL", 150.0));
        acciones.put("GOOG", new Accion("GOOG", 2800.0));
        acciones.put("TSLA", new Accion("TSLA", 700.0));
    }

    public void fluctuacionAleatoria() {
        mercadoLock.lock();
        try {
            Random rand = new Random();
            for (Accion accion : acciones.values()) {
                double variacion = (rand.nextDouble() - 0.5) * 10;
                double nuevoPrecio = Math.max(1, accion.getPrecioActual() + variacion);
                accion.actualizarPrecio(nuevoPrecio);
            }
        } finally {
            mercadoLock.unlock();
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

    public void registrarOperacion(OperacionConAgente operacion) {
        if (operacion != null) {
            operacionesPendientes.offer(operacion); 
        }
    }

    public List<OperacionConAgente> obtenerOperacionesPendientes() {
        List<OperacionConAgente> ops = new ArrayList<>();
        operacionesPendientes.drainTo(ops);
        return ops;
    }
}