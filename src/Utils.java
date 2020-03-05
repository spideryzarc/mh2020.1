import java.util.Random;

public class Utils {

    public static Random rd = new Random(7);

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
