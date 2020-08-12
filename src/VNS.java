/**
 * Variable Neighborhood Search
 */
public class VNS implements Solver {
    BPP bpp;
    /**
     * número de iterações
     */
    int ite;

    /**
     * número de vizinhanças
     */
    int k;

    /**
     * incremento na pertubação
     */
    int t;

    public VNS(int ite, int k, int t) {
        this.ite = ite;
        this.k = k;
        this.t = t;
    }

    @Override
    public String toString() {
        return "VNS{" +
                "ite=" + ite +
                ", k=" + k +
                ", t=" + t +
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
        int p = 1;
        int noImpCount = 0;
        int nc = ite / k;
        int lb = bpp.LB();
        for (int i = 1; i <= ite && lb < best.size(); i++) {
            pertub(current, p);
            vnd.run(current);
            double d = current.std();
            if (current.size() < best.size() ||
                    (current.size() == best.size() && d > bestStd + .001)) {
                best.copy(current);
                System.out.println(i + " " + p + " VNS " + best.size() + "\t" + d);
                p = 1;
                noImpCount = 0;
                bestStd = d;

            } else {
                current.copy(best);
            }

            if (noImpCount > nc) {
                p += t;
                noImpCount = 0;
            }
            noImpCount++;
        }

        return best.size();
    }

    /**
     * Provoca p trocas entre dois itens aleatórios alocados em
     * pacotes diferentes
     */
    private void pertub(Sol current, int p) {
        current.setAllFlags(false);
        for (int i = 0; i < p; i++) {
            int a, b;
            do {
                a = Utils.rd.nextInt(bpp.w.length);
                b = Utils.rd.nextInt(bpp.w.length);
            } while (current.binOf(a) == current.binOf(b) ||
                    current.binOf(a).getLoad() - bpp.w[a] + bpp.w[b] > bpp.C ||
                    current.binOf(b).getLoad() - bpp.w[b] + bpp.w[a] > bpp.C);
            current.swap(a, b);
        }
        assert current.isFeasible() : "Solução inviável";
    }


}
