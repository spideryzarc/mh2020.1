public class RMS {
    BPP bpp;
    int ite;

    public RMS(BPP bpp, int ite) {
        this.bpp = bpp;
        this.ite = ite;
    }

    public void run(Sol best){
        HC hc = new HC(bpp);
        bpp.trivial(best);
        Sol sol = new Sol(bpp);

        int order[] = new int[bpp.w.length];
        for (int i = 0; i < order.length; i++)
            order[i] = i;


        for (int i = 1; i <= ite; i++) {
            Utils.shuffle(order);
            bpp.firstFit(sol,order);
            hc.run(sol);
            if(sol.size() < best.size()){
                best.copy(sol);
                System.out.println(i+" HC "+best.size());
            }
        }

    }

}
