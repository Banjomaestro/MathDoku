package sample;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class MathDokuGrid {

    private ArrayList<Cell> myCells = new ArrayList<Cell>();
    private ArrayList<Cage> myCages = new ArrayList<Cage>();
    private int n;

    public ArrayList<Cell> getMyCells() {
        return myCells;
    }

    public void generateRandomGrid(int n){
        this.n = n;
    }

    public void setMyCells(ArrayList<Cell> myCells) {
        this.myCells = myCells;
    }

    public ArrayList<Cage> getMyCages() {
        return myCages;
    }

    public void setMyCages(ArrayList<Cage> myCages) {
        this.myCages = myCages;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public void addCell(Cell cellToAdd){
        myCells.add(cellToAdd);
    }

    public void addCage(Cage cageToAdd){
        myCages.add(cageToAdd);
    }

    public void calculateN(){
        this.n = (int) Math.sqrt(myCells.size());
        System.out.println(n);
    }
    public void setBorders(){
        calculateN();
        for(Cage currentCage : myCages){
            currentCage.setN(this.n);
            currentCage.setCellsBorder();
        }
    }

    public void generate(int n){
        this.n = n;
        generateCells();
        setCellsValue();
        generateCage();

    }
    public void generateCells(){

        for(int i = 0; i<n*n; i++){
            Cell cellToAdd = new Cell();
            cellToAdd.setPosition(i);
            myCells.add(cellToAdd);
        }
    }

    public void generateCage(){

        int cagesRemaining = n*n+1;
        int failures = 0;
        boolean valid = false;
        int cagesToAdd = 0;
        int maximumNumberOfCages = (int) Math.sqrt(n)+2;
        Cell cellToAdd=null;
        Cell possiblyNullCell = null;

        for(Cell currentCell : myCells) {

            if (!currentCell.isInCage() && cagesRemaining>0) {

                Cage cageToAdd = new Cage();
                cageToAdd.addCell(currentCell);
                cagesRemaining--;
                cellToAdd = currentCell;
                failures = 0;
                System.out.println(cagesRemaining);
                if (cagesRemaining < (int) Math.sqrt(n)+2) {
                    maximumNumberOfCages = cagesRemaining;
                }
                if (cagesRemaining > 0) {
                    cagesToAdd = new Random().nextInt(maximumNumberOfCages);

                    for (int i = 0; i < cagesToAdd; i++) {
                        if (failures > 50) {
                            break;
                        }
                        do {
                            possiblyNullCell = chosenCell(cellToAdd, new Random().nextInt(3));
                            if(possiblyNullCell!=null)
                                cellToAdd = possiblyNullCell;

                            failures++;
                            if (failures > 50) {
                                break;
                            }
                        } while (possiblyNullCell == null);

                        if (cellToAdd != null && !cellToAdd.isInCage()) {
                            cageToAdd.addCell(cellToAdd);
                            cagesRemaining--;
                        }

                    }

                    addCage(cageToAdd);
                }
            }
        }

        for(Cage currentCage : myCages){

            currentCage.setOperator(new Random().nextInt(3));
            currentCage.calculateGoal();
            System.out.println("This is the goal: "+currentCage.getGoal()+" this is the operator : "+currentCage.getOperator());
        }

    }

    public Cell chosenCell(Cell currentCell, int random){


        for(int i =0; i<=20; i++){

            if (random == 0) {
                if (!(currentCell.getPosition() + 1 % n == 0) && currentCell.getPosition()+1<myCells.size()){
                    if (!myCells.get(currentCell.getPosition() + 1).isInCage()) {
                        return myCells.get(currentCell.getPosition() + 1);
                    }
            }else{
                    random = 2;
                }
            }else if (random == 1) {
                if(!(currentCell.getPosition() + n >= n * n)) {
                    if (!myCells.get(currentCell.getPosition() + n).isInCage()) {
                        return myCells.get(currentCell.getPosition() + n);
                    }
                }else{
                    random = 3;
                }
            }else if (random == 2) {
                if(currentCell.getPosition() - n > 0) {
                    if (!myCells.get(currentCell.getPosition() - n).isInCage()) {
                        return myCells.get(currentCell.getPosition() - n);
                    }
                }else{
                    random = 1;
                }
            }else {
                if((currentCell.getPosition() % n != 0)) {
                    if (!myCells.get(currentCell.getPosition() - 1).isInCage()) {
                        return myCells.get(currentCell.getPosition() - 1);
                    }
                }else{
                    random = 0;
                }
            }

        }
        return null;
    }

    public void setCellsValue(){

        boolean isValid = false;
        boolean interference = false;
        int fails = 0;
        System.out.println("lul");
        for(Cell currentCell :myCells){
            isValid = false;
            fails = 0;
            while(!isValid){
                currentCell.setCorrectValue(new Random().nextInt(n)+1);
                interference = false;

                for(Cell otherCell :myCells){
                    if (otherCell.getCorrectValue()==currentCell.getCorrectValue() && otherCell.getPosition()!=currentCell.getPosition()){
                        if(otherCell.getPosition()/n == currentCell.getPosition()/n || otherCell.getPosition()%n == currentCell.getPosition()%n ){
                            System.out.println(otherCell.getPosition()+" the current "+ currentCell.getPosition()+" "+otherCell.getCorrectValue()+" "+currentCell.getCorrectValue());
                            interference = true;
                            fails++;
                        }
                    }
                }
                if(!interference){
                    isValid = true;
                    break;
                }
                if(fails>50){
                    break;
                }
            }
            if(fails>50){
                break;
            }
            System.out.println(currentCell.getPosition()+" value: "+currentCell.getCorrectValue());


        }if(fails>50){
            for(Cell currentCell : myCells){
                currentCell.setCorrectValue(0);
            }
            setCellsValue();
        }
    }
}
