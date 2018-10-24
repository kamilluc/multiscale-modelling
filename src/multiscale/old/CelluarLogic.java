public class CelluarLogic {

    private Cell[][] board, boardOld;
    private int WIDTH, HEIGHT;

    public void addInclusions(int x, int y, double size, InclusionType type){
        if(type==InclusionType.SQUARE){
            for(int i=0;i<size;i++){
                for(int j=0;j<size;j++){
                    board[x+i][y+j].setState(CellState.INCLUSION);
                }
            }
        }

        else if(type==InclusionType.CIRCLE){
            double r=size/2.;
            for(int i=0;i<size;i++){
                for(int j=0;j<size;j++){
                    if((x-r)*(x-r) + (y-r)*(y-r) <= r*r)
                        board[x+i][y+j].setState(CellState.INCLUSION);
                }
            }
        }

        else{
            System.out.println("ERR: Wrong Inclusion type");
        }
    }


   public CelluarLogic(){
       board=new Cell[HEIGHT][WIDTH];
       for (int i = 0; i < HEIGHT; i++) {
           for (int j = 0; j < WIDTH; j++) {
               board[i][j] = new Cell();
           }
       }
   }

    private void Neumann(int i, int j){
        if(boardOld[i][j].isState()) {
            if(!boardOld[i-1][j].isState()){
                board[i-1][j].setColor(boardOld[i][j].getId());
                board[i-1][j].setState(CellState.FULL);
            }
            if(!boardOld[i][j-1].isState()){
                board[i][j-1].setColor(boardOld[i][j].getId());
                board[i][j-1].setState(CellState.FULL);
            }
            if(!boardOld[i][j+1].isState()){
                board[i][j+1].setColor(boardOld[i][j].getId());
                board[i][j+1].setState(CellState.FULL);
            }
            if(!boardOld[i+1][j].isState()){
                board[i+1][j].setColor(boardOld[i][j].getId());
                board[i+1][j].setState(CellState.FULL);
            }
        }
    }
}



