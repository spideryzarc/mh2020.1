import java.io.*;
import java.util.ArrayList;
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

    /**
     * soma dos pesos
     */
    int wSum;

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
        wSum = 0;
        for (int i = 0; i < n; i++) {
            w[i] = sc.nextInt();
            wSum += w[i];
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

    public static void benchmark(String path, ArrayList<Solver> solvers) throws IOException {
        File dir = new File(path);
        File[] files = dir.listFiles();
        ArrayList<BPP> instances = new ArrayList<>();
        for (File f : files) {
            System.out.println(f.getName());
            try {
                instances.add(new BPP(f.getAbsolutePath()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


        FileWriter fw = new FileWriter(new File("result.txt"), true);

        for (Solver s : solvers) {
            int sumCost = 0;
            long t0 = System.currentTimeMillis();
            for (BPP bpp : instances) {
                Utils.rd.setSeed(7);
                sumCost += s.run(new Sol(bpp));
            }
            long t = System.currentTimeMillis() - t0;
            double avg = (double) sumCost / instances.size();
            System.out.println(s + " " + avg + " " + t);
            fw.write(s + " " + avg + " " + t + "\n");
        }
        fw.close();

    }

    public static void main(String args[]) throws IOException {
        ArrayList<Solver> solvers = new ArrayList<>();
        solvers.add(new RMS(5));
//        solvers.add(new ILS(5, 1));
//        benchmark("/home/einstein/Documentos/Hard28/", solvers);
        benchmark("/home/einstein/Documentos/Scholl/Scholl_1/", solvers);

    }

    public void trivial(Sol sol) {
        sol.reset();
        for (int i = 0; i < w.length; i++) {
            sol.add(i, i);
        }
    }
}

