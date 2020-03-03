package com.company;

import java.util.Random;

import static java.util.Arrays.fill;

public class Main {

    public static int cost(int sol[]) {
        return cost(sol, sol.length - 1);
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

    private static int cost(int[] sol, int n) {
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

    public static int busca(int sol[]) {
        int cost = cost(sol);
        boolean imp = false;
        do {
            imp = false;
//            int d = bestImp(sol, cost);
            int d = firstImp(sol, cost);
            if (d < cost) {
                imp = true;
                cost = d;
//                System.out.println(cost);
            }
        } while (imp);

        return cost;
    }

    private static int ataqueIndividual(int[] sol, int x){
        int a = 0;
        for (int i = 0; i < sol.length; i++) {
            if (sol[i] - i == sol[x] - x) {
                a++;
            }
            if (sol[i] + i == sol[x] + x) {
                a++;
            }
        }
        return a;
    }

    private static int[] ataques;
    private static int firstImp(int[] sol, int cost) {
        for (int i = 0, len = sol.length; i < len; i++)
            ataques[i] = ataqueIndividual(sol,i);

        for (int i = 0, len = sol.length; i < len; i++) {
            for (int j = i + 1; j < len; j++) {
                int aux = sol[i];
                sol[i] = sol[j];
                sol[j] = aux;
//                int d = cost(sol);
//                if (d < cost) {
                if (ataqueIndividual(sol,i)+ataqueIndividual(sol,j) < ataques[i]+ataques[j]) {
                    int d = cost(sol);
                    return d;
                } else {
                    aux = sol[i];
                    sol[i] = sol[j];
                    sol[j] = aux;
                }
            }
        }
        return cost;
    }

    private static int bestImp(int[] sol, int cost) {
        int argi = -1, argj = -1, min = cost;
        for (int i = 0; i < sol.length; i++) {
            for (int j = i + 1; j < sol.length; j++) {
                int aux = sol[i];
                sol[i] = sol[j];
                sol[j] = aux;
                int d = cost(sol);

                if (d < min) {
                    min = d;
                    argi = i;
                    argj = j;
                }

                aux = sol[i];
                sol[i] = sol[j];
                sol[j] = aux;

            }
        }

        if (argi != -1) {
            int aux = sol[argi];
            sol[argi] = sol[argj];
            sol[argj] = aux;
            return min;
        }

        return cost;
    }

    public static int[] randomMultiStart(int n, int ite) {
        ataques = new int[n];

        int bestCost = Integer.MAX_VALUE;
        int bestSol[] = new int[n];
        int current[] = new int[n];
        for (int i = 0; i < n; i++)
            current[i] = i;

        for (int i = 1; i <= ite; i++) {
            shuffle(current);
            int cost = busca(current);
            if (cost < bestCost) {
                System.out.println(i+" RMS: "+cost);
                bestCost = cost;
                for (int j = 0; j < current.length; j++)
                    bestSol[j] = current[j];
                if (cost == 0)
                    break;
            }
        }
        return bestSol;
    }

    public static void main(String[] args) {
        int n = 30; //representação do problema

        long t0 = System.currentTimeMillis();
//        int sol[] = enumere(n);
        int sol[] = randomMultiStart(n, 100);

        long t = System.currentTimeMillis() - t0;

        System.out.println("custo "+cost(sol));
//        print(sol);
        System.out.println("tempo: " + t);


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
