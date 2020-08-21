import java.util.Random;

public class Utils {

    /**
     * Gerador de números aleatórios
     */
    public static Random rd = new Random(7);

    /**
     * Embaralha um vetor de inteiros
     */
    public static void shuffle(int vet[]) {
        int n = vet.length;
        for (int i = 0; i < n; i++) {
            int x = rd.nextInt(n);
            int aux = vet[i];
            vet[i] = vet[x];
            vet[x] = aux;
        }


    }


}
