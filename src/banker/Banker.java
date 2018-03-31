/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author Muhammad Ali
 */
public class Banker {

    int nP = 0, count = 0, rT = 0, temp = 0;
    //Table
    ArrayList<Integer> allocation = new ArrayList<>();
    ArrayList<Integer> max = new ArrayList<>();
    ArrayList<String> data = new ArrayList<>();
    ArrayList<Integer> need = new ArrayList<>();
    //Total Allocation
    ArrayList<Integer> total = new ArrayList<>();
    //Starting Resources
    ArrayList<Integer> startR = new ArrayList<>();
    //Net Resource -> Total Allocation - Starting Resources
    ArrayList<Integer> netR = new ArrayList<>();
    //Sequence for Process
    ArrayList<String> sequence = new ArrayList<>();

    public void readFile() {
        try {
            File file = new File("input.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line, st;
            while ((line = br.readLine()) != null) {
                data.add(line);
            }
            for (int y = 0; y < data.size(); y++) {
                st = data.get(y);
                if (count > nP + rT) {
                    System.out.println("Number of Processes do not match number of allocations.");
                    return;
                }
                if (count == 0) {
                    if (tryParseProcess(st)) {
                        nP = Integer.parseInt(st);
                    } else {
                        System.out.println("Invalid input for number of processes.");
                    }
                }
                if (count == 1) {
                    if (tryParseResource(st)) {
                        rT = Integer.parseInt(st);
                    } else {
                        System.out.println("Invalid input for number of resources.");
                    }
                }
                if (count == 2) {
                    String[] parts = st.split(" ");
                    for (int x = 0; x < rT; x++) {
                        startR.add(Integer.parseInt(parts[x]));
                    }
                }

                if (count > 2) {
                    String[] parts = st.split(" ");
                    if (temp <= nP - 1) {
                        for (int x = 0; x < rT; x++) {
                            allocation.add(Integer.parseInt(parts[x]));
                        }
                        temp++;
                        if (y == data.size() - 1) {
                            y = y - nP;
                            count = count - nP;
                        }
                    } else {
                        for (int x = 0; x < rT; x++) {
                            max.add(Integer.parseInt(parts[x + 3]));
                        }
                    }
                }
                count++;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void calcInitialAlloc() {
        int sum = 0;
        for (int k = 0; k < rT; k++) {
            for (int j = k; j < nP * rT; j += rT) {
                sum += allocation.get(j);
            }
            total.add(sum);
            sum = 0;
        }
    }

    public void calcNetR() {
        for (int i = 0; i < rT; i++) {
            netR.add(startR.get(i) - total.get(i));
        }
    }
    
    
    public void calcNeed() {
        for (int i = 0; i < max.size() && i < allocation.size(); i++) {
            int x = max.get(i);
            int y = allocation.get(i);
            int z = 0;
            if (x > y) {
                z = x - y;
            }
            need.add(z);
        }
    }

    boolean tryParseResource(String value) {
        try {
            int temp = Integer.parseInt(value);
            if (temp > 0 && temp < 10) {
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
        boolean tryParseProcess(String value) {
        try {
            int temp = Integer.parseInt(value);
            if (temp > 0 && temp <= 10) {
                return true;
            } else {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void display() {
        System.out.print("Max  : ");
        for (int i = 0; i < max.size(); i++) {
            System.out.print(max.get(i) + " ");
        }
        System.out.println("");
        System.out.print("Alloc: ");
        for (int i = 0; i < allocation.size(); i++) {
            System.out.print(allocation.get(i) + " ");
        }
        System.out.println("");
        System.out.print("Need : ");
        for (int i = 0; i < need.size(); i++) {
            System.out.print(need.get(i) + " ");
        }
        System.out.println("");
        System.out.print("Total: ");
        for (int i = 0; i < total.size(); i++) {
            System.out.print(total.get(i) + " ");
        }
        System.out.println("");
        System.out.print("Start: ");
        for (int i = 0; i < startR.size(); i++) {
            System.out.print(startR.get(i) + " ");
        }
        System.out.println("");
        System.out.print("NetR : ");
        for (int i = 0; i < netR.size(); i++) {
            System.out.print(netR.get(i) + " ");
        }
        System.out.println("");

        System.out.println("");
        sequence = new ArrayList<String>(new LinkedHashSet<String>(sequence));
        if (sequence.size() == nP) {
            System.out.println("Sequence Found!");
            for (int i = 0; i < sequence.size(); i++) {
                System.out.print(sequence.get(i) + " ");
            }
        } else {
            System.out.println("No Safe Sequence!");
        }
        System.out.println("");
    }
    Boolean add = true;
    int process = 0;

    public void algorithm() {
        for (int j = 0; j < nP; j++) {
            for (int x = 0; x < nP * rT; x += rT) {
                ArrayList<Integer> addition = new ArrayList<>();
                addition.add(0);
                addition.add(0);
                addition.add(0);
                int z = 0;
                for (int y = x; y < (x + rT); y++) {

                    if (need.get(y) > netR.get(y - x)) {
                        add = false;
                    }
                    addition.set(z, allocation.get(y));
                    z++;
                }
                if (add) {
                    sequence.add("P" + (process));
                    for (int i = 0; i < rT; i++) {
                        netR.set(i, (netR.get(i) + addition.get(i)));
                    }
                }
                process += 1;
                add = true;
            }
            process = 0;
        }

    }

    public static void main(String[] args) {
        Banker b = new Banker();
        b.readFile();
        b.calcNeed();
        b.calcInitialAlloc();
        b.calcNetR();
        b.algorithm();
        b.display();
    }

}
