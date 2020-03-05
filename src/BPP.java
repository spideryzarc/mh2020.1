import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Bin Packing Problem - Problema de empacotamento
 */
public class BPP {
    /**
     * capacidade
     */
    int C;
    /**
     * pesos
     */
    int w[];

    public BPP(int n, int C) {
        this.C = C;
        w = new int[n];
    }

    public int LB() {
        int s = 0;
        for (int x : w)
            s += x;
        return (int) Math.ceil((double) s / C);
    }


    @Override
    public String toString() {
        return "BPP{" +
                "C=" + C +
                ", w=" + Arrays.toString(w) +
                '}';
    }

    public BPP(String path) throws FileNotFoundException {
        System.out.println(path);
        Scanner sc = new Scanner(new File(path));
        int n = sc.nextInt();
        C = sc.nextInt();
        w = new int[n];
        for (int i = 0; i < n; i++) {
            w[i] = sc.nextInt();
        }
        sc.close();
    }


    public void firstFit(Sol sol, int order[]) {
        sol.reset();
        for (int i : order)
            for (int j = 0; j <= sol.size(); j++)
                if (sol.add(i, j))
                    break;
    }

    public static void main(String args[]) throws FileNotFoundException {
//        BPP bpp = new BPP("/home/einstein/Documentos/Hard28/Hard28_BPP13.txt");
        BPP bpp = new BPP("/home/einstein/Documentos/rand/BPP_50_50_0.1_0.7_0.txt");
        System.out.println(bpp);
        System.out.println("LB "+bpp.LB());

        Sol sol = new Sol(bpp);

        int order[] = new int[bpp.w.length];
        for (int i = 0; i < order.length; i++) {
            order[i] = i;
        }

        Utils.shuffle(order);
        bpp.trivial(sol);
        System.out.println(sol);

        HC hc = new HC(bpp);
        hc.run(sol);


        System.out.println(sol);


    }

    private void trivial(Sol sol) {
        sol.reset();
        for (int i = 0; i < w.length; i++) {
            sol.add(i,i);
        }
    }
}
