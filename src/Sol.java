import java.util.ArrayList;

import static java.util.Arrays.fill;

/**
 * Representação de uma solução para o bpp
 */
public class Sol {
    private BPP bpp;
    private ArrayList<Bin> bins = new ArrayList<>();
    /**
     * binOf[i] é o pacote do item i
     */
    private Bin binOf[];

    @Override
    public String toString() {
        return bins.size() + " " + bins.toString();
    }

    /**
     * adiciona item i no pacote b, se i já não pertencer a qualquer pacote, se o
     * pacote j já estiver aberto ou seja o próximo a ser aberto e se não violar a
     * capacidade do pacote j.
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

        return add(i, bins.get(b));
    }

    public boolean add(int i, Bin b) {
        if (binOf[i] != null) {
            System.err.println("Item já alocado: " + i);
            return false;
        }

        if (b.load + bpp.w[i] > bpp.C) {
            //System.err.println("Capacidade insuficiente");
            return false;
        }
        b.add(i);
        binOf[i] = b;

        return true;
    }


    public Sol(BPP bpp) {
        this.bpp = bpp;
        binOf = new Bin[bpp.w.length];
    }

    public void reset() {
        fill(binOf, null);
        bins.clear();
    }

    public int size() {
        return bins.size();
    }

    public Bin binOf(int i) {
        return binOf[i];
    }

    public ArrayList<Bin> bins() {
        return bins;
    }

    /**
     * remove o item i da solução
     */
    public boolean remove(int i) {
        Bin b = binOf[i];
        if (b == null) {
            System.err.println("remover item não alocado");
            return false;
        }
        b.remove(i);
        binOf[i] = null;
        if (b.itens.size() == 0)
            bins.remove(b);
        return true;
    }

    /**
     * Representa um pacote
     */
    class Bin {
        ArrayList<Integer> itens = new ArrayList<>();
        /**
         * soma dos pesos dos itens  neste pacote
         */
        private int load;

        public int getLoad() {
            return load;
        }

        public void add(int i) {
            itens.add(i);
            load += bpp.w[i];
        }

        @Override
        public String toString() {
            return load + ":" + itens;
        }

        public void remove(Integer i) {
            itens.remove(i);
            load -= bpp.w[i];
        }
    }
}


