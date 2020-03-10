public class RMS implements Solver {
    BPP bpp;
    int ite;

    @Override
    public String toString() {
        return "RMS{" +
                "ite=" + ite +
                '}';
    }

    public RMS(int ite) {
        this.ite = ite;
    }

    public int run(Sol best) {
        bpp = best.bpp;
        HC hc = new HC(bpp);
        bpp.trivial(best);
        Sol sol = new Sol(bpp);

        int order[] = new int[bpp.w.length];
        for (int i = 0; i < order.length; i++)
            order[i] = i;


        for (int i = 1; i <= ite; i++) {
            Utils.shuffle(order);
            bpp.firstFit(sol, order);
            hc.run(sol);
            if (sol.size() < best.size()) {
                best.copy(sol);
                System.out.println(i + " HC " + best.size());
            }
        }
        return best.size();
    }
}
