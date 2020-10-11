import java.util.ArrayList;
import java.util.Arrays;

import static java.util.Arrays.fill;

/**
 * Representação de uma solução para o bpp
 */
public class Sol {
    BPP bpp;

    private final ArrayList<Bin> bins = new ArrayList<>();
    /**
     * binOf[i] é o pacote do item i
     */
    private final Bin[] binOf;


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
     * @throws AssertionError se o item i já estiver alocado
     */
    public boolean add(int i, int b) {
        assert (binOf[i] == null) : "Item já alocado: " + i;
        assert (bins.size() >= b) : "Pacote não aberto: " + b;
        if (bins.size() == b)
            bins.add(new Bin());
        return add(i, bins.get(b));
    }

    /**
     * adiciona o item i no pacote b se i não estiver alocado e não exceder a
     * capacidade de b.
     *
     * @return true sse o item for inserido no pacote
     * @throws AssertionError se o item i já estiver alocado
     */
    public boolean add(int i, Bin b) {
        assert (binOf[i] == null) : "Item já alocado: " + i;

        if (b.load + bpp.w[i] > bpp.C)
            return false;

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
     *
     * @throws AssertionError se tentar remover item não alocado
     */
    public void remove(int i) {
        Bin b = binOf[i];
        assert (b != null) : "remover item não alocado";

        b.remove(i);
        binOf[i] = null;
        if (b.itens.size() == 0)
            bins.remove(b);
    }

    public void copy(Sol src) {
        this.reset();
        for (int b = 0, len = src.size(); b < len; b++) {
            for (int i : src.bins.get(b).itens) {
                this.add(i, b);
            }
        }
        assert isFeasible() : "Solução inviável";
    }

    /**
     * troca os pacotes dos items a e b
     */
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
     * @return true se é uma solução viável de BPP
     */
    public boolean isFeasible() {
//        System.out.println("************************ ");
        for (Bin b : bins)
            if (b.load > bpp.C) {
                System.err.println("um dos pacotes excede a capacidade.");
                return false;
            }
        for (int i = 0; i < binOf.length; i++) {
            if (binOf[i] == null) {
                System.err.println("item " + i + " não está alocado.");
                return false;
            }
            if (!bins.contains(binOf[i])) {
                System.err.println("item alocado em pacote fora da solução.");
                return false;
            }
        }

        return true;
    }

    /**
     * Representa um pacote
     */
    class Bin {
        /**
         * flag para otimização de buscas sucessivas
         */
        public boolean flag = true;

        private final ArrayList<Integer> itens = new ArrayList<>();
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
            boolean r = itens.remove(i);
            assert r : "remoção de item que não está no pacote.";
            load -= bpp.w[i];
        }

        public int size() {
            return itens.size();
        }

        /**
         * @return o k-ésimo item do pacote
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sol sol = (Sol) o;
        for (int i = 0; i < bpp.N; i++) {
            for (int j = i + 1; j < bpp.N; j++) {
                if (binOf[i] == binOf[j] && sol.binOf[i] != sol.binOf[j])
                    return false;
            }
        }
        return true;

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(binOf);
    }
}


