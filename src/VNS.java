/**
 * Variable Neighborhood Search
 */
public class VNS implements Solver {
    BPP bpp;
    /**
     * número de iterações
     */
    int ite;


    public VNS(int ite) {
        this.ite = ite;
    }

    @Override
    public String toString() {
        return "VNS{" +
                "ite=" + ite +
                '}';
    }

    @Override
    public int run(Sol best) {
        bpp = best.bpp;
        VND vnd = new VND(bpp);

        bpp.firstFit(best);
        vnd.run(best);

        Sol current = new Sol(bpp);
        current.copy(best);
        double bestStd = current.std();
        int neig = 1;
        int noImpCount = 0;
        int nc = ite / 4;
        int lb = bpp.LB();
        if (lb < best.size())
            for (int i = 1; i <= ite; i++) {

                //Aplica pertubação
                if (neig == 1)
                    openBin(current);
                else if (neig == 2)
                    randomSwaps(current, 10);
                else if (neig == 3)
                    randomSwaps(current, 20);
                else
                    randomSwaps(current, 30);

                vnd.run(current);
                double d = current.std();
                if (current.size() < best.size() ||
                        (current.size() == best.size() && d - bestStd > .001)) {
                    best.copy(current);
                    System.out.println(i + " " + neig + " VNS " + best.size() + "\t" + d);
                    neig = 1;
                    noImpCount = 0;
                    bestStd = d;
                    if (lb == best.size())
                        break;
                } else {
                    current.copy(best);
                    noImpCount++;
                }

                if (noImpCount > nc) {
                    neig++;
                    noImpCount = 0;
                }

            }

        return best.size();
    }


    /**
     * Provoca p trocas entre dois itens aleatórios alocados em
     * pacotes diferentes
     */
    private void randomSwaps(Sol sol, int p) {
        sol.setAllFlags(false);
        for (int i = 0; i < p; i++) {
            int a, b;
            do {
                a = Utils.rd.nextInt(bpp.w.length);
                b = Utils.rd.nextInt(bpp.w.length);
            } while (sol.binOf(a) == sol.binOf(b) ||
                    sol.binOf(a).getLoad() - bpp.w[a] + bpp.w[b] > bpp.C ||
                    sol.binOf(b).getLoad() - bpp.w[b] + bpp.w[a] > bpp.C);
            sol.swap(a, b);
        }
        assert sol.isFeasible() : "Solução inviável";
    }


    /**
     * Cria um novo pacote vazio e o preenche com itens em ordem aleatória
     */
    static void openBin(Sol sol) {
        BPP bpp =sol.bpp;
        int idx[] = bpp.idx;
        Utils.shuffle(idx);
        sol.remove(idx[0]);
        int s = sol.size();
        sol.add(idx[0], s);
        Sol.Bin bs = sol.getBin(s);
        for (int i = 1; i < bpp.N; i++) {
            if (bs.getLoad() + bpp.w[idx[i]] <= bpp.C) {
                sol.remove(idx[i]);
                sol.add(idx[i], bs);
            }
        }
        assert sol.isFeasible() : "Solução inviável";
    }


}
