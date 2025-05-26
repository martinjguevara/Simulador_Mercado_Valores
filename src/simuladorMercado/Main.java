package simuladorMercado;

import simuladorMercado.mercado.Mercado;
import simuladorMercado.mercado.Operacion;
import simuladorMercado.agentes.Agente;
import simuladorMercado.estrategia.EstrategiaAleatoria;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Mercado mercado = new Mercado();
        EstrategiaAleatoria estrategia = new EstrategiaAleatoria();
        List<Agente> agentes = new ArrayList<>();
        List<Thread> hilos = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Agente agente = new Agente("Agente" + i, 10000, estrategia, mercado);
            agentes.add(agente);
            Thread t = new Thread(agente);
            hilos.add(t);
            t.start();
        }
        System.out.printf("%-10s | %-30s | %-30s\n", "Agente", "[ COMPRA ]", "[ VENTA ]");
        System.out.printf("%-10s | %-10s %-10s %-10s | %-10s %-10s %-10s\n",
                "", "Activo", "Valor", "Cantidad", "Activo", "Valor", "Cantidad");
        for (int i = 0; i < 30; i++) {
            mercado.fluctuacionAleatoria();
            for (Agente agente : agentes) {
                Operacion op = agente.getUltimaOperacion();
                String compraStr = "-", ventaStr = "-";
                if (op != null) {
                    if (op.getTipo() == Operacion.Tipo.COMPRA) {
                        compraStr = String.format("%-10s %-10.2f %-10d",
                                op.getSimbolo(), op.getValor(), op.getCantidad());
                        ventaStr = String.format("%-10s %-10s %-10s", "-", "-", "-");
                    } else {
                        ventaStr = String.format("%-10s %-10.2f %-10d",
                                op.getSimbolo(), op.getValor(), op.getCantidad());
                        compraStr = String.format("%-10s %-10s %-10s", "-", "-", "-");
                    }
                } else {
                    compraStr = String.format("%-10s %-10s %-10s", "-", "-", "-");
                    ventaStr = String.format("%-10s %-10s %-10s", "-", "-", "-");
                }
                System.out.printf("%-10s | %s | %s\n", agente.getNombre(), compraStr, ventaStr);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        mercado.cerrarMercado();
    }
}