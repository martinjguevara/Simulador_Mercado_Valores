package simuladorMercado.estrategia;

import simuladorMercado.agentes.Agente;
import simuladorMercado.mercado.Mercado;

public interface EstrategiaOperacion {
    void ejecutar(Mercado mercado, Agente agente);
}