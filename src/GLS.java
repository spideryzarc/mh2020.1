import java.util.ArrayList;

import static java.util.Arrays.fill;

/**
 * Guided Local Search
 */
public class GLS implements Solver {
    /**
     * número de iterações
     */
    int ite;


    /**
     * frequência em que dois itens foram alocados no mesmo pacote em ótimos locais
     */
    int p[][];


    public GLS(int ite) {
        this.ite = ite;
    }

    @Override
    public String toString() {
        return "GLS{" +
                "ite=" + ite +
                '}';
    }

    BPP bpp;

    @Override
    public int run(Sol best) {
        bpp = best.bpp;
        GuidedVND guidedVND = new GuidedVND(bpp);
        VND vnd = new VND(bpp);
        p = new int[bpp.N][];
        for (int i = 0; i < bpp.N; i++)
            p[i] = new int[i];

        bpp.firstFit(best);
        vnd.run(best);

        Sol current = new Sol(bpp);
        current.copy(best);
        extractPenalties(current);
        int lb = bpp.LB();
        double bestStd = best.std();
        for (int i = 1; i <= ite && lb < best.size(); i++) {

            double tmp = current.std();
            guidedVND.run(current);
            extractPenalties(current);
            vnd.run(current);

            double cstd = current.std();
            if (Math.abs(cstd - tmp) > .1) {//já escapou do ótimo local
                penaltyReset();
            }

            if (current.size() < best.size() || bestStd < cstd) {
                best.copy(current);
                bestStd = cstd;
                System.out.println(i + " GLS " + best.size() + " " + bestStd);
                penaltyReset();
            }
        }

        assert best.isFeasible() : "Solução inviável";
        return best.size();
    }

    /**
     * zera a matriz de penalidades
     */
    private void penaltyReset() {
        for (int i = 0; i < p.length; i++)
            fill(p[i], 0);
    }

    /**
     * @return penalidade da solução
     */
    private double penalty(Sol sol) {
        int s = 0;
        for (int i = 0; i < sol.size(); i++) {
            Sol.Bin b = sol.getBin(i);
            for (int j = 0, len = b.size(); j < len; j++) {
                int itemj = b.getItem(j);
                for (int k = j + 1; k < len; k++) {
                    if (itemj > b.getItem(k))
                        s += p[itemj][b.getItem(k)];
                    else
                        s += p[b.getItem(k)][itemj];

                }
            }
        }
        return (double) 1 / (1 + s);
    }

    /**
     * Seleciona um item aleatório e adiciona penalidades para mantê-lo junto
     * com os outros itens que dividem o mesmo pacote na solução corrente.
     */
    private void extractPenalties(Sol sol) {
        int j = Utils.rd.nextInt(bpp.N);
        Sol.Bin k = sol.binOf(j);
        for (int ii = 0, len = k.size(); ii < len; ii++) {
            int i = k.getItem(ii);
            if (i != j) {
                if (i > j) {
                    p[i][j]++;
                } else {
                    p[j][i]++;
                }
            }
        }

    }


    /**
     * Variable Neighborhood Descendant
     */
    public class GuidedVND {
        BPP bpp;
        private ArrayList<Sol.Bin> bins = new ArrayList<>();

        public GuidedVND(BPP bpp) {
            this.bpp = bpp;
        }

        /**
         * valor da função de avaliação da solução corrente
         */
        double CAF;
        /**
         * quantidade de pacotes da solução corrente
         */
        int cSize;

        public void run(Sol sol) {
            boolean imp;
            CAF = sol.std() * penalty(sol);
            cSize = sol.size();
            do {
                Utils.shuffle(bpp.idx);//embaralha indices
                imp = NBH1(sol);
                if (!imp)
                    imp = NBH2(sol);
            } while (imp);
        }


        /**
         * Vizinhança 1 - move um item de um pacote para outro
         */
        private boolean NBH1(Sol sol) {
            int s = sol.size();
            double media = (double) bpp.wSum / s;
            for (int j = 0; j < s; j++) {
                Sol.Bin bj = sol.getBin(j);
                int lack = bpp.C - bj.getLoad();
                for (int i : bpp.idx)//indices em ordem aleatória
                    if (bpp.w[i] <= lack) {
                        Sol.Bin bi = sol.binOf(i);
                        if (bj != bi) {

                            sol.remove(i);
                            sol.add(i, bj);
                            if (sol.size() >= cSize && 0.001 > penalty(sol) * sol.std() - CAF) {
                                //não melhorou a função de avaliação penalizada e não
                                //diminuiu a quantidade de pacotes desfaz o movimento
                                sol.remove(i);
                                sol.add(i, bi);

                            } else {
                                CAF = penalty(sol) * sol.std();
                                if (cSize > sol.size()) {
                                    cSize = sol.size();
                                    penaltyReset();
                                }
                                assert sol.isFeasible() : "Solução inviável";
                                return true;
                            }

                        }
                    }
            }
            return false;
        }


        /**
         * Vizinhança 2 - troca dois itens de pacotes diferentes
         */
        private boolean NBH2(Sol sol) {
            int s = sol.size();
            double media = (double) bpp.wSum / s;
            boolean flag = false;
            for (int aa = 0; aa < s; aa++) {
                Sol.Bin a = sol.getBin(aa);
                aqui:
                for (int bb = aa + 1; bb < s; bb++) {
                    Sol.Bin b = sol.getBin(bb);

                    for (int ii = 0, ilen = a.size(); ii < ilen; ii++)
                        for (int jj = 0, jlen = b.size(); jj < jlen; jj++) {
                            int i = a.getItem(ii);
                            int j = b.getItem(jj);
                            if (a.getLoad() + bpp.w[j] - bpp.w[i] <= bpp.C &&
                                    b.getLoad() + bpp.w[i] - bpp.w[j] <= bpp.C) {
                                sol.swap(i, j);
                                if (0.001 > penalty(sol) * sol.std() - CAF) {
                                    //não melhorou a função de avaliação penalizada e não
                                    //diminuiu a quantidade de pacotes desfaz o movimento
                                    sol.swap(i, j);
                                } else {
                                    CAF = penalty(sol) * sol.std();
                                    if (cSize > sol.size()) {
                                        cSize = sol.size();
                                        penaltyReset();
                                    }
                                    assert sol.isFeasible() : "Solução inviável";
                                    flag = true;
                                    continue aqui;
                                    //return true;
                                }
                            }
                        }

                }
            }
            return flag;
        }


    }


}

