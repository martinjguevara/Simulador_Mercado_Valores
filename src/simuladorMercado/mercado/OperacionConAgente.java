package simuladorMercado.mercado;

/**
 * Clase que encapsula una Operacion y el nombre del agente que la realizo.
 * Utilizada para el registro y visualizacion de operaciones en el mercado.
 */
public class OperacionConAgente {
    private final Operacion operacion; // La operacion de compra o venta realizada. 
    private final String nombreAgente; // El nombre del agente que efectuo la operacion. 

    /**
     * Constructor de la clase OperacionConAgente.
     *
     * @param operacion La operacion a encapsular.
     * @param nombreAgente El nombre del agente que realizo la operacion.
     */
    public OperacionConAgente(Operacion operacion, String nombreAgente) { 
        this.operacion = operacion; 
        this.nombreAgente = nombreAgente; 
    }

    /**
     * Obtiene el objeto Operacion encapsulado.
     *
     * @return Operacion de compra o venta.
     */
    public Operacion getOperacion() { 
        return operacion; 
    }

    /**
     * Obtiene el nombre del agente que realizo la operacion.
     *
     * @return Nombre del agente.
     */
    public String getNombreAgente() { 
        return nombreAgente; 
    }
}