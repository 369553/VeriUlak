package Base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class DataSplitter{

    public DataSplitter(){
    
    }

//İŞLEM YÖNTEMLERİ:
    public static HashMap<String, Object[][]> splitTrainTestSet(Object[][] data, double rateOfTestSet, long seed, boolean useSeed){
        // GERİ DÖNÜŞ : <train, eğitim seti>, <test, test seti>
        int total = data.length;// Toplam veri büyüklüğü
        int testDataNumber;// Test veri seti büyüklüğü
        int trainDataNumber;// Eğitim veri seti büyüklüğü
        testDataNumber = (int) (total * rateOfTestSet);
        trainDataNumber = total - testDataNumber;
        ArrayList<Integer> indexesOfTrainSet = new ArrayList<Integer>();// Eğitim veri seti indisleri
        ArrayList<Integer> indexesOfTestSet = new ArrayList<Integer>();// Test veri seti indisleri
        ArrayList<Object[]> train = new ArrayList<Object[]>();// Eğitim veri seti
        ArrayList<Object[]> test = new ArrayList<Object[]>();// Test veri seti
        HashMap<String, Object[][]> results;// Sonuçları döndürmek için değişken
        Object[][] trainData;// Eğitim verileri, tablo olarak
        Object[][] testData;// Test verileri, tablo olarak
        boolean keepGo = true;// Döngüyü kontrol etmek için
        Random rand;// Rastgele sayı üretmek için değişken
        boolean dontUse;// Veri indislerini bulurken aynı indisi bulduğumuzda tespit için kullanılan bayrak
        boolean isInTest;// İlgili indisin test içerisinde olup, olmadığını anlamak için
        if(useSeed)
            rand = new Random(seed);
        else
            rand = new Random();
        while(keepGo){// Test veri seti için rastgele sayı bul
           dontUse = false;
           int index = Math.abs(rand.nextInt()) % total;
            for(int inside : indexesOfTestSet){
                if(inside == index){
                    dontUse = true;
                    break;
                }
            }
            if(!dontUse){
                indexesOfTestSet.add(index);
            }
            if(indexesOfTestSet.size() == testDataNumber)
                break;
        }
//        indexesOfTestSet.forEach((number) -> {System.out.println("test indis : " + number);});
        for(int sayac = 0; sayac < total; sayac++){// Eğitim veri seti indislerini listeye ekle
           isInTest = false;
            for(int index : indexesOfTestSet){
                if(index == sayac){
                    isInTest = true;
                    break;
                }
            }
            if(!isInTest)
                indexesOfTrainSet.add(sayac);
        }
//        indexesOfTrainSet.forEach((number) -> {System.err.println("Eğitim indis : " + number);});
        indexesOfTestSet.forEach((number) -> {test.add(data[number]);});// Test veri setine ilgili indisteki verileri ekle
        indexesOfTrainSet.forEach((number) -> {train.add(data[number]);});// Eğitim veri setine ilgili indisteki verileri ekle
        results = new HashMap<String, Object[][]>();
        trainData = new Object[trainDataNumber][];
        testData = new Object[testDataNumber][];
        for(int sayac = 0; sayac < trainDataNumber; sayac++){
            trainData[sayac] = train.get(sayac);
        }
        for(int sayac = 0; sayac < testDataNumber; sayac++){
            testData[sayac] = test.get(sayac);
        }
        results.put("train", trainData);
        results.put("test", testData);
        return results;
    }

//ERİŞİM YÖNTEMLERİ:
    
}