package simuladorMercado;

import simuladorMercado.mercado.Mercado;
import simuladorMercado.mercado.Operacion;
import simuladorMercado.mercado.OperacionConAgente;
import simuladorMercado.agentes.Agente;
import simuladorMercado.estrategia.EstrategiaAleatoria;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase principal que inicializa y ejecuta la simulacion del mercado de valores.
 * Crea agentes, el mercado y gestiona los ciclos de simulacion, mostrando las operaciones.
 */
public class Main {
    private static final int NUM_AGENTES = 5; 
    private static final int NUM_CICLOS_SIMULACION = 30; 

    /**
     * Metodo principal para iniciar la simulacion del mercado.
     *
     * @param args Argumentos de linea de comandos, no se utiliza.
     */
    public static void main(String[] args) {
        Mercado mercado = new Mercado(); 
        EstrategiaAleatoria estrategia = new EstrategiaAleatoria(); 
        List<Agente> agentes = new ArrayList<>(); 
        List<Thread> hilos = new ArrayList<>(); // Lista para almacenar los hilos de los agentes.

        // Inicializacion y lanzamiento de agentes
        for (int i = 1; i <= NUM_AGENTES; i++) { 
            Agente agente = new Agente("Agente" + i, 10000, estrategia, mercado); 
            agentes.add(agente); 
            // Cada agente es una instancia de Runnable, envuelta en un nuevo Thread.
            Thread t = new Thread(agente); 
            hilos.add(t); 
            t.start(); // Inicia la ejecucion del hilo del agente en paralelo.
        }

        // Impresion de encabezado de la tabla de operaciones
        System.out.printf("%-54s | %-54s\n", "[ COMPRA ]", "[ VENTA ]");
        System.out.printf("%-10s %-10s %-10s %-10s %-10s | %-10s %-10s %-10s %-10s %-10s\n", 
                "Agente", "Activo", "Valor", "Cantidad", "Total", "Agente", "Activo", "Valor", "Cantidad", "Total");

        // Bucle principal de la simulacion
        for (int i = 0; i < NUM_CICLOS_SIMULACION; i++) { 
            try {
                // Pausa para permitir que los agentes generen operaciones antes de recolectarlas para este ciclo de impresion.
                Thread.sleep(500); 
            } catch (InterruptedException e) { 
                Thread.currentThread().interrupt();
                System.out.println("El hilo principal ha sido interrumpido.");
                break; 
            }

            // Obtener operaciones pendientes del mercado
            List<OperacionConAgente> operacionesDelCiclo = mercado.obtenerOperacionesPendientes(); 
            List<OperacionConAgente> compras = new ArrayList<>(); 
            List<OperacionConAgente> ventas = new ArrayList<>(); 

            // Clasificar operaciones en compras y ventas
            for (OperacionConAgente opConAgente : operacionesDelCiclo) { 
                if (opConAgente.getOperacion().getTipo() == Operacion.Tipo.COMPRA) { 
                    compras.add(opConAgente); 
                } else { 
                    ventas.add(opConAgente); 
                }
            }
            int maxOps = Math.max(compras.size(), ventas.size()); 

            // Imprimir operaciones en formato de tabla
            if (maxOps == 0) { 
                 System.out.printf("%-54s | %-54s\n", String.format("%-10s %-10s %-10s %-10s %-10s", "-", "-", "-", "-", "-"), 
                                    String.format("%-10s %-10s %-10s %-10s %-10s", "-", "-", "-", "-", "-")); 
            } else { 
                for (int j = 0; j < maxOps; j++) { 
                    String compraStr = String.format("%-10s %-10s %-10s %-10s %-10s", "-", "-", "-", "-", "-"); 
                    String ventaStr = String.format("%-10s %-10s %-10s %-10s %-10s", "-", "-", "-", "-", "-"); 
                    if (j < compras.size()) { 
                        OperacionConAgente opA = compras.get(j); 
                        compraStr = String.format("%-10s %-10s %-10.2f %-10d %-10.2f", 
                                opA.getNombreAgente(), opA.getOperacion().getSimbolo(), opA.getOperacion().getValor(),
                                opA.getOperacion().getCantidad(), opA.getOperacion().getTotal()); 
                    }
                    if (j < ventas.size()) { 
                        OperacionConAgente opB = ventas.get(j); 
                        ventaStr = String.format("%-10s %-10s %-10.2f %-10d %-10.2f", 
                                opB.getNombreAgente(), opB.getOperacion().getSimbolo(), opB.getOperacion().getValor(),
                                opB.getOperacion().getCantidad(), opB.getOperacion().getTotal()); 
                    }
                    System.out.printf("%s | %s\n", compraStr, ventaStr); 
                }
            }
            try {
                Thread.sleep(500); // Pausa entre cada ciclo de recoleccion/impresion 
            } catch (InterruptedException e) { 
                Thread.currentThread().interrupt(); 
            }
        }

        // Cierre del mercado y gestion de la finalizacion de los hilos.
        mercado.cerrarMercado(); 
        for (Thread t : hilos) { 
            try {
                t.join(2000); // Espera un maximo de 2 segundos a que cada hilo de agente termine su ejecucion
            } catch (InterruptedException e) { 
                Thread.currentThread().interrupt();
            }
        }
    }
}