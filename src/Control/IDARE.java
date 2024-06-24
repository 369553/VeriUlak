package Control;

import Base.DataAnalyzer;
import Base.DataB;
import Base.Statistic;
import Service.CSVReader;
import Service.XlsXReader;
import View.ContactPanel;
import View.MainFrame;
import View.PnlAdvices;
import View.PnlSideMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.filechooser.FileFilter;

public class IDARE implements ActionListener{
    private static IDARE idare = null;
    private String runningCode = "";
    private GUIIdare guiIDARE;
    private String[] acceptableExtensions;
    private FileFilter filterForDataSet;
    private Stream stream;
    private DataAnalyzer analyzer;
    // İşlemler analyzer üzerinden gidiliyor; data verisinin burada tutulmaması isâbetli olabilir
    //Ortam bilgileri:
    private String srcFilePath;
    private String srcFileExt;
    private Object[][] data;
    private HashMap<String, Object> lastProccessInfo;
    private HashMap<String, Object> lastDataPack;
    private ArrayList<String> liPanelsWhichIncludeColDetails;// Sütun bilgisi içeren paneller (sütun bilgilerinde değişklik olduğunda tazelenmesi gerekiyor)
    private ActionListener actForAdvices;// Tavsiye panelinden gelen eylemler için dinleyici

    private IDARE(String code, MainFrame frame){
        guiIDARE = GUIIdare.startGUIIDARE(this, frame);//Görsel idâreciyi başlat
        this.runningCode = code;//Çalışma zamânı güvenlik kodunu sakla
        Service.RWService.getService().setAcceptableExtensions(getAcceptableExtensions());//Girdi - çıktı biriminin tanıdığı dosya tiplerini belirle
        DataB.startDBase(this, runningCode);
        actForAdvices = this;
        //.;.
    }

//İŞLEM YÖNTEMLERİ:
    //BAŞLANGIÇ YÖNTEMİ:
    public static void startIDARE(String code, MainFrame frame){//code : Çalışma zamânı işlem güvenliği kodu, frame : Ana pencere
        if(idare == null){
            if(frame != null && code != null){
                if(!code.isEmpty()){
                    idare = new IDARE(code, frame);
                }
            }
        }
    }
    public FileFilter getDataSetFileFilter(){
        if(filterForDataSet == null){
            filterForDataSet = new FileFilter() {
                @Override
                public boolean accept(File f){
                    String pathAndName = f.getAbsolutePath();
                    if(pathAndName.endsWith(".xlsx"))
                        return true;
                    if(pathAndName.endsWith(".xls"))
                        return true;
                    if(pathAndName.endsWith(".csv"))
                        return true;
                    return false;
                }
                @Override
                public String getDescription(){
                    return "Veri seti dosyası";
                }
            };
        }
        return filterForDataSet;
    }
    public void goToNext(HashMap<String, Object> pack){
//        System.err.println("current : " + getCurrentStep());
        if(pack == null)
            return;
        getStream().putPackForNextStep(pack);
        boolean isSuccess = getStream().checkPackForNextStep();
        if(!isSuccess){
            ContactPanel.getContactPanel().showMessage("Dosya bilgisi eksik; dosyayı kontrol edin; fakat yazılım içi veri iletim hatâsı da olabilir", "Error");
            return;
        }
        if(getStream().getCurrentStep() == 1){
            boolean isFirstRowAsColumnNames = (Boolean) pack.get("isFirstRowAsColumnNames");
            boolean autoDetect = (Boolean) pack.get("autoDetect");
            analyzer = new DataAnalyzer((Object[][]) pack.get("data"), autoDetect, isFirstRowAsColumnNames);
        }
        getStream().goToNext(runningCode);
        guiIDARE.refresh();
    }
    public void goToBack(){
        getStream().goToBack(runningCode);
    }
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource().getClass() == PnlAdvices.class){
            PnlAdvices pnl = (PnlAdvices) e.getSource();
            //.;.
        }
    }
    public boolean requestChangingData(int row, int col, Object value){
        boolean isSuccess = analyzer.setCellData(row, col, value);
        if(isSuccess){
            getLastProccessInfo().clear();
            lastProccessInfo.put("processType", "changeCellData");
            lastProccessInfo.put("colNumber", col);
            lastProccessInfo.put("rowNumber", row);
            getLastDataPack().clear();
            lastDataPack = getAnalyzer().getColumnDetails(col);
            lastDataPack.put("statistic", getAnalyzer().getStatisticForColumn(col));
            guiIDARE.updateDataOnSelectedPanels(getLiPanelsWhichIncludeColDetails());
            triggerForSecondMenu(col);
        }
        return isSuccess;
    }
    public boolean requestChangingNameOfColumn(String oldName, String newName){
        boolean isSuccess = analyzer.setColumnName(oldName, newName);
        if(isSuccess){
            int colNo = analyzer.getColIndexByName(newName);
            // .;. : Ekran tazelemeleri
            getLastProccessInfo().clear();
            getLastDataPack().clear();
            lastProccessInfo.put("processType", "changeColName");
            lastProccessInfo.put("colNumber", colNo);
            lastProccessInfo.put("name", newName);// >GEREKSİZZZZ
            lastDataPack.put("number", analyzer.getColIndexByName(newName));// !i3i
            lastDataPack.put("name", newName);
            guiIDARE.updateDataOnActivePanels();
            triggerForSecondMenu(colNo);
        }
        return isSuccess;
    }
    public HashMap<Integer, HashMap<String, Object>> getColumnsDetail(){
        return this.analyzer.getColumnsDetail();
    }
    public ArrayList<String> getColumnNames(){
        String[] asArray = this.analyzer.getColumnNames();
        ArrayList<String> list = new ArrayList<String>();
        for(int sayac = 0; sayac < asArray.length; sayac++){
            list.add(asArray[sayac]);
        }
        return list;
    }
    public String[] getColumnNamesAsArray(){
        return this.analyzer.getColumnNames();
    }
    public String getColumnName(int colIndex){
        if(colIndex < 0 || colIndex >= this.analyzer.getColumnCount())
            return null;
        return this.analyzer.getColumnNames()[colIndex];
    }
    public HashMap<String, Object> getColumnDetails(int columnNumber){
        if(columnNumber > analyzer.getColumnCount() || columnNumber < 0)
            return null;
        return getColumnsDetail().get(columnNumber);
    }
    public Statistic getStatisticsForColumn(int colNumber){
        return getAnalyzer().getStatisticForColumn(colNumber);
    }
    public Class getDataTypeOfColumn(int colIndex){
        return getDataTypes()[colIndex];
    }
    public Class[] getDataTypes(){
        return getAnalyzer().getDataTypes();
    }
    public Class getDataTypeOfColumn(String columnName){
        int index = -1;
        String[] names = getAnalyzer().getColumnNames();
        for(int sayac = 0; sayac < names.length; sayac++){
            if(names[sayac].equals(columnName)){
                index = sayac;
            break;
            }
        }
        if(index != -1)
            return getDataTypeOfColumn(index);
        return null;
    }
    public Object[] getColumnData(String columnName){
        return getColumnData(getAnalyzer().getColIndexByName(columnName));
    }
    public Object[] getColumnData(int colIndex){
        return getAnalyzer().getColumnValues(colIndex);
    }
    public Integer[] getIndexesOfEmptyCellsOnColumn(int colNumber){
        return getAnalyzer().findEmptyCellOnColumn(colNumber);
    }
    public int getColIndexByName(String name){
        return getAnalyzer().getColIndexByName(name);
    }
    public Object[] getUniqueColValues(int colNumber){
        return getAnalyzer().getUniqueColValues(colNumber);
    }
    public int getColumnCount(){
        return getAnalyzer().getColumnCount();
    }
    public int getRowCount(){
        return getAnalyzer().getRowCount();
    }
    public boolean requestDeleteColumn(int colNumber){
        if(colNumber < 0)
            return false;
        int before = getAnalyzer().getColumnCount();
        if(colNumber >= before)
            return false;
        getAnalyzer().deleteCols(new int[]{colNumber});
        boolean isSuccess = getAnalyzer().getColumnCount() < before;
        if(isSuccess){
            // .;. : Ekran tazelemeleri
            getLastProccessInfo().clear();
            getLastDataPack().clear();
            lastProccessInfo.put("processType", "deleteCol");
            lastProccessInfo.put("colNumber", colNumber);
            lastDataPack.put("number", colNumber);
            guiIDARE.updateDataOnActivePanels();
            triggerForSecondMenu(colNumber);
        }
        return isSuccess;
    }
    public int getCurrentStep(){
        return getStream().getCurrentStep();
    }
    public boolean requestChangeDataType(int colIndex, Class targetType, HashMap<String, Object> configurations){
        // Veri değiştirme şu durumlarda söz konusudur : Integer -> Double'a , Double'dan Integer'a
                                                   // String -> Integer'a , String'ten Double'a
                  //!! : configurations ayarı kullanıcıya bırakılıyor, düzeltilmeli
        if(getAnalyzer().getIsColumnIsCategorical()[colIndex]){// Sütun kategorik veri barındırıyorsa
            ContactPanel.getContactPanel().showMessage("Kategorik olarak kodlanan sütunda bu işlem yapılamaz!", "Warning");
            return false;
        }
        boolean isSuccess = getAnalyzer().changeColumnDataType(colIndex, targetType, configurations);
        if(isSuccess){
            getLastProccessInfo().clear();
            getLastDataPack().clear();
            lastProccessInfo.put("processType", "changeDataType");
            lastProccessInfo.put("colNumber", "colNumber");
            lastProccessInfo.put("dataType", targetType.getClass().getName());
            lastDataPack = analyzer.getColumnDetails(colIndex);
            lastDataPack.put("statistic", analyzer.getStatisticForColumn(colIndex));
            lastDataPack.put("data", analyzer.getData());
            guiIDARE.updateDataOnActivePanels();
            triggerForSecondMenu(colIndex);
        }
        return isSuccess;
    }
    public boolean requestChangeDataType(int colIndex, Class targetType){
        return requestChangeDataType(colIndex, targetType, null);
    }
    public boolean requestFillEmptiesOnColumn(int colIndex, String policyName, Object value){
        if(getAnalyzer().getIsColumnIsCategorical()[colIndex]){// Sütun kategorik veri barındırıyorsa
            ContactPanel.getContactPanel().showMessage("Kategorik olarak kodlanan sütunda bu işlem yapılamaz!", "Warning");
            return false;
        }
        if(policyName == null)// Politika belirlenmediyse
            return false;
        boolean isSuccess = false;
        switch(policyName){
            case "Ortalama ile doldur" :{
                isSuccess = getAnalyzer().fillEmptyCellsByMean(colIndex);
                break;
            }
            case "En yüksek değerle doldur" :{
                isSuccess = getAnalyzer().fillEmptyCellsByMax(colIndex);
                break;
            }
            case "En düşük değerle doldur" :{
                isSuccess = getAnalyzer().fillEmptyCellsByMin(colIndex);
                break;
            }
            case "Sıfır(0) ile doldur" :{
                isSuccess = getAnalyzer().fillEmptyCellsByZero(colIndex);
                break;
            }
            case "Özel bir değerle doldur" :{
                isSuccess = getAnalyzer().fillEmptyCells(colIndex, value);
                break;
            }
            case "Boş metin(\"\") ile doldur" :{
                isSuccess = getAnalyzer().fillEmptyCellsByEmptyString(colIndex);
                break;
            }
            case "En çok tekrar eden değerle doldur" :{
                isSuccess = getAnalyzer().fillEmptyCellsByModValue(colIndex);
                break;
            }
            case "\"null\" ifâdesiyle doldur" :{
                isSuccess = getAnalyzer().fillEmptyCellsByNullExpression(colIndex);
                break;
            }
        }
        //Tetiklemeler:
        if(isSuccess){
            getLastProccessInfo().clear();
            getLastDataPack().clear();
            lastProccessInfo.put("processType", "fillEmptyCells");
            lastProccessInfo.put("colNumber", colIndex);
            lastDataPack.put("number", colIndex);
            lastDataPack = analyzer.getColumnDetails(colIndex);
            lastDataPack.put("statistic", analyzer.getStatisticForColumn(colIndex));
            lastDataPack.put("data", analyzer.getData());
            guiIDARE.updateDataOnActivePanels();
            triggerForSecondMenu(colIndex);
        }
        return isSuccess;
    }
    public boolean requestCodingColumn(int colIndex, String codingTypeName, HashMap<String, Object> configurations){
        /*
            colIndex : Sütun numarası
            codingTypeName : Kodlama tipi
            configurations : Kodlama yapılandırma ayarı (Boolean için bitsel kodlama için codeTrueAs1 (Boolean tipinde);
                sıralı kodlama için sıra liste (büyükten küçüğe doğru),
                int ve double -> boolean kodlamada code1AsTrue (Boolean tipinde)
        */
        boolean isSuccess = false;// İşlemin başarılı olmasını tâkiben yapılacak işlemlerin yapılabilmesi için başarıyı tâkip etmeli
        boolean isMultipleColumnAffected = false;// İşlem başarılı olduğunda birden fazla sütun etkilendiyse 'true' olmalıdır
        if(getAnalyzer().getIsColumnIsCategorical()[colIndex]){// Sütun kategorik veri barındırıyorsa
            ContactPanel.getContactPanel().showMessage("Kategorik değişken olarak kodlanan sütun yeniden kodlanamaz!", "Warning");
            return false;
        }
        if(colIndex < 0 ||colIndex >= analyzer.getColumnCount())
            return false;
        if(codingTypeName == null)
            return false;
        if(codingTypeName.isEmpty())
            return false;
        switch(codingTypeName){
            case "Bitsel kodlama" :{
                if(analyzer.getDataTypes()[colIndex] == Boolean.class){// Boolean -> Integer : true, false -> 1, 0 | 0, 1
                    boolean codeTrueAs1 = true;
                    if(configurations != null){// Eğer yapılandırma gönderilmişse uygula
                    Object cc = configurations.get("codeTrueAs1");
                        if(cc != null){
                            if(cc.getClass() == Boolean.class)
                                codeTrueAs1 = (Boolean) cc;
                        }
                    }
                    isSuccess = analyzer.setColumnAsCategoricalWithBinomialEncode(colIndex, codeTrueAs1);
                    if(!isSuccess)
                        ContactPanel.getContactPanel().showMessage("Kodlama işlemi başarısız!", "Warning");
                }
                break;
            }
            case "Sıralı kodlama" :{
                String[] arrOrder = (String[]) configurations.get("order");
                Object[] values = new Object[arrOrder.length];
                Class dType = analyzer.getDataTypes()[colIndex];
                for(int sayac = 0; sayac < values.length; sayac++){
                    if(arrOrder[sayac] == null)// Bunun olmaması lazım; yine de ekliyorum; olursa kesin tedbir olmayabilir
                        continue;
                    Object[] res = analyzer.convDataType(arrOrder[sayac], dType);
                    if(!(boolean) res[0]){
                        ContactPanel.getContactPanel().showMessage("Gönderilen veri ile sütun veri tipi uyuşmuyor", "Warning");
                        break;
                    }
                    values[sayac] = res[1];
                }
                isSuccess = analyzer.setColumnAsCategoricalWithOrdinalEncode(colIndex, values);
                break;
            }
            case "Tek nokta vektörü (One Hot Encoding) biçiminde kodlama" :{
                boolean codeAsBoolean = (boolean) configurations.get("codeAsBoolean");
                isSuccess = getAnalyzer().setColumnAsCategoricalWithOneHotVectorEncode(colIndex, codeAsBoolean);
                isMultipleColumnAffected = true;
                break;
            }
        }
        if(isSuccess){
            // TETİKLEMELER:
//            System.out.print("Başarılı kodlama = " + codingTypeName + "\tIDARE.requestCodingColumn.331");
            getLastProccessInfo().clear();
            getLastDataPack().clear();
            lastProccessInfo.put("processType", "codeColumn");
            lastProccessInfo.put("colNumber", colIndex);
            lastProccessInfo.put("isMultipleColumnAffected", isMultipleColumnAffected);
            lastDataPack = (HashMap<String, Object>) analyzer.getColumnDetails(colIndex).clone();
            lastDataPack.put("data", analyzer.getData());
            lastDataPack.put("statistic", analyzer.getStatisticForColumn(colIndex));
            lastDataPack.put("colNumberOfCategoricalVariable", getAnalyzer().getMapCategoricalVars().get(colIndex).getColCount());
            guiIDARE.updateDataOnActivePanels();
            triggerForSecondMenu(colIndex);
        }
        return isSuccess;
    }
    public boolean requestNormalization(int colIndex, boolean isNormalization){// Eğer standardizasyon yapılmak isteniyorsa 'isNormalization' biti 'false' olarak verilmelidir
        if(!getAnalyzer().getStatisticForColumn(colIndex).getIsNumber())// Sayı barındırmayan sütun normalize edilemez
            return false;
        if(getAnalyzer().getIsColumnIsCategorical()[colIndex]){// Kategorik sütun için bu işlem yapılamaz
            ContactPanel.getContactPanel().showMessage("Kategorik olarak kodlanan sütun için bu işlem yapılamaz", "Warning");
            return false;
        }
        boolean isSuccess = false;
        isSuccess = getAnalyzer().normalizeOrStandardizeColumn(colIndex, isNormalization);
        if(isSuccess){
            getLastProccessInfo().clear();
            getLastDataPack().clear();
            lastProccessInfo.put("colNumber", colIndex);
            lastProccessInfo.put("processType", "normalizeColumn");
            lastDataPack = (HashMap<String, Object>) analyzer.getColumnDetails(colIndex).clone();
            lastDataPack.put("data", analyzer.getData());
            lastDataPack.put("statistic", analyzer.getStatisticForColumn(colIndex));
            guiIDARE.updateDataOnActivePanels();
            triggerForSecondMenu(colIndex);
        }
        return isSuccess;
    }
    public boolean[] getIsColumnCategorical(){
        return getAnalyzer().getIsColumnIsCategorical();
    }
    public void triggerForSecondMenu(int colIndex){
        ArrayList<String> panelNames = new ArrayList<String>();
        getLastProccessInfo().clear();
        getLastDataPack().clear();
        lastProccessInfo.put("processType", "changedColumn");
        lastProccessInfo.put("colNumber", colIndex);
        lastDataPack.put("advices", getAnalyzer().getAdviceTexts(colIndex));
        panelNames.add(PnlAdvices.class.getName());
        guiIDARE.updateDataOnSelectedPanels(panelNames);
    }

//ERİŞİM YÖNTEMLERİ:
    //ANA ERİŞİM YÖNTEMİ:
    public static IDARE getIDARE(){
        return idare;
    }
    public String getRunningCodeAsMd5(){
        return runningCode;
    }
    public String[] getAcceptableExtensions(){
        if(acceptableExtensions == null){
            acceptableExtensions = new String[]{"xls", "xlsx", "csv"};
        }
        return acceptableExtensions;
    }
    public Class getIReaderClassDependExtension(String extension){
        if(extension == null)
            return null;
        if(extension.isEmpty())
            return null;
        switch(extension){
            case "xls" : return XlsXReader.class;
            case "xlsx" : return XlsXReader.class;
            //case "xml" : return XmlXReader.class;
//            case "json" : return XlsXReader.class;
            case "csv" : return CSVReader.class;
        }
        return null;
    }
    public Class getAcceptedTypeOfIReader(Class classOfreader){
       if(XlsXReader.class == classOfreader)
           return File.class;
       else if(CSVReader.class == classOfreader)
           return String.class;
        return null;
    }
    public Stream getStream(){
        if(stream == null){
            stream = new Stream(this);
        }
        return stream;
    }
    protected HashMap<String, Object> getLastProccessInfo(){
        if(lastProccessInfo == null)
            lastProccessInfo = new HashMap<String, Object>();
        return lastProccessInfo;
    }
    protected String getSrcFilePath(){
        return srcFilePath;
    }
    protected String getSrcFileExt(){
        return srcFileExt;
    }
    protected Object[][] getData(){
        return data;
    }
    protected DataAnalyzer getAnalyzer(){
        return analyzer;
    }
    public ArrayList<String> getLiPanelsWhichIncludeColDetails(){
        if(liPanelsWhichIncludeColDetails == null){
            liPanelsWhichIncludeColDetails = new ArrayList<String>();
            liPanelsWhichIncludeColDetails.add(PnlSideMenu.class.getName());// PnlSideMenu'nun içerdiği panelleri yazma : PnlStatistic, Pnl...
        }
        return liPanelsWhichIncludeColDetails;
    }
    public HashMap<String, Object> getLastDataPack(){
        if(lastDataPack == null){
            lastDataPack = new HashMap<String, Object>();
        }
        return lastDataPack;
    }
    public ActionListener getActForAdvices(){
        return actForAdvices;
    }
}