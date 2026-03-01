package functions;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class TabulatedFunctions {
    private static TabulatedFunctionFactory factory = new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();

    private TabulatedFunctions(){}

    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory factory){
        TabulatedFunctions.factory = factory;
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount){
        return factory.createTabulatedFunction(leftX, rightX, pointsCount);
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values){
        return factory.createTabulatedFunction(leftX, rightX, values);
    }

    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] points){
        return factory.createTabulatedFunction(points);
    }


    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> cls, double leftX, double rightX, int pointsCount){
        try{
            Constructor<? extends TabulatedFunction> constructor = cls.getConstructor(double.class, double.class, int.class);

            return constructor.newInstance(leftX, rightX, pointsCount);
        }catch (NoSuchMethodException |
                InstantiationException |
                IllegalAccessException |
                InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> cls, double leftX, double rightX, double[] values){
        try {
            Constructor<? extends TabulatedFunction> constructor = cls.getConstructor(double.class, double.class, double[].class);

            return constructor.newInstance(leftX, rightX, values);
        }catch (NoSuchMethodException |
                InstantiationException |
                IllegalAccessException |
                InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(Class<? extends TabulatedFunction> cls, FunctionPoint[] points){
        try{
            Constructor<? extends TabulatedFunction> constructor = cls.getConstructor(FunctionPoint[].class);

            return constructor.newInstance((Object) points);
        }catch (NoSuchMethodException |
                InstantiationException |
                IllegalAccessException |
                InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static TabulatedFunction tabulate(Class<? extends TabulatedFunction> cls, Function function, double leftX, double rightX, int pointsCount){
        if (leftX > rightX){
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }

        if (pointsCount < 2){
            throw new IllegalArgumentException("Количестов точек должно быть больше двух");
        }

        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()){
            throw new IllegalArgumentException("Границы табулирования [" + leftX + ", " + rightX + "] выходят за область определения функции");
        }

        TabulatedFunction func = createTabulatedFunction(cls, leftX, rightX, pointsCount);

        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++){
            double x = leftX + i * step;
            double y = function.getFunctionValue(x);
            try {
                func.setPoint(i, new FunctionPoint(x, y));
            } catch (InappropriateFunctionPointException e) {
                throw new RuntimeException(e);
            }
        }

        return func;
    }


    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount){
        if (leftX > rightX){
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }

        if (pointsCount < 2){
            throw new IllegalArgumentException("Количестов точек должно быть больше двух");
        }

        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()){
            throw new IllegalArgumentException("Границы табулирования [" + leftX + ", " + rightX + "] выходят за область определения функции");
        }

        FunctionPoint[] points = new FunctionPoint[pointsCount];

        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++){
            double x = leftX + i * step;
            double y = function.getFunctionValue(x);
            points[i] = new FunctionPoint(x, y);
        }

        return createTabulatedFunction(points);
    }

    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) throws IOException {
        DataOutputStream dataOut = new DataOutputStream(out);

        dataOut.writeInt(function.getPointsCount());

        for (int i = 0; i < function.getPointsCount(); i++){
            dataOut.writeDouble(function.getPointX(i));
            dataOut.writeDouble(function.getPointY(i));
        }
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) throws IOException {
        DataInputStream dataIn = new DataInputStream(in);

        int pointsCount = dataIn.readInt();
        FunctionPoint[] points = new FunctionPoint[pointsCount];

        for (int i = 0; i < pointsCount; i++){
            double x = dataIn.readDouble();
            double y = dataIn.readDouble();
            points[i] = new FunctionPoint(x, y);
        }

        return createTabulatedFunction(points);
    }

    public static void writeTabulatedFunction(TabulatedFunction function, Writer out){
        PrintWriter writer = new PrintWriter(out);

        writer.print(function.getPointsCount());
        writer.print(" ");

        for (int i = 0; i < function.getPointsCount(); i++){
            writer.print(function.getPointX(i));
            writer.print(" ");
            writer.print(function.getPointY(i));
            if (i < function.getPointsCount() - 1){
                writer.print(" ");
            }
        }
    }

    public static TabulatedFunction readTabulatedFunction(Reader in) throws IOException{
        StreamTokenizer tokenizer = new StreamTokenizer(in);

        tokenizer.resetSyntax();
        tokenizer.wordChars('0', '9');
        tokenizer.wordChars('.', '.');
        tokenizer.wordChars('-' , '-');
        tokenizer.whitespaceChars(' ', ' ');
        tokenizer.whitespaceChars('\t', '\t');
        tokenizer.eolIsSignificant(false);

        tokenizer.nextToken();
        int pointsCount = Integer.parseInt(tokenizer.sval);
        FunctionPoint[] points = new FunctionPoint[pointsCount];

        for(int i = 0; i < pointsCount; i++){
            tokenizer.nextToken();
            double x = Double.parseDouble(tokenizer.sval);
            tokenizer.nextToken();
            double y = Double.parseDouble(tokenizer.sval);
            points[i] = new FunctionPoint(x, y);
        }

        return createTabulatedFunction(points);
    }
}
