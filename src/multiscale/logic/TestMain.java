package multiscale.logic;

public class TestMain {
    public static void main(String[] args) {
        CellularAutomata ca=new CellularAutomata(20,20);
        ca.nextSteep();
        ca.nextSteep();
    }
}
