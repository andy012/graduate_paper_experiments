package com.shu.nita.test;

/**
 * Created by andy on 11/02/2017.
 */
public class Test {


    public static void model(){
        double[][] s={
                {10,1,1,2.5,654.9},
                {10,2,1,2.5,394.7},
                {20,1,1,2.5,363.6},
                {5,1,1,2.5,1415.4},
                {5,4,1,2.5,372.4},
                {2,1,1,0.5,646.1},
                {4,1,1,0.5,362.2},
                {4,1,2,0.5,355.6},
                {4,2,2,1,349.5},
                {2,2,2,0.5,358.4}
        };
        for(int i=0;i<s.length;++i){

            System.out.println(s[i][3]/(s[i][0]*s[i][1] ) + ","+ s[i][3]/(s[i][0]*s[i][2]) + "," +s[i][4]);
            //System.out.println(s[i][3]/(s[i][0]*s[i][1] )*2632.15+36.2094);

        }
    }


    public static void pred(){

        double[][] s= {
                {5, 1, 1, 2},
                {7, 2, 1, 3},
                {10, 2, 1, 20},
                {10,2,2,20},
                {8,1,2,12},
                {8,2,2,12},
                {10,1,1,2.5}
        };

        for(int i=0;i<s.length;++i){

            //System.out.println(s[i][3]/(s[i][0]*s[i][1] ) + ","+ s[i][3]/(s[i][0]*s[i][2]) + "," +s[i][4]);
            System.out.println(s[i][3]/(s[i][0]*s[i][1] )*2739.5765+13.6094);

        }


    }

    public static void main(String[] args) {
        pred();
    }
}
