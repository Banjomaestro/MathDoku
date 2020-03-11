package sample;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Reader {

    public MathDokuGrid readConfig(File file) throws IOException {

        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();

        String str = new String(data, "UTF-8");



        return analyseString(str);

    }

    public MathDokuGrid analyseString(String s){

        MathDokuGrid mathDokuGrid = new MathDokuGrid();
        String[] lines = s.split(System.getProperty("line.separator"));
        int iterations = 0;

        for(String line: lines){

            iterations = 0;
            Cage cageToAdd = new Cage();
            String[] position = line.split(",");

            for(String currentString : position){
                iterations++;

                if(iterations==1){
                    String[] splited = currentString.split(" ");

                    Cell cellToAdd = new Cell();
                    cellToAdd.setPosition(Integer.parseInt(splited[1])-1);
                    cageToAdd.addCell(cellToAdd);
                    if(splited[0].contains("x") || splited[0].contains("+")|| splited[0].contains("-")|| splited[0].contains("÷"))
                    cageToAdd.setOperator(splited[0].toCharArray()[splited[0].toCharArray().length-1]);
                    mathDokuGrid.addCell(cellToAdd);
                    cageToAdd.setGoal(Integer.parseInt(splited[0].replaceAll("\\D+","")));
                }else{
                    Cell cellToAdd = new Cell();
                    cellToAdd.setPosition(Integer.parseInt(currentString)-1);
                    cageToAdd.addCell(cellToAdd);
                    mathDokuGrid.addCell(cellToAdd);
                }

            }
            mathDokuGrid.addCage(cageToAdd);
        }
        return mathDokuGrid;

    }
}
