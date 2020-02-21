package com.company;

import java.util.Arrays;
import java.util.Random;

public class Main {

    public static int ataque(int sol[]){
        int a = 0;
        //supondo que sol é um arranjo de 0 ... n
        //diagonal principal
        for (int i = 0; i < sol.length; i++) {
            for (int j = i+1; j < sol.length; j++) {
                if(sol[i]-i == sol[j]-j){
                    a++;
                }
                if(sol[i]+i == sol[j]+j){
                    a++;
                }
            }
        }
        return a;
    }


    public static void print(int sol[]){
        for (int i = 0; i < sol.length; i++) {
            System.out.print("|");
            for (int j = 0; j < sol[i]; j++) {
                System.out.print(" |");
            }
            System.out.print("x|");
            for (int j = sol[i]+1; j < sol.length; j++) {
                System.out.print(" |");
            }
            System.out.println();
        }
    }


    public static void shuffle(int sol[]){
        for (int i = sol.length-1; i > 1 ; i--) {
            int x = rd.nextInt(i);
            int aux = sol[i];
            sol[i] = sol[x];
            sol[x] = aux;
        }
    }

    public static Random rd = new Random(7);

    public static void main(String[] args) {
	    int n = 20; //representação do problema
        int sol[] = new int[n];
        for (int i = 0; i < n; i++)
            sol[i]=i;

        int bestcost = Integer.MAX_VALUE;
        int bestSol[] = new int[n];
        for (int i = 0; i < 100000; i++) {
            shuffle(sol);
//            System.out.println(Arrays.toString(sol));
            int cost = ataque(sol);
            if (cost < bestcost) {
                System.out.println(cost);
                bestcost = cost;
                for (int j = 0; j < sol.length; j++)
                    bestSol[j] = sol[j];
                if(cost == 0)
                    break;
            }
        }
        System.out.println(Arrays.toString(bestSol));
        print(bestSol);




    }
}
