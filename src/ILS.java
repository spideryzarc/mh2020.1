/**
 * Iterated Local Search
 */
public class ILS implements Solver {
    /**número de iterações*/
    int ite;
    /**número de swaps em cada pertubação*/
    int k;


    public ILS(int ite, int k) {
        this.ite = ite;
        this.k = k;
    }

    @Override
    public String toString() {
        return "ILS{" +
                "ite=" + ite +
                ", k=" + k +
                '}';
    }

    BPP bpp;

    @Override
    public int run(Sol best) {
        bpp = best.bpp;
        VND vnd = new VND(bpp);

        bpp.firstFit(best);
        vnd.run(best);

        Sol current = new Sol(bpp);
        current.copy(best);
        int lb = bpp.LB();
        for (int i = 1; i <= ite && lb < best.size(); i++) {
            shake(current);
            vnd.run(current);
            if (current.size() < best.size()) {
                best.copy(current);
                System.out.println(i + " ILS " + best.size());
            }
        }
        return best.size();
    }

    /**Provoca k trocas entre dois itens aleatórios alocados em
     * pacotes diferentes */
    private void shake(Sol sol) {
        sol.setAllFlags(false);
        for (int i = 0; i < k; i++) {
            int a, b;
            do {
                a = Utils.rd.nextInt(bpp.w.length);
                b = Utils.rd.nextInt(bpp.w.length);
            } while (sol.binOf(a)== sol.binOf(b) ||
                    sol.binOf(a).getLoad()-bpp.w[a]+bpp.w[b] > bpp.C ||
                    sol.binOf(b).getLoad()-bpp.w[b]+bpp.w[a] > bpp.C);
            sol.swap(a,b);
        }
        assert sol.isFeasible() : "Solução inviável";
    }
}

