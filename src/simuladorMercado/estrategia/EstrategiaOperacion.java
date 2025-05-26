package simuladorMercado.estrategia;

import simuladorMercado.mercado.Mercado;
import simuladorMercado.agentes.Agente;

public interface EstrategiaOperacion {
    void ejecutar(Mercado mercado, Agente agente);
} 