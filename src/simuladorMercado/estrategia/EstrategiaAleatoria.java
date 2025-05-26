package simuladorMercado.estrategia;

import simuladorMercado.agentes.Agente;
import simuladorMercado.mercado.Accion;
import simuladorMercado.mercado.Mercado;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EstrategiaAleatoria implements EstrategiaOperacion {
    private final Random rand = new Random();

    @Override
    public void ejecutar(Mercado mercado, Agente agente) {
        if (!mercado.estaActivo()) { 
            agente.setUltimaOperacion(null);
            return;
        }

        boolean quiereComprar = rand.nextBoolean(); 
        if (quiereComprar) {
            List<String> simbolos = new ArrayList<>(mercado.obtenerSimbolos());
            if (simbolos.isEmpty()) {
                agente.setUltimaOperacion(null);
                return;
            }
            String simbolo = simbolos.get(rand.nextInt(simbolos.size())); 
            Accion accion = mercado.getAccion(simbolo); 
            if (accion == null) { 
                agente.setUltimaOperacion(null);
                return;
            }
            int cantidad = 1 + rand.nextInt(5); 
            agente.comprar(simbolo, cantidad, accion.getPrecioActual());
        } else {
            List<String> accionesEnPortafolio = new ArrayList<>(agente.getPortafolio().keySet());
            if (accionesEnPortafolio.isEmpty()) {
                agente.setUltimaOperacion(null);
                return; 
            }
            String simbolo = accionesEnPortafolio.get(rand.nextInt(accionesEnPortafolio.size()));
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
        }
    }
}