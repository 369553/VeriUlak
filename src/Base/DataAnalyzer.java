package Base;

import Service.MathFuncs;
import Service.MatrixFunctions;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
//Veri uzunluğu değiştiğinde rowCount ve colCount'u değiştirmeyi unutma veyâ bunu otomatize edecek şekilde bir yapı kur
//getUniqueAllColValues.ilkKontrolSatırı'nı düzelt; verimli hâle getir
public class DataAnalyzer{
    private Object[][] data;
    private int rowCount, colCount = -2;//Satır sayısı, sütun sayısı
    private boolean isCheckedIsDataNull = false;
    private boolean isDataNull = false;
    private ArrayList<Integer> emptyRowIndexes;
    private ArrayList<Integer> emptyColIndexes;
    private HashMap<Integer, Integer> emptyRowCounterForCol = new HashMap<Integer, Integer>();//<sütun numarası, sütundaki boş hücre sayısı>
    private HashMap<Integer, Integer> emptyColCounterForRow = new HashMap<Integer, Integer>();//<satır numarası, satırdaki boş hücre sayısı>
    private double[] emptyRateOfRows;
    private double[] emptyRateOfCols;
    private String[] columnNames;
    private boolean isColumnNamesAlreadyTaked = false;//İlk satır sütun ismi olarak kaydedildiğinde, ilgili yöntem yeniden çağrıldığında ilk sütunun tekrar silinmemesi için
    private ArrayList<Advice> adviceList;
    private ArrayList<String> messages;
    private boolean isEmptyRatesIsUpdate = false;
    private Class[] dTypes;
    private HashMap<Integer, HashMap<Class, Integer>> dTypesCounterForCols;//Her sütun için o sütunda hangi veri tipinden ne kadar olduğu tutuluyor;<sütun numarası, <veri tipinin sınıfı, o tipte olan satır sayısı>>
    private boolean autoConvertDataType = true;// String içerisindeki Double ve Boolean veri tiplerini otomatik algılasın mı?
    private boolean isColumnDetailsIsUpdate;// Sütun verilerinin taze - doğru olup, olmadığı bilgisini performans için tutar
    private HashMap<Integer, HashMap<String, Object>> colDetails;// En son hesâplanan sütun bilgisini tutar
    private boolean[] areUniqueValuesCalculated;// Her sütundaki münferid veriler hesâplanmış mı?
    private Object[][] uniqueColValues = null;// [sütunNo][... -> sütundaki münferid değerler]
    private boolean[] couldCategorical;// Sütunlar için etiketli (kategorik) veri olup, olmama ihtimâli
    private boolean isCalculatedCouldCategorical = false;// Sütun verilerinin etiketli veri olup, olmadığı husûsunda hesaplama yapıldı mı?
    private boolean isCalculatedEmptyRates = false;// Boş veri oranları hesâplandı mı?
    private boolean isCountedEmptyRows = false;// Boş veriler sayıldı mı?
    private boolean isEmptyCountNumbersUpdate = false;// Satır ve sütundaki boş hücre sayısı taze mi? emptyColCounterForRow ve emptyRowCounterForCol taze mi?
    private boolean isRunAutoAssignDataTypes = false;// autoAssignDataTypes() yöntemi daha önce çalıştırıldı mı?
    private ArrayList<Process> savedProcs;// İşlem adımlarını tutan liste
    private boolean[] isColumnIsCategorical;// <sütunNo, kategorik olup, olmadığı bilgisi>
    private HashMap<Integer, CategoricalVariable> mapCategoricalVars;// Kategorik sütunların bilgilerini tutmak için. Yapısı : <sütunNo, kategorikDeğişken değişkeni>
    private HashMap<Integer, Integer> mapColIndexToCategoricalColIndex;// Sütunun hangi kategorik değişkene âit olduğu değerini tutmak için Yapısı : <sütunNo, kategorikDeğişken değişkenin başlangıç sütun numarası>
    private Statistic[] statistics;// Her sütun için istatistik bilgileri
    private boolean[] isStatisticIsUpdate;// Her sütun için istatistik bilgilerinin taze olup, olmadığını anlamak için

    public DataAnalyzer(Object[][] data){
        this(data, true, false);
    }
    public DataAnalyzer(Object[][] data, boolean autoDetectDataType, boolean setFirstRowAsAColumnNames){//autoConvertDataType : Metîn tipindeki verileri otomatik tespit etsin mi
        this.autoConvertDataType = autoDetectDataType;
        this.data = data;
        isDataNull();
        if(isCheckedIsDataNull && !isDataNull){
            basicAnalyze();
            detectAndDeleteFullEmptyRows();
            detectAndDeleteFullEmptyCols();
            if(setFirstRowAsAColumnNames)
                setFirstRowAsColumnNames(true);
            detectDataTypes(autoDetectDataType);
            if(autoDetectDataType){
                autoAssignDataTypes();
                fixTypesOfDataDependMostMatchedType();
            }
        }
    }

//İŞLEM YÖNTEMLERİ:
    public boolean isDataNull(){
        if(!isCheckedIsDataNull){
            isDataNull = analyzeIsDataNull();
            isCheckedIsDataNull = true;
        }
        return isDataNull;
    }
    public void setFirstRowAsColumnNames(){
        setFirstRowAsColumnNames(false);
    }
    public boolean setColumnDataTypes(Class[] dataTypes){
        if(dataTypes == null){
            addMessage("Verilen veri tipleri null; devâm edilemiyor");
            return false;
        }
        boolean allTypesTaked = true;//Tüm sütunların veri tipleri verildiyse 'true'; değilse 'false'
        if(dataTypes.length < colCount){
            addMessage("Tüm sütunların veri tipi verilmedi; verilen sütunların veri tipleri ayarlanıyor");
            allTypesTaked = false;
        }
        boolean[] isMatchedResults = checkAreDataTypesTrue(dataTypes);
        boolean isSuccess = areAllValuesTrue(isMatchedResults);
        if(!isSuccess){
            addMessage("Verilen veri tipi ile sütundaki veriler uyuşmuyor; bu sebeple veri tipleri atanamıyor");
            // Uyumsuz veri tiplerini göster
            return false;
        }
        dTypes = new Class[colCount];
        for(int sayac = 0; sayac < dataTypes.length; sayac++){
            dTypes[sayac] = dataTypes[sayac];
        }
        if(!allTypesTaked){
            for(int sayac = dataTypes.length; sayac < colCount ; sayac++){
                dTypes[sayac] = null;
            }
            Method mth = null;
            try{
                mth = DataAnalyzer.class.getDeclaredMethod("autoAssignDataType", int[].class);
            }
            catch(NoSuchMethodException ex){
                //.;.
            }
            catch (SecurityException ex){
                //.;.
            }
            String str = "Veri tiplerinin otomatik atanması için yöntemimiz var!";
            if(mth != null){
                String strAdv = (dataTypes.length - colCount) + " adet sütunun veri tipi ayarlanmadı. Sütun veri tipini otomatik atama yöntemini kullanabilirsiniz";
                int[] colIndexes = new int[colCount - dataTypes.length];
                int counter = 0;
                for(int sayac = dataTypes.length; sayac < colCount; sayac++){//Veri tipi verilmeyen sütunları bir listeye atıyorum
                    colIndexes[counter] = sayac;
                }
                getAdvices().add(new Advice(strAdv, mth, this, new Object[]{colIndexes}));
                str += " Tavsiyelere bakınız";
            }
            addMessage(str);
            return true;
        }
        return true;
    }
    public boolean setCellData(int row, int col, Object value){// Bu yönteme erişim güvenli değil sanırım
        if(row < 0 || col < 0)
            return false;
        if(row >= rowCount || col >= colCount)
            return false;
        if(dTypes == null)
            autoAssignDataTypes();
        Class type = getDataTypes()[col];
        if(type == null)// Sütun veri tipi belirlenmediyse ilgili değişikliğe izin verilmiyor
            return false;
        Class valueType = null;
        if(value != null)
            valueType = wrapPrimitiveClass(new Class[]{value.getClass()})[0];
        if(type == valueType || value == null){
            data[row][col] = value;
            getAreUniqueValuesCalculated()[col] = false;// Sütun münferid değeri 'taze değil' olarak işâretleniyor
            getIsStatisticIsUpdate()[col] = false;// Sütun istatistiği 'taze değil' olarak işâretleniyor
            if(value == null){
                isEmptyCountNumbersUpdate = false;
                isEmptyRatesIsUpdate = false;
            }
            return true;
        }
        return false;
    }
    public boolean setColumnName(String oldName, String newName){
        int index = -1;
        boolean isNameUsedBefore = false;
        for(int sayac = 0; sayac < getColumnNames().length; sayac++){
            if(columnNames[sayac].equals(oldName))
                index = sayac;
        }
        for(String str : columnNames){
            if(str.equals(newName))
                isNameUsedBefore = true;
        }
        if(index != -1 && !isNameUsedBefore){
            columnNames[index] = newName;
            if(colDetails != null){
                if(colDetails.get(index) != null){
                    colDetails.get(index).put("name", newName);
                }
            }
            return true;
        }
        else
            return false;
    }
    public void autoAssignDataType(int[] colIndexes){// Tam tespit olmazsa ilgili sütunların veri tipi belirlenmemiş olarak kaydedilir
//        System.out.println("Fonksiyon çağrıldı : autoAssignDataType()");
        if(dTypesCounterForCols == null)
            detectDataTypes(true);
        if(dTypesCounterForCols == null)
            return;
        if(dTypes == null)
            dTypes = new Class[colCount];
        for(int sayac = 0; sayac < colIndexes.length; sayac++){
            HashMap<Class, Integer> mapCountedTypes = dTypesCounterForCols.get(colIndexes[sayac]);
            HashMap<String, Object> res = detectMatchedDataTypeForCol(colIndexes[sayac], mapCountedTypes);
            if(!((Boolean) res.get("isSuccess"))){// Bu sütun için veri tipi otomatik tespit edilemiyor
                dTypes[colIndexes[sayac]] = null;// Bu sütunun veri tipini 'null' olarak ata
                // Tavsiye ekleyebiliyorsan ekle
            }
            // Mesaj varsa, ekle:
            if(res.get("message") != null){
                addMessage((String) res.get("message"));
            }
            dTypes[colIndexes[sayac]] = (Class) res.get("result");
        }
    }
    public void autoAssignDataTypes(){// Double veri tipinin üstünlüğü var
        if(isRunAutoAssignDataTypes)// Daha önce bu yöntem çalıştırıldıysa, yeniden çalıştırma
            return;
        // Bu fonksiyon çağrılıyorsa autoDetectDataType == null ise, veri tiplerini baştan tespit et
        int[] colIndexes = new int[colCount];
        for(int sayac = 0; sayac < colCount; sayac++){
            colIndexes[sayac] = sayac;
        }
        autoAssignDataType(colIndexes);
        isRunAutoAssignDataTypes = true;
    }
    public void reRunAutoAssignDataTypes(){
        isRunAutoAssignDataTypes = false;
        autoAssignDataTypes();
    }
    public boolean[] checkAreDataTypesTrue(Class[] dataTypes){
        Class[] clss = wrapPrimitiveClass(dataTypes);
        int limit = 10;
        boolean[] isMatchedResults = new boolean[dataTypes.length];
        for(int sayac = 0; sayac < dataTypes.length; sayac++){
            int rowCounterForControl = 0;
            int isMatched = 0;
            for(int s2 = 0; s2 < limit; s2++){//Dolu ilk on satıra bakıp, cast işlemi uygulanmaya çalışılır; eğer veri 'null' değilse ve hatâ alınıyorsa, verilen veri tipi uygun değildir
                if(s2 == rowCount)
                    break;
                if(data[s2] == null){//Satırın tek null değişkenden oluşması durumu çözülmüştü; bu kod silinebilir
                    if(limit < 18){
                        limit++;
                        continue;
                    }
                    else
                        break;
                }
                if(data[s2][sayac] == null){//Boş veriyle karşılaşırsan kontrol edilen satır sayısının düşmemesi için 'limit'i bir arttır
                   limit++;
                   continue;
                }
//                System.out.println("veri tipi : " + data[s2][sayac].getClass().getName());
                rowCounterForControl++;//Kontrol edilen satır sayısını bir arttır
                if(clss[sayac].equals(data[s2][sayac].getClass())){
                    isMatched++;
                }
            }
            if(isMatched < (rowCounterForControl / 2))// Kontrol edilen satır sayısının yarısından azı eşleştiyse bu sütun bu veri tipiyle eşleşmiyor demektir
                isMatchedResults[sayac] = false;
            else
                isMatchedResults[sayac] = true;
        }
        return isMatchedResults;
    }
    public void detectDataTypes(boolean convertDataTypes){
        // Veri değişmediyse ve veri tipleri daha önce tespit edildiyse yeniden tespit etme, denebilir
        //Her satırda hangi tipten kaç veri örneği var, bakalım:
        dTypesCounterForCols = new HashMap<Integer, HashMap<Class, Integer>>();
        boolean checkForInteger = false;
        for(int sayac = 0; sayac < colCount; sayac++){
            HashMap<Class, Integer> counterForEveryDataTypes = new HashMap<Class, Integer>();
            for(int s2 = 0; s2 < rowCount; s2++){
                if(data[s2][sayac] == null){
//                    System.err.println("data[s2][sayac] == null imiş");
                    continue;
                }
                if(convertDataTypes){
                    Object cellValue = data[s2][sayac];
                    if(cellValue.getClass().getName().equals(Double.class.getName())){
                        checkForInteger = true;
                    }
                    if(cellValue.getClass().getName().equals(String.class.getName()) || checkForInteger){
                        String valueForSend = null;
                        if(checkForInteger)
                            valueForSend = String.valueOf(cellValue);
                        else
                            valueForSend = (String) cellValue;
                        data[s2][sayac] = convertStringDataType(valueForSend);
                    }
                }
                //Veri tipini kaydetmek için
                if(data[s2][sayac].getClass() == null)//Bu sütun için bu veri tipiyle ilk kez karşılaşılıyorsa
                    counterForEveryDataTypes.put(data[s2][sayac].getClass(), 1);
                else{
                    int number = 0;
                    Integer value = counterForEveryDataTypes.get(data[s2][sayac].getClass());
                    if(value != null)
                        number = value;
                    counterForEveryDataTypes.put(data[s2][sayac].getClass(), number + 1);
                }
            }
            dTypesCounterForCols.put(sayac, counterForEveryDataTypes);
        }
        // En çok veri örneği olan veri tipi o sütunun veri tipi olarak atanırsa
        // .. bu, doğru bir yaklaşım olabilir;
        // ama sütundaki verilerin çoğu boş ise bu yapılmamalı
        // o sebeple bu atama yapılırken sütundaki boşluk oranlarını da göz önüne al:
        //Veri tiplerini kullanıcı da ayarlayabilir; biz de bakmalıyız
        /*
        for(int sayac = 0; sayac < colCount; sayac++){
            System.out.println(sayac + " sütunu için veri tipi analiz sonucu:");
            HashMap<Class, Integer> map = dTypesCounterForCols.get(sayac);
            System.out.println("Bu sütunda:");
            for(Class cls : map.keySet()){
                System.out.println(cls.getName() + " veri tipinden " + map.get(cls) + " kadar veri örneği var");
            }
            System.out.println("\n**************\n");
        }
        */
    }
    public static Class[] wrapPrimitiveClass(Class[] dTypes){// Daha verimli bir yöntemi olması lazım!
        Class[] values = new Class[dTypes.length];
        for(int sayac = 0; sayac < dTypes.length; sayac++){
            if(dTypes[sayac].equals(int.class))
                values[sayac] = Integer.class;
            else if(dTypes[sayac].equals(double.class))
                values[sayac] = Double.class;
            else if(dTypes[sayac].equals(boolean.class))
                values[sayac] = Boolean.class;
            else if(dTypes[sayac].equals(char.class))
                values[sayac] = String.class;
            else
                values[sayac] = dTypes[sayac];
        }
        return values;
    }
    public HashMap<Integer, HashMap<String, Object>> getColumnsDetail(){// Dönüş : <sütunNo, sütunDetayHaritası>
        if(!isColumnDetailsIsUpdate){
            // Öncesinde çalıştırılması gerekenler: findEmptyRowsAndCols , calculateEmptyRates , eğer autoAssignDataType = true ise reRunAutoAssignDataTypes() , calculateUniqueValuesOnColumns
            if(!isEmptyRatesIsUpdate)
                calculateEmptyRates();
            /*if(autoConvertDataType)
                reRunAutoAssignDataTypes();*/
            getUniqueAllColValues();// Hesaplama fonksiyon içerisinde yapılıyor
            this.colDetails = new HashMap<Integer, HashMap<String, Object>>();
            for(int sayac = 0; sayac < colCount; sayac++){
                boolean isCategorical = getIsColumnIsCategorical()[sayac];
                HashMap<String, Object> curr = new HashMap<String, Object>();
                curr.put("number", sayac);// Sütun numarası : number
                curr.put("name", getColumnNames()[sayac]);// Sütun ismi : name
                curr.put("totalCellNumber", rowCount);// Sütundaki toplam veri sayısı : totalCellNumber
                curr.put("isNumber", getIsNumber(sayac));// Sütundaki veri sayısal veri ise 'true' olmalıdır
                curr.put("size", calculateNonEmptyOnCol(sayac));// Sütundaki dolu veri sayısı : size
                curr.put("emptyCellRate", getEmptyRateOfCols()[sayac]);// Sütundaki boş hücre sayısının toplam veriye oranı : emptyCellRate
                curr.put("dataType", getDataTypes()[sayac].getName());// Sütunun veri tipi : dataType
                curr.put("isCategorical", isCategorical);// Sütunun kategorik olup, olmadığı bilgisi : isCategorical
                curr.put("uniqueValueNumber", getUniqueColValues(sayac).length);// Sütundaki münferid veri sayısı : uniqueValueNumber
                if(isCalculatedCouldCategorical)
                    curr.put("couldCategorical", getCouldCategorical()[sayac]);
                if(isCategorical){
                    CategoricalVariable var = getMapCategoricalVars().get(sayac);
                    if(var == null){// Tek nokta vektöründe birden fazla sütun aynı kategoride olabiliyor; fakat kategori ilk sütunun numarası ile haritalanıyor;
                        Integer headIndex = getMapColIndexToCategoricalColIndex().get(sayac);// Kategorinin ilk sütununun numarasını bul
                        if(headIndex != null){
                            var = getMapCategoricalVars().get(headIndex);
                        }
                    }
                    curr.put("categoricalStatistic", CategoricalVariable.produceStatisticAsList(var));
                }
                /*
                Sütun kategorik ise kategori isimlerini ve her kategoriden..
                ne kadar örnek olduğunu da ekle
                */
                colDetails.put(sayac, curr);
            }
        }
        return colDetails;
    }
    public HashMap<String, Object> getColumnDetails(int colNumber){
        if(colNumber < 0 || colNumber >= colCount)
            return null;
        return getColumnsDetail().get(colNumber);
    }
    public Statistic getStatisticForColumn(int colNumber){
        if(colNumber >= colCount)
            return null;
        if(!getIsStatisticIsUpdate()[colNumber]){
            calculateStatistic(colNumber);
//            System.err.println("Sütun verisi taze değilmiş");
        }
        return statistics[colNumber];
    }
    public void reRunFindEmptyRowsAndCols(){
        isEmptyCountNumbersUpdate = false;
        findEmptyRowsAndCols();
    }
    public void reRunCalculateEmptyRates(){
        isEmptyRatesIsUpdate = false;
        reRunFindEmptyRowsAndCols();
        calculateEmptyRates();
    }
    public Integer[] findEmptyCellOnColumn(int colIndex){
        if(isEmptyCountNumbersUpdate){
            if(getEmptyRowCounterForCols().get(colIndex) == 0)// Eğer sütunda boş hücre yoksa 'null' döndür
                return null;
        }
        ArrayList<Integer> li = new ArrayList<Integer>();
        for(int sayac = 0; sayac < rowCount; sayac++){
            Object val = data[sayac][colIndex];
            if(val == null)
                li.add(sayac);
            else if(val.getClass() == String.class){
                if(((String) val).isEmpty())
                    li.add(sayac);
            }
        }
        Integer[] ind = new Integer[li.size()];
        li.toArray(ind);
        return ind;
    }
    //ARKAPLAN İŞLEM YÖNTEMLERİ:
    private boolean analyzeIsDataNull(){//veri 'null' mı, kaba kontrolünü yapan yöntem
        return areAllValuesEmpty(data, false);
    }
    private HashMap<String, Object> detectMatchedDataTypeForCol(int colIndex, HashMap<Class, Integer> mapOfCountedDataTypes){// Eğer bir sütunda hem noktalı sayı, hem de tamsayı varsa noktalı sayıyı varsayılan tip yap; ama Integer verilerin sayısı Double verilerin sayısından ciddî manâda fazla ise (toplam veri sayısına oranlanarak bakılmalı) öneri oluştur
//        System.out.println("Fonksiyon çağrıldı : detectMatchedDataTypeForCol (sütun numarası : " + colIndex + ")");
        int mostCountedTypeValue = -1;
        Class mostCountedType = null;// En çok tekrar eden veri tipi
        Class secondMostCountedType = null;// En çok tekrar eden ikinci veri tipi
        HashMap<String, Object> resVal = new HashMap<String, Object>();// Sonucu döndürmek için bir değişken
        boolean isSameCountedValue = false;// En çok tekrar eden iki veri tipinden aynı sayıda örnek varsa 'true', diğer durumlarda 'false'
        for(Class dTyp : mapOfCountedDataTypes.keySet()){
            int counted = mapOfCountedDataTypes.get(dTyp);
            if(counted > mostCountedTypeValue){
                mostCountedTypeValue = counted;
                mostCountedType = dTyp;
            }
        }
        for(Class dTyp : mapOfCountedDataTypes.keySet()){
            if(mapOfCountedDataTypes.get(dTyp) == mostCountedTypeValue)
                if(dTyp != mostCountedType){
                    secondMostCountedType = dTyp;
                    isSameCountedValue = true;
                }
        }
        if(mostCountedType == Integer.class){
            Integer doubleValueNumber = mapOfCountedDataTypes.get(Double.class);
            if(doubleValueNumber != null){
//                System.err.println("Sütunda hem tamsayı, hem de noktalı sayı var");
                int numberOfDoubleValues = doubleValueNumber;
                if(numberOfDoubleValues / mostCountedTypeValue < 0.25){// Eğer Double verilerin sayısı int verilerin sayısından 1/4'ten daha az ise, öneri oluştur
                    //getAdvices().add(new Advice(strAdvice, mthApplyAdvice, rowCount, columnNames));
//                    System.out.println("Double veri sayısı çok düşük ama!");
                }
                resVal.put("result", Double.class);
                resVal.put("isSuccess", true);
                resVal.put("message", "Sütunundaki verilerin çoğu tamsayı, bâzı verilerde hatâ varsa ve sütun veri tipi tamsayı ise sütun veri tipini değiştirebilirsiniz");
                return resVal;
            }
        }
        if(isSameCountedValue){
            System.err.println("Sütunda en çok tekrar eden iki veri tipinin tekrar sayısı aynı" + "(sütun no : " + colIndex + ")");
//            System.out.println("Birinci veri tipi.isim : " + mostCountedType.getName());
//            System.out.println("İkinci veri tipi.isim : " + secondMostCountedType.getName());
            resVal.put("message", "Sütunda en çok tekrar eden iki veri tipinin tekrar sayısı aynı : " + mostCountedType.getName() + " , " + secondMostCountedType.getName());
            resVal.put("isSuccess", false);
            return resVal;
        }
        double rate = emptyRateOfCols[colIndex];
        
        resVal.put("result", mostCountedType);
        resVal.put("isSuccess", true);
        if(rate > 50.0){
            String strMsg = colIndex + " sütunundaki veri tipinin otomatik tespiti yapıldı; fakat sütundaki verilerin çoğu boş";
            resVal.put("message", strMsg);
        }
        return resVal;
        // Dönüş haritası:
        // result : veri tipi (Class)
        // isSuccess : başarılı mı? (boolean)
        // message : Başarısız olma durumunda başarısızlık mesajı; başarı olduğunda mesaj eklenmiyor
    }
    private Object convertStringDataType(String stringValue){//Boolean verileri tespit eder, sayısal verileri tespit eder; tüm sayısal verileri Double tipine dönüştürür
        String sVal = stringValue.trim();
        Object objVal = null;
        if(sVal.equalsIgnoreCase("true") || sVal.equalsIgnoreCase("false")){
            objVal = Boolean.valueOf(sVal);
        }
        else{
            Double dValue = null;
            try{
                dValue = Double.valueOf(sVal);//Double'a dönüştürülebiliyorsa dönüştür
            }
            catch(NumberFormatException exc){
//                System.out.println("Dönüştürme hatâsı!");
            }
            if(dValue != null){
                objVal = dValue;
            }
            else
                objVal = stringValue;
        }
        Integer exactNumber;
        if(objVal.getClass() == Double.class){// Eğer veri bir sayı ise tamsayı olup, olmadığını sorgula
            double num = (Double) objVal;
            if(num - Math.ceil(num) == 0){
                exactNumber = (int) num;
                objVal = exactNumber;
            }
        }
        return objVal;
    }
    private void setFirstRowAsColumnNames(boolean dontReRunAutoAssignDataType){
        int[] howManyCellAreEmptyOnAffectedCol = new int[colCount];
        if(isColumnNamesAlreadyTaked){
            System.out.println("İlk satır zâten sütun ismi olarak atanmış");
            return;
        }
        columnNames = new String[data[0].length];
        for(int sayac = 0; sayac < columnNames.length; sayac++){
            columnNames[sayac] = String.valueOf(data[0][sayac]);
            if(columnNames[sayac] == null){
                columnNames[sayac] = "[" + sayac + "]";
                howManyCellAreEmptyOnAffectedCol[sayac] += 1;
            }
            else if(columnNames[sayac].isEmpty())
                howManyCellAreEmptyOnAffectedCol[sayac] += 1;
        }
        setData(MatrixFunctions.deleteSelectedRows(data, new int[]{0}));
        if(rowCount < 10){// Test sırasında karşılaştığım özel bir durum, veri sayısı çok az olursa ilk sütunun veri yâ dâ sütun ismi olup, olmaması veri tipi tespiti işlemini bozabiliyor; buna dâir tedbir
            if(autoConvertDataType && !dontReRunAutoAssignDataType){
                detectDataTypes(true);
                reRunAutoAssignDataTypes();
            }
        }
        isColumnNamesAlreadyTaked = true;
        getEmptyRowIndexes().remove(((Object) 0));
        if(emptyRateOfRows != null){
            emptyRateOfRows = MatrixFunctions.deleteSelectedMembers(emptyRateOfRows, new int[]{0});
        }
        if(emptyRateOfCols != null){
            for(int sayac = 0; sayac < colCount; sayac++){
                int emptyCellNumber = (int) ((emptyRateOfCols[sayac] * (rowCount + 1)) / 100);// Silme işleminden evvel kaç satırın boş olduğunu öğrenelim
                emptyCellNumber -= howManyCellAreEmptyOnAffectedCol[sayac];// İlgili sütunda şu an kaç boş hücre olduğunu öğrenelim
                getEmptyRateOfCols()[sayac] =  (((double) emptyCellNumber) / rowCount) * 100;
            }
        }
    }
    private void detectAndDeleteFullEmptyCols(){
        if(!isCalculatedEmptyRates || !isEmptyRatesIsUpdate)// Verideki boşluk oranları hesaplanmadıysa veyâ güncel değilse, hesapla
            calculateEmptyRates();
        ArrayList<Integer> empties = new ArrayList<Integer>();
        for(int sayac = 0; sayac < emptyRateOfCols.length; sayac++){
            if(emptyRateOfCols[sayac] == 100.0){
                empties.add(sayac);
            }
        }
        if(empties.size() == 0)// Boş sütun yoksa sütun silmeye çalışma!
            return;
        int[] del = new int[empties.size()];
        for(int sayac = 0; sayac < empties.size(); sayac++){
            del[sayac] = empties.get(sayac);
        }
        deleteCols(del);
    }
//    private void detectAndDeleteFullEmptyRows(){
//        
//    }
    private void basicAnalyze(){
        rowCount = data.length;// Satır sayısını al, kabaca:
        
        // Her satır için sütun sayısını öğren:
        ArrayList<Integer> nullRowIndexes = new ArrayList<Integer>();
        int[] colCounts = new int[rowCount];
        for(int sayac = 0; sayac < rowCount; sayac++){
            if(data[sayac] == null)
                colCounts[sayac] = 0;
            else
                colCounts[sayac] = data[sayac].length;
        }
        
        // Sütun sayılarının satırlar arasında fark edip, etmediğini kontrol et:
        int lastColCount = -1;// Son sütun numarası
        HashMap<Integer, Integer> mapvalidation = new HashMap<Integer, Integer>();// Sütun numaraları ve aynı sütun numarasına sâhip satır sayısı
        for(int sayac = 0; sayac < colCounts.length; sayac++){
            int cot = colCounts[sayac];
            if(cot == 0){// Sütun numarası sayısı 0 ise
                nullRowIndexes.add(sayac);// Boş satırları tutan listeye bu satırın numarasını ekle
                continue;
            }
            if(mapvalidation.get(cot) == null){//Bu sütun numarasıyla ilk defa karşılaşılıyorsa kaydet
                mapvalidation.put(cot, 1);
            }
            else//Bu sütun numarasına sâhip toplam satır sayısını bir arttır
                mapvalidation.put(cot, mapvalidation.get(cot) + 1);
            if(cot > lastColCount){//Sütun numarası son sütun numarasından büyük ise bu sütun numarasını yeni son sütun numarası olarak ata
                lastColCount = cot;
            }
        }
        // Elimizde muhtemel farklı sütun numaraları ve bu sütun numaralarındaki ittifak sayıları var
        
        // En çok ittifak edilen sütun numarası son sütun numarası sayısından düşükse,..
        // o sütun ile son sütun arasındaki sütunların değerleri boş ise..
        // ittifak edilen sütun numarasını sütun numarası olarak ata
        
        /*for(int colCount : mapvalidation.keySet()){
            System.out.println(colCount + " sütun uzunluğuna sâhip satır sayısı : " + mapvalidation.get(colCount));
        }*/
        
        if(mapvalidation.size() > 1){//Birden fazla son sütun numarası var ise;
            int mostValidateColCount = -1;
            for(int value : mapvalidation.keySet()){
                if(mapvalidation.get(value) > mostValidateColCount)//Eğer sayı olarak eşit olan sütun uzunluğu çıkarsa en uzun sütun numarası mostValidateColCount'a atanıyor; zîrâ yukarıda işlemi artan sırada yapıyoruz
                    mostValidateColCount = value;
            }
            //System.out.println("En çok ittifak edilen sütun numarası : " + mostValidateColCount);
        //Satırların son sütun numaraları arasında çok ittifak edilen sütun numarasını bulduk : mostValidateColCount
            if(mostValidateColCount < lastColCount){//En çok ittifak edilen sütun numarası son sütun numarası değilse : Son sütundaki verilerin çoğu boş ise bu durum olabilir; veriler excel dosyasından alındıysa, Apache POI'nin okurken herhangi bir özelliği değişmiş sütunları da dolu gibi kabûl etmesinden dolayı bu durum oluşabilir
                //System.out.println("Son sütun numarası ile en çok ittifak edilen sütun numarası birbirinden farklı!");
                Object[][] sub = takeSubData(data, mostValidateColCount, lastColCount);//
                boolean isNull = areAllValuesEmpty(sub, true);
                if(isNull)
                    colCount = mostValidateColCount;
                else
                    colCount = lastColCount;
            }
            else
                colCount = mostValidateColCount;
        }
        else
            colCount = lastColCount;
        //if(colCount != lastColCount)//Normalde bu satırdaki şart sağlanırsa alttaki işlemi yapmak gerekir; fakat, ilgili fonksiyon(divideMatrixOnColumn) satırların sütun sayılarını eşitlediğinden son sütun numarası ile ittifak edilen sütun numarası aynı olduğunda da çalıştırıyorum.
        this.data = MatrixFunctions.divideMatrixOnColumn(data, 0, colCount - 1);// Sütun uzunluğu '0' olan satırların sütun uzunluğu 'colCount' kadar yapılıyor
        //Sütun uzunluğu sıfır olan satırlar da siliniyor:
        int[] arr = new int[nullRowIndexes.size()];
        for(int sayac = 0; sayac < arr.length; sayac++){
            arr[sayac] = nullRowIndexes.get(sayac);
        }
        deleteRows(arr);
        //Şu an 'null' değerli olan satırlar silinmedi; çünkü 'null' değerli olan satırların uzunluğu 0 değil, 1;
        //ama güvenlik için (olur da sıfır uzunluklu bir satır gelirse diye) bu işlemi yapıyorum
    }
    private void detectAndDeleteFullEmptyRows(){
        //Tamâmı boş metîn değerinden oluşan satır ve sütunlar ele alınmalı:
        findEmptyRowsAndCols();
        calculateEmptyRates();// Satır ve sütunların doluluk oranları hesâplandı.
        //giveAdviceAboutEmptyRowsAndCols();// Tavsiye haritasına kullanıcının çok boş olan satırları ve sütunları silmesiyle alâkalı tavsiyeler koy
        if(getEmptyRowIndexes().isEmpty())// Boş satır yoksa fonksiyonu bitir; bu durumda bitirmezsen aşağıdaki kod hatâ verir
            return;
        int[] fullEmptyRows = new int[getEmptyRowIndexes().size()];
        String strForMsg = "";
        for(int sayac = 0; sayac < fullEmptyRows.length; sayac++){
            fullEmptyRows[sayac] = getEmptyRowIndexes().get(sayac);
            strForMsg += fullEmptyRows[sayac];
            if(sayac < fullEmptyRows.length - 1)
                strForMsg += ", ";
        }
        deleteRows(fullEmptyRows);
        getEmptyRowIndexes().clear();
        addMessage("Tamâmı boş olan şu indisli satırlar silindi : " + strForMsg);
    }
    public void detectCategoricalData(){// Bu fonksiyonun çalıştırılabilmesi için : Boş veri oranları, kategori veri tipleri tespit edilmiş olmalı
        if(!isEmptyRatesIsUpdate)
            calculateEmptyRates();
        getUniqueAllColValues();
        if(dTypes == null)
            detectDataTypes(true);
        // ÇİFT KONTROL YAP:
        // Sütun hücrelerindeki toplam farklı değer sayısı,
        // Toplam farklı değer sayısının toplam dolu hücre sayısına oranı
        double upperBoundOfDetectCategorical = 0.25;// Veri sayısı ile birlikte yapılan 'doğrudan kategorik veri olabilir' tahmîni için kullanılmak üzere münferid değer oranının eşik değeri
        Object[][] uniques = getUniqueAllColValues();
        couldCategorical = MatrixFunctions.produceBooleanArray(colCount, false);// Tüm sütunların kategorik olmadığını varsayalım
        for(int sayac = 0; sayac < colCount; sayac++){
            boolean checkDataType = false;
            if(dTypes[sayac] == Boolean.class){
                couldCategorical[sayac] = true;
                continue;
            }
            int empty = getEmptyRowCounterForCols().get(sayac);// Boş hücre sayısı
            int nonEmpty = rowCount - empty;// Dolu hücre sayısı
            int uniqValNum = uniques[sayac].length;// Münferid değer sayısı
            double rateOfUniqueValues = ((double) uniqValNum / (double) nonEmpty);// Münferid değer sayısının toplam değer sayısına oranı
//            System.out.println("rateOfUniqueVal[" + sayac + "] = " + rateOfUniqueValues);
            if((uniqValNum == 1 || uniqValNum == 2)){
                couldCategorical[sayac] = true;// Etiket verisi olabilir
            }
            if(rateOfUniqueValues > 0.51)// Münferid değer sayısı toplam dolu hücre sayısının %51'inden fazla ise bu sütunun kategorik olmadığını varsayalım
                continue;
            else if(rateOfUniqueValues <= upperBoundOfDetectCategorical){
                couldCategorical[sayac] = true;// Etiket verisi olabilir
            }
            else if(nonEmpty < 25 && rateOfUniqueValues <= 0.35){
                checkDataType = true;// Veri tipi 'double' ise etiket verisi değildir, tahmîninde bulunmak için veri tipini kontrol etmemiz gerektiğini 'checkDataType' biti ile belirtelim
            }
            else if(nonEmpty > 25 && rateOfUniqueValues < 0.2){
                checkDataType = true;// Veri tipi 'double' ise etiket verisi değildir, tahmîninde bulunmak için veri tipini kontrol etmemiz gerektiğini 'checkDataType' biti ile belirtelim
            }
            if(checkDataType){
                // Etiket verisi olabilir
                // Veri tipi noktalı sayı (Double) ise etiket verisi olmadığını varsayalım..
                // Değer aralığına tam sayı cihetinden bakma gibi ek analiz eklenebilir
                if(dTypes[sayac] == Double.class)
                    continue;
                else
                    couldCategorical[sayac] = true;// Etiket verisi olabilir
            }
        }
        isCalculatedCouldCategorical = true;// Sütunların kategorik olup, olmadığı tahmini hesâplandı
    }
    public boolean setColumnAsCategoricalWithOneHotVectorEncode(int columnIndex, boolean codeAsBoolean){
        if(dTypes == null)
            detectDataTypes(true);
        if(!isEmptyRatesIsUpdate)
            calculateEmptyRates();
        getUniqueAllColValues();
        Object[] original = getColumnValues(columnIndex).clone();
        Object[][] converted = convCategoriesAsOneHotVector(columnIndex, codeAsBoolean);
        if(converted == null)
            return false;
        Method m = getLocalMethod("convCategoriesAsOneHotVector", int.class, boolean.class);
        Object[][] dNew = MatrixFunctions.changeColumnData(data, converted[0], columnIndex);
        boolean nameAdded = false;
        boolean dataTypeAdded = false;
        if(getColumnNames() != null){
            columnNames[columnIndex] = String.valueOf(uniqueColValues[columnIndex][0]);
            nameAdded = true;
        }
        if(getDataTypes() != null){
            dTypes[columnIndex] = dNew[0][columnIndex].getClass();
            dataTypeAdded = true;
            getIsStatisticIsUpdate()[columnIndex] = false;
            areUniqueValuesCalculated[columnIndex] = false;
        }
        for(int sayac = 1; sayac < converted.length; sayac++){
            dNew = MatrixFunctions.addNewColumn(dNew, converted[sayac], columnIndex + sayac, false);
            String[] colN;
            if(nameAdded)
                columnNames = (String[]) MatrixFunctions.addNewMember(getColumnNames(), String.valueOf(uniqueColValues[columnIndex][sayac]), String[].class, columnIndex + sayac, false);
            if(dataTypeAdded)
                dTypes = (Class[]) MatrixFunctions.addNewMember(dTypes, converted[sayac][0].getClass(), Class[].class, columnIndex + sayac, false);
        }
        setData(dNew);
        {// İstatistik ve münferid veri hesâplamasının taze olup, olmadığını bildiren diziyi tazeleme:
            boolean[] areUniqueCalcsNew = MatrixFunctions.produceBooleanArray(colCount, false);
            boolean[] isStatsUpdateNew = MatrixFunctions.produceBooleanArray(colCount, false);
            for(int sayac = 0; sayac < columnIndex; sayac++){// ÖNCEKİ DEĞERLERİ KORUYARAK, GEREKSİZ İŞLEM YAPMAKTAN KAÇIN
                areUniqueCalcsNew[sayac] = areUniqueValuesCalculated[sayac];
                if(isStatisticIsUpdate != null)
                    isStatsUpdateNew[sayac] = isStatisticIsUpdate[sayac];
            }
            for(int sayac = columnIndex; sayac < columnIndex + converted.length; sayac++){
                areUniqueCalcsNew[sayac] = false;
                isStatsUpdateNew[sayac] = false;
            }
            for(int sayac = columnIndex + converted.length; sayac < colCount; sayac++){
                areUniqueCalcsNew[sayac] = areUniqueValuesCalculated[sayac - converted.length];
                if(isStatisticIsUpdate != null)
                    isStatsUpdateNew[sayac] = isStatisticIsUpdate[sayac - converted.length];
            }
            isStatisticIsUpdate = isStatsUpdateNew;
            areUniqueValuesCalculated = areUniqueCalcsNew;
            Object[][] uniqsNew = new Object[converted.length - 1][];
            if(converted.length > 1){
                for(int sayac = 1; sayac < uniqsNew.length; sayac++){
                    uniqsNew[sayac] = calculateUniqueValues(converted[sayac]);
                }
            }
            uniqueColValues[columnIndex] = calculateUniqueValues(converted[0]);
            uniqueColValues = MatrixFunctions.addNewRows(uniqueColValues, columnIndex, uniqsNew);
            if(statistics != null){
                statistics = MatrixFunctions.addNewMember(statistics, null, Statistic[].class, columnIndex, true);
                if(converted.length > 1){
                    for(int sayac = 0; sayac < converted.length -1; sayac++){
                        statistics = MatrixFunctions.addNewMember(statistics, null, Statistic[].class, columnIndex + sayac, false);
                    }
                }
            }
        }
        Class codingType = Boolean.class;
        if(!codeAsBoolean)
            codingType = Integer.class;
        // Bağlantılar kaydırılmalı:
        if(converted.length > 1)// Yeni sütun ekleniyorsa sağdaki sütunların bağlantıları kaydırılmalı:
            shiftConnectionWithVariableToRight(columnIndex + 1, converted.length - 1);
        addCategoricalData(columnIndex, original, converted, codingType, CategoricalVariable.CODING_TYPE.ONEHOTVECTOR);
        if(m != null){
//            System.out.println("Yöntem alındı");
            // İşlemi kaydet
        }
        isColumnDetailsIsUpdate = false;
        isEmptyRatesIsUpdate = false;
        return true;
    }
    public boolean changeColumnDataType(int colIndex, Class targetType, HashMap<String, Object> conversionPolicy){
        return changeColumnDataType(colIndex, targetType, true, conversionPolicy);
    }
    public boolean changeColumnDataType(int colIndex, Class targetType, boolean dontConvertIfSomeOnesAreNotMatched, HashMap<String, Object> conversionPolicy){
        boolean isSuccess = convColumnDataType(colIndex, targetType, dontConvertIfSomeOnesAreNotMatched, conversionPolicy);
        if(isSuccess){// Eğer dönüşüm başarılı olduysa
            isStatisticIsUpdate[colIndex] = false;// İstatistikler taze değil olarak işâretle
            isColumnDetailsIsUpdate = false;// Sütun bilgileri taze değil olarak işâretle
            if(areUniqueValuesCalculated[colIndex]){// Eğer daha evvel münferid değerler hesaplandıysa bu değerlerin veri tipini de değiştir
                for(int sayac = 0; sayac < getUniqueAllColValues()[colIndex].length; sayac++){
                    Object[] res = convDataType(uniqueColValues[colIndex][sayac], targetType);
                    if((boolean) res[0])
                        uniqueColValues[colIndex][sayac] = res[1];
                    else{
                        if(conversionPolicy != null){
                            if(targetType == Integer.class){
                                Object rollToUp = conversionPolicy.get("rollToUp");
                                Object rollToDown = conversionPolicy.get("rollToDown");
                                if(rollToUp != null && rollToDown != null){
                                    Object[] secondRes = convDataType(uniqueColValues[colIndex][sayac], Double.class);
                                    if((boolean) secondRes[0]){
                                        uniqueColValues[colIndex][sayac] = MathFuncs.rollDoubleToInteger((double) secondRes[1], (boolean) rollToUp, (boolean)  rollToDown);
                                        continue;
                                    }
                                }
                            }
                            else if(targetType == Double.class){
                                // Double veri tipini aşan String sayıların dönüşümünü burada yapmaya çalış
                                //Başarılı olursa şu satırı ekle : continue;
                            }
                        }
                        areUniqueValuesCalculated[colIndex] = false;
                        break;
                    }
                }
            }
            //.;. : Gereken başka işlem var mı?
        }
        return isSuccess;
//        System.err.println("Dönüşüm başarı değeri : " + isSuccess);
    }
    public boolean fillEmptyCells(int colIndex, Object value){// Sütundaki boş verileri verilen değerle doldur
        if(value == null)// Gelen değer 'null' ise işleme devâm etme
            return false;
        if(dTypes == null)// Eğer veri tipi belli değilse; veri tipini tespit et
            detectDataTypes(true);
        Object valueFinal;
        if(value == null)
            valueFinal = null;
        else{
            Object[] valueConverted = convDataType(value, dTypes[colIndex]);
            if(!(boolean) valueConverted[0]){
                addMessage("Verilen değerin veri tipi sütunun veri tipiyle uyuşmuyor; kontrol ediniz; dilerseniz sütun veri tipini dönüştürmeyi deneyebilirsiniz!");
                return false;
            }
            valueFinal = valueConverted[1];
        }
            /*if(value.getClass() == Integer.class && dTypes[colIndex] == Double.class)
                valueFinal = (double) value;
            else
                valueFinal = value;*/
        int emptyCounter = 0;// Boş hücre sayısını saymak için
        for(int sayac = 0; sayac < rowCount; sayac++){
            if(data[sayac][colIndex] == null){
                data[sayac][colIndex] = valueFinal;
                emptyCounter++;
            }
        }
        getIsStatisticIsUpdate()[colIndex] = false;
        isEmptyRatesIsUpdate = false;
        isEmptyCountNumbersUpdate = false;
        isColumnDetailsIsUpdate = false;
        addMessage(emptyCounter + " adet boş hücre " + String.valueOf(valueFinal) + " değeriyle dolduruldu.");
        return true;
    }
    public boolean fillEmptyCellsByMin(int colIndex){// Sütundaki boş verileri sütunun en küçük değeriyle doldur
        return fillEmptyCellsByStatisticValue("min", colIndex);
    }
    public boolean fillEmptyCellsByMax(int colIndex){// Sütundaki boş verileri sütunun en büyük değeriyle doldur
        return fillEmptyCellsByStatisticValue("max", colIndex);
    }
    private boolean fillEmptyCellsByStatisticValue(String fillBy, int colIndex){
        if(dTypes == null)
            detectDataTypes(true);
        if(!getIsStatisticIsUpdate()[colIndex])
            calculateStatistic(colIndex);
        Object value = null;
        boolean notFound = false;
        switch(fillBy){
            case "min" :{
                value = statistics[colIndex].min;
                break;
            }
            case "max" :{
                value = statistics[colIndex].max;
                break;
            }
            case "mean" :{
                value = statistics[colIndex].mean;
                break;
            }
            case "modValue" :{
                value = statistics[colIndex].modValue;
                break;
            }
            default : notFound = true;
        }
        if(notFound)
            return false;
        Object valueFinal;
        if(dTypes[colIndex] == Integer.class)
            valueFinal = (int) ((double) value);
        else
            valueFinal = value;
        return fillEmptyCells(colIndex, valueFinal);
    }
    public boolean fillEmptyCellsByMean(int colIndex){// Sütundaki boş verileri sütun ortalamasıyla doldur
        return fillEmptyCellsByStatisticValue("mean", colIndex);
    }
    public boolean fillEmptyCellsByZero(int colIndex){// Sütundaki boş verileri sıfır(0) ile doldur
        return fillEmptyCells(colIndex, 0);
    }
    public boolean fillEmptyCellsByEmptyString(int colIndex){// Sütundaki boş verileri boş String("") doldur
        return fillEmptyCells(colIndex, "");
    }
    public boolean fillEmptyCellsByModValue(int colIndex){// Sütundaki boş verileri sütunda en çok tekrar eden değerle doldur
        return fillEmptyCellsByStatisticValue("modValue", colIndex);
    }
    public boolean fillEmptyCellsByNullExpression(int colIndex){// Sütundaki boş verileri "null" ifâdesiyle doldur (String olarak)
        return fillEmptyCells(colIndex, "null");
    }
    public boolean setColumnAsCategoricalWithOrdinalEncode(int columnIndex, Object[] orderedAscending){
//        if(!isEmptyRatesIsUpdate)
//            calculateEmptyRates();
//        getUniqueAllColValues();
        if(dTypes == null)
            detectDataTypes(true);
        Object[] original = getColumnValues(columnIndex).clone();
        Method m = getLocalMethod("convCategoriesAsOrdinal", int.class, Object[].class);
        Object[] ready = convCategoriesAsOrdinal(columnIndex, orderedAscending);
        if(ready == null){
            System.err.println("Dönüşüm başarısız!");
            return false;
        }
        setData(MatrixFunctions.changeColumnData(data, ready, columnIndex));
        dTypes = MatrixFunctions.addNewMember(dTypes, ready[0].getClass(), Class[].class, columnIndex, true);
        getIsStatisticIsUpdate()[columnIndex] = false;
        isColumnDetailsIsUpdate = false;
        areUniqueValuesCalculated[columnIndex] = false;
        // .;.
        if(m != null){
            
        }
        addCategoricalData(columnIndex, original, ready, Integer.class, CategoricalVariable.CODING_TYPE.ORDINAL);// Sütun verisini kategorik veri olarak kaydet
        return true;
    }
    public boolean setColumnAsCategoricalWithBinomialEncode(int columnIndex, boolean codeTrueAs1){
//        if(!isCalculatedCouldCategorical)// Sütunun kategorik olup, olaamayacağı ile ilgili analize gerek yok; analizden menfî sonuç çıksa bile dönüşüm yapılabilir
//            detectCategoricalData();
//        if(!couldCategorical[columnIndex]){
//            // Kategorik yapmak istediğinize emîn misiniz?
//            return false;
//        }
        if(dTypes == null)
            detectDataTypes(true);
        // AŞAĞIDAKİ BEŞ SATIR İŞLEM 'true' değerinin 1 olarak kodlanıp, kodlanmayacağı bilgisinin ilgili yöntemdeki "ilk değeri 'true' olarak kodla" parametresine çevirim işlemidir
        boolean codeFirstValueAsTrue = codeTrueAs1;
        if(String.valueOf(uniqueColValues[columnIndex][0]).equals("true"))
            codeFirstValueAsTrue = codeTrueAs1;
        else
            codeFirstValueAsTrue = !codeTrueAs1;
        Object[] original = getColumnValues(columnIndex).clone();
        Object[] ctData = convCategoriesAsBinomial(columnIndex, codeFirstValueAsTrue, false);
        if(ctData == null)
            return false;
        Method m = null;
        try{
            m = this.getClass().getMethod("setColumnAsCategoricalWithBinomialEncode", int.class, boolean.class);
        }
        catch(SecurityException | NoSuchMethodException exc){
            System.err.println("hatânın yerel mesajı : " + exc.getLocalizedMessage());
        }
        if(m != null){
            ArrayList<Object> params = new ArrayList<Object>();
            params.add(columnIndex);
            params.add(codeTrueAs1);
            Process prc = new Process(m, params, Process.PROCESS_TYPE.CONVERT, false, true, columnIndex, columnIndex);
            saveCategoricConversion(data, prc, columnIndex);
        }
        setData(MatrixFunctions.changeColumnData(data, ctData, columnIndex));
        setColumnDataTypes(MatrixFunctions.addNewMember(dTypes, ctData[0].getClass(), Class[].class, columnIndex, true));
        getIsStatisticIsUpdate()[columnIndex] = false;
        areUniqueValuesCalculated[columnIndex] = false;
        isColumnDetailsIsUpdate = false;
        addCategoricalData(columnIndex, original, ctData, Integer.class, CategoricalVariable.CODING_TYPE.BINOMIAL);// Sütun verisini kategorik veri olarak kaydet
        return true;
    }
    private boolean convColumnDataType(int colIndex, Class targetType, boolean dontConvertIfSomeOnesAreNotMatched, HashMap<String, Object> conversionPolicy){// İYİLEŞTİRİLEBİLİR FONKSİYON İDİ, ŞİMDİ KONTROL ET : HER SATIR İÇİN DÖNÜŞÜM UYGULANABİLİR Mİ VE HEDEF VERİ TİPİ NEDİR, KONTROLÜ YAPILIYOR, YAPILMAMALI
        // conversionPolicy : <anahtar, değer>; <"targetType", targetType(Class tipinde)>;
        // Olası anahtar - değerler : <"rollDigitNumber", int>, <"rollToUp", boolean>,
        // <rollToDown, boolean> Eğer bu son iki değer de true ise Tamsayıya normal yuvarlanır
        if(dTypes == null)
            detectDataTypes(true);
        Object[] colVals = getColumnValues(colIndex);
        ArrayList<Integer> notConverted = new ArrayList<Integer>();
        boolean isAllConverted = true;
        String strError = "Şu indis için dönüşüm başarısız : ";
        for(int sayac = 0; sayac < rowCount; sayac++){
//            System.out.println(sayac + ". satır veri tipi : " + colVals[sayac].getClass().getName() + "\tVerisi : " + colVals[sayac]);
            if(colVals[sayac] == null)
                continue;
            try{
                Object[] results = convDataType(colVals[sayac], targetType);
                if(!((Boolean) results[0]))
                    throw new Exception(String.valueOf(sayac));
                else{
//                    System.out.println("Dönen değer : " + results[1] + "(" + results[1].getClass().getName() + ")");
                    colVals[sayac] = results[1];
//                    System.out.println("data[" + sayac + "][" + colIndex + "].tipi : " + data[sayac][colIndex].getClass().getName() + "\tresults veri tipi : " + results[1].getClass().getName());
                }
            }
            catch(Exception exc){
                if(dontConvertIfSomeOnesAreNotMatched && conversionPolicy == null)
                    return false;
                boolean keepGo = true;// İşleme devâm etmeyi işâretlemek için bayrak
                boolean isSuccess = true;// conversionPolicy = null olduğu durumda dönüşüm başarısız olursa tespit etmek için
                if(conversionPolicy != null){
                    if(targetType == Integer.class){
                        Object rollUp = conversionPolicy.get("rollToUp");
                        Object rollDown = conversionPolicy.get("rollToDown");
                        if(rollDown != null && rollUp != null){
                            Object[] results = convDataType(colVals[sayac], Double.class);
                            if(!(boolean) results[0])
                                isSuccess = false;
                            else{// Double'a dönüşümün başarılı olduğu durum
                                colVals[sayac] = MathFuncs.rollDoubleToInteger((double) results[1], (boolean) rollUp, (boolean) rollDown);
                                continue;
                            }
                        }
                        else
                            isSuccess = false;
                    }
                    else if(targetType == Double.class && colVals[sayac].getClass() == String.class){
                        // Double veri tipini aşan durumlarda double'a dönüşümü burada yapmalısın:
                        
                        isSuccess = false;// Buranın düzeltilmesi gerekiyor
                    }
                    if(!isSuccess && dontConvertIfSomeOnesAreNotMatched)
                        return false;
                    else{
                        notConverted.add(sayac);
                        isAllConverted = false;
                    }
                }
                else{
                    notConverted.add(sayac);
                    isAllConverted = false;
                }
                System.err.println(strError + exc.getMessage());
            }
        }
        if(notConverted.size() < colVals.length / 3){
            if(conversionPolicy != null){
                if(targetType == Double.class){
                    Object rollDigitNumber = conversionPolicy.get("rollDigitNumber");
                    if(rollDigitNumber != null){
                        for(int sayac = 0; sayac < colVals.length; sayac++){
                            boolean dontRoll = false;
                            if(dontConvertIfSomeOnesAreNotMatched){
                                for(int s2 = 0; s2 < notConverted.size(); s2++){
                                    if(s2 == notConverted.get(s2)){
//                                        System.err.println("Dönüştürülemeyen hücre : " + s2 + " //anl.846");
                                        dontRoll = true;
                                        break;
                                    }
                                }
                            }
                            if(!dontRoll){
                            if(colVals[sayac] != null)
                                colVals[sayac] = MathFuncs.roundNumber((double) colVals[sayac], (int) rollDigitNumber);
                            }
                        }
                    }
                }
            }
            setData(MatrixFunctions.changeColumnData(data, colVals, colIndex));
            this.dTypes[colIndex] = targetType;
            return true;
        }
        return false;
    }
    public Object[] convDataType(Object data, Class targetType){// Dönüş tipi : Object[sonuç(başarılı ise 'true', başarısız ise 'false'), dönüştürülen değer (başarılı ise)]
        if(data == null)
            return new Object[]{false};
        Class curr = data.getClass();// Verinin şimdiki tipi
        if(curr == targetType)// Verilerin tipleri aynıysa işlemi durdur
            return new Object[]{true, data};
        targetType = wrapPrimitiveClass(new Class[]{targetType})[0];// Hedef veri tipini kapsülle
        boolean isNumber = false;
        boolean isTargetTypeIsNumber = false;
        String sVal = String.valueOf(data);
        try{
            if(targetType == String.class){// Hedef veri tipi String ise 'String.valueOf()' yöntemi kâfî
                if(sVal == null)
                    throw new Exception();
                return new Object[]{true, sVal};
            }
            else if(targetType == Boolean.class){// Hedef == Boolean ise
                if(sVal.equalsIgnoreCase("true") || sVal.equalsIgnoreCase("t"))
                    data = true;
                else if(sVal.equalsIgnoreCase("false") || sVal.equalsIgnoreCase("f"))
                    data = false;
                else
                    return new Object[]{false};
                return new Object[]{true, data};
            }
            else if(curr == Boolean.class)
                return new Object[]{false};// Mevcut veri tipi Boolean ise yalnızca String'e çevrilebilr; o da yukarıda yapıldığından 'false' döndürebiliriz
            if(targetType == Number.class)
                isTargetTypeIsNumber = true;
            else if(targetType.getSuperclass() == Number.class)
                isTargetTypeIsNumber = true;
            if(isTargetTypeIsNumber){// Hedef = sayı ise
                sVal = sVal.replace(",", ".");
                Number num = null;
                if(targetType == Integer.class){
                    // EK İŞLEM GEREKİYOR  : DOUBLE->INT sıkıntılı ; Double bir String->Int sıkıntılı:
                    String[] splitted = sVal.split("\\.");
                    for(int sayac = 1; sayac < splitted.length; sayac++){// Noktadan sonraki her veriyi gözden geçir
                        for(int s2 = 0; s2 < splitted[sayac].length(); s2++){// Bunların her bir harfini gözden geçir
                            boolean isGoodToConv = false;
                            switch(splitted[sayac].charAt(s2)){// Eğer harf 0 veyâ '.' dışında karakter içeriyorsa 'dönüştürülemez' olarak işâretle ve dur
                                case '0' :{
                                    isGoodToConv = true;
                                    break;
                                }
                                case ' ' :{
                                    isGoodToConv = true;
                                    break;
                                }
                            }
                            if(!isGoodToConv)
                                return new Object[]{false};
                        }
                    }
                    num = Integer.valueOf(splitted[0]);
//                    System.out.println("spli,tted[0] : " + splitted[0]);
                }
                else if(targetType == Double.class)
                    num = Double.valueOf(sVal);
                else if(targetType == Short.class)
                    num = Short.valueOf(sVal);
                else if(targetType == Long.class)
                    num = Long.valueOf(sVal);
                if(num == null)// Hatâ fırlatıldığı için buna gerek yok sanırım
                    throw new Exception();
                return new Object[]{true, num};
            }
        }
        catch(Exception exc){
            System.err.println("Dönüşüm başarısız!\nVeri : " + data + "\tHedef veri tipi : " + targetType.getName() + "\nexc.toString : " + exc.toString());
            return new Object[]{false};
        }
        return new Object[]{false};
    }
    private void saveCategoricConversion(Object[][] before, Process process, int colIndex){
        HashMap<String, Object> infos = process.getOtherInfo();
        if(infos == null)
            infos = new HashMap<String, Object>();
        // KOYULACAKLARI KOY = infos.put("", "");
        addCheckpoint(before, process);
    }
    private void addCheckpoint(Object[][] before, Process process){// before : İşlemden önce, verinin son hâli
        // Kayıt da tut
        // Eğer ihtiyaç olursa, bir şeyler koyulabilir : process.getOtherInfo();
        getSavedProcs().add(process);
    }
    private void calculateUniqueValuesOnColumns(){
        uniqueColValues = new Object[colCount][];
        for(int sayac = 0; sayac < colCount; sayac++){
            uniqueColValues[sayac] = calculateUniqueValues(getColumnValues(sayac));
        }
//            System.err.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//            MatrixFunctions.printMatrixWithTitle(uniqueColValues, "MünferidDeğerler");
        areUniqueValuesCalculated = MatrixFunctions.produceBooleanArray(colCount, true);
    }
    public Object[] calculateUniqueValues(Object[] colData){
        ArrayList<Object> uniqVals = new ArrayList<Object>();
        for(int sayac = 0; sayac < rowCount; sayac++){
            boolean isNew = true;
            for(Object objVal : uniqVals){
                if(objVal.equals(colData[sayac])){// Bu kontrol nesnelerin aynı değerde olup, olmadıklarını kontrol ediyor, test edildi
                    isNew = false;
                    break;
                }
            }
            if(isNew){
                if(colData[sayac] != null)
                    uniqVals.add(colData[sayac]);
            }
        }
        Object[] val = new Object[uniqVals.size()];
        uniqVals.toArray(val);
        return val;
    }
    public Object[] getColumnValues(int colNumber){
        Object[] colData = new Object[rowCount];
        for(int sayac = 0; sayac < rowCount; sayac++){
            colData[sayac] = data[sayac][colNumber];
        }
        return colData;
    }
    private void addMessage(String message){
        // Mesajın anında gösterilmesi için ilgili yöntemi tetikle; bunun için yöntem tanımla
        getMessages().add(message);
    }
    private boolean areAllValuesEmpty(Object[][] data, boolean acceptEmptyStringAsNull){//veri 'null' mı, kaba kontrolünü yapan yöntem
        if(data == null)
            return true;
        for(int sayac = 0; sayac < data.length; sayac++){
            if(data[sayac] == null)
                continue;
            for(int s2 = 0; s2 < data[sayac].length; s2++){
                Object cell = data[sayac][s2];
                if(cell != null){
                    if(acceptEmptyStringAsNull){
                        if(cell.getClass().getName().equals(String.class.getName())){
                            if(!((String) cell).isEmpty())
                                return false;
                        }
                    }
                    else
                        return false;
                }
            }
        }
        return true;
    }
    private boolean areAllValuesTrue(boolean[] values){
        for(boolean b : values)
            if(!b)
                return false;
        return true;
    }
    private Method getLocalMethod(String name, Class... params){
        Method m = null;
        try{
            m = this.getClass().getDeclaredMethod(name, params);
            return m;
        }
        catch(SecurityException | NoSuchMethodException exc){
            System.err.println("Yöntem alınamadı : " + exc.getClass().getName() + "\n" + exc.getLocalizedMessage());
            return null;
        }
    }
    private Object[][] takeSubData(Object[][] data, int startRowIndex, int startColIndex){
        Object[][] value = new Object[data.length - startRowIndex][data[0].length - startColIndex];
        int rowCounter = 0, colCounter = 0;
        for(int sayac = startRowIndex; sayac < data.length; sayac++){
            for(int s2 = startColIndex; s2 < data[sayac].length; s2++){
                value[rowCounter][colCounter] = data[sayac][s2];
                colCounter++;
            }
            rowCounter++;
        }
        return value;
    }
    private void deleteRows(int[] rowNumbers){
        int[] howManyCellAreEmptyOnAffectedCol = new int[colCount];
//        isEmptyRatesIsUpdate = false;// Boş veri oranı değerleri taze değil, olarak işâretlendi
        for(int sayac = 0; sayac < rowNumbers.length; sayac++){
            if(getEmptyRowIndexes().size() > rowNumbers[sayac]){
                if(getEmptyRowIndexes().get(rowNumbers[sayac]) != null)
                    getEmptyRowIndexes().remove(rowNumbers[sayac]);// emptyRateIndexes'ten eleman çıkarılıyor
            }
            getEmptyColCounterForRows().remove(rowNumbers[sayac]);// emptyColCounterForRow'dan eleman çıkarılıyor
            for(int s2 = 0; s2 < colCount; s2++){
                if(data[rowNumbers[sayac]][s2] == null)
                    howManyCellAreEmptyOnAffectedCol[s2] += 1;
                else if(String.valueOf(data[rowNumbers[sayac]][s2]).isEmpty())
                    howManyCellAreEmptyOnAffectedCol[s2] += 1;
            }
        }
        setData(MatrixFunctions.deleteSelectedRows(data, rowNumbers));// Veri ve satır sayısı tazelendi, sütun bilgileri tâze değil, olarak işâretlendi
        isEmptyCountNumbersUpdate = false;// Satır ve sütundaki boş hücre sayısı taze değil, olarak işâretlendi
        areUniqueValuesCalculated = MatrixFunctions.produceBooleanArray(colCount, false);
        // Performans için şu işlem toplu yapılıyor : emptyRateOfRows'tan ilgili elemanların çıkartılması
        if(getEmptyRateOfRows() != null)
            emptyRateOfRows = MatrixFunctions.deleteSelectedMembers(getEmptyRateOfRows(), rowNumbers);//emptyRateOfRows'tan elemanlar çıkarılıyor
        if(emptyRateOfCols != null){
            for(int sayac = 0; sayac < colCount; sayac++){
                int emptyCellNumber = (int) ((emptyRateOfCols[sayac] * (rowCount + rowNumbers.length)) / 100);// Silme işleminden evvel kaç satırın boş olduğunu öğrenelim
                emptyCellNumber -= howManyCellAreEmptyOnAffectedCol[sayac];// İlgili sütunda şu an kaç boş hücre olduğunu öğrenelim
                getEmptyRateOfCols()[sayac] =  (((double) emptyCellNumber) / rowCount) * 100;
            }
        }
    }
    public void deleteCols(int[] colIndexes){
        int[] howManyCellAreEmptyOnAffectedRow = new int[rowCount];
//        System.err.println("deleteCols çalıştı");
        for(int sayac = 0; sayac < colIndexes.length; sayac++){
            if(getEmptyColIndexes().size() > colIndexes[sayac]){
                if(getEmptyColIndexes().get(colIndexes[sayac]) != null)
                    getEmptyColIndexes().remove(colIndexes[sayac]);// emptyRateIndexes'ten eleman çıkarılıyor
            }
            getEmptyRowCounterForCols().remove(colIndexes[sayac]);// emptyRowCounterForCol'dan eleman çıkarılıyor
            for(int s2 = 0; s2 < rowCount; s2++){
                if(data[s2][colIndexes[sayac]] == null)
                    howManyCellAreEmptyOnAffectedRow[sayac] += 1;
                else if(String.valueOf(data[s2][colIndexes[sayac]]).isEmpty())
                    howManyCellAreEmptyOnAffectedRow[sayac] += 1;
            }
        }
        if(colDetails != null){// Bu işlemin 'setData(..)' satırından önce işlenmesi gerekiyor
            for(int sayac = 0; sayac < colIndexes.length; sayac++){// Sütun bilgilerini sil
                HashMap<String, Object> next = colDetails.get(colIndexes[sayac] + 1);
                if(next != null){
                    colDetails.put(colIndexes[sayac], next);
                    colDetails.remove(colIndexes[sayac] + 1);
                }
            }
        }
        // Eğer kategorik değişkenin bir sütunu silindiyse tespit et; ve işlemler...
        for(int sayac = 0; sayac < colIndexes.length; sayac++){
            CategoricalVariable var = getCategoricVariableOfCol(colIndexes[sayac]);
            if(var == null)// Silinmesi istenen sütun kategorik değişken barındırmıyorsa, bu işlemleri yapma
                continue;
            if(var.getCodingType() == CategoricalVariable.CODING_TYPE.ONEHOTVECTOR){// Kategorik değişken 'tek nokta vektörü' olarak kodlanmışsa kodlamayı tazele
                reCodeOneHotCodedColsForBeforeColDeleting(colIndexes[sayac], var);
            }
            else// Diğer durumlarda kategorik değişkeni sil
                getMapCategoricalVars().remove(colIndexes[sayac]);
        }
        
        // ÖNEMLİ İŞLEM:
        setData(MatrixFunctions.deleteSelectedCols(data, colIndexes));// Veri ve satır sayısı tazelendi, sütun bilgileri tâze değil, olarak işâretlendi
        
        if(getEmptyRateOfCols()!= null)
            emptyRateOfCols = MatrixFunctions.deleteSelectedMembers(getEmptyRateOfCols(), colIndexes);//getEmptyRateOfCols'tan elemanlar çıkarılıyor

        if(columnNames != null)
            columnNames = MatrixFunctions.deleteSelectedMembers(columnNames, colIndexes, String[].class);// Sütun isimlerinden ilgili değerler çıkarıldı
        if(dTypes != null)
            dTypes = MatrixFunctions.deleteSelectedMembers(dTypes, colIndexes, Class[].class);// Sütun veri tiplerinden ilgili değerler çıkarıldı
        if(uniqueColValues != null)
            uniqueColValues = MatrixFunctions.deleteSelectedRows(uniqueColValues, colIndexes);// Sütun münferid değerlerinin bulunduğu diziden ilgili değerler siliniyor
        if(couldCategorical != null)
            couldCategorical = MatrixFunctions.deleteSelectedMembers(couldCategorical, colIndexes);// Kategorik olup - olmama tahmîni verilerinden ilgili yerleri çıkar
        
        isEmptyCountNumbersUpdate = false;// Satır ve sütundaki boş hücre sayısı taze değil, olarak işâretlendi
//        isEmptyRatesIsUpdate = false;// Boş veri oranı değerleri taze değil, olarak işâretlendi
        if(emptyRateOfRows != null){
            for(int sayac = 0; sayac < rowCount; sayac++){
                int emptyCellNumber = (int) ((emptyRateOfRows[sayac] * (colCount + colIndexes.length)) / 100);// Silme işleminden evvel kaç satırın boş olduğunu öğrenelim
                emptyCellNumber -= howManyCellAreEmptyOnAffectedRow[sayac];// İlgili sütunda şu an kaç boş hücre olduğunu öğrenelim
                getEmptyRateOfRows()[sayac] =  (((double) emptyCellNumber) / colCount) * 100;
            }
        }
        if(statistics != null){// İYİLEŞTİRİLEBİLİR
            isStatisticIsUpdate = MatrixFunctions.produceBooleanArray(colCount, false);
        }
        for(int sayac = 0; sayac < colIndexes.length; sayac++){
            isColumnIsCategorical = MatrixFunctions.deleteSelectedMembers(getIsColumnIsCategorical(), colIndexes);
        }
        
        // 'Sütun numarası -> kategorik değişken' haritasındaki sütun numaralarını tazele (eğer gerekiyorsa):
        HashMap<Integer, CategoricalVariable> mapNewVar = (HashMap<Integer, CategoricalVariable>) mapCategoricalVars.clone();
        for(int colNo : getMapCategoricalVars().keySet()){
            int shiftLeft = 0;
            for(int sayac = 0; sayac < colIndexes.length; sayac++){// Kategorik verilerin sütun numarasıyla eşleştirildiği haritayı tazele
                if(colIndexes[sayac] < colNo)// Kendisinden önceki bir sütun silindiyse haritadaki sütun numarası da bir azaltılmalı
                    shiftLeft++;
            }
            if(shiftLeft > 0){// Kaydırmak gerekiyorsa kaydır (sütun numarasını azaltarak haritaya yeniden koy; eski sütun numarasındaki eşleşmeyi kaldır)
                CategoricalVariable var = getMapCategoricalVars().get(colNo);
                mapNewVar.remove(colNo);
                mapNewVar.put(colNo - shiftLeft, var);
                if(var.getCodingType() == CategoricalVariable.CODING_TYPE.ONEHOTVECTOR){
                    for(int sayac = 1; sayac < var.getCategoryNumber(); sayac++){// Kategorinin diğer sütunları varsa o sütunları
                        Integer headIndexOfCategoricalCol = mapColIndexToCategoricalColIndex.get(colNo + sayac);
                        getMapColIndexToCategoricalColIndex().put(colNo + sayac, colNo - shiftLeft);
                    }
                }
            }
        }
        mapCategoricalVars = mapNewVar;
        //getMapColIndexToCategoricalColIndex haritasının anahtarları kaydırılmalı, eğer gerekiyorsa:
        HashMap<Integer, Integer> mapForNewIndexToHeadIndex = new HashMap<Integer, Integer>();
        for(int colNo : getMapColIndexToCategoricalColIndex().keySet()){
            Integer headIndexWhichPointed = getMapColIndexToCategoricalColIndex().get(colNo);
            int shiftLeft = 0;
            for(int sayac = 0; sayac < colIndexes.length; sayac++){
                if(colIndexes[sayac] < colNo)
                    shiftLeft++;
            }
                mapForNewIndexToHeadIndex.put(colNo - shiftLeft, headIndexWhichPointed);
        }
        this.mapColIndexToCategoricalColIndex = mapForNewIndexToHeadIndex;// Yeni indis -> kategorik baş indisi haritasını sistemdeki ile değiştir
    }
    public boolean normalizeOrStandardizeColumn(int colIndex, boolean isNormalize){
        if(colIndex < 0 || colIndex >= colCount)// Hatâli sütun numarası verildiyse
            return false;
        if(getIsColumnIsCategorical()[colIndex])// Kategorik değişken olarak kodlanmış bir sütunun numarası verildiyse
            return false;
        if(!getIsNumber(colIndex))// Sayısal veri barındırmayan bir sütun numarası verildiyse
            return false;
        return normalizeOrStandardize(colIndex, isNormalize);
    }
    private void findEmptyRowsAndCols(){//Tamâmen 'null' olan satırlar tespit edilmiyor; bunlar 'basicAnalyze()' kısmında önceki adımda silindi
        if(isEmptyCountNumbersUpdate)
            return;
        int counterForDetectFullEmptyRow;
        startEmpyColCounterAndEmptyRowCounter();
        for(int sayac = 0; sayac < rowCount; sayac++){
            counterForDetectFullEmptyRow = 0;// Satırdaki tüm sütunların boş olup, olmadığını tespit için kullanılan bir değişken
            for(int s2 = 0; s2 < colCount; s2++){
                boolean isDataNull = false;
                boolean isDataEmpty = false;
                if(data[sayac][s2] == null){
//                System.err.println("Hücredeki veri null : [" + sayac + "][" + s2 + "]");
                    isDataNull = true;
                    isDataEmpty = true;
                }
                if(!isDataNull){
                    if(data[sayac][s2].getClass().getName().equals(String.class.getName())){
                        if(data[sayac][s2].equals("")){
//                            System.err.println("Hücredeki veri boş metîn : [" + sayac + "][" + s2 + "]");
                            isDataEmpty = true;
                        }
                    }
                }
                if(isDataEmpty){
//                    System.out.println("data[" + sayac + "][" + s2 + "] = " + data[sayac][s2]);
                    if(getEmptyColCounterForRows().get(sayac) == null)//Daha önce bu satırda boş hücreye rastlanmadıysa
                        emptyColCounterForRow.put(sayac, 1);
                    else
                        emptyColCounterForRow.put(sayac, emptyColCounterForRow.get(sayac) + 1);//Satırdaki boş hücre sayısını bir arttır
                    counterForDetectFullEmptyRow++;
                    if(getEmptyRowCounterForCols().get(s2) == null)
                        emptyRowCounterForCol.put(s2, 1);//Daha önce bu sütunda boş veri bulunmadıysa bu sütun için boş hücre değerini bir arttır
                    else
                        emptyRowCounterForCol.put(s2, emptyRowCounterForCol.get(s2) + 1);//Sütundaki boş hücre sayısını bir arttır
                }
            }
            if(counterForDetectFullEmptyRow == colCount)
                getEmptyRowIndexes().add(sayac);
        }
        isCountedEmptyRows = true;
        isEmptyCountNumbersUpdate = true;
        isCalculatedEmptyRates = false;
        isEmptyRatesIsUpdate = false;
    }
    private void calculateEmptyRates(){//Bu yöntem 'public' olabilir; üzerinde düşün
        if(!isCountedEmptyRows)
            findEmptyRowsAndCols();
        if(!isEmptyCountNumbersUpdate)
            findEmptyRowsAndCols();
        /*for(int num : getEmptyRowIndexes()){
            System.err.println("Tamâmen boş satır : [" + num + "]");
        }*/
        emptyRateOfRows = new double[rowCount];
        emptyRateOfCols = new double[colCount];
        /*for(int kk : getEmptyRowCounterForCols().keySet()){
            System.out.println("[" + kk + "] sütunu boş hücre sayısı : " + getEmptyRowCounterForCols().get(kk));
        }*/
        
        /*for(int tt : getEmptyColCounterForRows().keySet()){
            System.out.println("[" + tt + "] satırı boş hücre sayısı : " + getEmptyColCounterForRows().get(tt));
        }*/
        
        for(int sayac = 0; sayac < colCount; sayac++){
            double rate = 0.0;
            if(getEmptyRowCounterForCols().get(sayac) == null)//Bu sütunda hiç boş hücre yoksa
                rate = 0.0;
            else{
                int emptyCellNumber = getEmptyRowCounterForCols().get(sayac);
                emptyRateOfCols[sayac] = ((double) emptyCellNumber / rowCount) * 100;
            }
        }
        for(int sayac = 0; sayac < rowCount; sayac++){
            double rate = 0.0;
            if(getEmptyColCounterForRows().get(sayac) == null)//Bu satırda hiç boş hücre yoksa
                rate = 0.0;
            else{
                int emptyCellNumber = getEmptyColCounterForRows().get(sayac);
//                System.err.println("hesap : " + ((double) emptyNumber / colCount) + " (" + emptyNumber + " / " + colCount + "   * 100");
                emptyRateOfRows[sayac] = ((double) emptyCellNumber / colCount) * 100;
            }
        }
        isCalculatedEmptyRates = true;
        isEmptyRatesIsUpdate = true;
    }
    private void giveAdviceAboutEmptyRowsAndCols(){
        double[] rates = getEmptyRateOfCols();
        
        String str = "";
        
        // Bir kısmı boş olan satır ve sütunların ele alınması belli bir ölçüye göre yapılabilir.
        // Kullanıcıdan bu ölçüyü aldıktan sonra bu ölçüye göre işlemi yapan yöntemi tanımlarsak ve..
        // bu yöntemi dışarıdan erişime açarsak bu yöntemin çalıştırılması da advice üzerinden yapılabilir
        //Advice adv = new Advice(str, mthApplyAdvice, rowCount, paramsOfMethod);
    }
    private void setData(Object[][] data){
        this.data = data;
        rowCount = data.length;
        colCount = data[0].length;
//        isEmptyRatesIsUpdate = false;
        isColumnDetailsIsUpdate = false;
    }
    private void startEmpyColCounterAndEmptyRowCounter(){
        emptyColCounterForRow = new HashMap<Integer, Integer>();
        emptyRowCounterForCol = new HashMap<Integer, Integer>();
        for(int sayac = 0; sayac < colCount; sayac++){
            emptyRowCounterForCol.put(sayac, 0);
        }
        for(int sayac = 0; sayac < rowCount; sayac++){
            emptyColCounterForRow.put(sayac, 0);
        }
    }
    private Object[] convCategoriesAsBinomial(int colIndex, boolean setFirstValueAsTrue, boolean setValueAsBoolean){
        Object[] colD = getColumnValues(colIndex);
        if(!getAreUniqueValuesCalculated()[colIndex])
            uniqueColValues[colIndex] = calculateUniqueValues(colD);// Münferid değerler hesaplanmamışsa, hesapla
        if(uniqueColValues[colIndex] == null)// Sütunda hiç veri yoksa işlemi sonlandır
            return null;
        if(uniqueColValues[colIndex].length > 2)// İkili kodlama için sütunda en fazla iki değer olmalı
            return null;
        Object[] value = new Object[rowCount];
        Object valAsTrue = uniqueColValues[colIndex][0];
        if(!setFirstValueAsTrue){
            if(uniqueColValues[colIndex].length == 2)
                valAsTrue = uniqueColValues[colIndex][1];
            else
                valAsTrue = null;
        }
        for(int sayac = 0; sayac < rowCount; sayac++){
            if(colD[sayac] == null)
                value[sayac] = null;
            else if(colD[sayac] == valAsTrue){
                if(setValueAsBoolean)
                    value[sayac] = true;
                else
                    value[sayac] = 1;
            }
            else{
                if(setValueAsBoolean)
                    value[sayac] = false;
                else
                    value[sayac] = 0;
            }
        }
        return value;
    }
    private Object[] convCategoriesAsOrdinal(int colIndex, Object[] valuesAsAscending){// İkinci parametre hangi değerin sırasının yüksek olması isteniyorsa, o şekilde sıralanmış sütun kategori verisidir
        Object[] colD = getColumnValues(colIndex);
        HashMap<Object, Integer> valToOrder = new HashMap<Object, Integer>();
        Object[] value = new Object[rowCount];
        for(int sayac = 0; sayac < valuesAsAscending.length; sayac++){
            valToOrder.put(valuesAsAscending[sayac], (sayac + 1));
        }
        for(int sayac = 0; sayac < rowCount; sayac++){
            value[sayac] = valToOrder.get(colD[sayac]);
        }
        return value;
    }
    private Object[][] convCategoriesAsOneHotVector(int colIndex, boolean codeAsBoolean){// 'null' değerli bir satır varsa, 'null' yeni bir sütun yapılmıyor
        Object[] colD = getColumnValues(colIndex);
        if(!getAreUniqueValuesCalculated()[colIndex])
            uniqueColValues[colIndex] = calculateUniqueValues(colD);// Münferid değerler hesaplanmamışsa, hesapla
        Object[] categories = uniqueColValues[colIndex];
        categories = removeEmptyString(categories);
        HashMap<Object, Integer> valToColOrder = new HashMap<Object, Integer>();
        int counter = 0;
        for(int sayac = 0; sayac < categories.length; sayac++){
            if(categories[sayac] != null){
                valToColOrder.put(categories[sayac], counter);
                counter++;
            }
        }
        /*
            for(Object obj : valToColOrder.keySet()){
                System.out.println(obj + " -> " + valToColOrder.get(obj));
            }
            return false;
        */
        Object[][] values = new Object[valToColOrder.size()][rowCount];
        Object bitOfTrue, bitOfFalse;
        if(codeAsBoolean){
            bitOfTrue = true;
            bitOfFalse = false;
        }
        else{
            bitOfTrue = 1;
            bitOfFalse = 0;
        }
        for(int sayac = 0; sayac < valToColOrder.size(); sayac++){
            for(int s2 = 0; s2 < rowCount; s2++){
                if(colD[s2] == null){
                    values[sayac][s2] = bitOfFalse;
                    continue;
                }
                if(valToColOrder.get(colD[s2]) == sayac)
                    values[sayac][s2] = bitOfTrue;
                else
                    values[sayac][s2] = bitOfFalse;
            }
        }
        return values;
    }
    private void calculateBasicStatistics(){
        // Temel istatistik hesâplamalarını yaparken;
        // .;. : BURADA KALINDI
    }
    private int calculateNonEmptyOnCol(int colIndex){
        double emptyCellNo = (getEmptyRateOfCols()[colIndex] * rowCount) / 100;
        return rowCount - ((int) Math.ceil(emptyCellNo));
    }
    private void calculateStatistics(){
        for(int sayac = 0; sayac < colCount; sayac++){
            calculateStatistic(sayac);
        }
    }
    private void calculateStatistic(int colNumber){
        Object[] colData = getColumnValues(colNumber);
        if(statistics == null)
            statistics = new Statistic[colCount];
        statistics[colNumber] = Statistic.calculateBasicStatistics(colData, getDataTypes()[colNumber]);
        if(!getIsColumnIsCategorical()[colNumber])
            Statistic.calculateDistributionMetrics(statistics[colNumber], colData);
        getIsStatisticIsUpdate()[colNumber] = true;
    }
    private boolean getIsNumber(int colNumber){
        Class typ = getDataTypes()[colNumber];
        if(typ == Number.class)
            return true;
        if(typ.getSuperclass() != null){
            if(typ.getSuperclass() == Number.class)
                return true;
        }
        return false;
    }
    private String getTypeNameAsShort(Class cls){// Veri tipi ismini kısa hâliyle döndür
        String[] splitted = cls.getName().split("\\.");
        return splitted[splitted.length - 1];
    }
    private String getTypeNameAsShort(Object data){
        return getTypeNameAsShort(data.getClass());
    }
    private void fixTypesOfDataDependMostMatchedType(int colNumber){// Sütundaki tüm verileri sütunda en çok bulunan veri tipine dönüştür
        if(dTypes == null)
            autoAssignDataTypes();
        //targetType = wrapPrimitiveClass(new Class[]{targetType})[0];
        HashMap<Class, Integer> typs = dTypesCounterForCols.get(colNumber);
        int max = -1;
        Class clsOfMax = null;
        boolean moreMatched = false;// Aynı sayıda birden fazla veri tipinde örnek var
        for(int sayac = 0; sayac < typs.size(); sayac++){
            for(Class cls : typs.keySet()){
                if(typs.get(cls) > max){
                    max = typs.get(cls);
                    clsOfMax = cls;
                }
            }
        }
        for(Class cls : typs.keySet()){
            if(typs.get(cls) == max && cls != clsOfMax)// Birden fazla aynı sayıda veri örneği bulunduran sütun varsa
                moreMatched = true;
        }
        if(clsOfMax != null){
            if(!moreMatched){
                for(int sayac = 0; sayac < rowCount; sayac++){
                    if(data[sayac][colNumber] != null){
                        Object[] results = convDataType(data[sayac][colNumber], clsOfMax);
                        if(!((Boolean)results[0])){
                            addMessage(sayac + " indeksli satırdaki veri" + getTypeNameAsShort(clsOfMax) + " tipine dönüştürülemiyor!");
                        }
                        else
                            data[sayac][colNumber] = results[1];
                    }
                }
            }
            else
                addMessage("Aynı örnek sayısına sâhip birden fazla veri tipi olduğundan sayıya bağlı olarak veri tipi dönüşümü yapılamadı");
        }
        addMessage("Veri olmadığından veri tipine dayalı olarak veri tipi dönüşümü yapılamadı");
    }
    private void fixTypesOfDataDependMostMatchedType(){
        for(int sayac = 0; sayac < colCount; sayac++){
            fixTypesOfDataDependMostMatchedType(sayac);
        }
    }
    private void rollingNumbers(int colIndex, int digitNumber){// Double veriler için
        if(dTypes == null)
            detectDataTypes(true);
        if(dTypes[colIndex] != Double.class)
            return;
        for(int sayac = 0; sayac < rowCount; sayac++){
            data[sayac][colIndex] = MathFuncs.roundNumber((double) data[sayac][colIndex], digitNumber);
        }
        isColumnDetailsIsUpdate = false;// Sütun detay bilgileri 'taze değil' olarak işâretleniyor
        areUniqueValuesCalculated[colIndex] = false;// Münferid değerler 'hesaplanmadı' olarak işâretleniyor
        isStatisticIsUpdate[colIndex] = false;// Sütun istatistiği 'taze değil' olarak işâretleniyor
    }
    private void addCategoricalData(int beginIndex, Object[] realData, Object codedData, Class typeOfCodedUnitValue, CategoricalVariable.CODING_TYPE codingType){
        // BURADA KALINDI:
        // Elimizde veriler yok
        // Sütun kategorik verileri kaydet
        // Kategorik veriler için istatistik oluşturma işlemi
        // Kategorik veri barındıran sütunlar için istatistik istendiğinde kategori istatistiğini gönder
        int numberOfColumn = 0;
         Object converted;
        if(codingType == CategoricalVariable.CODING_TYPE.ONEHOTVECTOR){
            numberOfColumn = ((Object[][]) codedData).length;
            converted = MatrixFunctions.transpoze((Object[][]) codedData);
        }
        else
            converted = codedData;
        
        // Sütunların kategorik veri barındırdığını kaydet:
        getIsColumnIsCategorical()[beginIndex] = true;
        for(int sayac = 1; sayac < numberOfColumn; sayac++){// Bu döngü yalnızca Tek nokta vektörü kodlaması için çalışıyor
            isColumnIsCategorical = MatrixFunctions.addNewMember(isColumnIsCategorical, true, beginIndex + sayac, false);
            getMapColIndexToCategoricalColIndex().put(beginIndex + sayac, beginIndex);
        }
        Class typeOfRealData = null;
        if(realData.length > 0)
            typeOfRealData = realData[0].getClass();// Sütunun gerçek verilerinin tipi
        // Kategorik verileri tutmak ve erişmek için hâzırlanan 'CategoricalVariable' sınıfını kullan
        CategoricalVariable var = new CategoricalVariable(getColumnNames()[beginIndex], typeOfRealData, realData, converted, typeOfCodedUnitValue, codingType);
        
        // Kategorik veriyi sakla:
        getMapCategoricalVars().put(beginIndex, var);
    }
    private CategoricalVariable getCategoricVariableOfCol(int colIndex){
        if(!getIsColumnIsCategorical()[colIndex])// Sütun kategorik veri barındırmıyorsa 'null' döndür
            return null;
        CategoricalVariable var = getMapCategoricalVars().get(colIndex);
        if(var != null)
            return var;
        return getMapCategoricalVars().get(getMapColIndexToCategoricalColIndex().get(colIndex));
    }
    private void reCodeOneHotCodedColsForBeforeColDeleting(int deletingColIndex, CategoricalVariable var){// Kategorik veri barındıran sütunun silinmeden önce gerekli düzeltmelerden geçmesi için...
        // Sütun verisi silinmesi işlemi yapılmıyor
        // Sütun verisinde silinen değeri barındıran satırlar diğer sütunlar için 'null' olarak dolduruluyor (Kategorik değişken çok sütunlu ise)
        // Kategorik değişken tek satırdan oluşuyorsa 'mapCategoricalVars'taki bağlantı kaldırılıyor;
        // çok satırdan oluşuyorsa ve 'mapCategoricalVars' bağlantısı olan satır siliniyorsa, bağlantı kaydırılıyor
        // 'isColumnIsCategorical' değişkenine dokunulmuyor; zîrâ bu 'deleteCols' yöntemi içerisinde yapılıyor
        int len = var.getCategoryNumber();
        if(len == 1){
            getMapCategoricalVars().remove(deletingColIndex);
            return;
        }
        int headColNo = deletingColIndex;// Kategorik verinin ilk sütununun indisinin verilen indis olduğunu varsayalım
        if(getMapCategoricalVars().get(deletingColIndex) == null){
            headColNo = getMapColIndexToCategoricalColIndex().get(deletingColIndex);// Bu durumda, baş indisi bul
        }
        // Kodlamayı düzelt : Silinen sütun değerlerinin diğer sütunlardaki karşılığını (aynı satır oluyor) 'null' yap
        // CategoricalVariable değişkenini tazele (categoryNumber, istatistik ve gerçek verileri ve kodlamış verileri vs.)
        // BURADA KALINDI...
        Object[] colDataWhichDeleting = getColumnValues(deletingColIndex);
        ArrayList<Integer> liValuesOfDeleting = new ArrayList<Integer>();// Silinen sütunun '1' olduğu satırları sonra 'null' yapmak üzere işâretle
        for(int sayac = 0; sayac < colDataWhichDeleting.length; sayac++){
            if(colDataWhichDeleting[sayac] == var.getBitOfTrue())
                liValuesOfDeleting.add(sayac);
        }
        ArrayList<Integer> otherColNumbers = new ArrayList<Integer>();// Kategorik değişkenin diğer sütunlarının indisleri
        for(int sayac = 0; sayac < var.getCategoryNumber(); sayac++){
            otherColNumbers.add(headColNo + sayac);
        }
        otherColNumbers.remove((Integer) deletingColIndex);// Silinmesi istenen sütunun indisini listeden çıkar
        for(int colNo : otherColNumbers){// Kategorik değişkenin diğer tüm sütunlarda gez
            for(int sayac = 0; sayac < liValuesOfDeleting.size(); sayac++){
                data[liValuesOfDeleting.get(sayac)][colNo] = null;
            }
        }
        int orderOfDeletingIndex =  deletingColIndex - headColNo;// Silinmesi istenen sütunun kategorik değişken içerisindeki sütun numarası
        Object[][] reCodedData = MatrixFunctions.deleteSelectedCols(((Object[][])var.getCodedData()), new int[]{orderOfDeletingIndex});
        Object[] editedRealData = var.getRealData();// Gerçek veriden silinen sütunun ifâde ettiği değeri barındıran satırları silmek için önce silinmemiş hâlini alalım
        for(int rowNo : liValuesOfDeleting){// Gerçek veri yeniden düzenlenmiş oluyor
            editedRealData[rowNo] = null;
        }
//        MatrixFunctions.printVector(editedRealData, true);
        CategoricalVariable reCoded = new CategoricalVariable(var.getName(), var.getdType(), editedRealData, reCodedData, var.getTypeOfCodedUnitValues(), CategoricalVariable.CODING_TYPE.ONEHOTVECTOR);
       //////
        if(headColNo == deletingColIndex){// Kategorik değişkenin ilk sütunu siliniyorsa
            headColNo++;// yeni ilk sütun indisi sonraki indis olur
            getMapColIndexToCategoricalColIndex().remove((Integer) headColNo);// Baş sütun indisinin baş sütun indisini işâret etmesine gerek yok
            for(int colNo : otherColNumbers){// Diğer indislerin işâret ettiği ilk sütun indisi de tazelenmeli
                if(colNo == headColNo)// Baş sütun indisini atla
                    continue;
                getMapColIndexToCategoricalColIndex().put(colNo, headColNo);
            }
        }
        else{// İlk sütun silinmediyse, silinen sütunun indisi bir kategorik değişkene işâret etmesin
            getMapColIndexToCategoricalColIndex().remove((Integer) deletingColIndex);
        }
        getMapCategoricalVars().put(headColNo, reCoded);// Kategorik değişkeni işâret eden baş indisi yerleştir
    }
    private Object[] removeEmptyString(Object[] data){
        ArrayList<Object> values = new ArrayList<Object>();
        for(int sayac = 0; sayac < data.length; sayac++){
            if(String.valueOf(data[sayac]).isEmpty())
                continue;
            values.add(data[sayac]);
        }
        Object[] arrValues = new Object[values.size()];
        values.toArray(arrValues);
        return arrValues;
    }
    private void shiftConnectionWithVariableToRight(int indexOfFirstNewCol, int numberOfNewColumn){// indexOfFirstNewCol : Yeni sütunun eklenmeye başlandığı nokta, numberOfNewColumn : yeni sütun sayısı
        HashMap<Integer, CategoricalVariable> mapVarNew = new HashMap<Integer, CategoricalVariable>();
        HashMap<Integer, Integer> mapNewIndexToCategoricalColIndex = new HashMap<Integer, Integer>();
        for(int sayac = 0; sayac < indexOfFirstNewCol; sayac++){// Kodlama sütununun solundaki değerleri yeni haritaya olduğu gib ekle
            CategoricalVariable var = getMapCategoricalVars().get(sayac);
            if(var != null){
                mapVarNew.put(sayac, var);
            }
            Integer headIndexOfCurrentIndex = getMapColIndexToCategoricalColIndex().get(sayac);
            if(headIndexOfCurrentIndex != null){
                mapNewIndexToCategoricalColIndex.put(sayac, headIndexOfCurrentIndex);
            }
        }
        // Aşağıdaki kod 'mapCategoricalVars' ve 'mapColIndexToCategoricalColIndex' haritalarının indis numaralarının tazelenmesi için..
        for(int colNo : getMapCategoricalVars().keySet()){// Kodlanmış sütunun arasına yeni sütun kodlaması eklenemeyeceğinden, getMapCategoricalVars haritasının kaydırılması işlemini yaparken 'mapColIndexToCategoricalColIndex' haritasının tazelenmesi de yapılabilir
            if(colNo >= indexOfFirstNewCol){
                CategoricalVariable var = getMapCategoricalVars().get(colNo);
                int len = var.getColCount();// Değişkenin sütun sayısı
                int headIndexNew = colNo + numberOfNewColumn;
                mapVarNew.put(headIndexNew, var);
                for(int sayac = 1; sayac < len; sayac++){
                    mapNewIndexToCategoricalColIndex.put(headIndexNew + sayac, headIndexNew);
                }
            }
        }
        mapCategoricalVars = mapVarNew;
        mapColIndexToCategoricalColIndex = mapNewIndexToCategoricalColIndex;
        // isColumnIsCategorical dizisi değiştirilmiyor; zîrâ bu değişiklik bu fonksiyonun çağrıldığı satırdan sonra çalışan 'addCategoricalData' fonksiyonunda yapılıyor
    }
    private boolean normalizeOrStandardize(int colIndex, boolean isNormalize){// Verilerin normalize veyâ standardize edilmesi için kullanılır;
        Statistic stats;
        boolean isSuccess = false;
        if(dTypes[colIndex] != Double.class){// Veri tipi 'double' değilse, dönüştür
            changeColumnDataType(colIndex, Double.class, true, null);
            if(!isSuccess)
                return false;
        }
        stats = getStatisticForColumn(colIndex);
        if(stats.size <= 1)// Sütunda dolu veri yoksa veyâ bir tâne varsa
            return false;
        double base;
        if(isNormalize)// İşlem normalizasyon ise payda = xmax - xmin
            base = stats.max - stats.min;
        else
            base = stats.stdDeviation;// İşlem standardizasyon ise payda = standart sapma
        for(int sayac = 0; sayac < rowCount; sayac++){
            if(data[sayac][colIndex] == null)// 'null' olan satırları atla
                continue;
            double upNumber;
            if(isNormalize)// İşlem normalizasyon ise pay = x - xmin
                upNumber = ((Double) data[sayac][colIndex] - stats.min);
            else// İşlem standardizasyon ise pay = x - ortalama
                upNumber = ((Double) data[sayac][colIndex] - stats.mean);
            data[sayac][colIndex] = (Double) (upNumber / base);
        }
        getIsStatisticIsUpdate()[colIndex] = false;
        getAreUniqueValuesCalculated()[colIndex] = false;
        return true;
    }

//ERİŞİM YÖNTEMLERİ:
    public int getColumnCount(){
        return colCount;
    }
    public int getRowCount(){
        return rowCount;
    }
    public Object[][] getData(){
        return data;
    }
    public double[] getEmptyRateOfCols(){
        return emptyRateOfCols;
    }
    public double[] getEmptyRateOfRows(){
        return emptyRateOfRows;
    }
    public String[] getColumnNames(){
        if(columnNames == null){
            columnNames = new String[colCount];
            for(int sayac = 0; sayac < colCount; sayac++){
                columnNames[sayac] = "Sütun - " + sayac;
            }
        }
        return columnNames;
    }
    public int getColIndexByName(String colName){
        int value = -1;
        for(int sayac = 0; sayac < colCount; sayac++){
            if(getColumnNames()[sayac].equals(colName)){
                value = sayac;
                break;
            }
        }
        return value;
    }
    public ArrayList<Advice> getAdvices(){
        if(adviceList == null){
            adviceList = new ArrayList<Advice>();
        }
        return adviceList;
    }
    public String[] getAdviceTexts(){
        if(getAdvices().isEmpty())
            return new String[]{""};
        String[] texts = new String[adviceList.size()];
        int sayac = 0;
        for(Advice ad : adviceList){
            texts[sayac] = ad.getStrAdvice();
            sayac++;
        }
        return texts;
    }
    public String[] getMessageTexts(){
        String[] texts = new String[getMessages().size()];
        int sayac = 0;
        for(String msg : getMessages()){
            texts[sayac] = (sayac + 1) + " --> " + msg;
            sayac++;
        }
        return texts;
    }
    public boolean getIsEmptyRatesIsUpdate(){
        return isEmptyRatesIsUpdate;
    }
    public HashMap<Integer, HashMap<Class, Integer>> getDTypesCounterForCols(){
        return dTypesCounterForCols;
    }
    public Class[] getDataTypes(){
        if(dTypes == null)
            autoAssignDataTypes();
        return dTypes;
    }
    public boolean[] getCouldCategorical(){// Bu yönteme bu sınıf için gerek yok; 'NullPointerException' durumuna düşülmemesi için güvenlik tedbîri olarak ve arayüz ve testten erişim için eklendi
        if(couldCategorical == null)
            couldCategorical = MatrixFunctions.produceBooleanArray(colCount, false);
        return couldCategorical;
    }
    public Object[][] getUniqueAllColValues(){
        if(!getAreUniqueValuesCalculated()[0]){// Sadece ilk sütundaki münferid değer hesâplanmamışsa tüm sütunlar yeniden hesâplanıyor, verimli değil, düzelt
            calculateUniqueValuesOnColumns();
        }
        return uniqueColValues;
    }
    public Object[] getUniqueColValues(int colNumber){
        if(uniqueColValues == null){
            uniqueColValues = new Object[colCount][];
        }
        if(!getAreUniqueValuesCalculated()[colNumber]){
            uniqueColValues[colNumber] = calculateUniqueValues(getColumnValues(colNumber));
        }
        return uniqueColValues[colNumber];
    }
    //GİZLİ ERİŞİM YÖNTEMLERİ:
    private HashMap<Integer, Integer> getEmptyRowCounterForCols(){
        if(emptyRowCounterForCol == null){
            emptyRowCounterForCol = new HashMap<Integer, Integer>();
        }
        return emptyRowCounterForCol;
    }
    private HashMap<Integer, Integer> getEmptyColCounterForRows(){
        if(emptyColCounterForRow == null){
            emptyColCounterForRow = new HashMap<Integer, Integer>();
        }
        return emptyColCounterForRow;
    }
    public ArrayList<Integer> getEmptyRowIndexes(){
        if(emptyRowIndexes == null){
            emptyRowIndexes = new ArrayList<Integer>();
        }
        return emptyRowIndexes;
    }
    public ArrayList<Integer> getEmptyColIndexes(){
        if(emptyColIndexes == null){
            emptyColIndexes = new ArrayList<Integer>();
        }
        return emptyColIndexes;
    }
    private ArrayList<String> getMessages(){
        if(messages == null){
            messages = new ArrayList<String>();
        }
        return messages;
    }
    private boolean[] getAreUniqueValuesCalculated(){
        if(areUniqueValuesCalculated == null){
            areUniqueValuesCalculated = MatrixFunctions.produceBooleanArray(colCount, false);
        }
        return areUniqueValuesCalculated;
    }
    private ArrayList<Process> getSavedProcs(){
        if(savedProcs == null){
            savedProcs = new ArrayList<Process>();
        }
        return savedProcs;
    }
    public Statistic[] getStatistics(){
        return statistics;
    }
    public boolean[] getIsStatisticIsUpdate(){
        if(isStatisticIsUpdate == null){
            isStatisticIsUpdate = MatrixFunctions.produceBooleanArray(colCount, false);
        }
        return isStatisticIsUpdate;
    }
    public boolean[] getIsColumnIsCategorical(){
        if(isColumnIsCategorical == null)
            isColumnIsCategorical = MatrixFunctions.produceBooleanArray(colCount, false);
        return isColumnIsCategorical;
    }
    public HashMap<Integer, CategoricalVariable> getMapCategoricalVars(){
        if(mapCategoricalVars == null)
            mapCategoricalVars = new HashMap<Integer, CategoricalVariable>();
        return mapCategoricalVars;
    }
    public HashMap<Integer, Integer> getMapColIndexToCategoricalColIndex(){
        if(mapColIndexToCategoricalColIndex == null)
            mapColIndexToCategoricalColIndex = new HashMap<Integer, Integer>();
        return mapColIndexToCategoricalColIndex;
    }
    
}