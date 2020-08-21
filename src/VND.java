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
            Utils.shuffle(bpp.idx);//embaralha indices
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
                        double delta = deltaDev(media, l0i, lfi) + deltaDev(media, l0j, lfj);
                        if (delta > 0.001) {
                            sol.remove(i);
                            sol.add(i, bj);
                            assert sol.isFeasible() : "Solução inviável";
                            return true;
                        }

                    }
                }
        }
        return false;
    }

//    /**
//     * Vizinhaça 2 - troca dois itens de pacotes diferentes
//     */
//    private boolean NBH2_old(Sol sol) {
//        double media = (double) bpp.wSum / sol.size();
//        for (int i = 0; i < bpp.w.length; i++) {
//            Sol.Bin bi = sol.binOf(i);
//            for (int j = i + 1; j < bpp.w.length; j++) {
//                Sol.Bin bj = sol.binOf(j);
//                if (bi != bj && (bi.flag || bj.flag) &&
//                        bi.getLoad() + bpp.w[j] - bpp.w[i] <= bpp.C &&
//                        bj.getLoad() + bpp.w[i] - bpp.w[j] <= bpp.C) {
//                    int l0i = bi.getLoad();
//                    int lfi = l0i - bpp.w[i] + bpp.w[j];
//                    int l0j = bj.getLoad();
//                    int lfj = l0j - bpp.w[j] + bpp.w[i];
//                    double delta = deltaDev(media, l0i, lfi) + deltaDev(media, l0j, lfj);
//                    if (delta > 0.001) {
//                        sol.swap(i, j);
//                        assert sol.isFeasible() : "Solução inviável";
//                        return true;
//                    }
//                }
//            }
//        }
//        return false;
//    }

    /**
     * Vizinhaça 2 - troca dois itens de pacotes diferentes
     */
    private boolean NBH2(Sol sol) {
        int s = sol.size();
        double media = (double) bpp.wSum / s;

        for (int aa = 0; aa < s; aa++) {
            Sol.Bin a = sol.getBin(aa);
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
                                double delta = deltaDev(media, l0i, lfi) + deltaDev(media, l0j, lfj);
                                if (delta > 0.001) {
                                    sol.swap(i, j);
                                    assert sol.isFeasible() : "Solução inviável";
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
                                double delta = deltaDev(media, l0i, lfi) + deltaDev(media, l0j, lfj);
                                if (delta > 0.001) {
                                    sol.remove(k);
                                    sol.swap(i, l);
                                    sol.add(k, a);
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

        return false;
    }


}

