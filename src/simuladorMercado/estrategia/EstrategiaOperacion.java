package simuladorMercado.estrategia;

import simuladorMercado.agentes.Agente;
import simuladorMercado.mercado.Mercado;

/**
 * Interfaz que define el comportamiento de una estrategia de operacion para los agentes.
 * Las clases que implementen esta interfaz definiran como un agente decide comprar o vender.
 */
public interface EstrategiaOperacion {
    /**
     * Ejecuta la logica de la estrategia de operacion para un agente dado en un mercado especifico.
     *
     * @param mercado El mercado en el que el agente realizara operaciones.
     * @param agente El agente que esta ejecutando la estrategia.
     */
    void ejecutar(Mercado mercado, Agente agente); 
}