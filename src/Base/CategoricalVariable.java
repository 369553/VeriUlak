package Base;

import Service.ClassStringDoubleConverter;
import Service.MatrixFunctions;
import java.util.ArrayList;
import java.util.HashMap;

public class CategoricalVariable{
    private String name;// Değişken ismi
    private int categoryNumber;// Bu değişkenin barındırdığı kategori sayısı. Misal, Türkiye için 'il' değişkeni 81 sınıf barındırıyor
    private Class dType;// Asıl verilerin veri tipi
    private Object[] realData;// Asıl veriler
    private Object codedData;// Kodlanmış sütun verileri Buradaki veriler dizi yâ dâ matris şeklindedir
    private Class typeOfCodedUnitValue;// 'codedData' verisinin veri tipi
    private CODING_TYPE codingType;// Kategori kodlama tipi
    private Object[] uniqueValues;// Kategori değerleri, misal 'il' değişkeninin münferid kategori değerleri = {İstanbul, Amasya, ..}
    private HashMap<Object, Integer> memberCountsOfCategories;// Her kategoriden kaç tâne veri var, bilgisini tutuyor. Yapısı <kategori değeri, kategorideki veri sayısı>
    private Object bitOfTrue = null;// Tek nokta vektörü kodlamasındaki olumlu bit : true | 1
    private HashMap<Object, Object> mapCodeToReal;// Kodlanmış veriden kategoriye ulaşmak için bir değişken. ONEHOTVECTOR kodlaması için yapısı farklıdır. Yapısı : <kodlanmış veri, kategori değeri>; ONEHOTVECTOR için yapısı : <'true' yâ dâ '1' olan bitin indisi, kategori değeri>
    // SON TANIMLANAN DEĞİŞKEN TEK NOKTA VEKTÖRÜ KODLAMASI İÇİN ÇALIŞMAZ; UYGUN DEĞİL; ONUN İÇİN AYRI BİR YAPI KURULMASI GEREKİR

    public enum CODING_TYPE{
        ORDINAL,
        BINOMIAL,
        ONEHOTVECTOR,
        NO_CODING
    }
//Sıralı kodlama için sıra numarasını verdiğimde kategori ismini alamıyorum
    public CategoricalVariable(String name, Class typeOfRealData, Object[] realData, Object codedData, Class typeOfCodedUnitValue, CODING_TYPE codingType){
        // eğer kullanıcı typeOfCodedUnitValue değerini öncül veri tipi (primitive types)
        // şeklinde verdiyse bunu kapsülle. Misal, boolean.class verildiyse Boolean.class olarak değiştir
        
        this.name = name;
        this.dType = typeOfRealData;
        this.realData = realData;
        this.codedData = codedData;
        this.typeOfCodedUnitValue = typeOfCodedUnitValue;
        this.codingType = codingType;
        findCategoryNumberAndUniqueValues(realData);
        extractMapOfCodeToValue();
    }

//İŞLEM YÖNTEMLERİ:
    public static ArrayList<String> produceStatisticAsList(CategoricalVariable variable){
        if(variable == null)
            return null;
        ArrayList<String> values = new ArrayList<String>();
        values.add("Sütun gerçek veri tipi : " + ClassStringDoubleConverter.getService().getShortName(variable.dType));
        values.add("Kodlama tipi : " + getCodingTypeNameAsTR(variable.codingType));
        values.add("Veri sayısı : " + variable.realData.length);
        values.add("Dolu veri sayısı : " + getNonEmptyCellCount(variable.realData));
        values.add("Kategori sayısı : " + variable.categoryNumber);
        int len = 1;
        if(variable.codingType == CODING_TYPE.ONEHOTVECTOR)
            len = ((Object[][]) variable.codedData)[0].length;
        values.add("Kodlanmış veri sütun sayısı : " + len);
        for(int sayac = 0; sayac < variable.categoryNumber; sayac++){// Kategorilerdeki veri sayısını ekle
            Object ct = variable.getUniqueValues()[sayac];
            values.add(String.valueOf(ct) + " kategorisi veri sayısı : " + variable.getMemberCountOfCategory(ct));
        }
        if(variable.codingType != CODING_TYPE.NO_CODING){
            for(int sayac = 0; sayac < variable.categoryNumber; sayac++){// Kategorilerin kodunu ekle
                Object ct = variable.getUniqueValues()[sayac];
                values.add(String.valueOf(ct) + " kategorisi kodu : " + variable.getCodeAsStringFromCategoryValue(ct));
            }
        }
        return values;
    }
    public static String getCodingTypeNameAsTR(CategoricalVariable.CODING_TYPE codingType){
        String value = null;
        if(codingType == CODING_TYPE.ORDINAL)
            value = "Sıralı kodlama";
        if(codingType == CODING_TYPE.BINOMIAL)
            value = "Bitsel kodlama";
        if(codingType == CODING_TYPE.ONEHOTVECTOR)
            value = "Tek nokta vektörü (One Hot Encoding) biçiminde kodlama";
        if(codingType == CODING_TYPE.NO_CODING)
            value = "Kodlama yok";
        return value;
    }
    public static int getNonEmptyCellCount(Object[] data){
        if(data == null)
            return 0;
        if(data.length == 0)
            return 0;
        int nonEmptyCounter = 0;
        for(int sayac = 0; sayac < data.length; sayac++){
            if(data[sayac] != null)
                nonEmptyCounter++;
        }
        return nonEmptyCounter;
    }
    public int getColCount(){
        if(this.codingType != CODING_TYPE.ONEHOTVECTOR)
            return 1;
        return getCategoryNumber();
    }
    //ARKAPLAN İŞLEM YÖNTEMLERİ:
    private void findCategoryNumberAndUniqueValues(Object[] colData){
        ArrayList<Object> uniqVals = new ArrayList<Object>();
        for(int sayac = 0; sayac < colData.length; sayac++){
            boolean isNew = true;
            for(Object objVal : uniqVals){
                if(objVal.equals(colData[sayac])){// Bu kontrol nesnelerin aynı değerde olup, olmadıklarını kontrol ediyor, test edildi
                    isNew = false;
                    int member = getMemberCountsOfCategories().get(colData[sayac]);
                    getMemberCountsOfCategories().put(colData[sayac], member + 1);
                    break;
                }
            }
            if(isNew){
                if(colData[sayac] != null){
                    uniqVals.add(colData[sayac]);
                    getMemberCountsOfCategories().put(colData[sayac], 1);
                }
            }
        }
        uniqueValues = new Object[uniqVals.size()];
        uniqVals.toArray(uniqueValues);
        categoryNumber = uniqueValues.length;
    }
    private void extractMapOfCodeToValue(){// Kod'dan gerçek değere ulaşabilmeyi sağlayan haritayı üret
        mapCodeToReal = new HashMap<Object, Object>();
        if(codingType == CODING_TYPE.ORDINAL || codingType == CODING_TYPE.BINOMIAL){
            for(int sayac = 0; sayac < realData.length; sayac++){// Bütün gerçek veriler dolaşılmamalı
                mapCodeToReal.put(((Object[])codedData)[sayac], realData[sayac]);
            }
        }
        else if(codingType == CODING_TYPE.ONEHOTVECTOR){// Tek nokta vektörü kodlamasında değişken sayısı 1'den fazla olmazsa hatâ verebilir; güvenlik önlemi al
            if(typeOfCodedUnitValue == boolean.class || typeOfCodedUnitValue == Boolean.class)
                bitOfTrue = true;
            else
                bitOfTrue = 1;
            for(int sayac = 0; sayac < categoryNumber; sayac++){
                for(int s2 = 0; s2 < realData.length; s2++){// Verilen kategori değerini gerçek değerler arasında ara
                    if(realData[s2] == getUniqueValues()[sayac]){
                        Object[] code = ((Object[][]) codedData)[s2];
                        for(int s3 = 0; s3 < code.length; s3++){
                            // BURADA KALINDI, aşağıdaki yöntem maâlesef hatâ veriyor:
                            if(typeOfCodedUnitValue.cast(code[s3]) == bitOfTrue){
//                                System.err.println("true olan bitin indisi : " + s3);
                                mapCodeToReal.put(s3, realData[s2]);
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

//ERİŞİM YÖNTEMLERİ:
    public String getName(){
        return name;
    }
    public int getCategoryNumber(){
        return categoryNumber;
    }
    public Class getdType(){
        return dType;
    }
    public Object[] getRealData(){
        return realData;
    }
    public Object getCodedData(){
        if(codedData == null)
            return realData;
        return codedData;
    }
    public Object[] getCodedDataAsArray(){
        if(codedData == null)
            return realData;
        if(codingType != CODING_TYPE.ONEHOTVECTOR){
            return Object[].class.cast(codedData);
        }
        return null;
    }
    public Object[][] getCodedDataAsMatrixForOneHotCoding(){
        if(codedData == null)
            return null;
        if(codingType == CODING_TYPE.ONEHOTVECTOR){
            return Object[][].class.cast(codedData);
        }
        return null;
    }
    public Class getTypeOfCodedUnitValues(){
        return typeOfCodedUnitValue;
    }
    public CODING_TYPE getCodingType() {
        return codingType;
    }
    public HashMap<Object, Integer> getMemberCountsOfCategories(){
        if(memberCountsOfCategories == null){
            memberCountsOfCategories = new HashMap<Object, Integer>();
        }
        return memberCountsOfCategories;
    }
    public Object[] getUniqueValues() {
        return uniqueValues;
    }
    public int getMemberCountOfCategory(Object categoryValue){
        return getMemberCountsOfCategories().get(categoryValue);
    }
    public HashMap<Object, Object> getMapCodeToReal(){
//        if(mapCodeToReal == null)
//            mapCodeToReal = new HashMap<Object, Object>();
        return mapCodeToReal;
    }
    public Object getCategoryValueFromCode(Object code){
        if(codingType == CODING_TYPE.ONEHOTVECTOR){
            Object[] codeAsArray = (Object[]) code;
            boolean found = false;// Bu bit birden fazla 'true' yâ dâ '1' değeri barındıran kodun kabûl edilmemesi içindir
            int bitNumberOfValue = -1;
            for(int sayac = 0; sayac < codeAsArray.length; sayac++){
                if(typeOfCodedUnitValue.cast(codeAsArray[sayac]) == bitOfTrue){
                    if(found)// Daha evvel 'true' yâ dâ '1' barındıran hücre bulunduysa
                        return null;
                    found = true;
                    bitNumberOfValue = sayac;
                }
            }
            if(found)// Eğer değer bulunduysa
                return getMapCodeToReal().get(bitNumberOfValue);
        }
        return getMapCodeToReal().get(code);
    }
    public Object getCodeFromCategoryValue(Object categoryValue){
        for(Object key : getMapCodeToReal().keySet()){
            if(mapCodeToReal.get(key) == categoryValue){
                if(codingType == CODING_TYPE.ONEHOTVECTOR){
                    Object[] code = null;
                    if(typeOfCodedUnitValue == boolean.class || typeOfCodedUnitValue == Boolean.class)
                        code = MatrixFunctions.produceBooleanArrayAsNotPrimitive(categoryNumber, false);
                    else{
                        if(typeOfCodedUnitValue == Integer.class || typeOfCodedUnitValue == int.class)
                            code = MatrixFunctions.produceZeroArray(categoryNumber, Integer[].class);
                        else if(typeOfCodedUnitValue == Short.class || typeOfCodedUnitValue == short.class)
                            code = MatrixFunctions.produceZeroArray(categoryNumber, Short[].class);
                    }
                    code[Integer.valueOf(String.valueOf(key))] = bitOfTrue;
                    return code;
                }
                else
                    return key;
            }
        }
        return null;
    }
    public String getCodeAsStringFromCategoryValue(Object categoryValue){
//        System.out.println(String.valueOf(categoryValue));
        Object code = getCodeFromCategoryValue(categoryValue);
        String value = null;
        if(codingType == CODING_TYPE.ONEHOTVECTOR){
            Object[] codeAsArray = (Object[]) code;
            StringBuilder strbui = new StringBuilder();
            for(int sayac = 0; sayac < categoryNumber; sayac++){
                strbui.append(String.valueOf(codeAsArray[sayac]));
                if(sayac < categoryNumber - 1)
                    strbui.append(", ");
            }
            value = strbui.toString();
        }
        else
            value = String.valueOf(code);
        return value;
    }
    public Object getCategoryValueFromCodeForOneHotEncoding(int bitNumberOfTrueOr1){// 'true' yâ dâ '1' barındıran bitin numarası
        return getMapCodeToReal().get((Integer) bitNumberOfTrueOr1);
    }
    public Object[] getCodeFromCategoryValueForOneHotEncoding(Object categoryValue){
        return (Object[]) getCodeFromCategoryValue(categoryValue);
    }
    public Object getBitOfTrue(){
        return bitOfTrue;
    }
}