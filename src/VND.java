import java.util.ArrayList;

/**
 * Variable Neighborhood Descendant
 */
public class VND {
    BPP bpp;
    private ArrayList<Sol.Bin> bins = new ArrayList<>();

    public VND(BPP bpp) {
        this.bpp = bpp;
    }

    public void run(Sol sol) {
        int fo = sol.size();
        boolean imp;
        do {
            imp = NBH1(sol);
            if (!imp)
                imp = NBH2(sol);
            if (!imp)
                imp = NBH3(sol);
        } while (imp);
    }


    /**
     * @param avg load médio
     * @param l0  load inicial (antes da alteração)
     * @param lf  load final (depois da alteração)
     * @return variação no desvio padrão depois da alteração
     */
    private static final double deltaDev(double avg, int l0, int lf) {
        double d0 = (l0 - avg);
        double df = (lf - avg);
        return df * df - d0 * d0;
    }

    /**
     * Vizinhaça 1 - move um item de um pacote para outro
     */
    private boolean NBH1(Sol sol) {
        bins = sol.getBins(bins);
        double media = (double) bpp.wSum / sol.size();
        for (int j = 0; j < bins.size(); j++) {
            Sol.Bin bj = bins.get(j);
            if (bj.flag)
                for (int i = 0; i < bpp.w.length; i++) {
                    Sol.Bin bi = sol.binOf(i);
                    if (bj != bi && bj.getLoad() + bpp.w[i] <= bpp.C) {
                        int l0i = sol.binOf(i).getLoad();
                        int lfi = l0i - bpp.w[i];
                        int l0j = bj.getLoad();
                        int lfj = l0j + bpp.w[i];
                        double delta = deltaDev(media, l0i, lfi) + deltaDev(media, l0j, lfj);
                        if (delta > 0.001) {
                            sol.remove(i);
                            sol.add(i, bj);
                            return true;
                        }

                    }
                }
        }
        return false;
    }

    /**
     * Vizinhaça 2 - troca dois itens de pacotes diferentes
     */
    private boolean NBH2_old(Sol sol) {
        double media = (double) bpp.wSum / sol.size();
        for (int i = 0; i < bpp.w.length; i++) {
            Sol.Bin bi = sol.binOf(i);
            for (int j = i + 1; j < bpp.w.length; j++) {
                Sol.Bin bj = sol.binOf(j);
                if (bi != bj && (bi.flag || bj.flag) &&
                        bi.getLoad() + bpp.w[j] - bpp.w[i] <= bpp.C &&
                        bj.getLoad() + bpp.w[i] - bpp.w[j] <= bpp.C) {
                    int l0i = bi.getLoad();
                    int lfi = l0i - bpp.w[i] + bpp.w[j];
                    int l0j = bj.getLoad();
                    int lfj = l0j - bpp.w[j] + bpp.w[i];
                    double delta = deltaDev(media, l0i, lfi) + deltaDev(media, l0j, lfj);
                    if (delta > 0.001) {
                        sol.swap(i, j);
                        return true;
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
        double media = (double) bpp.wSum / sol.size();
        int s = sol.size();
        for (int a = 0; a < s; a++) {
            Sol.Bin bi = sol.getBin(a);
            for (int b = a + 1; b < s; b++) {
                Sol.Bin bj = sol.getBin(b);
                if (bi.flag || bj.flag) {
                    for (int ii = 0, ilen = bi.size(); ii < ilen; ii++)
                        for (int jj = 0, jlen = bj.size(); jj < jlen; jj++) {
                            int i = bi.getItem(ii);
                            int j = bj.getItem(jj);
                            if (bi.getLoad() + bpp.w[j] - bpp.w[i] <= bpp.C &&
                                    bj.getLoad() + bpp.w[i] - bpp.w[j] <= bpp.C) {
                                int l0i = bi.getLoad();
                                int lfi = l0i - bpp.w[i] + bpp.w[j];
                                int l0j = bj.getLoad();
                                int lfj = l0j - bpp.w[j] + bpp.w[i];
                                double delta = deltaDev(media, l0i, lfi) + deltaDev(media, l0j, lfj);
                                if (delta > 0.001) {
                                    sol.swap(i, j);
                                    return true;
                                }
                            }
                        }
                }
            }
        }
        return false;
    }

    /**
     * Vizinhaça 3 - troca dois itens de um pacote por um de outro pacote
     */
    private boolean NBH3(Sol sol) {
        bins = sol.getBins(bins);
        double media = (double) bpp.wSum / sol.size();
        for (int i = 0; i < bpp.w.length; i++) {
            Sol.Bin bi = sol.binOf(i);
            for (int j = 0; j < bins.size(); j++) {
                Sol.Bin bj = bins.get(j);
                if (bi != bj && (bi.flag || bj.flag)) {
                    for (int k = 0; k < bj.size(); k++) {
                        for (int l = k + 1; l < bj.size(); l++) {
                            int itemk = bj.getItem(k);
                            int iteml = bj.getItem(l);
                            if (bi.getLoad() - bpp.w[i] + bpp.w[itemk] + bpp.w[iteml] <= bpp.C &&
                                    bj.getLoad() + bpp.w[i] - bpp.w[itemk] - bpp.w[iteml] <= bpp.C) {
                                int l0i = bi.getLoad();
                                int lfi = l0i - bpp.w[i] + bpp.w[itemk] + bpp.w[iteml];
                                int l0j = bj.getLoad();
                                int lfj = l0j + bpp.w[i] - bpp.w[itemk] - bpp.w[iteml];
                                double delta = deltaDev(media, l0i, lfi) + deltaDev(media, l0j, lfj);
                                if (delta > 0.001) {
                                    sol.remove(itemk);
                                    sol.swap(i, iteml);
                                    sol.add(itemk, bi);
//                                    System.out.println("nbh3!!");
                                    return true;
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

