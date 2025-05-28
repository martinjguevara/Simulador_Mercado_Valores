package simuladorMercado.mercado;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        acciones.put("AAPL", new Accion("AAPL", 300.0));
        acciones.put("GOOG", new Accion("GOOG", 2000.0));
        acciones.put("TSLA", new Accion("TSLA", 700.0));
    }

    public void ajustarPrecioPorCompra(String simbolo, int cantidad) {
        mercadoLock.lock();
        try {
            Accion accion = acciones.get(simbolo);
            if (accion != null) {
                double aumento = (double) cantidad * 1.0; 
                double nuevoPrecio = accion.getPrecioActual() + aumento;
                accion.actualizarPrecio(nuevoPrecio);
            }
        } finally {
            mercadoLock.unlock();
        }
    }

    public void ajustarPrecioPorVenta(String simbolo, int cantidad) {
        mercadoLock.lock();
        try {
            Accion accion = acciones.get(simbolo);
            if (accion != null) {
                double disminucion = (double) cantidad * 1.0; 
                double nuevoPrecio = Math.max(1, accion.getPrecioActual() - disminucion);
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