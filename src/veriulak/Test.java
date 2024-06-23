package veriulak;

import Base.CategoricalVariable;
import Base.DataAnalyzer;
import Service.CSVReader;
import Service.MatrixFunctions;
import Service.XlsXReader;
import View.PnlTable;
import View.PnlVariety;
import View.PnlVarietyForButton;
import View.PnlVarietyForOrderingTheList;
import View.PnlVarietyForText;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookType;

public class Test{

//TEST YÖNTEMLERİ:
    public void csvOkumaOrnegi(){
        //Veri:
        String csvData = "isim, soyIsim, dogumTarihi, aldigiKurum\n" +
"Ali, Karaahmetoğlu, 13.03.1994, YeşilYalı";
        
        //Geçici dosyayı oluştur:
        File csvFile = Service.RWService.getService().produceTempFile("csv");
        if(csvFile == null){
            System.err.println("Dosya oluşturma işlemi sırasında hatâ alındı; test süreci sonlandırılıyor.");
            return;
        }
        System.out.println("Geçici dosya ismi : " + csvFile.getAbsolutePath());
        
        String fileContent;
        //Veriyi geçici dosyaya yaz:
        if(!Service.RWService.getService().writeFile(csvFile, csvData)){
            System.err.println("Geçici dosyaya veri yazma sırasında hatâ alındı; dosya oluşturulmadan test ediliyor");
            fileContent = csvData;
        }
        else{
        //Geçici dosyadan veriyi oku:
            fileContent = Service.RWService.getService().readDataAsText(csvFile);
            //Geçici dosyayı program bitince silmesi talimâtını ver:
            csvFile.delete();
        }
        
        //CSV dosyasını ayrıştır:
        CSVReader csvR = new CSVReader(fileContent);
        
        //Veriyi kontrol et:
        Object[][] data = csvR.getData();
        int rowCount = data.length;
        System.out.println("veri.satır sayısı : " + rowCount);
        for(int sayac = 0; sayac < rowCount; sayac++){
            System.out.println(sayac + ". satırdaki sütun sayısı : " + data[sayac].length);
        }
    }
    public void xlsxOkumaOrnegi(){
        //Çalışma dizinini öğren:
        String pwd = System.getProperty("java.io.tmpdir");
        System.out.println("Geçici çalışma dizini : " + pwd);
        
        String message = "Geçici excel dosyası oluşturulamadı; test süreci sonlandırılıyor.";
        String fileName = UUID.randomUUID().toString().substring(0, 15) + ".xlsx";
        File target = new File(pwd + fileName);
        XSSFWorkbook book = null;
        FileOutputStream outStream = null;
        try{
            boolean isSuccess = target.createNewFile();
            if(!isSuccess)
                throw new IOException(message);
            System.out.println("Geçici dosya oluşturuldu : " + fileName);
            book = new XSSFWorkbook(XSSFWorkbookType.XLSX);
        }
        catch(IOException exc){
            System.err.println(message);
            return;
        }
        //Dosya verisi:
        double[][] exampleData = {
        {2.0, 234.4, 1234.1, 355.5, 33.0},
        {34.3, 34.0, 32.2342, 563.33, 35.0},
        {235.9, 345.2, 2345.6, 345.5, 344.4}
    };
        
        XSSFSheet sheet = book.createSheet();
        //Tabloyu verilerle doldur:
        for(int sayac = 0; sayac < 3; sayac++){
            XSSFRow row = sheet.createRow(sayac);
            for(int s2 = 0; s2 < 5; s2++){
                if(sayac == 0){//İlk satırdaysa...
                    row.createCell(s2, CellType.STRING).setCellValue("sütun - " + (s2 + 1));
                }
                else{
                    row.createCell(s2, CellType.NUMERIC).setCellValue(exampleData[sayac][s2]);
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
            System.err.println("Geçici dosyaya yazma işlemi başarısız!\nTest süreci sonlandırılıyor.");
            try{
                outStream.close();
            }
            catch(IOException closeEx){
                System.out.println("Yazma akış borusunu kapatırken hatâ alındı : " + closeEx.getLocalizedMessage());
                return;
            }
            target.delete();
            return;
        }
        
        //Dosyayı oku:
        XlsXReader xlsxReader = new XlsXReader(new File(pwd + "çalışmaDosyası.xlsx"));//target
        boolean isSuccess = xlsxReader.readData();
        if(!isSuccess){
            System.err.println("Excel dosyasını okuma işlemi başarısız!\nTest süreci sonlandırılıyor.");
            target.delete();
            return;
        }
        System.out.println("Excel dosyası başarıyla okundu");
        Object[][] data = xlsxReader.getData();
        System.out.println("Verideki satır sayısı : " + data.length);
        /*for(int sayac = 0; sayac < data.length; sayac++){
            System.out.println((sayac + 1) + ". satır verisi:");
            for(int s2 = 0; s2 < data[sayac].length; s2++){
                System.out.print(data[sayac][s2] + "\t");
            }
            System.out.println("\n");
        }*/
        target.delete();
    }
    public void xlsxDenemeler(){
        //Boş hücrelerdeki verileri hatâ almadan 'null' olarak alabiliyor muyuz?
        String pwd = System.getProperty("java.io.tmpdir");
        XSSFWorkbook book = null;
        File target;
        target = new File(pwd + "2024DERS.xlsx");
        XlsXReader reader = new XlsXReader(target);
        boolean isSuccess = reader.readData();
        if(!isSuccess){
            System.out.println("Okuma işlemi başarısız, sonlandırılıyor..");
        }
        Object[][] data = reader.getData();
        boolean[][] isNull = new boolean[data.length][];
        int rowCount = data.length;
        System.out.println("Veri satır sayısı : " + rowCount);
        System.out.println("Veri.ilkSatır sütun sayısı : " + data[0].length);
        
        //Mod değiştirildikten sonra 'null' değerli verilerin boş metne dönmesi gerekiyor; test edelim:
        reader.setMode(XlsXReader.ReadMode.EmptyCellAsEmptyString);
        data = reader.getData();
        for(int sayac = 0; sayac < data.length; sayac++){
            if(data[sayac] == null){
                System.err.println("Okuma modu(XlsXReader.ReadMode) uygulanması hatâsı:\nHâlen 'null' değer var!");
                break;
            }
            for(int s2 = 0; s2 < data[sayac].length; s2++){
                if(data[sayac][s2] == null){
                    System.err.println("Okuma modu(XlsXReader.ReadMode) uygulanması hatâsı:\nHâlen 'null' değer var!");
                    break;
                }
            }
        }
    }
    public void divideMatrix(){//380911ed-2fee-4.xlsx : 'xlsxOkumaOrnegi()' yönteminde oluşturulan dosyaya ihtiyaç var.
        String pwd = System.getProperty("java.io.tmpdir");
        Object[][] data = null;
        XlsXReader re = new XlsXReader(new File(pwd + "380911ed-2fee-4.xlsx"));
        re.readData();
        data = re.getData();
        Object[][] nData = MatrixFunctions.divideMatrixOnColumn(data, 2, 4);
        MatrixFunctions.printMatrix(nData);
    }
    public void sortTest(boolean isAscending){
        int[] values = new int[]{
            28,
            22,
            443,
            24,
            25,
            345,
            894,
            25,
            13522,
            26,
            2,
            1
        };
        int[] sorted = MatrixFunctions.sort(values, isAscending);
        if(isAscending)
            System.out.println("Küçükten büyüğe:\n");
        else
            System.out.println("Büyükten küçüğe:\n");
        for(int sayac = 0; sayac < sorted.length; sayac++){
            System.out.println("[" + sayac + "] : " + sorted[sayac]);
        }
    }
    public void deleteSelectedRows(){
        Double[][] numData = new Double[][]{
            {1.0, 2.0, 3.0, 4.0, 5.0},
            {34.3, 34.0, 32.2342, 563.33, 35.0},
            {235.9, 345.2, 2345.6, 345.5, 344.4}
        };
        MatrixFunctions.printMatrixWithTitle(numData, "Ham veri");
        int[] rowIndexes = new int[]{0, 2};
        double[][] deleted = new double[numData.length - rowIndexes.length][];
        Object[][] value = MatrixFunctions.deleteSelectedRows(numData, rowIndexes);
        for(int sayac = 0; sayac < value.length; sayac++){
            deleted[sayac] = new double[value[sayac].length];
            for(int s2 = 0; s2 < value[sayac].length; s2++){
                deleted[sayac][s2] = (Double) value[sayac][s2];
            }
        }
        System.out.println("--------------------------------------------------");
        MatrixFunctions.printMatrixWithTitle(value, "Kırpılmış veri (0. ve 2. indisli satırlar çıkarıldı):");
    }
    public void invokeMethodWithManyArgsAsList(){
        int[] array = new int[]{2, 245, 45, 11, 7345};
        int startIndex = 2;
        int endIndex = 4;
        //Object[] params = {array, startIndex, endIndex};
        Object[] params = new Object[3];
        Object result = null;
        params[0] = array;
        params[1] = startIndex;
        params[2] = endIndex;
        /*
        for(Method s : Test.class.getDeclaredMethods()){
            if(s.getName().equals("takeSubArray")){
                for(Parameter par : s.getParameters()){
                    System.out.println("Parametre ismi : " + par.getName());
                    System.out.println("Parametre tipi : " + par.getType());
                    System.out.println("Parametre sınıfı : " + par.getClass());
                    System.out.println("Parametre.annotatedType.toString : " + par.getAnnotatedType().toString());
                    System.err.println("***********************");
                }
            }
        }*/
        Class[] typeOfParams = new Class[]{int[].class, int.class, int.class};
        Method method = null;
        try{
            method = Test.class.getDeclaredMethod("takeSubArray", typeOfParams);
        }
        catch(NoSuchMethodException ex){
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SecurityException ex){
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(method != null){
            System.out.println("Yöntem alındı");
            try{
                result = method.invoke(new Test(), array, startIndex, endIndex);
            }
            catch(IllegalAccessException ex){
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch(IllegalArgumentException ex){
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
            }catch(InvocationTargetException ex){
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(result != null){
                System.out.println("Yöntem başarıyla çalıştırılmış ve sonuç değeri alınmış : " + result.toString());
            }
            System.out.println("Parametreler tek tek verilerek yöntem başarıyla çalıştırıldı");
            System.out.println("Şimdi, parametreleri bir dizi olarak vermeye çalışalım.");
            System.out.println("\n\n*******************************\n\n");
            Object rs2 = null;
            try{
                rs2 = method.invoke(new Test(), params);
            }
            catch(IllegalAccessException ex){
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (IllegalArgumentException ex){
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (InvocationTargetException ex){
                Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(rs2 != null){
                System.out.println("Yöntem bu şekilde de çalıştı; kuvvetle muhtemel girdilerin veri tiplerine bakılarak bunun üstesinden gelinmiş");
            }
            else{
                System.err.println("Yöntem bu şekilde çalışmadı; girdiler bir dizi şeklinde verilemiyormuş");
            }
        }
    }
    public void testIsNumericalCellIfCellIsEmpty(){
        String pwd = System.getProperty("java.io.tmpdir");
        XlsXReader reader = new XlsXReader(new File(pwd + "emptyNumericalCells.xlsx"));
        boolean isSuccess = reader.readData();
        if(!isSuccess){
            System.err.println("Dosya okunamadı!\nTest süreci sonlandırılıyor.");
            return;
        }
        Object[][] data = reader.getData();
        if(data[3][1] == null)
            System.out.println("Hücre değeri = null");
        MatrixFunctions.printMatrix(data);
        return;
    /*
        Class[][] types = new Class[data.length][];
        for(int sayac = 0; sayac < data.length; sayac++){
            types[sayac] = new Class[data[sayac].length];
            for(int s2 = 0; s2 < data[sayac].length; s2++){
                Class csl = data[sayac][s2].getClass();
                types[sayac][s2] = csl;
            }
        }
        MatrixFunctions.printMatrixWithTitle(types, "Hücrelerin veri tipleri");
    */
    }
    public void testPnlTable(){
        Object[][] data = null;
        String pwd = System.getProperty("java.io.tmpdir");
        File fl = new File(pwd + "exampleData.xlsx");
        XlsXReader reader = new XlsXReader(fl);
        if(!reader.readData()){
            System.err.println("Veri okunamadığından test sonlandırılıyor..");
            return;
        }
        data = reader.getData();
        if(data == null){
            System.err.println("Veri okuma yöntemi düzgün çalışmıyor\nTest süreci sonlandırılıyor");
            return;
        }
        DataAnalyzer anl = new DataAnalyzer(data);
        anl.setFirstRowAsColumnNames();
        anl.detectDataTypes(true);
        anl.autoAssignDataTypes();
        data = anl.getData();
        String[] colNames = anl.getColumnNames();
        PnlTable tbl = new PnlTable(data, colNames);
        tbl.setPreferredSize(new Dimension(760, 460));
        JFrame fr = new JFrame("TEST : PnlTable");
        fr.setPreferredSize(new Dimension(890, 350));
        fr.setLayout(new BorderLayout(5, 5));
        fr.add(tbl, BorderLayout.CENTER);
        fr.setVisible(true);
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public void exampleAnalysis(){
        Object[][] data = null;
        String pwd = System.getProperty("java.io.tmpdir");
        File fl = new File(pwd + "exampleData.xlsx");
        XlsXReader reader = new XlsXReader(fl);
        if(!reader.readData()){
            System.err.println("Veri okunamadığından test sonlandırılıyor..");
            return;
        }
        data = reader.getData();
        if(data == null){
            System.err.println("Veri okuma yöntemi düzgün çalışmıyor\nTest süreci sonlandırılıyor");
            return;
        }
        DataAnalyzer anl = new DataAnalyzer(data);
        double[] rates = anl.getEmptyRateOfCols();
        /*for(int sayac = 0; sayac < rates.length; sayac++){
            System.out.println("[" + sayac + "] sütunu boş veri oranı : " + rates[sayac]);
        }
        double[] ratesOnRows = anl.getEmptyRateOfRows();
        for(int sayac = 0; sayac < ratesOnRows.length; sayac++){
            System.out.println("[" + sayac + "] satırı boş veri oranı : " + ratesOnRows[sayac]);
        }*/
        anl.setFirstRowAsColumnNames();
        //MatrixFunctions.printMatrix(anl.getData());
        //System.out.println("3. satırın uzunluğu : " + anl.getData()[2].length);
        //anl.detectDataTypes(true);
        //Veri tiplerinin atanması:
        /*
        Class[] dTypes = new Class[]{Double.class, Double.class, Double.class, Double.class};
        boolean isSuccess = anl.setColumnDataTypes(dTypes);
        if(isSuccess)
            System.out.println("Veri tipleri başarıyla atandı");
        else
            System.err.println("Verilen veri tiplerinin ataması başarısız");
        */
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
        anl.detectCategoricalData();
        /*boolean[] areCouldCategorical = anl.getCouldCategorical();
        for(int sayac = 0; sayac < areCouldCategorical.length; sayac++){
            System.out.println(sayac + ". indeksli sütunun verisi etiketli veri olabilir mi : " + areCouldCategorical[sayac]);
        }*/
        anl.deleteCols(new int[]{3, 0});
//        data = anl.getData();
//        MatrixFunctions.printMatrix(data);
//        anl.writeAllInfoForTest();
        anl.reRunFindEmptyRowsAndCols();
        anl.reRunCalculateEmptyRates();
        data = anl.getData();
        MatrixFunctions.printMatrix(data);
        
    }
    public void deleteSelectedMembers(){
        String[] colNames = {"A", "B", "C", "Ç", "D", "E", "F"};
        System.out.print("\n");
        for(int sayac = 0; sayac < colNames.length; sayac++){
            System.out.print(colNames[sayac] + "\t");
        }
        System.out.println("\nSilinmesi istenen elemanların indeks numaraları:");
        int[] delIndexes = {0, 4, 3};
        System.out.print("\n");
        for(int sayac = 0; sayac < delIndexes.length; sayac++){
            System.out.print(delIndexes[sayac] + "\t");
        }
        System.err.println("\n::::::::::::::::::::::::::....");
        Object[] result = MatrixFunctions.deleteSelectedMembers(colNames, delIndexes, String[].class);
        System.out.println("\nVeriler silindikten sonraki hâli:\n");
        System.out.print("\n");
        for(int sayac = 0; sayac < result.length; sayac++){
            System.out.print(result[sayac] + "\t");
        }
    }
    public void deleteSelectedMembers2(){
        String[] dd = new String[]{""};
        Double[] dsdd = {2.2, 234.3, 35.4};
        int[] delIndexes = {0, 1};
        dsdd = MatrixFunctions.deleteSelectedMembers(dsdd, delIndexes, Double[].class);
    }
    public void deleteSelectedCols(){
        Object[][] data = new Object[][]{
            {23.4, 24.5, 23.5, 23.7},
            {36.2, 36.7, 38.3, 34.1},
            {6.4, 6.1, 6.9, 6.2},
            {1.6, 1.7, 1.8, 1.2}
        };
        System.out.println("VERİ ASIL HÂLİ");
        MatrixFunctions.printMatrix(data);
        
        data = MatrixFunctions.deleteSelectedCols(data, new int[]{3, 1});
        System.err.println(":::::::::::::::::::::::");
        System.out.println("2 ve 3 indeksli sütunlar silindikten sonra:");
        MatrixFunctions.printMatrix(data);
    }
    public void testDeleteSelectedMembersFunctionFromMatrixFunc(){
        double[] array = {2.3, 235.3, 465.3, 23.4, 67.3, 86.3};
        int[] indexesToDelete = new int[]{3, 1, 4};// Sil : 235.3, 23.4, 67.3
        MatrixFunctions.printVectorWithPrimitiveDoubleType(array, true);
        for(int i : indexesToDelete){
            System.out.println("\nSilinmesi gereken indeks : " + i);
        }
        double[] result = MatrixFunctions.deleteSelectedMembers(array, indexesToDelete);// SONUÇ : 2.3	465.3	86.3
        System.err.println("******");
        System.out.println("Sonuç dizisi:");
        MatrixFunctions.printVectorWithPrimitiveDoubleType(result, true);
    }
    public void testCanWrapperClassAsPrimitive(){
        Integer i = new Integer(3);
        System.out.println("sınıf ismi : " + i.getClass().getName());
        if(i.getClass().getName().equals(int.class.getName())){
            System.out.println("Integer sınıfı int.class olarak da algılanabiliyor");
        }
        
        int f = 55;
        Object obj = f;
        Class cls = obj.getClass();
        System.out.println("Object sargısında olan int değişkeninin sınıfı : " + cls.getName());
        if(cls.equals(int.class)){
            System.err.println("Sargı yapıldı, ama yine de int olarak algılanıyor");
        }
        if(cls.equals(Integer.class)){
            System.out.println("Sargı yapıldığından Integer olarak algılanıyor");
        }
    }
    public void canIntegerDetectDoubleValueNormally(){
        double value = 3.0;
        if(value - Math.ceil(value) == 0)
            System.out.println("Bu, tam sayıya dönüştürülebilir");
        Object objDoubleValue = value;
        System.out.println("value(double) : " + value);
        String strValue = String.valueOf(objDoubleValue);
        System.out.println("strValue (String) : " + strValue);
        String strValueWithoutDot = strValue.replace('.', ',');
        System.out.println("strValueWithoutDot (String) : " + strValueWithoutDot);
        String errorMessage = "Şu veriden Integer'a dönüşüm başarısız : ";
        Integer num, num2;
        try{
            num = Integer.valueOf(strValue.trim());
            if(num != null){
                System.out.println("strValue'dan dönüştürüldü : " + num + "(int)");
            }
            else
                System.err.println(errorMessage + strValue);
        }
        catch(NumberFormatException exc){
            System.err.println(errorMessage + strValue + " (" + exc.toString() + ")");
        }
        try{
            num2 = Integer.valueOf(strValueWithoutDot.trim());
            if(num2 != null){
                System.out.println("strValueWithoutDot'dan dönüştürüldü : " + num2 + "(int)");
            }
            else
                System.err.println(errorMessage + strValueWithoutDot);
        }
        catch(NumberFormatException exc){
            System.err.println(errorMessage + strValueWithoutDot + " (" + exc.toString() + ")");
        }
    }
    public void canDoubleDetectIntegerValueNormally(){
        int value = 3;
        Object objDoubleValue = value;
        System.out.println("value(int) : " + value);
        String strValue = String.valueOf(objDoubleValue);
        System.out.println("strValue (String) : " + strValue);
        Double num = Double.valueOf(strValue);
        if(num != null){
            System.out.println("int tipindeki bir değişken Double.valueOf() ile noktalı sayıya dönüştürülebiliyor");
            System.out.println("strValue'dan dönüştürüldü : " + num + "(double)");
        }
    }
    public void controllingObjectValuesForDetectUnique(){
        int val01 = 22;
        String val02 = "22";
        String val03 = "yirmi iki";
        Integer val04 = 22;
        int val05 = 22;
        Object[] vals = new Object[]{val01, val02, val03, val04, val05};
        ArrayList<Object> uniqVals = new ArrayList<Object>();
        for(int sayac = 0; sayac < vals.length; sayac++){
            boolean isNew = true;
            for(Object objVal : uniqVals){
                if(vals[sayac] == objVal){// Bu kontrol aynı nesne olup, olmadıklarını mı kontrol ediyor, yoksa nesnelerin aynı değerde olup, olmadıklarını mı?
                    //Cevâp : nesnelerin değerleri kontrol ediliyor.
                    isNew = false;
                    break;
                }
            }
            if(isNew)
                uniqVals.add(vals[sayac]);
        }
        Object[] arrayUniq = new Object[uniqVals.size()];
        uniqVals.toArray(arrayUniq);
        System.out.println("Münferid veri sayısı : " + arrayUniq.length);
        for(int sayac = 0; sayac < arrayUniq.length; sayac++){
            System.out.println("Münferid değer - " + sayac + " : " + arrayUniq[sayac]);
        }
    }
    public void exampleCouldCategorical(){
        Object[][] data = null;
        String pwd = System.getProperty("java.io.tmpdir");
        File fl = new File(pwd + "exampleCategorical.xlsx");
        XlsXReader reader = new XlsXReader(fl);
        if(!reader.readData()){
            System.err.println("Veri okunamadığından test sonlandırılıyor..");
            return;
        }
        data = reader.getData();
        if(data == null){
            System.err.println("Veri okuma yöntemi düzgün çalışmıyor\nTest süreci sonlandırılıyor");
            return;
        }
        DataAnalyzer anl = new DataAnalyzer(data, true, true);
        anl.autoAssignDataTypes();
        
        
//        Class[] dTypes = anl.getDataTypes();
//        for(int sayac = 0; sayac < dTypes.length; sayac++){
//            System.out.println(sayac + " indisli sütunun veri tipi : " + dTypes[sayac].getName());
//        }
        anl.detectCategoricalData();
        boolean[] couldCategorical = anl.getCouldCategorical();
//        short isnt = 0;
//        for(int sayac = 0; sayac < couldCategorical.length; sayac++){
//            if(couldCategorical[sayac])
//                System.out.println(sayac + " indisli sütun kategorik veri barındırıyor olabilir");
//            else
//                isnt++;
//        }
//        if(isnt == anl.getColumnCount())
//            System.out.println("Verilerin hiçbiri kategorik görünmüyor");
//        HashMap<Integer, HashMap<String, Object>> details = anl.getColumnsDetail();
//        for(String key : details.get(4).keySet()){
//            System.out.println("key : \t" + key + "\t\t==> " + details.get(4).get(key));
//        }
//        System.out.println("'4' indeks numarasına sâhip sütun kategoriye dönüştürülüyor");
        boolean isSuccess = anl.setColumnAsCategoricalWithBinomialEncode(4, true);
        if(isSuccess){
            data = anl.getData();
//            System.out.println("İşlem başarılı");
//            MatrixFunctions.printMatrix(data);
//        MatrixFunctions.printVector(anl.getDataTypes(), false);
        }
        else{
            System.err.println("İşlem başarısız");
            System.err.println("Veri son hâli:\n");
            MatrixFunctions.printMatrix(data);
        }
        /*
            addNewMember yöntemi test:
                String nS = "Yeni sütun";
                String[] strNew = MatrixFunctions.addNewMember(anl.getColumnNames(), nS, String[].class, 4, true);
                for(int sayac = 0; sayac < strNew.length; sayac++){
                    System.out.println("sayac[" + sayac + "] = " + strNew[sayac]);
                }
                return;
        */
//        MatrixFunctions.printVector(anl.getDataTypes(), false);
        boolean isSuccessful = anl.setColumnAsCategoricalWithOneHotVectorEncode(0, true);
        if(isSuccessful){
                    MatrixFunctions.printVector(anl.getDataTypes(), false);

//            System.out.println("İşlem başarılı");
//            MatrixFunctions.printMatrix(anl.getData());
//            MatrixFunctions.printVector(anl.getColumnNames(), false);
        }
    }
    public void exampleCouldCategorical2(boolean runChangeColumnDataTypesTest){
        String pwd = System.getProperty("java.io.tmpdir");
        File file = new File(pwd + "exampleCategorical - 2.xlsx");
        XlsXReader rDr = new XlsXReader(file);
        boolean isSuc = rDr.readData();
        if(!isSuc)
            System.out.println("Veri okunamadı");
        Object[][] data = rDr.getData();
        if(data == null)
            System.out.println("Veri okunamadı");
        
        DataAnalyzer anl = new DataAnalyzer(data, true, true);
        data = anl.getData();
        anl.detectCategoricalData();
        boolean[] couldCategorical = anl.getCouldCategorical();
        short isnt = 0;
        for(int sayac = 0; sayac < couldCategorical.length; sayac++){
//            System.err.println("couldCategorical[" + sayac + "] = " + couldCategorical[sayac]);
            if(couldCategorical[sayac])
                System.out.println(sayac + " indisli sütun ayrık veri barındırıyor olabilir");
            else
                isnt++;
        }
        if(isnt == couldCategorical.length)
            System.err.println("Veriler ayrık görünmüyor");
//        MatrixFunctions.printMatrix(data);
        // 2 indisli sütunu sırasal ayrık veriye çevirelim:
        anl.setColumnAsCategoricalWithOrdinalEncode(2, new String[]{"Temel seviye", "İleri seviye", "Üst ileri seviye"});
        if(!runChangeColumnDataTypesTest)
            return;
        changeColumnDataTypes(anl);
    }
    public void testDetectingInteger(){
        String filePath = "C:\\Users\\Yazılım alanı\\Documents\\example - 2 data.xlsx";
        File fl = new File(filePath);
        XlsXReader reader = new XlsXReader(fl);
        reader.readData();
        Object[][] data = reader.getData();
        if(data == null)
            System.err.println("Veri okunamadı");
        /*for(int sayac = 0; sayac < data.length; sayac++){
            for(int s2 = 0; s2 < data[0].length; s2++){
                System.out.println("data[" + sayac + "][" + s2 + "] = " + data[sayac][s2]);
            }
        }*/
        DataAnalyzer anl = new DataAnalyzer(data, true, true);
        for(Class cls : anl.getDataTypes()){
            System.out.println("cls.name : " + cls.getName());
        }
    }
    public void exampleEmptyData(){
        String pwd = System.getProperty("java.io.tmpdir");
        File file = new File(pwd + "exampleSomeEmptyData.xlsx");
        
        XlsXReader rDr = new XlsXReader(file);
        boolean isSuc = rDr.readData();
        if(!isSuc)
            System.out.println("Veri okunamadı");
        Object[][] data = rDr.getData();
        if(data == null)
            System.out.println("Veri okunamadı");
        DataAnalyzer anl = new DataAnalyzer(data, true, false);
        data = anl.getData();
        anl.setFirstRowAsColumnNames();
//        for(int sayac = 0; sayac < anl.getRowCount(); sayac++){
//            for(int s2 = 0; s2 < anl.getColumnCount(); s2++){
//                if(data[sayac][s2] == null)
//                    System.err.println("null tespit edildi");
//            }
//        }
        
        //anl.getColumnsDetail();
        double[] ratesRow = anl.getEmptyRateOfRows();
        double[] ratesCol = anl.getEmptyRateOfCols();
        Double[] ratesRowAsD = new Double[ratesRow.length];
        Double[] ratesColAsD = new Double[ratesCol.length];
        for(int sayac = 0; sayac < ratesRow.length; sayac++){
            ratesRowAsD[sayac] = ratesRow[sayac];
        }
        for(int sayac = 0; sayac < ratesCol.length; sayac++){
            ratesColAsD[sayac] = ratesCol[sayac];
        }
        MatrixFunctions.printVector(ratesColAsD, false);
    }
    public void changeColumnDataTypes(DataAnalyzer anl){
        Class[] dTypes = anl.getDataTypes();
        // BURADA KALINDI, setColumnDataTypes ile convColumnDataType uyumlu olmalı; boolean çevrilirken
        // Veri dönüştürme noktasında birbirlerini tetiklemeleri gerekiyorsa bunu yap
//        anl.setColumnDataTypes(new Class[]{String.class, String.class, Boolean.class});
        anl.changeColumnDataType(2, Double.class, null);
        for(int sayac = 0; sayac < dTypes.length; sayac++){
            System.out.println("Sütun[" + sayac + "] veri tipi : " + dTypes[sayac].getName());
        }
        System.out.println("3. sütun veri tipi : " + anl.getData()[0][2].getClass().getName());
        MatrixFunctions.printMatrix(anl.getData());
        MatrixFunctions.printVector(dTypes, false);
        
//        try{
//            clear();
//        }
//        catch(AWTException ex){
//            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    public DataAnalyzer readAndStartAnalyzerForExampleData(){
        return readAndStartAnalyzerFor("exampleDataForConversion.xlsx");
    }
    public DataAnalyzer readAndStartAnalyzerFor(String fileName){
        String dir = "C:\\Users\\Yazılım alanı\\Documents";
        File fl = new File(dir, fileName);
        if(fl == null)
            System.out.println("Dosya okunamadı");
        Object[][] data = null;
        XlsXReader rDr = new XlsXReader(fl);
        rDr.readData();
        data = rDr.getData();
        if(data == null)
            System.out.println("Veri okunamadı");
        DataAnalyzer anl = new DataAnalyzer(data, true, true);
        return anl;
    }
    public void testConversions(){
        DataAnalyzer anl = readAndStartAnalyzerForExampleData();
//        MatrixFunctions.printVector(anl.getDataTypes(), true);// Veri tiplerini yazdır
//        convertColumnDataType(anl, 0, String.class);// Double'dan String'e dönüşüm


//        convertColumnDataType(anl, 1, String.class);// Double'dan String'e dönüşüm
//        convertColumnDataType(anl, 1, Double.class);// String'den Double'a dönüşüm
        
        
//        MatrixFunctions.printVector(anl.getDataTypes(), true);// Veri tiplerini yazdır

//        convertColumnDataType(anl, 2, String.class);
//        convertColumnDataType(anl, 2, Integer.class);
//        convertColumnDataType(anl, 2, String.class);
//        convertColumnDataType(anl, 2, Double.class);
//        convertColumnDataType(anl, 2, Integer.class);


//        convertColumnDataType(anl, 3, String.class);
//        convertColumnDataType(anl, 3, Boolean.class);
        
        
//        convertColumnDataType(anl, 2, Double.class);
    }
    public boolean convertColumnDataType(DataAnalyzer anl, int colNumber, Class targetType){
        String current = returnDataTypeNameAsShort(anl.getData()[0][colNumber]);
        String target = null;
        anl.changeColumnDataType(colNumber, targetType, null);
        if(anl.getDataTypes()[colNumber] == targetType){
//            System.out.println("İlk aşama aşıldı");
            if(anl.getData()[0][colNumber].getClass() == targetType){
                target = returnDataTypeNameAsShort(anl.getData()[0][colNumber]);
                System.out.println("Dönüşüm işlemi başarılı : " + current + " -> " + target);
                return true;
            }
        }
        System.err.println("Başarısız!\tMevcut veri tipi : " + anl.getData()[0][colNumber].getClass());
        return false;
    }
    public String returnDataTypeNameAsShort(Object data){
        String[] splitted = data.getClass().getName().split("\\.");
        return splitted[splitted.length - 1];
    }
    public void testCalculatingUniqueValues(){
        Object[] ddd = new Object[]{
            2,
            2.2423,
            2,
            2,
            2.2423
        };
        Object[] res = calculateUniqueValues(ddd);
        for(int sayac = 0; sayac < res.length; sayac++){
            System.out.println("Münferid - " + sayac + " : " + res[sayac]);
        }
    }
    public void testShiftArrayToLeftFunction(){
        Integer[] dizi = new Integer[]{2, 345, 13, 55, 66, 115, 234, 525};
        dizi = MatrixFunctions.shiftArrayToLeft(dizi, 2, Integer[].class);
        for(int sayac = 0; sayac < dizi.length; sayac++){
            System.out.println("dizi[" + sayac + "] = " + dizi[sayac]);
        }
    }
    public static void testFireScrollBarButton(){
        JFrame fr = new JFrame("Deneme");
        fr.setPreferredSize(new Dimension(700, 200));
        fr.setSize(700, 200);
        fr.setLayout(new BorderLayout(5, 5));
        JPanel pnl = new JPanel();
        pnl.setPreferredSize(new Dimension(550, 160));

        JScrollPane scrpane = new JScrollPane(pnl, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        fr.add(scrpane, BorderLayout.CENTER);
        JButton btnSlide = new JButton("Kaydır");
        pnl.add(btnSlide);
        for(int sayac = 0; sayac < 5; sayac++){
            pnl.add(new JButton("Düğme - " + sayac));
        }
        JButton btnSlideBack = new JButton("Başa dön");
        pnl.add(btnSlideBack);
        btnSlideBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                scrpane.getHorizontalScrollBar().setValue(-1);
            }
        });
        fr.setVisible(true);
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        scrpane.getHorizontalScrollBar().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                System.out.println("Eylem bilgileri:\n" + evt.toString());
            }
        });
        for(PropertyChangeListener prpListener : scrpane.getHorizontalScrollBar().getPropertyChangeListeners()){
            System.out.println("Özellik değişim dinleyicisi : " + prpListener.toString());
        }
        btnSlide.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                int val =  scrpane.getHorizontalScrollBar().getValue();
                val += 370;
                if(val >= scrpane.getWidth())
                    val = scrpane.getWidth();
                scrpane.getHorizontalScrollBar().setValue(val);
//                    scrpane.getHorizontalScrollBar().setValue(46);
                fr.setVisible(true);
            }
        });
    }
    public static void testJPopupMenu(){
        JFrame fr = new JFrame("Deneme");
        fr.setPreferredSize(new Dimension(700, 200));
        fr.setSize(700, 200);
        fr.setLayout(new BorderLayout(50, 50));
        JPanel pnl = new JPanel(new BorderLayout(40, 40));
        JPopupMenu popMenu = new JPopupMenu("Sütun sil");
        JMenuItem mm = new JMenuItem("Sütun sil");
        mm.setName("AAAA");
        mm.setArmed(true);
        popMenu.add(mm);
        popMenu.add(new JMenuItem("Sütun yerini değiştir"));
        JButton btn = new JButton("İşlem yap");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
//                popMenu.setPopupSize(100, 30);
                popMenu.setVisible(true);
//                popMenu.setInvoker(btn);
                popMenu.show(btn, 20, 20);
                for(Component cmp : popMenu.getComponents()){
                    System.out.println("cmp.name . " + cmp.getClass().getName());
                    JMenuItem mn = (JMenuItem) cmp;
                    mn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            JMenuItem mn = (JMenuItem) e.getSource();
                            System.out.println("mn.getName : " + mn.getName());
                        }
                    });
                }
            }
        });
        popMenu.addMenuKeyListener(new MenuKeyListener() {
            @Override
            public void menuKeyTyped(MenuKeyEvent e){
                System.out.println("ttttt");
                if(e.getSource() == popMenu)
                    System.out.println("menuKeyTyped");
            }

            @Override
            public void menuKeyPressed(MenuKeyEvent e){
                System.err.println("sss");
                if(e.getSource() == popMenu)
                    System.out.println("menuKeyPressed");
            }

            @Override
            public void menuKeyReleased(MenuKeyEvent e){
                System.err.println("aaa");
                if(e.getSource() == popMenu)
                    System.out.println("menuKeyReleased");
            }
        });
        pnl.add(btn, BorderLayout.CENTER);
        fr.add(pnl, BorderLayout.CENTER);
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fr.setVisible(true);
    }
    public static void testPnlVariety(){
        JFrame fr = new JFrame("Deneme");
        fr.setPreferredSize(new Dimension(700, 200));
        fr.setSize(700, 200);
        fr.setLayout(new BorderLayout(5, 5));
        ArrayList<String> list = new ArrayList<String>();
        list.add("Yazı - 1");
        list.add("YAzı YAzı -YAzı -YAzı -YAzı -YAzı -YAzı -YAzı -YAzı -YAzı -YAzı -- 2");
        list.add("fwef");
        list.add("effefe");
        PnlVariety pnl = new PnlVariety(list, "Başlık");
        pnl.setPreferredSize(new Dimension(220, 330));
        fr.add(pnl, BorderLayout.CENTER);
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fr.setVisible(true);
    }
    public static void testPnlVarietyForButton(){
        JFrame fr = new JFrame("Deneme");
        fr.setPreferredSize(new Dimension(700, 200));
        fr.setSize(700, 200);
        fr.setLayout(new BorderLayout(5, 5));
        String[] names = new String[]{
            "Yazı - 1",
            "YAzı YAzı -YAzı -YAzı -YAzı -YAzı -YAzı -YAzı -YAzı -YAzı -YAzı -- 2",
            "fwef",
            "effefe"
        };
        PnlVarietyForButton pnl = new PnlVarietyForButton(4, names, null, "Bismillâh");
        pnl.setPreferredSize(new Dimension(220, 330));
        fr.add(pnl, BorderLayout.CENTER);
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fr.setVisible(true);
    }
    public void testPnlVarietyForOrderingTheList(){
        JFrame fr = new JFrame("Deneme");
        fr.setPreferredSize(new Dimension(700, 200));
        fr.setSize(700, 200);
        fr.setLayout(new BorderLayout(5, 5));
        ArrayList<String> list = new ArrayList<String>();
        list.add("Yazı - 1");
        list.add("YAzı YAzı -YAzı -YAzı -YAzı -YAzı -YAzı -YAzı -YAzı -YAzı -YAzı -- 2");
        list.add("fwef");
        list.add("effefe");
        PnlVarietyForOrderingTheList pnl = new PnlVarietyForOrderingTheList(list, "Başlık");
        pnl.setPreferredSize(new Dimension(220, 330));
        fr.add(pnl, BorderLayout.CENTER);
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fr.setVisible(true);
    }
    public void fillEmptyCells(){
        /*
        TEST POLİTİKASI:
            Birinci sütun : Özel değerle doldur
            İkinci sütun : En çok tekrar eden değerle doldur
            Üçüncü sütun : Boş String ile doldur
            Dördüncü sütun : En büyük sayı ile doldur
            Beşinci sütun : Ortalama değerle doldur
            Altıncı sütun : En çok tekrar eden değerle doldur
            Yedinci sütun : 'null' ifâdesiyle ile doldur (String)
            Sekizinci sütun : 'false' ile doldur
        EK TEST (9. test):
            Üçüncü sütun : 'null' ile doldur (String olarak değil)
        EK TEST (10. test):
            Birinci sütun : 'null' ile doldur (String olarak değil)
        */
        /*
        VERİ:
            [2.0] [true] [Anahtar] [1] [1992.2] [os] [query] [true] 
            [34.44] [false] [os] [2] [1992.2] [os] [alg] [null] 
            [235.56] [true] [İn-out] [3] [2002.3] [os] [dir] [true] 
            [233.1] [null] [env] [null] [2023.0] [lg] [libs] [null] 
            [null] [true] [null] [5] [null] [null] [null] [true] 
            [null] [true] [opt] [6] [2022.0] [final] [null] [true] 
            [22.22] [true] [final] [7] [null] [final] [true] [true] 
        */
        DataAnalyzer anl = readAndStartAnalyzerFor("example - forFillEmptyCells.xlsx");
//        MatrixFunctions.printMatrix(anl.getData());// Veriyi yazdır
        
        int testNo = 10;
        switch(testNo){
            case 1 :{// TEST - 1:
                anl.fillEmptyCells(0, 1.1);
                MatrixFunctions.printVector(anl.getColumnValues(0), false);
                break;
            }
            case 2 :{// TEST - 2:
                anl.fillEmptyCellsByModValue(1);
                MatrixFunctions.printVector(anl.getColumnValues(1), false);
                break;
            }
            case 3 :{// TEST - 3:
                anl.fillEmptyCellsByEmptyString(2);
                MatrixFunctions.printVector(anl.getColumnValues(2), false);
                break;
            }
            case 4 :{// TEST - 4:
                anl.fillEmptyCellsByMax(3);
                MatrixFunctions.printVector(anl.getColumnValues(3), false);
                break;
            }
            case 5 :{// TEST - 5:
                anl.fillEmptyCellsByMean(4);
                MatrixFunctions.printVector(anl.getColumnValues(4), false);
                break;
            }
            case 6 :{// TEST - 6:
                anl.fillEmptyCellsByModValue(5);
                MatrixFunctions.printVector(anl.getColumnValues(5), false);
                break;
            }
            case 7 :{// TEST - 7:
                anl.fillEmptyCellsByNullExpression(6);
                MatrixFunctions.printVector(anl.getColumnValues(6), false);
                break;
            }
            case 8 :{// TEST - 8:
                anl.fillEmptyCells(7, false);
                MatrixFunctions.printVector(anl.getColumnValues(7), false);
                break;
            }
            case 9 :{// TEST - 9:
                anl.fillEmptyCells(2, null);
                MatrixFunctions.printVector(anl.getColumnValues(2), false);
                break;
            }
            case 10 :{// TEST - 10:
                anl.fillEmptyCells(0, null);
                MatrixFunctions.printVector(anl.getColumnValues(0), false);
                break;
            }
        }
    }
    public void testExchangeElementOnTheList(){
        Integer[] dizi = new Integer[]{1, 2, 3, 4, 5, 6, 7};
        Integer[] dizi2;
        dizi2 = MatrixFunctions.exchangeElementOnTheList(dizi, Integer[].class, 0, 1);
        MatrixFunctions.printVector(dizi2, false);
    }
    public void testMatrixFunctionAddNewRows(){
        /*
        [2.0] [34.44] [235.56] [233.1] [3.21] [22.22] 
        [true] [false] 
        [Anahtar] [os] [İn-out] [env] [start] [opt] [final] 
        [1] [2] [3] [4] [5] [6] [7] 
        */
        Object[][] uniques;
        DataAnalyzer anl = readAndStartAnalyzerFor("example - 2 data.xlsx");
        uniques = anl.getUniqueAllColValues();
//        MatrixFunctions.printMatrix(uniques);
//        Object[] dNew = new Object[]{"os", "path", "dir", "cpu"};
//        uniques = MatrixFunctions.addNewRow(uniques, 0, dNew);
        // TEST - 2:
//        Object[][] dNewRows = new Object[2][];
//        dNewRows[0] = dNew;
//        dNewRows[1] = new Object[]{"timer", "checker", "syncer", "finder"};
//        uniques = MatrixFunctions.addNewRows(uniques, 4, dNewRows);
//        MatrixFunctions.printMatrix(uniques);

        // TEST - 3:
        Object[][] dNews = new Object[5][];
        for(int sayac = 0; sayac < dNews.length; sayac++){
            Object[] produced = new Object[8];
            for(int s2 = 0; s2 < produced.length; s2++){
                produced[s2] = sayac + " - " + s2;
            }
            dNews[sayac] = produced;
        }
//        MatrixFunctions.printMatrix(dNews);
        uniques = MatrixFunctions.addNewRows(uniques, 3, dNews);
        MatrixFunctions.printMatrix(uniques);
    }
    public void testCategoricalVariableClassWithNoCodingType(){
        String[] realData = new String[]{"Amasya", "Erzurum", "Kars", "Şam", "Üsküp", "Üsküp", "Üsküp", "Üsküp", "Üsküp", "Şam"};
        
        CategoricalVariable var = new CategoricalVariable("İl", String.class, realData, null, String.class, CategoricalVariable.CODING_TYPE.NO_CODING);
        System.out.println("Kategori sayısı : " + var.getCategoryNumber());
        System.out.println("Şam kategorisi veri sayısı : " + var.getMemberCountOfCategory("Şam"));
        System.out.println("Üsküp kategorisi veri sayısı : " + var.getMemberCountOfCategory("Üsküp"));
        System.out.println("Erzurum kategorisi veri sayısı : " + var.getMemberCountOfCategory("Erzurum"));
        
        ArrayList<String> stats = CategoricalVariable.produceStatisticAsList(var);
        for(int sayac = 0; sayac < stats.size(); sayac++){
            System.out.println(stats.get(sayac));
        }
    }
    public void testCategoricalVariableClassWithBinomialCodingType(){
        String[] realData = new String[]{"Üsküp", "Şam", "Üsküp", "Şam", "Üsküp", "Üsküp", "Üsküp", "Üsküp", "Üsküp", "Şam"};
        String[] arrOrdinal = new String[]{"Üsküp", "Şam"};
        Object[] codedData = convCategoriesAsBinomial(realData, arrOrdinal, true, true);
//        MatrixFunctions.printVector(codedData, false);
        
        CategoricalVariable var = new CategoricalVariable("İl", String.class, realData, codedData, Boolean.class, CategoricalVariable.CODING_TYPE.BINOMIAL);
        System.out.println("Kategori sayısı : " + var.getCategoryNumber());
        System.out.println("Şam kategorisi veri sayısı : " + var.getMemberCountOfCategory("Şam"));
        System.out.println("Üsküp kategorisi veri sayısı : " + var.getMemberCountOfCategory("Üsküp"));
        System.out.println("Şam kategorisinin kodu : " + var.getCodeFromCategoryValue("Şam"));
        System.out.println("true kodunun kategorisi : " + var.getCategoryValueFromCode(true));
        
        
        ArrayList<String> stats = CategoricalVariable.produceStatisticAsList(var);
        for(int sayac = 0; sayac < stats.size(); sayac++){
            System.out.println(stats.get(sayac));
        }
    }
    public void testCategoricalVariableClassWithOrdinalCodingType(){
        String[] realData = new String[]{"Amasya", "Erzurum", "Kars", "Şam", "Üsküp", "Üsküp", "Üsküp", "Üsküp", "Üsküp", "Şam"};
        String[] arrOrdinal = new String[]{"Üsküp", "Erzurum", "Kars", "Şam", "Amasya"};
        Object[] codedData = convCategoriesAsOrdinal(realData, arrOrdinal);
//        MatrixFunctions.printVector(codedData, false);
        CategoricalVariable var = new CategoricalVariable("İl", String.class, realData, codedData, int.class, CategoricalVariable.CODING_TYPE.ORDINAL);
//        MatrixFunctions.printVector(var.getUniqueValues(), true);
//        MatrixFunctions.printVector(var.getCodedDataAsArray(), true);
        System.out.println("Kategori sayısı : " + var.getCategoryNumber());
        System.out.println("Şam kategorisi veri sayısı : " + var.getMemberCountOfCategory("Şam"));
        System.out.println("Üsküp kategorisi veri sayısı : " + var.getMemberCountOfCategory("Üsküp"));
        System.out.println("Erzurum kategorisi veri sayısı : " + var.getMemberCountOfCategory("Erzurum"));
        System.err.println("-------------------");
        System.err.println(codedData[1] + " kodunun temsil ettiği kategori : " + var.getCategoryValueFromCode(codedData[1]));
        System.err.println(codedData[0] + " kodunun temsil ettiği kategori : " + var.getCategoryValueFromCode(codedData[0]));
        System.err.println("Üsküp kategorisinin kodu : " + var.getCodeFromCategoryValue("Üsküp"));
        System.err.println("Şam kategorisinin kodu : " + var.getCodeFromCategoryValue("Şam"));
        
        ArrayList<String> stats = CategoricalVariable.produceStatisticAsList(var);
        for(int sayac = 0; sayac < stats.size(); sayac++){
            System.out.println(stats.get(sayac));
        }
    }
    public Object[] convCategoriesAsOrdinal(Object[] colData, Object[] valuesAsAscending){
        HashMap<Object, Integer> valToOrder = new HashMap<Object, Integer>();
        Object[] value = new Object[colData.length];
        for(int sayac = 0; sayac < valuesAsAscending.length; sayac++){
            valToOrder.put(valuesAsAscending[sayac], (sayac + 1));
        }
        for(int sayac = 0; sayac < colData.length; sayac++){
            value[sayac] = valToOrder.get(colData[sayac]);
        }
        return value;
    }
    private Object[] convCategoriesAsBinomial(Object[] colData, Object[] uniqueColValues, boolean setFirstValueAsTrue, boolean setValueAsBoolean){
        if(uniqueColValues == null)// Sütunda hiç veri yoksa işlemi sonlandır
            return null;
        if(uniqueColValues.length > 2)// İkili kodlama için sütunda en fazla iki değer olmalı
            return null;
        Object[] value = new Object[colData.length];
        Object valAsTrue = uniqueColValues[0];
        if(!setFirstValueAsTrue){
            if(uniqueColValues.length == 2)
                valAsTrue = uniqueColValues[1];
            else
                valAsTrue = null;
        }
        for(int sayac = 0; sayac < colData.length; sayac++){
            if(colData[sayac] == null)
                value[sayac] = null;
            else if(colData[sayac] == valAsTrue){
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
    public void testTranspozeFunction(){
        Object[][] dd = new Object[][]{
            {2,   4,   5,   6,   91,  19},
            {2,   4,   5,   1}
        };
//        MatrixFunctions.printMatrix(dd);
        
        MatrixFunctions.printMatrix(MatrixFunctions.transpoze(dd));
    }
    public void testCategoricalVariableClassWithOneHotVectorCodingType(){
        String[] realData = new String[]{"Amasya", "Erzurum", "Kars", "Şam", "Üsküp", "Üsküp", "Üsküp", "Üsküp", "Üsküp", "Şam"};
        String[] arrUniques = new String[]{"Amasya", "Erzurum", "Kars", "Şam", "Üsküp"};
        Object[][] codedData = MatrixFunctions.transpoze(convCategoriesAsOneHotVector(realData, arrUniques));;

        
//        MatrixFunctions.printMatrix(codedData);
        CategoricalVariable var = new CategoricalVariable("İl", String.class, realData, codedData, Boolean.class, CategoricalVariable.CODING_TYPE.ONEHOTVECTOR);
        System.out.println("Kategori sayısı : " + var.getCategoryNumber());
        System.out.println("Şam kategorisi veri sayısı : " + var.getMemberCountOfCategory("Şam"));
        System.out.println("Üsküp kategorisi veri sayısı : " + var.getMemberCountOfCategory("Üsküp"));
        System.out.println("Erzurum kategorisi veri sayısı : " + var.getMemberCountOfCategory("Erzurum"));
        System.err.println("-------------------");
        Object[] code = new Object[]{false, false, false, true, false};
        System.err.println(code + " kodunun kategorisi : " + var.getCategoryValueFromCode(code));
        System.err.println("3 numaralı biti 'true' olan kodun kategorisi : " + var.getCategoryValueFromCodeForOneHotEncoding(3));
        System.err.println("1 numaralı biti 'true' olan kodun kategorisi : " + var.getCategoryValueFromCodeForOneHotEncoding(1));
        System.out.println("Şam kategorisinin kodu : ");
        MatrixFunctions.printVector(var.getCodeFromCategoryValueForOneHotEncoding("Şam"), true);
//        
        
        
        
        ArrayList<String> stats = CategoricalVariable.produceStatisticAsList(var);
        for(int sayac = 0; sayac < stats.size(); sayac++){
            System.out.println(stats.get(sayac));
        }
    }
    private Object[][] convCategoriesAsOneHotVector(Object[] colData, Object[] categories){
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
        Object[][] values = new Object[valToColOrder.size()][colData.length];
        for(int sayac = 0; sayac < valToColOrder.size(); sayac++){
            for(int s2 = 0; s2 < colData.length; s2++){
                if(colData[s2] == null){
                    values[sayac][s2] = false;
                    continue;
                }
                if(valToColOrder.get(colData[s2]) == sayac)
                    values[sayac][s2] = true;
                else
                    values[sayac][s2] = false;
            }
        }
        return values;
    }
    public void nesneselKontroldeTamsayiNoktaliSayiFarki(){
        Object t1 = (Integer) 2;
        Object tamKontrol = (Integer) 2;
        Double d1 = (Double) 2.3;
        Double noktaliKontrol = (Double) 2.3;
        if(t1 == tamKontrol)
            System.out.println("Veri tipi tamsayı iken nesne olarak kontrol edilirken değerleri kontrol ediliyor");
        else
            System.err.println("Veri tipi tamsayı iken nesne olarak kontrol edildiğinde değerler kontrol edilmiyor");
        if(d1 == noktaliKontrol)
            System.out.println("Veri tipi noktalı sayı iken nesne olarak kontrol edilirken değerleri kontrol ediliyor");
        else
            System.err.println("Veri tipi noktalı iken nesne olarak kontrol edildiğinde değerler kontrol edilmiyor");
    }
    public void testPnlVarietyForText(){
        JFrame fr = new JFrame("Deneme");
        fr.setPreferredSize(new Dimension(700, 200));
        fr.setSize(700, 200);
        fr.setLayout(new BorderLayout(5, 5));
        String content = "Veri tipleri arasındaki dönüşümler\n" +
"String->    Integer : 'Integer.valueOf()' ile doğrudan (Eğer veri tamsayı ise)\n" +
"            Double : 'Double.valueOf()' ile doğrudan\n" +
"            Boolean : 'Boolean.valueOf()' ile doğrudan\n" +
"            \n" +
"Integer->   String : 'String.valueOf()' ile doğrudan\n" +
"            Double : 'Double.valueOf()' ile doğrudan\n" +
"\n" +
"Double->    String : 'String.valueOf()' ile doğrudan\n" +
"            Integer : 'String.valueOf()' ile doğrudan yapılamıyor\n" +
"\n" +
"Boolean->   String : 'String.valueOf()' ile doğrudan\n" +
"            Integer(Kodlama ile dönüşebilir(true = 1, false = 0 gibi))\n" +
"            Double(Kodlama ile dönüşebilir(true = 1.0, false = 0.0 gibi))\n" +
"            \n" +
"İKİ TEMEL MESELE:\n" +
"1) Double'dan Integer'a dönüşüm için ek fonksiyon lazım\n" +
"2) \"3.0\" metnini Integer'a dönüştürmek için aynı ek fonksiyon lazım\n" +
"\n" +
"\n" +
"\n" +
"target == Double -> tüm dönüşüm için 'Double.valueOf' kâfî (Boolean hâricî dönüşümler için)\n" +
"target == String -> tüm dönüşüm için 'String.valueOf' kâfî\n" +
"\n" +
"String\n" +
"\n" +
"Integer --> Double\n" +
"Double --> Integer\n" +
"\n" +
"\n" +
"\n" +
"\n" +
"Double -> String : Tamâm\n" +
"Double -> Integer : Tamâm\n" +
"Double -> Boolean : Tamâm (olumsuz)\n" +
"\n" +
"\n" +
"Integer -> Double : Tamâm\n" +
"Integer -> Boolean : Tamâm (olumsuz)\n" +
"Integer -> String : Tamâm\n" +
"\n" +
"\n" +
"Boolean -> Integer : Tamâm (olumsuz)\n" +
"Boolean -> Double : Tamâm (olumsuz)\n" +
"Boolean -> String : Tamâm\n" +
"\n" +
"\n" +
"String -> Integer : Tamâm\n" +
"String -> Double : Tamâm\n" +
"String -> Boolean : Tamâm\n" +
"\n" +
"Gönderilen verinin tipi değişiyor mu?";
        PnlVarietyForText pnl = new PnlVarietyForText("Bismillâh", content);
//        pnl.setPreferredSize(new Dimension(220, 330));
        fr.add(pnl, BorderLayout.CENTER);
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fr.setVisible(true);
    }
    public void clear() throws AWTException {
        Robot rob = new Robot();
    try {
        rob.keyPress(KeyEvent.VK_CONTROL); // press "CTRL"
        rob.keyPress(KeyEvent.VK_L); // press "L"
        rob.keyRelease(KeyEvent.VK_L); // unpress "L"
        rob.keyRelease(KeyEvent.VK_CONTROL); // unpress "CTRL"
        Thread.sleep(1000); // add delay in milisecond, if not there will automatically stop after clear
    } catch (InterruptedException e) {
    }
}
    //ARKAPLAN İŞLEM YÖNTEMLERİ:
    private int[] takeSubArray(int[] array, int startIndex, int endIndex){
        int[] arr = new int[endIndex - startIndex + 1];
        int counter = 0;
        for(int sayac = startIndex; sayac < endIndex + 1; sayac++){
            arr[counter] = array[sayac];
        }
        return arr;
    }
    private Object[] calculateUniqueValues(Object[] colData){
        ArrayList<Object> uniqVals = new ArrayList<Object>();
        for(int sayac = 0; sayac < colData.length; sayac++){
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
}