import java.util.ArrayList;

/**
 * hill climbing
 */
public class HC {
    BPP bpp;

    public HC(BPP bpp) {
        this.bpp = bpp;
    }

    public void run(Sol sol) {
        int fo = sol.size();
        boolean imp;
        do {
            imp = firstImp3(sol, fo);
        } while (imp);
    }

    private ArrayList<Sol.Bin> bins = new ArrayList<>();

    private boolean firstImp1(Sol sol, int fo) {
        bins = sol.getBins(bins);
        for (int i = 0; i < bpp.w.length; i++)
            if (sol.binOf(i).size() == 1) {
                for (int j = 0; j < bins.size(); j++) {
                    Sol.Bin b = bins.get(j);
                    if (b != sol.binOf(i) && b.getLoad() + bpp.w[i] <= bpp.C) {
                        sol.remove(i);
                        sol.add(i, b);
                        return true;
                    }
                }
            }
        return false;
    }

    private boolean firstImp2(Sol sol, int fo) {
        bins = sol.getBins(bins);

        int minLoad = Integer.MAX_VALUE;
        for (Sol.Bin b : bins)
            if (minLoad > b.getLoad())
                minLoad = b.getLoad();

        for (int i = 0; i < bpp.w.length; i++)
            if (sol.binOf(i).getLoad() - bpp.w[i] < minLoad) {
                for (int j = 0; j < bins.size(); j++) {
                    Sol.Bin b = bins.get(j);
                    if (b != sol.binOf(i) && b.getLoad() + bpp.w[i] <= bpp.C) {
                        sol.remove(i);
                        sol.add(i, b);
                        return true;
                    }
                }
            }
        return false;
    }

    private boolean firstImp3(Sol sol, int fo) {
        bins = sol.getBins(bins);
        double media = (double) bpp.wSum / sol.size();

        for (int i = 0; i < bpp.w.length; i++) {
            for (int j = 0; j < bins.size(); j++) {
                Sol.Bin b = bins.get(j);
                if (b != sol.binOf(i) && b.getLoad() + bpp.w[i] <= bpp.C) {
                    int l0i = sol.binOf(i).getLoad();
                    int lfi = l0i - bpp.w[i];
                    int l0j = b.getLoad();
                    int lfj = l0j + bpp.w[i];
                    double d0 = (l0i - media) * (l0i - media) + (l0j - media) * (l0j - media);
                    double df = (lfi - media) * (lfi - media) + (lfj - media) * (lfj - media);
                    double delta = df - d0;
                    if (delta > 0.001) {
                        sol.remove(i);
                        sol.add(i, b);
                        return true;
                    }

                }
            }
        }
        return false;
    }
}
