package main.java.Duke;

import main.java.Duke.Task.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Storage {
    public String filePath;
    public ArrayList<Task> arr;
    public ArrayList<Expense> arrExpenses;

    public Storage(String filePath) {
        this.filePath = filePath;
        this.arr = new ArrayList<>();
        this.arrExpenses = new ArrayList<>();
        readTasks();
    }

    /**
     * Reads duke.txt for any pre-logged task list and appends the tasks inside to the current Task List.
     * Creates a duke.txt file if it is not found in the directory.
     *
     * @throws IOException when file is unreadable.
     */
    public void readTasks() {
        BufferedReader objReader = null;
        try {
            String strCurrentLine;

            objReader = new BufferedReader(new FileReader(filePath));

            while ((strCurrentLine = objReader.readLine()) != null && strCurrentLine.length()!=0) {
                String taskType = strCurrentLine.substring(1, 2);
                boolean taskCompletion = strCurrentLine.contains("✗") ? false : true;
                String taskDetails = taskCompletion
                        ? strCurrentLine.substring(strCurrentLine.indexOf("✓") + 3)
                        : strCurrentLine.substring(strCurrentLine.indexOf("✗") + 3);

                switch (taskType) {
                case "T":
                    arr.add(new toDo(taskDetails, taskCompletion));
                    break;

                case "D":
                    String deadlineString = taskDetails.substring(0, taskDetails.indexOf("("));
                    String taskDeadlineString = taskDetails.substring(taskDetails.indexOf("by:") + 3, taskDetails.indexOf(")"));
                    LocalDate taskDeadline = LocalDate.parse(taskDeadlineString);
                    arr.add(new Deadline(deadlineString, taskCompletion, taskDeadline));
                    break;
                case "E":
                    String eventString = taskDetails.substring(0, taskDetails.indexOf("("));
                    String eventDateString = taskDetails.substring(taskDetails.indexOf("at:") + 3, taskDetails.indexOf(")"));
                    LocalDate eventDate = LocalDate.parse(eventDateString);
                    arr.add(new Event(eventString, taskCompletion, eventDate));
                }
            }

            while ((strCurrentLine = objReader.readLine()) != null) {
                String expenseString = strCurrentLine.substring(0, strCurrentLine.indexOf(","));
                double expenseAmount = Double.parseDouble(strCurrentLine.substring(strCurrentLine.indexOf("Cost:") + 6));
                arrExpenses.add(new Expense(expenseString,false,expenseAmount));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (objReader != null) {
                    objReader.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Saves all the tasks in the Task List when program ends(ie. when the user input is 'bye').
     *
     * @param arr Current task list.
     */
    public void saveTasks(ArrayList<Task> arr , ArrayList<Expense> arrEx) {
        try {
            // Creates a FileWriter
            FileWriter file = new FileWriter(filePath);

            // Creates a BufferedWriter
            BufferedWriter output = new BufferedWriter(file);

            // Writes the string to the file
            for (int i = 0 ; i < arr.size() ; i++ ) {
                output.write(arr.get(i).stringify() + "\n");
            }

            output.write("\n");

            for (int i = 0 ; i < arrEx.size() ; i++ ) {
                output.write(arrEx.get(i).stringify() + "\n");
            }
            // Closes the writer
            output.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}
