import java.util.ArrayList;

import static java.util.Arrays.fill;

/**
 * Representação de uma solução para o bpp
 */
public class Sol {
    private BPP bpp;
    private ArrayList<Bin> bins = new ArrayList<>();
    /**binOf[i] é o pacote do item i*/
    private Bin binOf[];

    @Override
    public String toString() {
        return bins.toString();
    }

    /**
     * adiciona item i no pacote b
     */
    public boolean add(int i, int b) {
        if (binOf[i] != null) {
            System.err.println("Item já alocado: " + i);
            return false;
        }

        if (bins.size() < b) {
            System.err.println("Pacote não aberto " + b);
            return false;
        } else if (bins.size() == b) {
            //abre novo pacote
            bins.add(new Bin());
        }

        if (bins.get(b).load + bpp.w[i] > bpp.C) {
            System.err.println("Capacidade insuficiente");
            return false;
        }



        bins.get(b).add(i);
        binOf[i] = bins.get(b);

        return true;
    }



    public Sol(BPP bpp) {
        this.bpp = bpp;
        binOf = new Bin[bpp.w.length];
    }

    /**Representa um pacote */
    class Bin{
        ArrayList<Integer> itens = new ArrayList<>();
        int load;
        private int load() {
            int s = 0;
            for (int i : itens)
                s += bpp.w[i];
            return s;
        }

        public void add(int i) {
            itens.add(i);
            load+= bpp.w[i];
        }

        @Override
        public String toString() {
            return load+":"+itens;
        }
    }
}


