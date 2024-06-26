package Service;

import Base.Statistic;
import java.lang.reflect.Array;
import java.util.HashMap;


public class MatrixFunctions{
    // deleteSelectedMembers gibi fonksiyonlarda silinmesi gereken indeks numarası aynı verildiğinde hatâ vermemeli, yâ öncesinde veriyi tekilleştir; yâ dâ bunu göz önüne alarak tasarım değişikliği yap
    public MatrixFunctions(){
        
    }

//İŞLEM YÖNTEMLERİ:
    public static Object[][] divideMatrixOnColumn(Object[][] data, int startColIndex, int endColIndex){//Yöntem ayrıca satırların sütun sayısını eşitliyor; boşlukları 'null' ile dolduruyor
        Object[][] value = new Object[data.length][];
        int len = endColIndex - startColIndex + 1;
        int colCounter;
        for(int sayac = 0; sayac < data.length; sayac++){
            value[sayac] = new Object[len];
            colCounter = 0;
            if(data[sayac] == null){
                value[sayac] = null;
                continue;
            }
            for(int s2 = startColIndex; s2 < endColIndex + 1; s2++){
                if(s2 >= data[sayac].length)//Satırların sütun uzunlukları farklı olduğundan aşağıdaki kod 'NullPointerException' fırlatabiliyor; ?
                    continue;
                if(data[sayac][colCounter] != null)
                    value[sayac][colCounter] = data[sayac][s2];
                else
                    value[sayac][colCounter] = null;
                
                colCounter++;
            }
        }
        return value;
    }
    public static void printMatrix(Object[][] matrix){//Matrisi matris formunda yazdırır
        for(int sayac = 0; sayac < matrix.length; sayac++){
            for(int s2 = 0; s2 < matrix[sayac].length; s2++){
                System.out.print("[" + matrix[sayac][s2] + "] ");
            }
            System.out.println();
        }
    }
    public static void printMatrix(double[][] matrix){
        for(int sayac = 0; sayac < matrix.length; sayac++){
            for(int s2 = 0; s2 < matrix[sayac].length; s2++){
                System.out.print("[" + matrix[sayac][s2] + "] ");
            }
            System.out.println();
        }
    }
    public static void printMatrixWithTitle(Object[][] matrix, String title){//'title' başlığıyla matrisi matris formunda göster
        System.out.println(title + "\n");
        printMatrix(matrix);
    }
    public static Object[][] deleteSelectedRows(Object[][] data, int[] rowIndexes){//sort yönteminden dönen değer atanmıyor
        Object[][] value = new Object[data.length - rowIndexes.length][];
        int rowCounter = 0;
        int delRowCounter = 0;
        int[] rowIndexesAsSorted = sort(rowIndexes, true);
        boolean del;
        for(int sayac = 0; sayac < data.length; sayac++){
            del = false;
            for(int s2 = delRowCounter; s2 < rowIndexesAsSorted.length; s2++){//Sıralı şekilde siliniyor; her silmede silme imleci bir kaydırılıyor; silinecek sıra numaraları sıralandı
                if(sayac == rowIndexesAsSorted[s2]){
                    del = true;
                    delRowCounter++;
                    break;
                }
            }
            if(!del){
                value[rowCounter] = data[sayac];
                rowCounter++;
            }
        }
        return value;
    }
    public static Object[][] deleteSelectedCols(Object[][] data, int[] colIndexes){
        if(data.length <= colIndexes.length)
            return null;
        Object[][] value = new Object[data.length][];
        int[] colIndexesAsSorted = sort(colIndexes, true);
        int colCounter = 0, delIndexCounter = 0;
        int len = data[0].length - colIndexes.length;
        boolean del = false;
        for(int sayac = 0; sayac < data.length; sayac++){
            value[sayac] = new Object[len];
            colCounter = 0;
            delIndexCounter = 0;
            for(int s2 = 0; s2 < data[sayac].length; s2++){
                del = false;
                for(int s3 = delIndexCounter; s3 < colIndexesAsSorted.length; s3++){
                    if(s2 == colIndexesAsSorted[s3]){
//                        System.err.println("sayac - s2 - s3: " + sayac + " - " + s2 + " - " + s3);
                        del = true;
                        delIndexCounter++;
                        break;
                    }
                }
                if(!del){
                    value[sayac][colCounter] = data[sayac][s2];
                    colCounter++;
                }
            }
        }
        return value;
    }
    public static <T> T[] deleteSelectedMembers(T[] array, int[] deleteIndexes, Class <T[]> cls){
        if(array == null)
            return null;
        if(deleteIndexes == null)
            return array;
        if(deleteIndexes.length == 0)
            return array;
        if(deleteIndexes.length >= array.length)
            return null;
        T[] value = cls.cast(Array.newInstance(cls.getComponentType(), (array.length - deleteIndexes.length)));
        //T[] value = new T[array.length - deleteIndexes.length];
        int delCounter = 0;
        int valCounter = 0;
        int[] delAsSorted = sort(deleteIndexes, true);
        boolean del;
        for(int sayac = 0; sayac < array.length; sayac++){
            del = false;
            for(int s2 = delCounter; s2 < delAsSorted.length; s2++){
                if(sayac == delAsSorted[s2]){
                    delCounter++;
                    del = true;
                    break;
                }
            }
            if(!del){
                value[valCounter] = array[sayac];
                valCounter++;
            }
        }
        return value;
    }
    public static <T> T[] setNullSelectedMembers(T[] array, int[] deleteIndexes){
        if(array == null)
            return null;
        if(deleteIndexes == null)
            return array;
        if(deleteIndexes.length == 0)
            return array;
        if(deleteIndexes.length >= array.length)
            return null;
        T[] values = array.clone();
        int delCounter = 0;
        int[] delAsSorted = sort(deleteIndexes, true);
        for(int sayac = 0; sayac < array.length; sayac++){
            for(int s2 = delCounter; s2 < delAsSorted.length; s2++){
                if(sayac == delAsSorted[s2]){
                    delCounter++;
                    values[sayac] = null;
                    break;
                }
            }
        }
        return values;
    }
    public static <T> T[] shiftArrayToLeft(T[] array, int beginIndex, Class<T[]> classOfArray){
        if(array == null)
            return null;
        int maxIndex = array.length - 1;
        if(beginIndex == 0)
            beginIndex++;
        if(beginIndex > maxIndex)
            return array;
        T[] values = classOfArray.cast(Array.newInstance(classOfArray.getComponentType(), (array.length - 1)));
        for(int sayac = 0; sayac < array.length; sayac++){
            if(sayac < beginIndex){
                values[sayac] = array[sayac];
                continue;
            }
            values[sayac - 1] = array[sayac];
        }
        return values;
    }
    
    
    // 0'dan başlanarak sola kaydırılamaz, 1'den başlanarak sola kaydırılabilir
    /*public static <T> T[] shiftArray(T[] array, int beginIndex, Class<T[]> classOfArray, boolean shiftToLeft){// Geliştirilebilir yöntem : Kaydırma işleminde bir eleman siliniyor; dizi büyütülmüyor; dizi büyütülsün mü, seçeneği eklenerek yöntem daha kapsamlı hâle getirilebilir; ayrıca çevirimli kaydırılsın mı, seçeneği eklenerek sağa kaydırmada son elemanın başa, sola kaydırmada ilk elemanın sona eklenmesi kodlanabilir
        if(array == null)
            return null;
        int maxIndex = array.length - 1;
        if(beginIndex > maxIndex)
            return array;
        if(beginIndex == maxIndex && !shiftToLeft)
            return array;
        if(beginIndex == 0 && shiftToLeft)
            return array;
        T[] values = classOfArray.cast(Array.newInstance(classOfArray.getComponentType(), (array.length - 1)));
        int cycleLen = array.length;
        if(!shiftToLeft)
            cycleLen++;
        for(int sayac = 0; sayac < cycleLen; sayac++){
            if(sayac < beginIndex){
                values[sayac] = array[sayac];
                continue;
            }
            if(shiftToLeft)
                values[sayac - 1] = array[sayac];
            else
                values[sayac] = array[sayac + 1];
        }
        return values;
    }*/
    public static boolean[] deleteSelectedMembers(boolean[] array, int[] deleteIndexes){
        if(array == null)
            return null;
        if(deleteIndexes == null)
            return array;
        if(deleteIndexes.length == 0)
            return array;
        if(deleteIndexes.length >= array.length)
            return null;
        boolean[] value = new boolean[array.length - deleteIndexes.length];
        //T[] value = new T[array.length - deleteIndexes.length];
        int delCounter = 0;
        int valCounter = 0;
        int[] delAsSorted = sort(deleteIndexes, true);
        boolean del;
        for(int sayac = 0; sayac < array.length; sayac++){
            del = false;
            for(int s2 = delCounter; s2 < delAsSorted.length; s2++){
                if(sayac == delAsSorted[s2]){
                    delCounter++;
                    del = true;
                    break;
                }
            }
            if(!del){
                value[valCounter] = array[sayac];
                valCounter++;
            }
        }
        return value;
    }
    public static double[] deleteSelectedMembers(double[] array, int[] selectedMemberIndexes){// İyileştirilebilir fonksiyon : Büyük dizilerde önce sıralama yapılırsa daha iyi olabilir, başka iyileştirmeler de bulunabilir
        double[] val = new double[array.length - selectedMemberIndexes.length];
        int addIndex = 0;
        boolean del;
        for(int sayac = 0; sayac < array.length; sayac++){// Yüksek boyutlu diziler için önce dizi sıralanmalı mı?
            del = false;
            for(int s2 = 0; s2 < selectedMemberIndexes.length; s2++){
                if(sayac == selectedMemberIndexes[s2])
                    del = true;
            }
            if(!del){
                val[addIndex] = array[sayac];
                addIndex++;
            }
        }
        return val;
    }
    public static Object[] convertPrimitiveToObject(Object[] aa){
        return aa;
    }
    public static void printVectorWithPrimitiveDoubleType(double[] vector, boolean isARow){
        Object[] val = convertArrayPrimitiveToObjectValue(vector);
        printVectorWithLabels(val, isARow, null);
    }
    public static void printVectorWithPrimitiveIntegerType(int[] vector, boolean isARow){
        Object[] val = convertArrayPrimitiveToObjectValue(vector);
        printVectorWithLabels(val, isARow, null);
    }
    public static void printVector(Object[] vector, boolean isARow){
        printVectorWithLabels(vector, isARow, null);
    }
    public static void printVectorWithLabels(Object[] vector, boolean isARow, String[] labels){
        System.out.println("");
        for(int sayac = 0; sayac < vector.length; sayac++){
            if(labels != null){
                if(labels[sayac] != null)
                    System.out.print(labels[sayac] + " = ");
            }
            System.out.print(vector[sayac]);
            if(isARow)
                System.out.print("\t");
            else
                System.out.print("\n");
        }
    }
    public static double[] sort(double[] data, boolean isAscending){
        double[] value = data.clone();
        for(int sayac = 0; sayac < value.length; sayac++){
            for(int s2 = 0; s2 < value.length - sayac - 1; s2++){
                if(isAscending){
                    if(value[s2] > value[s2 + 1]){
                        double temp = value[s2];
                        value[s2] = value[s2 + 1];
                        value[s2 + 1] = temp;
                    }
                }
                else{
                    if(value[s2] < value[s2 + 1]){
                        double temp = value[s2];
                        value[s2] = value[s2 + 1];
                        value[s2 + 1] = temp;
                    }
                }
            }
        }
        return value;
    }
    public static int[] sort(int[] data, boolean isAscending){
        int[] value = data.clone();
        for(int sayac = 0; sayac < value.length; sayac++){
            for(int s2 = 0; s2 < value.length - sayac - 1; s2++){
                if(isAscending){
                    if(value[s2] > value[s2 + 1]){
                        int temp = value[s2];
                        value[s2] = value[s2 + 1];
                        value[s2 + 1] = temp;
                    }
                }
                else{
                    if(value[s2] < value[s2 + 1]){
                        int temp = value[s2];
                        value[s2] = value[s2 + 1];
                        value[s2 + 1] = temp;
                    }
                }
            }
        }
        return value;
    }
    public static<T extends Number> HashMap<String, Object[]> sortValuesReturnValuesAndIndexes(T[] data, Integer[] indexes, boolean isAscending){
        // Verileri sıralar; sonra sıralanmış verileri ve sıralanmış verilerin asıl dizideki indislerini döndürür
        // Dönüş tipi : <data, sıralanmış veri>, <indexes, sıralanmış verilerin asıl dizideki indisleri>
        T[] values = data.clone();
        if(indexes == null){
            indexes = new Integer[values.length];
            for(int sayac = 0; sayac < indexes.length; sayac++){
                indexes[sayac] = sayac;
            }
        }
        for(int sayac = 0; sayac < values.length; sayac++){
            for(int s2 = 0; s2 < values.length - sayac - 1; s2++){
                if(isAscending){
                    if(values[s2].doubleValue() > values[s2 + 1].doubleValue()){
                        T temp = values[s2];
                        int indexOfTemp = indexes[s2];
                        values[s2] = values[s2 + 1];
                        indexes[s2] = indexes[s2 + 1];// İNDİS
                        values[s2 + 1] = temp;
                        indexes[s2 + 1] = indexOfTemp;// İNDİS
                    }
                }
                else{
                    if(values[s2].doubleValue() < values[s2 + 1].doubleValue()){
                        T temp = values[s2];
                        int indexOfTemp = indexes[s2];
                        values[s2] = values[s2 + 1];
                        values[s2 + 1] = temp;
                        indexes[s2] = indexes[s2 + 1];// İNDİS
                        indexes[s2 + 1] = indexOfTemp;// İNDİS
                    }
                }
            }
        }
        HashMap<String, Object[]> res = new HashMap<String, Object[]>();
        res.put("data", values);
        res.put("indexes", indexes);
        return res;
    }
    public static<T extends Number> HashMap<String, Object[]> sortValuesReturnValuesAndIndexes(T[] data, boolean isAscending){
        return sortValuesReturnValuesAndIndexes(data, null, isAscending);
    }
    public static boolean[] produceBooleanArray(int length, boolean value){
        if(length == 0)
            return null;
        boolean[] arr = new boolean[length];
        for(int sayac = 0; sayac < length; sayac++){
            arr[sayac] = value;
        }
        return arr;
    }
    public static Boolean[] produceBooleanArrayAsNotPrimitive(int length, boolean value){
        if(length == 0)
            return null;
        Boolean[] arr = new Boolean[length];
        for(int sayac = 0; sayac < length; sayac++){
            arr[sayac] = value;
        }
        return arr;
    }
    public static Object[][] produceZeroMatrix(int rowCount, int colCount){//Verilen satır ve sütun sayısına göre o'lardan oluşan matris döndürür.
        Object[][] matrix = new Object[rowCount][colCount];
        for(int index = 0; index < matrix.length; index++){
            for(int index2 = 0; index2 < matrix[0].length; index2++){
                matrix[index][index2] = 0;
            }
        }
        return matrix;
    }
    public static Object[] produceZeroArray(int length){//Verilen uzunluğa göre o'lardan oluşan dizi döndürür.
        return produceZeroArray(length, false);
    }
    public static Object[] produceZeroArray(int length, boolean produceAsShort){//Verilen uzunluğa göre o'lardan oluşan diziyi Short yâ dâ Integer olarak döndürür.
        Object[] arr;
        if(produceAsShort)
            arr = new Short[length];
        else
            arr = new Integer[length];
        for(int index = 0; index < arr.length; index++){
            arr[index] = 0;
        }
        return arr;
    }
    public static<T> T[] produceZeroArray(int length, Class <T[]> cls){
        T[] arr = cls.cast(Array.newInstance(cls.getComponentType(), length));
        for(int sayac = 0; sayac < length; sayac++){
            arr[sayac] = (T) ((Integer) 0);
        }
        return arr;
    }
    public static Object[][] changeColumnData(Object[][] data, Object[] colData, int colIndex){
        boolean setNull = false;
        if(data == null)
            return null;
        if(colData == null)
            setNull = true;
        Object[][] dData = data.clone();
        for(int sayac = 0; sayac < dData.length; sayac++){
            if(colData[sayac] != null && !setNull)
                dData[sayac][colIndex] = colData[sayac];
            else
                dData[sayac][colIndex] = null;
        }
        return dData;
    }
    public static Object[][] addNewColumn(Object[][] data, Object[] newColumn, int colIndex, boolean deleteOldDataOnCol){// 'deleteOldDataOnCol' değeri 'false' ise sütun yeni sütun olarak eklenir
        int len = data[0].length;
        int rowLen = data.length;
        Object[][] value;
        if(len <= colIndex)
            len = colIndex + 1;
        else if(!deleteOldDataOnCol)
            len++;
        if(newColumn.length > data.length)
            rowLen = newColumn.length;
        value = new Object[rowLen][len];
        boolean isThisNewLine;// Yeni satır ise
        for(int sayac = 0; sayac < rowLen; sayac++){
            isThisNewLine = false;
            if(sayac >= data.length)
                isThisNewLine = true;
            for(int s2 = 0; s2 < len; s2++){
                if(isThisNewLine){
                    if(s2 == colIndex)
                        value[sayac][s2] = newColumn[sayac];
                    else
                        value[sayac][s2] = null;
                    continue;
                }
                if(s2 == colIndex){
                    value[sayac][s2] = newColumn[sayac];
                }
                else if(!deleteOldDataOnCol && s2 > colIndex){
                    value[sayac][s2] = data[sayac][s2 - 1];
                }
                else if(s2 < data[0].length){
                    value[sayac][s2] = data[sayac][s2];
                }
                else
                    value[sayac][s2] = null;
            }
        }
        return value;
    }
    public static Object[][] addNewRow(Object[][] data, int colIndex, Object[] dataNew){
        return addNewRows(data, colIndex, new Object[][]{dataNew});
    }
    public static Object[][] addNewRows(Object[][] data, int colIndex, Object[][] dataNew){
        if(colIndex > data.length)
            return data;
        Object[][] values = new Object[data.length + dataNew.length][];
        int counterForNew = 0;
        for(int sayac = 0; sayac < values.length; sayac++){
            if(sayac < colIndex){
                values[sayac] = data[sayac];
            }
            else if(sayac < (colIndex + dataNew.length)){// colIndex'ten itibâren yeni tüm veriler için
                values[sayac] = dataNew[counterForNew];
                counterForNew++;
            }
            else// Yeni sütunlar eklendikten sonraki indisler
                values[sayac] = data[sayac - counterForNew];
        }
        return values;
    }
    public static<T> T[] addNewMember(T[] data, T newMember, Class<T[]> classOfDataArray, int index, boolean deleteDataOnIndex){// İYİLEŞTİRİLEBİLİR FONKSİYON
        int len = data.length;
        if(len <= index)
            len = index + 1;
        else if(!deleteDataOnIndex)
            len++;
        boolean isThisNewIndex;
        T[] value = classOfDataArray.cast(Array.newInstance(classOfDataArray.getComponentType(), len));
        for(int sayac = 0; sayac < len; sayac++){
            isThisNewIndex = false;
            if(sayac >= data.length)
                isThisNewIndex = true;
            if(sayac == index)
                value[sayac] = newMember;
            else if(isThisNewIndex){
                if(sayac == data.length){
                    if(!deleteDataOnIndex && sayac > index)
                        value[sayac] = data[sayac - 1];
                    else
                        value[sayac] = null;
                }
                else
                    value[sayac] = null;
            }
            else{// sayac < data.length && sayac != index
                if(!deleteDataOnIndex && sayac >= index)
                    value[sayac] = data[sayac - 1];
                else
                    value[sayac] = data[sayac];
            }
        }
        return value;
    }
    public static boolean[] addNewMember(boolean[] array, boolean newMember, int index, boolean deleteDataOnIndex){
        Boolean[] capsulated = new Boolean[array.length];
        for(int sayac = 0; sayac < capsulated.length; sayac++){
            capsulated[sayac] = array[sayac];
        }
        capsulated = addNewMember(capsulated, newMember, Boolean[].class, index, deleteDataOnIndex);
        boolean[] vals = new boolean[capsulated.length];
        for(int sayac = 0; sayac < capsulated.length; sayac++){
            vals[sayac] = capsulated[sayac];
        }
        return vals;
    }
    public static Object[] deleteNullValues(Object[] data){
        int counterForNull = 0;
        for(Object o : data){
            if(o == null)
                counterForNull++;
        }
        Object[] values = new Object[data.length - counterForNull];
        int sayac = 0;
        for(Object o : data){
            if(o != null){
                values[sayac] = o;
                sayac++;
            }
        }
        return values;
    }
    public static Object[] convertArrayPrimitiveToObjectValue(double[] values){// BİRLEŞTİRİLEBİLİR ve İYİLEŞTİRİLEBİLİR FONKSİYON
        Object[] res = new Object[values.length];
        for(int sayac = 0; sayac < res.length; sayac++){
            res[sayac] = values[sayac];
        }
        return res;
    }
    public static Object[] convertArrayPrimitiveToObjectValue(int[] values){
        Object[] res = new Object[values.length];
        for(int sayac = 0; sayac < res.length; sayac++){
            res[sayac] = values[sayac];
        }
        return res;
    }
    public static<T> T[] exchangeElementOnTheList(T[] data, Class<T[]> classOfDataArray, int beginIndex, int targetIndex){// Dizideki iki elemanın yerini değiştir
        /*
            T[} data : Veri
            classOfDataArray : Verinin dizi hâlinin sınıfı. Misal, int için : int[].class gönderilmeli
            beginIndex : Yerinin değiştirilmesi istenen elemanın indisi
            targetIndex : Hedef indis
        */
        int len = data.length;
        if(beginIndex < 0 || beginIndex > len -1)
            return null;
        if(targetIndex < 0 || targetIndex > len -1 || targetIndex == beginIndex)
            return data;
        T[] value = classOfDataArray.cast(Array.newInstance(classOfDataArray.getComponentType(), len));// Geri döndürülmek üzere yeni diziyi oluştur
        for(int sayac = beginIndex; sayac < len; sayac++){// Tüm değerleri ata
            value[sayac] = data[sayac];
        }
        T temp = value[targetIndex];
        value[targetIndex] = value[beginIndex];
        value[beginIndex] = temp;
        return value;
    }
    public static Object[][] transpoze(Object[][] data){//Transpozunu alma işlemi, en uzun sütunlu satıra göre hepsini aynı boyuta getirir
        int lenOfCol = data[0].length;
        for(int sayac = 1; sayac < data.length; sayac++){// En uzun sütun uzunluğunu bul
            if(data[sayac].length > lenOfCol)
                lenOfCol = data[sayac].length;
        }
        Object[][] transpozed = new Object[lenOfCol][data.length];
        for(int sayac = 0; sayac < transpozed.length; sayac++){
            for(int s2 = 0; s2 < data.length; s2++){
                if(sayac >= data[s2].length){
                    transpozed[sayac][s2] = null;
                }
                else
                    transpozed[sayac][s2] = data[s2][sayac];
            }
        }
        return transpozed;
    }
    public static Object[] normalizeValues(Object[] values){
        return normalizeOrStandardizeValues(values, true);
    }
    public static Object[] standardizeValues(Object[] values){
        return normalizeOrStandardizeValues(values, false);
    }
    //ARKAPLAN İŞLEM YÖNTEMLERİ:
    private static Object[] normalizeOrStandardizeValues(Object[] values, boolean isNormalize){
        if(values == null)
            return null;
        if(!ClassStringDoubleConverter.getService().areNumber(values))
            return null;
        Statistic stats = Statistic.calculateBasicStatistics(values, values[0].getClass());
        if(stats.getSize() <= 1)// Sütunda dolu veri yoksa veyâ bir tâne varsa
            return null;
        double base;
        if(isNormalize)// İşlem normalizasyon ise payda = xmax - xmin
            base = stats.getRange();
        else
            base = stats.getStdDeviation();// İşlem standardizasyon ise payda = standart sapma
        if(base == Double.NaN || base == Double.NEGATIVE_INFINITY || base == Double.POSITIVE_INFINITY)// Payda sıfırsa işleme devam edilemez!
            return null;
        Object[] processed = values.clone();
        for(int sayac = 0; sayac < values.length; sayac++){
            if(values[sayac] == null)// 'null' olan satırları atla
                continue;
            double upNumber;
            if(isNormalize)// İşlem normalizasyon ise pay = x - xmin
                upNumber = ((Double) values[sayac] - stats.getMin());
            else// İşlem standardizasyon ise pay = x - ortalama
                upNumber = ((Double) values[sayac] - stats.getMean());
            processed[sayac] = (Double) (upNumber / base);
        }
        return processed;
    }
    
    /*Alltaki fonksiyon kullanılabilir:
    public static<T> Object[] convertArrayPrimitiveToObjectValue(T[] values){
        Object[] res = new Object[values.length];
        for(int sayac = 0; sayac < res.length; sayac++){
            res[sayac] = values[sayac];
        }
        return res;
    }*/
    /*public static Object[][] wrapDataInMatrix(Object[][] data){
        
    }*/
    /*private static Object[] fillValue(Class typ, Object val, int length){
        
    }*/

//ERİŞİM YÖNTEMLERİ:
    
}