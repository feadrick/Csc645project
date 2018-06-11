/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekalgo;

/**
 *
 * @author feadrick
 */
public class Projekalgo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       int n=3;
        double C[][]=new double[n][n];
        C[0][0]=250;C[0][1]=400;C[0][2]=350;
        C[1][0]=400;C[1][1]=600;C[1][2]=350;
        C[2][0]=200;C[2][1]=400;C[2][2]=250;
        Algo obj=new Algo(C);
        System.out.println("Total cost: "+obj.getSum());
        System.out.println("Optimal solution");
        obj.printoptimalsolution();
    }
    
}
