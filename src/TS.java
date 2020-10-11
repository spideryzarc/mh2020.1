import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Tabu Search
 */
public class TS implements Solver {
    BPP bpp;
    /**
     * número de iterações
     */
    int ite;

    /**
     * número de trocas por pertubação
     */
    int K;


    /**
     * número máximo de ótimos locais armazenados na lista tabu
     */
    int tenure;

    /**
     * lista de ótimos locais (tabus)
     */
    LinkedList<Sol> list = new LinkedList<>();

    public TS(int ite, int k, int tenure) {
        this.ite = ite;
        K = k;
        this.tenure = tenure;
    }

    @Override
    public String toString() {
        return "TS{" +
                "ite=" + ite +
                ", K=" + K +
                ", tenure=" + tenure +
                '}';
    }

    @Override
    public int run(Sol best) {
        bpp = best.bpp;
        list.clear();
        VNDtabu vnd = new VNDtabu(bpp);

        bpp.firstFit(best);
        vnd.run(best);
        addTabu(best);

        Sol current = new Sol(bpp);
        current.copy(best);
        double bestStd = best.std();
        int lb = bpp.LB();
        for (int i = 1; i <= ite && lb < best.size(); i++) {
            ILS.shake(current, K);
            vnd.run(current);
            double curStd = current.std();
            if (curStd > bestStd || current.size() < best.size()) {
                best.copy(current);
                addTabu(best);
                bestStd = curStd;
                System.out.println(i + " Tabu " + best.size() + "\t" + bestStd);
            }

        }
        assert best.isFeasible() : "Solução inviável";
        return best.size();
    }

    private void addTabu(Sol sol) {
        Sol aux;
        if (list.size() >= tenure)
            aux = list.pollFirst();
        else
            aux = new Sol(sol.bpp);
        aux.copy(sol);
        list.addLast(aux);
    }

    private boolean isTabu(Sol sol) {
        return list.contains(sol);
    }

    class VNDtabu {
        BPP bpp;
        private ArrayList<Sol.Bin> bins = new ArrayList<>();


        public VNDtabu(BPP bpp) {
            this.bpp = bpp;
        }

        public void run(Sol sol) {
            int fo = sol.size();
            boolean imp;
            do {
                Utils.shuffle(bpp.idx);//embaralha indices
                imp = NBH1(sol);
                if (!imp)
                    imp = NBH2(sol);
                if (!imp)
                    imp = NBH3(sol);
            } while (imp);
        }

        /**
         * Vizinhaça 1 - move um item de um pacote para outro
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
                        if (bj != bi && (bi.flag || bj.flag)) {
                            int l0i = bi.getLoad();
                            int lfi = l0i - bpp.w[i];
                            int l0j = bj.getLoad();
                            int lfj = l0j + bpp.w[i];
                            double delta = Utils.deltaDev(media, l0i, lfi) + Utils.deltaDev(media, l0j, lfj);
                            if (delta > 0.001) {
                                sol.remove(i);
                                sol.add(i, bj);
                                if (isTabu(sol)) {
                                    //desfazer a troca
                                    sol.remove(i);
                                    sol.add(i, bi);
                                } else {
                                    assert sol.isFeasible() : "Solução inviável";
                                    return true;
                                }
                            }

                        }
                    }
            }
            return false;
        }

        /**
         * Vizinhaça 2 - troca dois itens de pacotes diferentes
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
                    if (a.flag || b.flag) {
                        for (int ii = 0, ilen = a.size(); ii < ilen; ii++)
                            for (int jj = 0, jlen = b.size(); jj < jlen; jj++) {
                                int i = a.getItem(ii);
                                int j = b.getItem(jj);
                                if (a.getLoad() + bpp.w[j] - bpp.w[i] <= bpp.C &&
                                        b.getLoad() + bpp.w[i] - bpp.w[j] <= bpp.C) {
                                    int l0i = a.getLoad();
                                    int lfi = l0i - bpp.w[i] + bpp.w[j];
                                    int l0j = b.getLoad();
                                    int lfj = l0j - bpp.w[j] + bpp.w[i];
                                    double delta = Utils.deltaDev(media, l0i, lfi) + Utils.deltaDev(media, l0j, lfj);
                                    if (delta > 0.001) {
                                        sol.swap(i, j);
                                        if (isTabu(sol)) {
                                            //desfazer troca
                                            sol.swap(i, j);
                                        } else {
                                            assert sol.isFeasible() : "Solução inviável";
                                            flag = true;
//                                    return true;
                                            continue aqui;
                                        }
                                    }
                                }
                            }
                    }
                }
            }
            return flag;
        }

        /**
         * Vizinhaça 3 - troca dois itens de um pacote por um de outro pacote
         */
        private boolean NBH3(Sol sol) {
            final int s = sol.size();
            double media = (double) bpp.wSum / sol.size();
            for (int i : bpp.idx) {//indices em ordem aleatória
                Sol.Bin a = sol.binOf(i);
                for (int bb = 0; bb < s; bb++) {
                    Sol.Bin b = sol.getBin(bb);
                    if (a != b && (a.flag || b.flag)) {
                        for (int kk = 0; kk < b.size(); kk++) {
                            for (int ll = kk + 1; ll < b.size(); ll++) {
                                int k = b.getItem(kk);
                                int l = b.getItem(ll);
                                if (a.getLoad() - bpp.w[i] + bpp.w[k] + bpp.w[l] <= bpp.C &&
                                        b.getLoad() + bpp.w[i] - bpp.w[k] - bpp.w[l] <= bpp.C) {
                                    int l0i = a.getLoad();
                                    int lfi = l0i - bpp.w[i] + bpp.w[k] + bpp.w[l];
                                    int l0j = b.getLoad();
                                    int lfj = l0j + bpp.w[i] - bpp.w[k] - bpp.w[l];
                                    double delta = Utils.deltaDev(media, l0i, lfi) + Utils.deltaDev(media, l0j, lfj);
                                    if (delta > 0.001) {
                                        sol.remove(k);
                                        sol.swap(i, l);
                                        sol.add(k, a);
                                        if (isTabu(sol)) {
                                            sol.remove(k);
                                            sol.swap(i, l);
                                            sol.add(k, b);
                                        }  else {
//                                    System.out.println("nbh3!!");
                                            assert sol.isFeasible() : "Solução inviável";
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return false;
        }


    }
}
