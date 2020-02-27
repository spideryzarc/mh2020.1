package com.company;

import java.util.Arrays;
import java.util.Random;

import static java.util.Arrays.fill;

public class Main {

    public static int ataque(int sol[]) {
        return ataque(sol, sol.length - 1);
    }


    public static void print(int sol[]) {
        for (int i = 0; i < sol.length; i++) {
            System.out.print("|");
            for (int j = 0; j < sol[i]; j++) {
                System.out.print(" |");
            }
            System.out.print("x|");
            for (int j = sol[i] + 1; j < sol.length; j++) {
                System.out.print(" |");
            }
            System.out.println();
        }
    }


    public static void shuffle(int sol[]) {
        int n = sol.length;
        for (int i = 0; i < n; i++) {
            int x = rd.nextInt(n);
            int aux = sol[i];
            sol[i] = sol[x];
            sol[x] = aux;
        }
    }

    public static Random rd = new Random(7);

    public static boolean posicione(int sol[], int i, boolean linhaOcupada[]) {
        for (int j = 0; j < sol.length; j++)
            if (!linhaOcupada[j]) {
                sol[i] = j;
                if (ataqueUltimo(sol, i) == 0) {
                    if (i == sol.length - 1) {
                        return true;//ancora da recursão
                    }

                    linhaOcupada[j] = true;
                    if (posicione(sol, i + 1, linhaOcupada))
                        return true;
                    linhaOcupada[j] = false;
                }
            }
        return false;
    }

    public static int[] enumere(int n) {
        int[] sol = new int[n];
        posicione(sol, 0, new boolean[n]);
        return sol;
    }

    private static int ataque(int[] sol, int n) {
        int a = 0;
        //supondo que sol é um arranjo de 0 ... n-1
        //diagonal principal
        for (int i = 0; i <= n; i++) {
            for (int j = i + 1; j <= n; j++) {
                if (sol[i] - i == sol[j] - j) {
                    a++;
                }
                if (sol[i] + i == sol[j] + j) {
                    a++;
                }
            }
        }
        return a;
    }

    private static int ataqueUltimo(int[] sol, int n) {
        int a = 0;
        //supondo que sol é um arranjo de 0 ... n-1
        //diagonal principal
        for (int i = 0; i < n; i++) {
            if (sol[i] - i == sol[n] - n) {
                a++;
            }
            if (sol[i] + i == sol[n] + n) {
                a++;
            }

        }
        return a;
    }

    public static void main(String[] args) {
        int n = 32; //representação do problema

        long t0 = System.currentTimeMillis();
        int sol[] = enumere(n);
        long t = System.currentTimeMillis()-t0;

        System.out.println(ataque(sol));
        print(sol);
        System.out.println(t);



//        int current[] = new int[n];
//        //incializa como um arranjo 0..n-1
//        for (int i = 0; i < n; i++)
//            current[i]=i;
//
//        int bestCost = Integer.MAX_VALUE;
//        int bestSol[] = new int[n];
//        for (int i = 0; i < 10000000; i++) {
//            shuffle(current);
////            System.out.println(Arrays.toString(current));
//            int cost = ataque(current);
//            if (cost < bestCost) {
//                System.out.println(cost);
//                bestCost = cost;
//                for (int j = 0; j < current.length; j++)
//                    bestSol[j] = current[j];
//                if(cost == 0)
//                    break;
//            }
//        }
//        System.out.println(Arrays.toString(bestSol));
//        print(bestSol);


    }
}
