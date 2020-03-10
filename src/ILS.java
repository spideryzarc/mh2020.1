/**
 * Iterated Local Search
 */
public class ILS implements Solver {
    int ite;
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
        HC hc = new HC(bpp);

        bpp.trivial(best);
        hc.run(best);

        Sol current = new Sol(bpp);
        current.copy(best);

        for (int i = 1; i <= ite; i++) {
            pertub(current);
            hc.run(current);
            if (current.size() < best.size()) {
                best.copy(current);
                System.out.println(i + " HC " + best.size());
            }
        }
        return best.size();
    }

    private void pertub(Sol current) {
        for (int i = 0; i < k; i++) {
            int a, b;
            do {
                a = Utils.rd.nextInt(bpp.w.length);
                b = Utils.rd.nextInt(bpp.w.length);
            } while (current.binOf(a)== current.binOf(b) ||
                    current.binOf(a).getLoad()-bpp.w[a]+bpp.w[b] > bpp.C ||
                    current.binOf(b).getLoad()-bpp.w[b]+bpp.w[a] > bpp.C);
            current.swap(a,b);
        }
    }
}

