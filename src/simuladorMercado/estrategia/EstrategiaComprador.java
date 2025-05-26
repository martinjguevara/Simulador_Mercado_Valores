package simuladorMercado.estrategia;

import simuladorMercado.mercado.Mercado;
import simuladorMercado.mercado.Accion;
import simuladorMercado.agentes.Agente;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EstrategiaComprador implements EstrategiaOperacion {
    private final Random rand = new Random();

    @Override
    public void ejecutar(Mercado mercado, Agente agente) {
        List<String> simbolos = new ArrayList<>(mercado.obtenerSimbolos());
        String simbolo = simbolos.get(rand.nextInt(simbolos.size()));
        Accion accion = mercado.getAccion(simbolo);
        int cantidad = 1 + rand.nextInt(5);

        double costo = accion.getPrecioActual() * cantidad;
        if (agente.getDineroDisponible() >= costo) {
            agente.comprar(simbolo, cantidad, accion.getPrecioActual());
            System.out.printf("[COMPRA] %s compró %d de %s a %.2f\n", agente.getNombre(), cantidad, simbolo, accion.getPrecioActual());
        }
    }
}