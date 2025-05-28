package simuladorMercado.mercado;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Representa el mercado de valores donde se negocian las acciones.
 * Gestiona las acciones, sus precios y el registro de operaciones pendientes.
 */
public class Mercado {
    private final Map<String, Accion> acciones; // Almacena las acciones disponibles en el mercado. 
    private volatile boolean activo = true; // Indica si el mercado esta activo o cerrado. 
    private final ReentrantLock mercadoLock = new ReentrantLock(); // Bloqueo para asegurar la concurrencia segura en las operaciones del mercado. 
    private final BlockingQueue<OperacionConAgente> operacionesPendientes = new LinkedBlockingQueue<>(); // Cola de operaciones pendientes para ser procesadas y mostradas. 

    /**
     * Constructor de la clase Mercado.
     * Inicializa las acciones con sus simbolos y precios iniciales.
     */
    public Mercado() {
        this.acciones = new ConcurrentHashMap<>(); 
        acciones.put("AAPL", new Accion("AAPL", 300.0)); 
        acciones.put("GOOG", new Accion("GOOG", 2000.0)); 
        acciones.put("TSLA", new Accion("TSLA", 700.0)); 
    }

    /**
     * Ajusta el precio de una accion al alza debido a una operacion de compra.
     * El aumento de precio es proporcional a la cantidad comprada.
     *
     * @param simbolo El simbolo de la accion cuyo precio se va a ajustar.
     * @param cantidad La cantidad de la accion que se compro.
     */
    public void ajustarPrecioPorCompra(String simbolo, int cantidad) { 
        mercadoLock.lock(); 
        try {
            Accion accion = acciones.get(simbolo); 
            if (accion != null) { 
                double aumento = (double) cantidad * 1.0; // Valor de fluctuacion de compra ajustable
                double nuevoPrecio = accion.getPrecioActual() + aumento; 
                accion.actualizarPrecio(nuevoPrecio); 
            }
        } finally {
            mercadoLock.unlock(); 
        }
    }

    /**
     * Ajusta el precio de una accion a la baja debido a una operacion de venta.
     * La disminucion de precio es proporcional a la cantidad vendida.
     *
     * @param simbolo El simbolo de la accion cuyo precio se va a ajustar.
     * @param cantidad La cantidad de la accion que se vendio.
     */
    public void ajustarPrecioPorVenta(String simbolo, int cantidad) { 
        mercadoLock.lock(); 
        try {
            Accion accion = acciones.get(simbolo); 
            if (accion != null) { 
                double disminucion = (double) cantidad * 1.0; // Valor de fluctuacion de venta ajustable 
                double nuevoPrecio = Math.max(1, accion.getPrecioActual() - disminucion); 
                accion.actualizarPrecio(nuevoPrecio); 
            }
        } finally {
            mercadoLock.unlock(); 
        }
    }

    /**
     * Obtiene una accion especifica del mercado.
     *
     * @param simbolo El simbolo de la accion a obtener.
     * @return Accion correspondiente al simbolo, o null si no se encuentra.
     */
    public Accion getAccion(String simbolo) { 
        return acciones.get(simbolo); 
    }

    /**
     * Obtiene el conjunto de simbolos de todas las acciones disponibles en el mercado.
     *
     * @return Set de String que contiene los simbolos de las acciones.
     */
    public Set<String> obtenerSimbolos() { 
        return acciones.keySet(); 
    }

    /**
     * Verifica si el mercado esta actualmente activo.
     *
     * @return true si el mercado esta activo, false en caso contrario.
     */
    public boolean estaActivo() { 
        return activo; 
    }

    /**
     * Cierra el mercado, deteniendo la posibilidad de nuevas operaciones.
     */
    public void cerrarMercado() { 
        activo = false; 
    }

    /**
     * Registra una operacion pendiente para su posterior procesamiento y visualizacion.
     *
     * @param operacion Operacion con agente a registrar.
     */
    public void registrarOperacion(OperacionConAgente operacion) { 
        if (operacion != null) { 
            operacionesPendientes.offer(operacion); 
        }
    }

    /**
     * Obtiene y vacia la lista de operaciones pendientes del mercado.
     *
     * @return Lista de OperacionConAgente que representa las operaciones pendientes.
     */
    public List<OperacionConAgente> obtenerOperacionesPendientes() { 
        List<OperacionConAgente> ops = new ArrayList<>(); 
        operacionesPendientes.drainTo(ops); 
        return ops; 
    }
}