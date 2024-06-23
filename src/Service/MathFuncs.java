package Service;

import java.text.NumberFormat;
import java.util.Locale;

public class MathFuncs{
    private static NumberFormat setFormat = NumberFormat.getInstance(Locale.US);

    private MathFuncs(){}

//İŞLEM YÖNTEMLERİ:
    public static float roundNumber(float number, int numberAfterDot){// Noktalı sayının kesirli kısmını sınırlandırma
        return (float) roundNumber((double) number, numberAfterDot);
    }
    public static double roundNumber(double number, int numberAfterDot){// Noktalı sayının kesirli kısmını sınırlandırma
        setFormat.setMaximumFractionDigits(numberAfterDot);
        setFormat.setGroupingUsed(false);
        double value = number;
        String ready = setFormat.format(number); 
        value = Double.parseDouble(ready);
        return value;
    }
    public static int rollDoubleToInteger(double value, boolean rollToUp, boolean rollToDown){// Her iki bit bayrağı da true ise normal yuvarlama yapılır
        String[] sValSplitted = String.valueOf(value).split("\\.");
        int firstNumberAfterDot = Integer.valueOf(String.valueOf(sValSplitted[1].charAt(0)));
        int numberBeforeDot = Integer.valueOf(sValSplitted[0]);
        if((rollToUp && firstNumberAfterDot >= 5) || rollToUp && !rollToDown)
            numberBeforeDot++;
        return numberBeforeDot;
    }
}