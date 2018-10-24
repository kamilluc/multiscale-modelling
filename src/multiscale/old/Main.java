/**
 * Created by kamil on 04.05.2017.
 */
public class Main {

    public static void main(String[] args){
        Logic logic=new Logic(600,600,100);
        logic.setSeedRule(2);
        logic.start();
        logic.show();
        logic.nextStep();
        logic.show();
    }
}
