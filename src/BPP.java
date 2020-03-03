import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

/**Bin Packing Problem - Problema de empacotamento*/
public class BPP {
    /**capacidade*/
    int C;
    /**pesos*/
    int w[];

    public BPP(int n, int C){
        this.C = C;
        w = new int[n];
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

    public static void main(String args[]) throws FileNotFoundException {
        BPP bpp = new BPP("/home/einstein/Documentos/Hard28/Hard28_BPP13.txt");
        System.out.println(bpp);

        Sol sol = new Sol(bpp);

        sol.add(0,0);
        sol.add(1,1);
        sol.add(179,0);

        System.out.println(sol);


    }
}
