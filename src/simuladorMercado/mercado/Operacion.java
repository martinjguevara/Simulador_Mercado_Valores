package simuladorMercado.mercado;

public class Operacion {
    public enum Tipo { COMPRA, VENTA }

    private final Tipo tipo;
    private final String simbolo;
    private final double valor;
    private final int cantidad;

    public Operacion(Tipo tipo, String simbolo, double valor, int cantidad) {
        this.tipo = tipo;
        this.simbolo = simbolo;
        this.valor = valor;
        this.cantidad = cantidad;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public double getValor() {
        return valor;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getTotal() {
        return valor * cantidad;
    }
}