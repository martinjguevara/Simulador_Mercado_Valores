package simuladorMercado.estrategia;

import simuladorMercado.agentes.Agente;
import simuladorMercado.mercado.Accion;
import simuladorMercado.mercado.Mercado;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Implementacion de una estrategia de operacion aleatoria para los agentes.
 * Los agentes que usan esta estrategia deciden aleatoriamente si comprar o vender,
 * y que activo operar, asi como la cantidad.
 */
public class EstrategiaAleatoria implements EstrategiaOperacion {
    private final Random rand = new Random(); // Generador de numeros aleatorios para las decisiones de la estrategia.

    /**
     * Ejecuta la estrategia aleatoria de compra o venta para un agente.
     * El agente decide aleatoriamente si comprar o vender, y selecciona un activo y cantidad al azar.
     *
     * @param mercado El mercado donde el agente realizara la operacion.
     * @param agente El agente que ejecuta la estrategia.
     */
    @Override
    public void ejecutar(Mercado mercado, Agente agente) { 
        if (!mercado.estaActivo()) { 
            return; 
        }
        boolean quiereComprar = rand.nextBoolean(); // Decide aleatoriamente si el agente quiere comprar o vender. 
        if (quiereComprar) { 
            List<String> simbolos = new ArrayList<>(mercado.obtenerSimbolos()); // Obtiene una lista de todos los simbolos de acciones disponibles. 
            if (simbolos.isEmpty()) { 
                return; 
            }
            String simbolo = simbolos.get(rand.nextInt(simbolos.size())); // Selecciona un simbolo de accion aleatoriamente. 
            Accion accion = mercado.getAccion(simbolo); 
            if (accion == null) { 
                return; 
            }
            int cantidad = 1 + rand.nextInt(5); // Decide una cantidad aleatoria para comprar, entre 1 y 5. 
            agente.comprar(simbolo, cantidad, accion.getPrecioActual()); // Realiza la operacion de compra. 
        } else {
            List<String> accionesEnPortafolio = new ArrayList<>(agente.getPortafolio().keySet()); // Obtiene una lista de simbolos de acciones que el agente posee. 
            if (accionesEnPortafolio.isEmpty()) { 
                return; 
            }
            String simbolo = accionesEnPortafolio.get(rand.nextInt(accionesEnPortafolio.size())); // Selecciona un simbolo de accion de su portafolio aleatoriamente. 
            int disponibles = agente.getCantidad(simbolo); // Obtiene la cantidad de acciones disponibles de ese simbolo. 
            if (disponibles == 0) { 
                return; 
            }
            int cantidad = 1 + rand.nextInt(disponibles); // Decide una cantidad aleatoria para vender, entre 1 y las disponibles. 
            Accion accion = mercado.getAccion(simbolo); 
            if (accion == null) { 
                return; 
            }
            agente.vender(simbolo, cantidad, accion.getPrecioActual()); // Realiza la operacion de venta. 
        }
    }
}