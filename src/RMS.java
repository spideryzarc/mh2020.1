/**Random MultiStart*/
public class RMS implements Solver {
    BPP bpp;
    /**número de iterações */
    int ite;

    @Override
    public String toString() {
        return "RMS{" +
                "ite=" + ite +
                '}';
    }

    /**@param ite - número de iterações*/
    public RMS(int ite) {
        this.ite = ite;
    }

    /**@param best - receberá a melhor solução encontrada no processo*/
    public int run(Sol best) {
        bpp = best.bpp;
        VND vnd = new VND(bpp);

        bpp.firstFit(best);
        Sol sol = new Sol(bpp);

        int order[] = new int[bpp.w.length];
        for (int i = 0; i < order.length; i++)
            order[i] = i;

        int lb = bpp.LB();
        for (int i = 1; i <= ite && lb < best.size(); i++) {
            Utils.shuffle(order);
            bpp.firstFit(sol, order);
            vnd.run(sol);
            if (sol.size() < best.size()) {
                best.copy(sol);
                System.out.println(i + " RMS " + best.size());
            }
        }
        return best.size();
    }
}
