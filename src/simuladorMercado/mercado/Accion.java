package simuladorMercado.mercado;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Representa una accion negociable en el mercado, con su simbolo, precio actual e historial de precios.
 */
public class Accion {
    private final String simbolo; // Simbolo unico de la accion. 
    private double precioActual; // El precio actual de la accion en el mercado. 
    private final Queue<Double> historialPrecios; // Cola que almacena un historial limitado de los precios de la accion.
    private final int maxHistorial = 10; // Tamanio maximo del historial de precios. 

    /**
     * Constructor de la clase Accion.
     *
     * @param simbolo El simbolo de la accion.
     * @param precioInicial El precio inicial de la accion.
     */
    public Accion(String simbolo, double precioInicial) { 
        this.simbolo = simbolo; 
        this.precioActual = precioInicial; 
        this.historialPrecios = new LinkedList<>(); 
        this.historialPrecios.add(precioInicial); 
    }

    /**
     * Obtiene el simbolo de la accion.
     *
     * @return Simbolo de la accion.
     */
    public String getSimbolo() { 
        return simbolo;
    }

    /**
     * Obtiene el precio actual de la accion.
     *
     * @return Precio actual de la accion.
     */
    public double getPrecioActual() { 
        return precioActual; 
    }

    /**
     * Actualiza el precio actual de la accion y lo agrega al historial.
     * Si el historial excede su tamanio maximo, se elimina el precio mas antiguo.
     *
     * @param nuevoPrecio El nuevo precio de la accion.
     */
    public void actualizarPrecio(double nuevoPrecio) { 
        this.precioActual = nuevoPrecio; 
        if (historialPrecios.size() >= maxHistorial) { 
            historialPrecios.poll(); 
        }
        historialPrecios.add(nuevoPrecio); 
    }

    /**
     * Obtiene el historial de precios de la accion.
     *
     * @return Una Queue de Double que representa el historial de precios.
     */
    public Queue<Double> getHistorialPrecios() { 
        return historialPrecios; 
    }
}