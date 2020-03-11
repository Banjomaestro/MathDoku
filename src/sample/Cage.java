package sample;

import java.util.ArrayList;

public class Cage {

    private int goal;
    private char operator;
    private ArrayList<Cell> myCells = new ArrayList<Cell>();
    private int n;

    public Cage(){

    }

    public void setCellsBorder(){

        for(Cell currentCell: myCells){
            for (Cell otherCell : myCells){
                if(currentCell.getPosition()%n == otherCell.getPosition()%n-1){
                    currentCell.setHasRightBorder(false);
                }
                else if(currentCell.getPosition()%n == otherCell.getPosition()%n+1){
                    currentCell.setHasLeftBorder(false);
                }
                else if(currentCell.getPosition()/n == otherCell.getPosition()/n+1){
                    currentCell.setHasTopBorder(false);
                }
                else if(currentCell.getPosition()/n == otherCell.getPosition()/n-1){
                    currentCell.setHasBottomBorder(false);
                }
            }
        }
        for(Cell currentCell : myCells){
            currentCell.drawBorders();
            System.out.println(currentCell.isHasBottomBorder()+" "+currentCell.isHasLeftBorder()+" "+currentCell.isHasTopBorder()+" "+currentCell.isHasRightBorder());
        }
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
        myCells.get(0).getLabelAbove().setText(goal+" "+operator);
    }

    public char getOperator() {
        return operator;
    }

    public void setOperator(char operator) {
        this.operator = operator;
    }

    public void setOperator(int random){

        if(myCells.size()>1) {
            if (random == 0)
                operator = '+';
            else if (random == 1)
                operator = 'x';
            else if (random == 2)
                operator = '-';
            else if (random == 3)
                operator = '÷';
        }
    }

    public ArrayList<Cell> getMyCells() {
        return myCells;
    }

    public void setMyCells(ArrayList<Cell> myCells) {
        this.myCells = myCells;
    }

    public void addCell(Cell cellToAdd){
        myCells.add(cellToAdd);
        cellToAdd.setInCage(true);
    }

    public void setN(int n){
        this.n = n;
    }

    public void calculateGoal(){

        Integer result = null;
        int iterations = 0;

        for(Cell currentCell : myCells){

            iterations++;

            if(iterations==1) {
                result = currentCell.getCorrectValue();
            }else{

                if(operator=='x'){
                    result*=currentCell.getCorrectValue();
                }else if(operator=='+'){
                    result+=currentCell.getCorrectValue();
                }else if(operator=='-'){
                    result-=currentCell.getCorrectValue();
                }else if(operator=='÷'){
                    result/=currentCell.getCorrectValue();
                }
            }


        }
        setGoal(result);

    }
}
