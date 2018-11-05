package multiscale.logic;

public class TestMain {
    public static void main(String[] args) {
        CellularAutomata ca=new CellularAutomata(20,20);
        while(!ca.isBoardFull())
        ca.nextSteep();

    }
}
