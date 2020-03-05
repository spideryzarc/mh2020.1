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
            imp = firstImp(sol, fo);


        } while (imp);
    }

    private boolean firstImp(Sol sol, int fo) {
        for (int i = 0; i < bpp.w.length; i++) {
            for (Sol.Bin b : sol.bins())
                if (b != sol.binOf(i)) {
                    if(b.getLoad()+bpp.w[i] <= bpp.C){
                        if(sol.binOf(i).itens.size() == 1){
                            sol.remove(i);
                            sol.add(i,b);
                        }
                    }
                }
        }
        return false;
    }

}
