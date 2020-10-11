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


    /**
     * @param avg load médio
     * @param l0  load inicial (antes da alteração)
     * @param lf  load final (depois da alteração)
     * @return variação no desvio padrão depois da alteração
     */
    public static double deltaDev(double avg, int l0, int lf) {
        double d0 = (l0 - avg);
        double df = (lf - avg);
        return df * df - d0 * d0;
    }


}
