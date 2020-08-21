import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import static java.util.Arrays.fill;

/**
 * Greedy Randomized Adaptive Search Procedure
 */
public class GRASP implements Solver {
    private BPP bpp;
    /**
     * número de candidatos máximo por sorteio
     */
    int k;
    /**
     * número de iterações
     */
    int ite;

    /**
     * Lista de pacotes candidatos
     */
    ArrayList<Sol.Bin> candidatos = new ArrayList<>();


    public GRASP(int ite, int k) {
        this.k = k;
        this.ite = ite;
    }


    @Override
    public String toString() {
        return "GRASP{" +
                "k=" + k +
                ", ite=" + ite +
                '}';
    }

    /**
     * método guloso randomizado
     */
    private int greedyRandom(Sol sol, int lb) {
        sol.reset();// esvazia a solução

        for (int i = 0; i < lb; i++)//abre lb pacotes com os lb maiores itens
            sol.add(i, i);

        for (int i = lb; i < bpp.N; i++) {// para cada um dos itens restantes
            candidatos.clear();
            for (int j = 0, size = sol.size(); j < size; j++) // lista os pacotes compatíveis
                if (sol.getBin(j).getLoad() + bpp.w[i] <= bpp.C)
                    candidatos.add(sol.getBin(j));

            if (candidatos.isEmpty()) {
                //novo pacote se não há pacotes compatíveis
                sol.add(i, sol.size());
            } else {
                //ordena os pacotes compatíveis  em ordem decrescente de carga
                Collections.sort(candidatos, Comparator.comparingInt(x -> -x.getLoad()));
                // sorteia um entre os k primeiros
                int x = Utils.rd.nextInt(Math.min(candidatos.size(), k));
                // adiciona o item i neste pacote
                sol.add(i, candidatos.get(x));
            }
        }


        assert sol.isFeasible() : "Solução inviável";
        return sol.size();
    }


    @Override
    public int run(Sol best) {
        bpp = best.bpp;
        Sol current = new Sol(bpp);
        VND vnd = new VND(bpp);
        int lb = bpp.LB();

        greedyRandom(current, lb);
        vnd.run(current);
        best.copy(current);
        if (best.size() > lb)
            for (int i = 1; i < ite; i++) {

                greedyRandom(current, lb);
                vnd.run(current);

                if (current.size() < best.size()) {
                    best.copy(current);
                    System.out.println(i + " GRASP: " + current.size());
                    if (current.size() == lb)
                        break;
                }
            }
        return best.size();
    }


}
