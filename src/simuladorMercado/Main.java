package simuladorMercado;

import simuladorMercado.mercado.Mercado;
import simuladorMercado.mercado.Operacion;
import simuladorMercado.mercado.OperacionConAgente;
import simuladorMercado.agentes.Agente;
import simuladorMercado.estrategia.EstrategiaAleatoria;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final int NUM_AGENTES = 4;
    private static final int NUM_CICLOS_SIMULACION = 20;

    public static void main(String[] args) {
        Mercado mercado = new Mercado();
        EstrategiaAleatoria estrategia = new EstrategiaAleatoria();
        List<Agente> agentes = new ArrayList<>();
        List<Thread> hilos = new ArrayList<>();
        for (int i = 1; i <= NUM_AGENTES; i++) {
            Agente agente = new Agente("Agente" + i, 10000, estrategia, mercado);
            agentes.add(agente);
            Thread t = new Thread(agente);
            hilos.add(t);
            t.start();
        }
        System.out.printf("%-54s | %-54s\n", "[ COMPRA ]", "[ VENTA ]");
        System.out.printf("%-10s %-10s %-10s %-10s %-10s | %-10s %-10s %-10s %-10s %-10s\n",
                "Agente", "Activo", "Valor", "Cantidad", "Total", "Agente", "Activo", "Valor", "Cantidad", "Total");
        for (int i = 0; i < NUM_CICLOS_SIMULACION; i++) {
            try {
                Thread.sleep(500); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("El hilo principal ha sido interrumpido.");
                break;
            }
            List<OperacionConAgente> operacionesDelCiclo = mercado.obtenerOperacionesPendientes();
            List<OperacionConAgente> compras = new ArrayList<>();
            List<OperacionConAgente> ventas = new ArrayList<>();
            for (OperacionConAgente opConAgente : operacionesDelCiclo) {
                if (opConAgente.getOperacion().getTipo() == Operacion.Tipo.COMPRA) {
                    compras.add(opConAgente);
                } else {
                    ventas.add(opConAgente);
                }
            }
            int maxOps = Math.max(compras.size(), ventas.size());
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
                Thread.sleep(1000); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        mercado.cerrarMercado();
        for (Thread t : hilos) {
            try {
                t.join(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}