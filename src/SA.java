/**
 * Simulated Annealing
 */
public class SA implements Solver {
    private BPP bpp;
    double T0, Tf;
    int ite;
    VND vnd;


    /**
     * ite - número de iterações
     * T0 - Temperatura inicial
     * Tf - temperatura final
     */
    public SA(int ite, double T0, double Tf) {
        this.T0 = T0;
        this.Tf = Tf;
        this.ite = ite;
    }

    @Override
    public String toString() {
        return "SA{" +
                "T0=" + T0 +
                ", Tf=" + Tf +
                ", ite=" + ite +
                '}';
    }

    @Override
    public int run(Sol best) {
        bpp = best.bpp;
        vnd = new VND(bpp);

        bpp.firstFit(best);

        Sol curr = new Sol(bpp);
        curr.copy(best);

        double bestStd = best.std();
        double currStd = bestStd;

        int bestSize = best.size();


        double lambda = Math.pow(Tf / T0, 1.0 / ite);

        Sol aux = new Sol(bpp);
        int lb = bpp.LB();
        for (double T = T0; T > Tf && lb < best.size(); T *= lambda) {
            shake(curr, aux);
            double auxStd = aux.std();
            if ((auxStd > currStd && aux.size() == bestSize) || aux.size() < bestSize) {
                curr.copy(aux);
                currStd = auxStd;

                if (aux.size() < bestSize) {
                    bestSize = aux.size();
                    best.copy(curr);
                    System.out.println("SA: " + bestSize);
                    T = T0;
                }
                //System.out.println(T + " "+currFA);
            } else if (.001 < currStd - auxStd)
                if (Utils.rd.nextDouble() < P(auxStd - currStd, T)) {
                    curr.copy(aux);
                    currStd = auxStd;
//                    System.out.println(T + " "+currStd +" *");
                }


        }

        assert best.isFeasible() : "Solução inviável";
        return best.size();
    }

    /**delta - variação na função objetivo
     * t - temperatura do sistema
     * @return  probabilidade de aceitar um piora na função objetivo  */
    private double P(double delta, double t) {
        return Math.exp(delta / t);
    }

    /**
     * aux recebe uma versão pertubada  de curr
     */
    private void shake(Sol curr, Sol aux) {
        aux.copy(curr);
        VNS.openBin(aux);
        vnd.run(aux);
        assert aux.isFeasible() : "Solução inviável";
    }


}
