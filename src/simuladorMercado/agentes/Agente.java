package simuladorMercado.agentes;

import simuladorMercado.estrategia.EstrategiaOperacion;
import simuladorMercado.mercado.Mercado;
import simuladorMercado.mercado.Operacion;
import simuladorMercado.mercado.OperacionConAgente;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Representa un agente participante en el mercado de valores.
 * Cada agente tiene dinero disponible, un portafolio de acciones y una estrategia de operacion.
 */
public class Agente implements Runnable {
    private final String nombre; // Nombre unico del agente. 
    private double dineroDisponible; // Cantidad de dinero que el agente tiene para operar. 
    private final Map<String, Integer> portafolio; // Mapa que almacena la cantidad de cada accion que posee el agente. 
    private final EstrategiaOperacion estrategia; // Estrategia que el agente utiliza para decidir sus operaciones. 
    private final Mercado mercado; // Referencia al mercado en el que el agente opera. 

    /**
     * Constructor de la clase Agente.
     *
     * @param nombre El nombre del agente.
     * @param dineroInicial La cantidad inicial de dinero disponible para el agente.
     * @param estrategia La estrategia de operacion que el agente seguira.
     * @param mercado La instancia del mercado en el que el agente operara.
     */
    public Agente(String nombre, double dineroInicial, EstrategiaOperacion estrategia, Mercado mercado) { 
        this.nombre = nombre; 
        this.dineroDisponible = dineroInicial; 
        this.estrategia = estrategia; 
        this.mercado = mercado; 
        this.portafolio = new HashMap<>(); 
    }

    /**
     * Realiza una operacion de compra de una accion.
     * El metodo esta sincronizado para proteger el estado interno del agente (dinero y portafolio).
     *
     * @param simbolo El simbolo de la accion a comprar.
     * @param cantidad La cantidad de acciones a comprar.
     * @param precioUnitario El precio unitario de la accion en el momento de la compra.
     */
    public synchronized void comprar(String simbolo, int cantidad, double precioUnitario) { 
        if (cantidad <= 0 || precioUnitario <= 0) { 
            return; 
        }
        double costo = cantidad * precioUnitario; 
        if (dineroDisponible >= costo) { 
            dineroDisponible -= costo; 
            portafolio.merge(simbolo, cantidad, Integer::sum); 
            // Registra la operacion con el precio al que se realizo.
            mercado.registrarOperacion(new OperacionConAgente(new Operacion(Operacion.Tipo.COMPRA, simbolo, precioUnitario, cantidad), this.nombre)); // [cite: 298]
            // Ajusta el precio en el mercado despues de la transaccion.
            mercado.ajustarPrecioPorCompra(simbolo, cantidad);
        }
    }

    /**
     * Realiza una operacion de venta de una accion.
     * El metodo esta sincronizado para proteger el estado interno del agente (dinero y portafolio).
     *
     * @param simbolo El simbolo de la accion a vender.
     * @param cantidad La cantidad de acciones a vender.
     * @param precioUnitario El precio unitario de la accion en el momento de la venta.
     */
    public synchronized void vender(String simbolo, int cantidad, double precioUnitario) { 
        if (cantidad <= 0 || precioUnitario <= 0) { 
            return; 
        }
        int actuales = portafolio.getOrDefault(simbolo, 0); 
        if (actuales >= cantidad) { 
            portafolio.put(simbolo, actuales - cantidad); 
            dineroDisponible += cantidad * precioUnitario; 
            // Registra la operacion con el precio al que se realizo.
            mercado.registrarOperacion(new OperacionConAgente(new Operacion(Operacion.Tipo.VENTA, simbolo, precioUnitario, cantidad), this.nombre)); 
            // Ajusta el precio en el mercado despues de la transaccion.
            mercado.ajustarPrecioPorVenta(simbolo, cantidad); 
        }
    }

    /**
     * Obtiene la cantidad de acciones de un simbolo especifico que posee el agente.
     *
     * @param simbolo El simbolo de la accion.
     * @return Cantidad de acciones de ese simbolo en el portafolio del agente.
     */
    public int getCantidad(String simbolo) { 
        return portafolio.getOrDefault(simbolo, 0); 
    }

    /**
     * Obtiene el portafolio completo del agente.
     *
     * @return Mapa que representa el portafolio del agente.
     */
    public Map<String, Integer> getPortafolio() { 
        return portafolio; 
    }

    /**
     * Obtiene el nombre del agente.
     *
     * @return Nombre del agente.
     */
    public String getNombre() { 
        return nombre;
    }

    /**
     * Obtiene la cantidad de dinero disponible del agente.
     *
     * @return Dinero disponible del agente.
     */
    public double getDineroDisponible() { 
        return dineroDisponible; 
    }

    /**
     * Metodo run que ejecuta la estrategia del agente en un hilo separado.
     * El agente opera mientras el mercado este activo y el hilo no este interrumpido.
     */
    @Override 
    public void run() {
        // El bucle continua mientras el mercado este activo Y el hilo no haya sido interrumpido.
        while (mercado.estaActivo() || !Thread.currentThread().isInterrupted()) { // Continuar si el mercado esta activo o si se esta interrumpiendo.
            try {
                estrategia.ejecutar(mercado, this); // El agente ejecuta su estrategia de operacion. 
                Thread.sleep(1000 + new Random().nextInt(2000)); // Pausa aleatoria entre operaciones. 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Interrumpe el hilo actual. 
                System.out.println(nombre + " ha sido interrumpido y esta terminando."); 
                break; 
            }
        }
        System.out.println(nombre + " ha terminado su ejecucion."); 
    }
}