import java.util.ArrayList;

import static java.util.Arrays.fill;

/**
 * Representação de uma solução para o bpp
 */
public class Sol {
    BPP bpp;

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
     *
     * @return true sse o item for inserido no pacote
     * @throws NullPointerException se o item i já estiver alocado
     */
    public boolean add(int i, int b) {
        if (binOf[i] != null) {
            throw new NullPointerException("Item já alocado: " + i);
        }

        if (bins.size() < b) {
            throw new NullPointerException("Pacote não aberto");
        } else if (bins.size() == b) {
            //abre novo pacote
            bins.add(new Bin());
        }

        return add(i, bins.get(b));
    }

    /**
     * adiciona o item i no pacote b se i não estiver alocado e não exceder a
     * capacidade de b.
     *
     * @return true sse o item for inserido no pacote
     * @throws NullPointerException se o item i já estiver alocado
     */
    public boolean add(int i, Bin b) {
        if (binOf[i] != null) {
            throw new NullPointerException("Item já alocado: " + i);
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

    /**
     * Faz a solução se tornar vazia
     */
    public void reset() {
        fill(binOf, null);
        bins.clear();
    }


    /**
     * remove o item i da solução
     */
    public boolean remove(int i) {
        Bin b = binOf[i];
        if (b == null) {
            throw new NullPointerException("remover item não alocado");
        }
        b.remove(i);
        binOf[i] = null;
        if (b.itens.size() == 0)
            bins.remove(b);
        return true;
    }

    public void copy(Sol src) {
        this.reset();
        for (int b = 0; b < src.size(); b++) {
            for (int i : src.bins.get(b).itens) {
                this.add(i, b);
            }
        }
    }

    /**troca os pacotes dos items a e b */
    public void swap(int a, int b) {
        Bin ba = binOf[a];
        Bin bb = binOf[b];
        ba.remove(a);
        bb.remove(b);
        ba.add(b);
        bb.add(a);
        binOf[a] = bb;
        binOf[b] = ba;
    }

    /**
     * @return desvio padrão das cargas dos pacotes
     */
    public double std() {
        double avg = (double) bpp.wSum / size();
        double s = 0;
        for (Bin b : bins) {
            double x = b.load - avg;
            s += x * x;
        }
        return s;
//        return s/size() ;
    }

    /**
     * Representa um pacote
     */
    class Bin {
        /**
         * flag para otimização de buscas sucessivas
         */
        public boolean flag = true;

        private ArrayList<Integer> itens = new ArrayList<>();
        /**
         * soma dos pesos dos itens  neste pacote
         */
        private int load;

        public int getLoad() {
            return load;
        }

        private void add(int i) {
            flag = true;
            itens.add(i);
            load += bpp.w[i];
        }

        @Override
        public String toString() {
            return load + ":" + itens;
        }


        private void remove(Integer i) {
            flag = true;
            itens.remove(i);
            load -= bpp.w[i];
        }

        public int size() {
            return itens.size();
        }

        /**
         * @retorn o k ésimo item do pacote
         */
        public int getItem(int k) {
            return itens.get(k);
        }
    }

    public int size() {
        return bins.size();
    }

    public Bin binOf(int i) {
        return binOf[i];
    }

    public ArrayList<Bin> getBins(ArrayList<Bin> list) {
        list.clear();
        list.addAll(bins);
        return list;
    }

    public void setAllFlags(boolean v) {
        for (Bin b : bins)
            b.flag = v;
    }

    public Bin getBin(int index) {
        return bins.get(index);
    }
}


