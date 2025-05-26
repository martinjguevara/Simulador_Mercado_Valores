package simuladorMercado.mercado;

public class OperacionConAgente {
    private final Operacion operacion;
    private final String nombreAgente;

    public OperacionConAgente(Operacion operacion, String nombreAgente) {
        this.operacion = operacion;
        this.nombreAgente = nombreAgente;
    }

    public Operacion getOperacion() {
        return operacion;
    }

    public String getNombreAgente() {
        return nombreAgente;
    }
}