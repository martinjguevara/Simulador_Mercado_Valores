package simuladorMercado.estrategia;

import simuladorMercado.mercado.Mercado;
import simuladorMercado.mercado.Accion;
import simuladorMercado.agentes.Agente;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EstrategiaVendedor implements EstrategiaOperacion {
    private final Random rand = new Random();

    @Override
    public void ejecutar(Mercado mercado, Agente agente) {
        List<String> acciones = new ArrayList<>(agente.getPortafolio().keySet());
        if (acciones.isEmpty()) return;

        String simbolo = acciones.get(rand.nextInt(acciones.size()));
        int disponibles = agente.getCantidad(simbolo);
        if (disponibles == 0) return;

        int cantidad = 1 + rand.nextInt(disponibles);
        Accion accion = mercado.getAccion(simbolo);
        agente.vender(simbolo, cantidad, accion.getPrecioActual());
        System.out.printf("[VENTA] %s vendió %d de %s a %.2f\n", agente.getNombre(), cantidad, simbolo, accion.getPrecioActual());
    }
}