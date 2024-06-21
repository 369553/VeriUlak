package veriulak;

import Base.DataAnalyzer;
import Service.MatrixFunctions;
import Service.XlsXReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookType;

public class TestOrdered{
    Object[][] simpleData;
    Object[][] categoricalData;
    Object[][] dataForConverting;
    String tempLocation;

    public TestOrdered(){
        
    }

//TEST YÖNTEMLERİ:
    public void test1Basic(){
//        produceSimpleDataFile();
//        simpleData = getSimpleDataFromFile();
        Object[][] data = getSimpleData();
        DataAnalyzer anl = new DataAnalyzer(data);
        anl.setFirstRowAsColumnNames();
        double[] rates = anl.getEmptyRateOfCols();
        anl.autoAssignDataTypes();
        Class[] dTypes = anl.getDataTypes();
        int counterForMatched = 0;
        for(int sayac = 0; sayac < dTypes.length; sayac++){
//            System.out.println(sayac + " sütunu veri tipi : " + dTypes[sayac].getName());
            if(dTypes[sayac] != null)
                counterForMatched++;
        }
        if(counterForMatched == anl.getColumnCount())
            System.out.println("Veri tiplerinin tamâmı başarıyla tespit edildi");
        else
            System.err.println("Veri tiplerinin bir kısmının tespiti başarısız oldu");
        // Sütun bilgilerini al:
        HashMap<Integer, HashMap<String, Object>> mapColsDetail = anl.getColumnsDetail();
        /*HashMap<String, Object> mapSecondCol = mapColsDetail.get(1);
        System.out.println("\nİkinci sütun detay bilgileri:");
        for(String str : mapSecondCol.keySet()){
            System.out.println("key : " + str + "\t\tvalue : " + mapSecondCol.get(str));
        }*/
        data = anl.getData();
        MatrixFunctions.printMatrix(data);
    }
    public void test2DetectCategoricalColumns(){
        produceCategoricalDataFile();
        Object[][] data = getCategoricalDataFromFile();
        DataAnalyzer anl = new DataAnalyzer(data, true, true);
        MatrixFunctions.printMatrix(data);
        boolean[] areCouldCategorical = anl.getCouldCategorical();
//        short isnt = 0;
//        for(int sayac = 0; sayac < areCouldCategorical.length; sayac++){
//            if(areCouldCategorical[sayac]);
////                System.out.println(sayac + " indisli sütun kategorik veri barındırıyor olabilir");
//            else
//                isnt++;
//        }
//        if(isnt == anl.getColumnCount())
//            System.out.println("Verilerin hiçbiri kategorik görünmüyor");
        Object[][] uniq = anl.getUniqueAllColValues();
        System.out.println("Verideki münferid (tekil) değerler (her satır bir sütun):");
        MatrixFunctions.printMatrix(uniq);
    }
    public void test3ConvertOneHotVector(){
        
    }
    //ALT YÖNTEMLER:
    public Object[][] readXlsxData(String location, String fileNameWithExtension){
        Object[][] data = null;
        File fl = new File(location + fileNameWithExtension);
        XlsXReader reader = new XlsXReader(fl);
        if(!reader.readData()){
            System.err.println("Veri okunamadı : .readXlsxData()");
            return null;
        }
        data = reader.getData();
//        System.out.println("Excel dosyası başarıyla okundu");
//        System.out.println("Verideki satır sayısı : " + data.length);
        /*for(int sayac = 0; sayac < data.length; sayac++){
            System.out.println((sayac + 1) + ". satır verisi:");
            for(int s2 = 0; s2 < data[sayac].length; s2++){
                System.out.print(data[sayac][s2] + "\t");
            }
            System.out.println("\n");
        }*/
        if(data == null){
//            System.err.println("Veri okuma yöntemi düzgün çalışmıyor : .readXlsxData()");
            return null;
        }
        return data;
    }
    public boolean produceSimpleDataFile(){
        return produceFile(getSimpleData(), getTempLocation(), "simpleData.xlsx");
    }
    public boolean produceCategoricalDataFile(){
        return produceFile(getCategoricalData(), getTempLocation(), "categoricalData.xlsx");
    }
    /*public boolean produceLargeDataFile(){
        
    }*/
    //ARKAPLAN İŞLEM YÖNTEMLERİ:
    private boolean produceFile(Object[][] data, String location, String fileNameWithExtension){
        File target = new File(location + fileNameWithExtension);
        XSSFWorkbook book = null;
        FileOutputStream outStream = null;
        String message = "Geçici excel dosyası oluşturulamadı";
        try{
            boolean isSuccess = target.createNewFile();
            if(!isSuccess)
                throw new IOException(message);
//            System.out.println("Geçici dosya oluşturuldu : " + fileNameWithExtension);
            book = new XSSFWorkbook(XSSFWorkbookType.XLSX);
        }
        catch(IOException exc){
            System.err.println(message);
            return false;
        }
        
        XSSFSheet sheet = book.createSheet();
        //Tabloyu verilerle doldur:
        for(int sayac = 0; sayac < 3; sayac++){
            XSSFRow row = sheet.createRow(sayac);
            for(int s2 = 0; s2 < 5; s2++){
                if(sayac == 0){//İlk satırdaysa...
                    if(data[sayac][s2] != null)
                        row.createCell(s2, CellType.STRING).setCellValue("Sütun - " + (s2 + 1));
                }
                else{
                    if(data[sayac][s2] != null)
                        row.createCell(s2, CellType.NUMERIC).setCellValue((Double)data[sayac][s2]);
                }
            }
        }
        
        //Verileri dosyaya yaz:
        try{
            outStream = new FileOutputStream(target);
            book.write(outStream);
            outStream.close();
        }
        catch(IOException ex){
            System.err.println("Geçici dosyaya yazma işlemi başarısız! : .produceFile()");
            try{
                outStream.close();
            }
            catch(IOException closeEx){
                System.out.println("Yazma akış borusunu kapatırken hatâ alındı : " + closeEx.getLocalizedMessage() + " : .produceFile()");
                return false;
            }
//            target.delete();
            return false;
        }
            return true;
    }

//ERİŞİM YÖNTEMLERİ:
    public Object[][] getSimpleData(){
        if(simpleData == null){
            simpleData = new Object[][]{
//                {"Sütun - 1", "Sütun - 2", "Sütun - 3", "Sütun - 4", "Sütun - 5"},
                {2.0, 234.4, 1234.1, 355.5, 33.0},
                {34.3, 34.0, 32.2342, 563.33, 35.0},
                {235.9, 345.2, 2345.6, 345.5, 344.4}
            };
        }
        return simpleData;
    }
    public Object[][] getCategoricalData(){
        if(categoricalData == null){
            categoricalData = new Object[][]{
//                {"Sütun - 1", "Sütun - 2", "Sütun - 3", "Sütun - 4", "Sütun - 5"},
                {2.0, 2.2423, 2.0, 2.0, 2.2423},
                {32.5, 436.5, null, null, null},
                {245.2, 454.2, null, null, null},
                {true, false, true, true}
            };
        }
        return categoricalData;
    }
    public Object[][] getDataForConverting(){
        if(dataForConverting == null){
            Object[][] dataForConverting = {
                null
            };
        }
        return dataForConverting;
    }
    public String getTempLocation(){
        if(tempLocation == null){
            tempLocation = System.getProperty("java.io.tmpdir");
        }
        return tempLocation;
    }
    public Object[][] getSimpleDataFromFile(){
        return readXlsxData(getTempLocation(), "simpleData.xlsx");
    }
    public Object[][] getCategoricalDataFromFile(){
        return readXlsxData(getTempLocation(), "categoricalData.xlsx");
    }
    /*public Object[][] getLargeDataFromFile(){
        return readXlsxData(getTempLocation(), "largeData.xlsx");
    }*/
    
}