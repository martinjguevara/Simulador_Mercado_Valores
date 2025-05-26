package simuladorMercado.estrategia;

import simuladorMercado.agentes.Agente;
import simuladorMercado.mercado.Accion;
import simuladorMercado.mercado.Mercado;
import simuladorMercado.mercado.Operacion;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EstrategiaAleatoria implements EstrategiaOperacion {
    private final Random rand = new Random();

    @Override
    public void ejecutar(Mercado mercado, Agente agente) {
        boolean quiereComprar = rand.nextBoolean();
        if (quiereComprar) {
            List<String> simbolos = new ArrayList<>(mercado.obtenerSimbolos());
            String simbolo = simbolos.get(rand.nextInt(simbolos.size()));
            Accion accion = mercado.getAccion(simbolo);
            int cantidad = 1 + rand.nextInt(5);
            double costo = accion.getPrecioActual() * cantidad;
            if (agente.getDineroDisponible() >= costo) {
                agente.comprar(simbolo, cantidad, accion.getPrecioActual());
                agente.setUltimaOperacion(new Operacion(Operacion.Tipo.COMPRA, simbolo, accion.getPrecioActual(), cantidad));
            } else {
                agente.setUltimaOperacion(null);
            }
        } else {
            List<String> acciones = new ArrayList<>(agente.getPortafolio().keySet());
            if (acciones.isEmpty()) {
                agente.setUltimaOperacion(null);
                return;
            }
            String simbolo = acciones.get(rand.nextInt(acciones.size()));
            int disponibles = agente.getCantidad(simbolo);
            if (disponibles == 0) {
                agente.setUltimaOperacion(null);
                return;
            }
            int cantidad = 1 + rand.nextInt(disponibles);
            Accion accion = mercado.getAccion(simbolo);
            if (accion == null) {
                agente.setUltimaOperacion(null);
                return;
            }
            agente.vender(simbolo, cantidad, accion.getPrecioActual());
            agente.setUltimaOperacion(new Operacion(Operacion.Tipo.VENTA, simbolo, accion.getPrecioActual(), cantidad));
        }
    }
}