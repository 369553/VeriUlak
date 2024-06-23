package Base;

import Service.MathFuncs;
import Service.MatrixFunctions;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class Statistic{
    /*
    BİLGİ:
        Standart sapma : Verinin aritmetik ortalamadan ne kadar uzak düştüğünü ifâde eder
        Varyans : Verilerin yayılışının ortalama değerini ifâde eder.
            Varyansın küçük yâ dâ büyük olması sivrilik hakkında fikir verir;
            varyansı büyük olan dağılım daha basıktır, veri setindeki değişkenliğin ölçüsüdür
        Çarpıklık : Dağılımın ortalama etrafındaki simetriden ne kadar saptığını ifâde eder
            Ç = 0 ise simetriktir,
            Ç > 0 ise sağa çarpıktır
            Ç < 0 sola çarpıktır
        Basıklık : Çarpıklığın sivrilik derecesini gösterir:
            B > 0 ise dağılım sivridir
            B < 0 ise dağılım basıktır
        
    */
    private static HashMap<String, String> statisticAsTurkce;
    // BU İSTATİSTİKLER TEK BİR SÜTUN İÇİN
    protected double mean = 0.0;;// Ortalama
    protected double min = 0.0;;// Asğarî değer
    protected double max = 0.0;;// Azamî değer
    protected int size;// Veri sayısı
    protected double stdDeviation = 0.0;// Standart sapma
    protected double variation = 0.0;// Varyans (standart sapmanın karesidir)
    protected double skewness = 0.0;// Çarpıklık
    protected double stickiness = 0.0;// Basıklık
    protected double[] cors;// Diğer sütunlarla olan korelasyonları
    protected boolean isContinuous = false;// Veri sürekli ise 'true' olmalıdır
    protected boolean isNumber = false;// Veriler sayı ise 'true' olmalıdır
    protected boolean isIntOrShort;// Veriler tam sayı ise 'true' olmalıdır
    protected boolean isBool;// Veriler bitsel veri ise 'true' olmalıdır
    protected Object modValue;// En çok tekrar eden veri
    protected int mod = 0;// En çok tekrar eden verinin tekrar sayısı
    protected double range = 0;// En yüksek değer ile en düşük değer arasındaki fark
    private HashMap<String, Integer> categoricCounter = new HashMap<String, Integer>();// Mod hesâplanırken kullanılan bir değişken
    private ArrayList<Double> decrementFromRowNextToRowBack;// Sayısal verideki değişimi algılayabilmek için
    private boolean isDistsCalculated = false;
    private int numberAfterDot = 6;// 'Double' ve 'Float' veri tiplerinde noktadan sonraki varsayılan basamak sayısı (veriyi değiştirmek için değil, istatistikleri hesâplarken kullanmak için)
//    private HashMap<String, Boolean> whichOnesCalculated;// Hangi değerlerin hesaplandığını tutan bitsel harita

    private Statistic(){
    }

//İŞLEM YÖNTEMLERİ:
    //SINIF YÖNTEMLERİ:
    public static Statistic calculateBasicStatistics(Object[] data, Class dataType){
        return calculateBasicStatistics(data, dataType, 6, true);
    }
    public static Statistic calculateBasicStatistics(Object[] data, Class dataType, int numberAfterDotForDoubleOrFloat, boolean roundValues){// Noktalı sayı tipinden olan sayılarda verinin sürekli veyâ ayrık olup, olmadığına bakılmıyor; doğrudan 'ayrık' olarak işâretleniyor
        double totalizerForMean = 0.0;
        Statistic st = new Statistic();
        st.decrementFromRowNextToRowBack = new ArrayList<Double>();
        if(data == null)
            return null;
        data = MatrixFunctions.deleteNullValues(data);// null olan veriler çıkarılır
        st.size = data.length;
        st.numberAfterDot = numberAfterDotForDoubleOrFloat;
        // VERİ TİPİNİ ALGILAMA:
        if(dataType == null)
            dataType = data[0].getClass();
        if(dataType == Number.class)
            st.isNumber = true;
        else if(dataType.getSuperclass() != null){
            if(dataType.getSuperclass() == Number.class){
                st.isNumber = true;
                st.isContinuous = true;
                if(dataType == Integer.class || dataType == Short.class){
                    st.isContinuous = false;
                    st.isIntOrShort = true;
                }
            }
//            else
//                return st;// DÜZELTİLMESİ GEREKİYORSA DÜZELT
        }
        if(!st.isNumber){
            st.isContinuous = false;
            st.isNumber = false;
            if(dataType == Boolean.class)
                st.isBool = true;
            else
                st.isBool = false;
        }
        if(st.isNumber){// Sayı ise min, max değerlerini ilk eleman yap (for içerisindeki kontrol sayısını azaltmak için)
            st.min = Double.valueOf(String.valueOf(data[0]));
            st.max = Double.valueOf(String.valueOf(data[0]));
        }
        // HESAPLAMALARI YAP:
        for(int sayac = 0; sayac < data.length; sayac++){
            String valAsStr = String.valueOf(data[sayac]);
            Double valAsDouble;
            if(st.isNumber){
                try{
                    valAsDouble = Double.valueOf(valAsStr);
                    if(valAsDouble < st.min)
                        st.min = valAsDouble;
                    if(valAsDouble > st.max)
                        st.max = valAsDouble;
                    totalizerForMean += valAsDouble;
                    if(sayac > 0){
                        double decr = valAsDouble - Double.valueOf(String.valueOf(data[sayac]));
                        st.decrementFromRowNextToRowBack.add(decr);
                    }
                }
                catch(NumberFormatException exc){// BURADA NE YAPILACAĞI ELE ALINMALI
                    System.out.println("Base.Statistic.calculateBasicStatistics()");
                    break;
                }
            }
            {// MOD BULMAK İÇİN TEKRAR SAYILARINI KAYDET:
                String key = String.valueOf(data[sayac]);
                Object ctVal = st.categoricCounter.get(key);// Bu ayrık değere daha evvel rastlanılmış mı?
                int ctCount = 0;
                if(ctVal != null)
                    ctCount = st.categoricCounter.get(key);
                st.categoricCounter.put(valAsStr, ctCount + 1);
            }
        }
        if(st.isNumber){
            st.mean = totalizerForMean / st.size;
            st.range = st.max - st.min;
        }
        st.modValue = null;
        st.mod = 0;
        // VERİ TEKRAR SAYILARINA GÖRE MODU BUL:
        {
        for(String key : st.categoricCounter.keySet()){
            int repeated = st.categoricCounter.get(key);
            if(repeated > st.mod){
                st.mod = repeated;
                if(st.isNumber){
                    if(st.isIntOrShort)
                        st.modValue = Integer.valueOf(key);
                    else
                        st.modValue = Double.valueOf(key);
                }
                else if(st.isBool)
                    st.modValue = Boolean.valueOf(key);
                else
                    st.modValue = key;
            }
        }
        /*
        AYNI MOD DEĞERİNE SÂHİP BAŞKA VERİLER TESPİT EDİLMİYOR; EDİLMESİ GEREKİYORSA AŞAĞIDAKİ KODU AÇ VE DÜZENLE
        for(String key : st.categoricCounter.keySet()){// Aynı mod değerine sâhip başka veri var mı, kontrol et:
            if(key == String.valueOf(st.modValue))
                continue;
            
        }*/
        }
        if(st.isNumber && roundValues)
            st.roundBasicStatisticValues();
        return st;
    }
    public static void calculateDistributionMetrics(Statistic stats, Object[] data){
        calculateDistributionMetrics(stats, data, true);
    }
    public static void calculateDistributionMetrics(Statistic stats, Object[] data, boolean roundValues){
        //Önce veri içerisindeki 'null' değerleri çıkartalım, bi iznillâh:
        data = MatrixFunctions.deleteNullValues(data);
        if(!stats.isNumber)// Yalnızca sayısal veriler için dağılım ölçüleri hesâplanıyor
            return;
        double varForTotal = 0.0;
        double[] vals = new double[data.length];
        for(int sayac = 0; sayac < stats.size; sayac++){
            vals[sayac] = Double.valueOf(String.valueOf(data[sayac]));
            varForTotal += Math.pow((Double.valueOf(vals[sayac]) - stats.mean), 2);
        }
        stats.variation = varForTotal / (stats.size - 1);
        stats.stdDeviation = Math.sqrt(stats.variation);
        varForTotal = 0.0;
        for(int sayac = 0; sayac < stats.size; sayac++){
            varForTotal+= Math.pow(vals[sayac] - stats.mean, 3);
        }
        stats.skewness = (varForTotal / stats.size) / Math.pow(stats.stdDeviation, 3);
        varForTotal = 0.0;
        for(int sayac = 0; sayac < stats.size; sayac++){
            varForTotal+= Math.pow(vals[sayac] - stats.mean, 4);
        }
        stats.stickiness = (varForTotal / stats.size) / Math.pow(stats.stdDeviation, 4) - 3;
        if(stats.isNumber && roundValues)
            stats.roundDistributionStatisticValues();
    }
    public static ArrayList<String> getStatisticAsList(Statistic stats){
        if(stats == null)
            return null;
        ArrayList<String> sts = new ArrayList<String>();
        if(!stats.isNumber){
            sts.add(getStatisticAsTurkce().get("size") + " : " + stats.size);
            sts.add(getStatisticAsTurkce().get("isNumber") + " : " + takeValueAsTRAndAsString(stats.isNumber));
            sts.add(getStatisticAsTurkce().get("isBool") + " : " + takeValueAsTRAndAsString(stats.isBool));
            sts.add(getStatisticAsTurkce().get("mod") + " : " + stats.mod);
            sts.add(getStatisticAsTurkce().get("modValue") + " : " + stats.modValue);
        }
        else{// Sayı ise
            for(Field fd : Statistic.class.getDeclaredFields()){
                String fdAsTR = getStatisticAsTurkce().get(fd.getName());
                if(fdAsTR == null)
                    continue;
                if(!stats.isDistsCalculated && isDistributionMetric(fd.getName()))// Dağılım ölçüsü hesaplanmamışsa dağılım ölçüsünü listeye ekleme
                    continue;
                try{
                    sts.add(fdAsTR + " : " + takeValueAsTRAndAsString(fd.get(stats)));
                }
                catch(IllegalArgumentException | IllegalAccessException exc){
                    System.err.println("İlginç şekilde Statistic alanı verisine erişilemedi : " + exc.toString());
                }
            }
        }
        return sts;
    }
    public static String takeValueAsTRAndAsString(Object value){
        if(value == null)
            return "null";
        if(value.getClass() == String.class)
            return (String) value;
        if(value.getClass() == Boolean.class){
            if((Boolean) value)
                return "Evet";
            else
                return "Hayır";
        }
        return String.valueOf(value);
    }
    public static boolean isDistributionMetric(String fieldName){
        boolean isIt = false;
        switch(fieldName){
            case "stdDeviation" :{
                isIt = true;
                break;
            }
            case "variation" :{
                isIt = true;
                break;
            }
            case "skewness" :{
                isIt = true;
                break;
            }
            case "stickiness" :{
                isIt = true;
                break;
            }
        }
        return isIt;
    }
    public void roundBasicStatisticValues(){// Temel istatistikleri yuvarlayarak tazele
        mean = MathFuncs.roundNumber(mean, numberAfterDot);
        if(isContinuous){// Sürekli sayılar için olan istatistikleri yuvarlayarak tazele
            min = MathFuncs.roundNumber(min, numberAfterDot);
            max = MathFuncs.roundNumber(max, numberAfterDot);
            range = MathFuncs.roundNumber(range, numberAfterDot);
            modValue = MathFuncs.roundNumber((Double) modValue, numberAfterDot);
            ArrayList<Double> liRounded = new ArrayList<Double>();
            decrementFromRowNextToRowBack.forEach((number)->{liRounded.add(MathFuncs.roundNumber(number, numberAfterDot));});
            decrementFromRowNextToRowBack = liRounded;
        }
    }
    public void roundDistributionStatisticValues(){// Dağılım ölçülerini yuvarlayarak tazele
        if(isNumber){
            stdDeviation = MathFuncs.roundNumber(stdDeviation, numberAfterDot);
            variation = MathFuncs.roundNumber(variation, numberAfterDot);
            skewness = MathFuncs.roundNumber(skewness, numberAfterDot);
            stickiness = MathFuncs.roundNumber(stickiness, numberAfterDot);
        }
    }

//ERİŞİM YÖNTEMLERİ:
    public static HashMap<String, String> getStatisticAsTurkce(){
        if(statisticAsTurkce == null){
            statisticAsTurkce = new HashMap<String, String>();
            statisticAsTurkce.put("mean", "Ortalama");
            statisticAsTurkce.put("min", "Asgarî");
            statisticAsTurkce.put("max", "Azamî");
            statisticAsTurkce.put("size", "Boyut (veri sayısı)");
            statisticAsTurkce.put("stdDeviation", "Standart sapma");
            statisticAsTurkce.put("variation", "Varyans");
            statisticAsTurkce.put("skewness", "Çarpıklık");
            statisticAsTurkce.put("stickiness", "Basıklık");
            statisticAsTurkce.put("cors", "Korelasyon");
            statisticAsTurkce.put("isContinuous", "Veri sürekli mi");
            statisticAsTurkce.put("isNumber", "Veri sayı mı");
            statisticAsTurkce.put("isIntOrShort", "Veri tam sayı mı");
            statisticAsTurkce.put("isBool", "Veri bitsel veri mi");
            statisticAsTurkce.put("modValue", "En çok tekrar eden veri (mod)");
            statisticAsTurkce.put("mod", "En çok tekrar eden verinin tekrar sayısı");
        }
        return statisticAsTurkce;
    }
    public double getMean(){
        return mean;
    }
    public double getMin(){
        return min;
    }
    public double getMax(){
        return max;
    }
    public int getSize(){
        return size;
    }
    public double[] getCors(){
        return cors;
    }
    public boolean getIsContinuous(){
        return isContinuous;
    }
    public boolean getIsNumber(){
        return isNumber;
    }
    public boolean getIsIntOrShort(){
        return isIntOrShort;
    }
    public boolean isBool(){
        return isBool;
    }
    public Object getModValue(){
        return modValue;
    }
    public int getMod(){
        return mod;
    }
    public double getRange(){
        return range;
    }
    public double getStdDeviation(){
        return stdDeviation;
    }
    public double getStickiness(){
        return stickiness;
    }
    public double getSkewness(){
        return skewness;
    }
    public boolean getIsDistsCalculated(){
        return isDistsCalculated;
    }
}