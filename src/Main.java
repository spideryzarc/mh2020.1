import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String args[]) throws IOException {
//        assert false: "verifica se assert está ativado";
        ArrayList<Solver> solvers = new ArrayList<>();
        int ite = 10000;
//        solvers.add(new RMS(ite));
//        solvers.add(new ILS(ite, 1));
//        solvers.add(new VNS(ite));
        solvers.add(new GRASP(ite,3));



//        BPP.benchmark("../instances/Hard28/", solvers);
        BPP.benchmark("../instances/Wascher/", solvers);
//        BPP.benchmark("../instances/Scholl/Scholl_3/", solvers);
//        BPP.benchmark("../instances/Falkenauer/Falkenauer U/", solvers);



    }
}

