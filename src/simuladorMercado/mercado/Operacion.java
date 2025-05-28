package simuladorMercado.mercado;

/**
 * Representa una operacion de compra o venta de una accion.
 * Contiene el tipo de operacion, el simbolo de la accion, el valor unitario y la cantidad.
 */
public class Operacion {
    /**
     * Enumeracion para los tipos de operacion: COMPRA o VENTA.
     */
    public enum Tipo { COMPRA, VENTA } 

    private final Tipo tipo; // Tipo de operacion. 
    private final String simbolo; // Simbolo de la accion involucrada en la operacion. 
    private final double valor; // Valor unitario de la accion en el momento de la operacion. 
    private final int cantidad; // Cantidad de acciones involucradas en la operacion. 

    /**
     * Constructor de la clase Operacion.
     *
     * @param tipo El tipo de operacion.
     * @param simbolo El simbolo de la accion.
     * @param valor El valor unitario de la accion en la operacion.
     * @param cantidad La cantidad de acciones.
     */
    public Operacion(Tipo tipo, String simbolo, double valor, int cantidad) { 
        this.tipo = tipo; 
        this.simbolo = simbolo; 
        this.valor = valor; 
        this.cantidad = cantidad; 
    }

    /**
     * Obtiene el tipo de operacion.
     *
     * @return Tipo de operacion.
     */
    public Tipo getTipo() { 
        return tipo; 
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
     * Obtiene el valor unitario de la accion en la operacion.
     *
     * @return Valor unitario.
     */
    public double getValor() { 
        return valor; 
    }

    /**
     * Obtiene la cantidad de acciones involucradas en la operacion.
     *
     * @return Cantidad de acciones.
     */
    public int getCantidad() { 
        return cantidad; 
    }

    /**
     * Calcula el valor total de la operacion.
     *
     * @return Valor total de la operacion.
     */
    public double getTotal() { 
        return valor * cantidad; 
    }
}