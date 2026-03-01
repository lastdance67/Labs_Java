import functions.*;
import functions.basic.*;
import threads.*;

import java.io.*;

public class Main {
    public static void main(String[] args) {

        TabulatedFunction f;
        f = TabulatedFunctions.createTabulatedFunction(ArrayTabulatedFunction.class, 0, 10, 9);
        for (FunctionPoint p : f){
            System.out.println(p);
        }
    }
}