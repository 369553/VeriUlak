package Base;

import Service.MatrixFunctions;
import java.util.ArrayList;
import java.util.HashMap;

public class OutlierDetection{
    private Object[] values;// Veriler
    private HashMap<SOLUTION, Boolean> isCalculatedScores;// Skorların hesaplanıp, hesaplanmadığını tutmak için...
    private HashMap<SOLUTION, Boolean> isCalculatedOutliers;// Aykırı değerlerin hesaplanıp, hesaplanmadığını tutmak için...
    private HashMap<SOLUTION, Double[]> scores;// Skorları tutmak için... Yapısı : <Çözüm yöntemi, skor değerleri>
    private HashMap<SOLUTION, Double[]> outliers;// Aykırı verileri tutmak için... Yapısı : <Çözüm yöntemi, Aykırı veriler>
    private HashMap<SOLUTION, Integer[]> indexOfOutliers;// Aykırı verilerin indislerini tutmak için... Yapısı : <Çözüm yöntemi, Aykırı verinin indisi>
    private double absoluteLineOfZScore = 2.4;// Z skor yöntemi kullanıldığında aykırı veriyi tespit için bir sınır. ZScore(veri) > lineOfZScore -> veri aykırıdır
    private Class dType;
    private Statistic stats = null;

    
    public enum SOLUTION{
        Z_SCORE
    }
    private OutlierDetection(Object[] values){
        this.values = values;
        isCalculatedScores = new HashMap<SOLUTION, Boolean>();
        isCalculatedOutliers = new HashMap<SOLUTION, Boolean>();
        detectDataTypes();
        resetIsCalculatedScores();// Skorları 'hesaplanmadı' olarak işâretle
        resetIsCalculatedOutliers();// Aykırı değerleri 'hesaplanmadı' olarak işâretle
    }

//İŞLEM YÖNTEMLERİ:
    //ÜRETİM YÖNTEMİ:
    public static OutlierDetection produceOutlierDetection(Object[] values){
        if(values == null)
            return null;
        if(!checkDataType(values)){// Sayı olmayan veri varsa
            System.err.println("outlierDetection üretilemedi");return null;}
        return new OutlierDetection(values);
    }
    public void setValues(Object[] values){
        if(values == null)
            return;
        this.values = values;
        resetIsCalculatedScores();// Skorları 'hesaplanmadı' olarak işâretle
        resetIsCalculatedOutliers();// Aykırı değerleri 'hesaplanmadı' olarak işâretle
        resetScores();// Skorları sıfırla
        resetOutliers();// Aykırı veriler haritasını sıfırla
        stats = null;// İstastistikleri sıfırla
        stats = null;// İstatistikleri sıfırla
        dType = null;// Veri tipini sıfırla
    }
    public void setAbsoluteLineOfZScore(double lineOfOutlier){
        absoluteLineOfZScore = lineOfOutlier;
        getIsCalculatedOutliers().put(SOLUTION.Z_SCORE, Boolean.FALSE);
    }
    public Double[] getScores(SOLUTION solution){// Değerlendirme skorlarını getirmek için
        if(solution == null)
            return null;
        if(!(boolean) getIsCalculatedScores().get(solution))
            calculateScores(solution);
        return getScores().get(solution);
    }
    public Double[] getOutliers(SOLUTION solution){// Aykırı verileri döndürür; bunun için evvelâ verilen çözüm yöntemiyle skorların hesaplanmış olması gerekir
        if(solution == null)
            return null;
        if(!getIsCalculatedOutliers().get(solution))
            calculateOutliers(solution);
        return getOutliers().get(solution);
    }
    public int getRowCountOfOutlierDependPercentValue(SOLUTION solution, double percentValue){// Verilen yüzdelik dilimin kaç satıra tekâbül ettiğini döndürür
        if(percentValue <= 0.0 || solution == null)
            return 0;
        if(percentValue > 100.0)
            percentValue = 100.0;
        Double[] outs = getOutliers(solution);// Aykırı verilerin skorlarını al
//        System.err.println("Verilen yüzdeye göre silinmesi gereken satır sayısı : " + (int) ((outs.length * percentValue) / 100));
        return (int) ((outs.length * percentValue) / 100);
    }
    public int[] getOutlierIndexesDependPercentValue(SOLUTION solution, double percentValue){// Verilen yüzdelik dilime seçilen aykırı satırların indislerini döndürür
        if(percentValue <= 0.0)
            return null;
        if(percentValue > 100.0)
            percentValue = 100.0;
        Double[] outs = getOutliers(solution);// Aykırı verilerin skorlarını al
        HashMap<String, Object[]> results = MatrixFunctions.sortValuesReturnValuesAndIndexes(outs, getIndexOfOutliers().get(SOLUTION.Z_SCORE), false);// Verileri büyükten küçüğe doğru sırala, sıralanmış aykırı veri skorlarını ve bu skorların normaldeki indislerini al
        Double[] sortedOutliers = (Double[])results.get("data");
        Integer[] indexesOfSortedOutliers = (Integer[]) results.get("indexes");
        int total = indexesOfSortedOutliers.length;
        int rowNumber = (int) ((outs.length * percentValue) / 100);
        int[] indexesToCutted = new int[rowNumber];
        for(int sayac = 0; sayac < indexesToCutted.length; sayac++){
            indexesToCutted[sayac] = indexesOfSortedOutliers[sayac];
        }
        return indexesToCutted;
    }
    public Object[] deleteOutliersDependPercentValue(SOLUTION solution, double percentValue){
        int[] indexesToDel = getOutlierIndexesDependPercentValue(solution, percentValue);
        return MatrixFunctions.deleteSelectedMembers(values, indexesToDel, Object[].class);
    }
    public Object[] setNullOutliersDependPercentValue(SOLUTION solution, double percentValue){
        int[] indexesToDel = getOutlierIndexesDependPercentValue(solution, percentValue);
        return MatrixFunctions.setNullSelectedMembers(values, indexesToDel);
    }
    //ARKAPLAN İŞLEM YÖNTEMLERİ:
    private void calculateScores(SOLUTION solution){
        switch(solution){
            case Z_SCORE :{
                calculateZScore();
                break;
            }
        }
        getIsCalculatedScores().put(solution, Boolean.TRUE);
    }
    private void calculateZScore(){
        standardize();
    }
    private void calculateOutliers(SOLUTION solution){
        if(!getIsCalculatedScores().get(solution))
            getScores(solution);
        Object[] scrs = getScores().get(solution);
        ArrayList<Double> listOfOutliers = new ArrayList<Double>();
        ArrayList<Integer> listOfIndexOfOutliers = new ArrayList<Integer>();
        switch(solution){
            case Z_SCORE :{
                for(int sayac = 0; sayac < values.length; sayac++){
                    if(scrs[sayac] == null)
                        continue;
                    if(Math.abs(((Double) scrs[sayac])) > absoluteLineOfZScore){
                        listOfOutliers.add(Double.valueOf(String.valueOf(scrs[sayac])));
                        listOfIndexOfOutliers.add(sayac);
                    }
                }
                break;
            }
        }
        Double[] outliersAsArray = new Double[listOfOutliers.size()];
        Integer[] indexesAsArray = new Integer[listOfIndexOfOutliers.size()];
        listOfOutliers.toArray(outliersAsArray);
        listOfIndexOfOutliers.toArray(indexesAsArray);
        getOutliers().put(solution, outliersAsArray);
        getIndexOfOutliers().put(solution, indexesAsArray);
        getIsCalculatedOutliers().put(solution, Boolean.TRUE);
    }
    private void detectDataTypes(){// Karışık verilerde hatâ verebilir; Double Integer karışık verilerde hatâ vermez, ama Long Double verilerde hatâ verebilir
        for(Object val : values){
            if(val == null)
                continue;
            if(val.getClass() == Double.class){
                dType = Double.class;
                break;
            }
            dType = val.getClass();
        }
    }
    private void standardize(){// Verilerin standardize edilmesi için kullanılır;
        if(getStats().size <= 1)// Sütunda dolu veri yoksa veyâ bir tâne varsa
            return;
        double base;
        base = stats.stdDeviation;// Payda = standart sapma
        if(base == Double.NaN || base == Double.NEGATIVE_INFINITY || base == Double.POSITIVE_INFINITY)// Payda sıfırsa işleme devam edilemez!
            return;
        Double[] zScores = new Double[values.length];
        for(int sayac = 0; sayac < values.length; sayac++){
            if(values[sayac] == null)// 'null' olan satırları atla
                continue;
            double upNumber;
            upNumber = (Double.valueOf(String.valueOf(values[sayac])) - stats.mean);// pay = x - ortalama
            zScores[sayac] = (Double) (upNumber / base);
        }
        getScores().put(SOLUTION.Z_SCORE, zScores);
    }
    private static boolean checkDataType(Object[] values){
        for(Object val : values){
            if(val == null)// Veri içerisinde 'null' olan satırlar varsa, hatâ verme
                continue;
            if(val.getClass() == Number.class)
                continue;
            if(val.getClass().getSuperclass() != null){
                if(val.getClass().getSuperclass() == Number.class)
                    continue;
                else
                    return false;
            }
            return false;
        }
        return true;
    }
    private void resetIsCalculatedScores(){
        getIsCalculatedScores().clear();
        for(SOLUTION slt : OutlierDetection.SOLUTION.values()){
            isCalculatedScores.put(slt, Boolean.FALSE);
        }
    }
    private void resetIsCalculatedOutliers(){
        getIsCalculatedOutliers().clear();
        for(SOLUTION slt : OutlierDetection.SOLUTION.values()){
            isCalculatedOutliers.put(slt, Boolean.FALSE);
        }
    }
    private void resetScores(){
        getScores().clear();
    }
    private void resetOutliers(){
        getOutliers().clear();
    }

//ERİŞİM YÖNTEMLERİ:
    public HashMap<SOLUTION, Double[]> getScores(){
        if(scores == null){
            scores = new HashMap<SOLUTION, Double[]>();
        }
        return scores;
    }
    public Statistic getStats(){
        if(stats == null){
            stats = Statistic.calculateBasicStatistics(values, dType);
            stats = Statistic.calculateDistributionMetrics(stats, values);
        }
        return stats;
    }
    //GİZLİ ERİŞİM YÖNTEMLERİ:
    private HashMap<SOLUTION, Boolean> getIsCalculatedScores(){
        return isCalculatedScores;
    }
    private HashMap<SOLUTION, Boolean> getIsCalculatedOutliers(){
        return isCalculatedOutliers;
    }
    private HashMap<SOLUTION, Double[]> getOutliers(){
        if(outliers == null){
            outliers = new HashMap<SOLUTION, Double[]>();
        }
        return outliers;
    }
    private HashMap<SOLUTION, Integer[]> getIndexOfOutliers(){
        if(indexOfOutliers == null)
            indexOfOutliers = new HashMap<SOLUTION, Integer[]>();
        return indexOfOutliers;
    }
}