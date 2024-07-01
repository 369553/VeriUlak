package Base;

import Control.IDARE;
import Service.CSVWriter;
import Service.ClassStringDoubleConverter;
import Service.CryptService;
import Service.IWriter;
import Service.JSONReader;
import Service.RWService;
import Service.XlsXWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class DataB {
    private static DataB dBase;// Veritabanı örneği
    private IDARE idare;// İdâre sınıfı
    private ArrayList<String> manipulationsForString;// 'String' tipindeki veri üzerinde yapılabilecek işlemlerin yazılı hâli
    private ArrayList<String> manipulationsForNumber;// Sayı tipindeki veri üzerinde yapılabilecek işlemlerin yazılı hâli
    private ArrayList<String> dataTypes;// Veri tipleri listesi
    private HashMap<Class, String> dataTypeToName;// Verilen veri tipinin ismini saklayıp, her defasında 'split' yöntemiyle ayrıştırmamak için değişken
    private ArrayList<String> liInterestingOnWrongDataForNumerical;
    private ArrayList<String> liInterestingOnWrongDataForStringType;
    private ArrayList<String> liSolutionsForOutliner;
    private String highLigthedColorForCellForeground = "#27196D";
    private String highLigthedColorForCellBackground = "#FF1919";
    private ArrayList<HashMap<String, String>> infoAboutSoftware;
    private ArrayList<String> liSolutionsOfNormalization;
    private HashMap<String, String> allInfos;
    private HashMap<String, String> mapTextToSupportedFileTypes;// Veri tipi ismi -> veri tipi uzantısı

    private DataB(IDARE idare){
        this.idare = idare;
        readInfos();
    }

//İŞLEM YÖNTEMLERİ:
    public static boolean startDBase(IDARE idare, String runningCode){
        if(idare == null)
            return false;
        if(runningCode == null)
            return false;
        if(!CryptService.getService().getMd5(runningCode).equals(idare.getRunningCodeAsMd5()))
            return false;
        DataB.dBase = new DataB(idare);
        return true;
    }
    //ARKAPLAN İŞLEM YÖNTEMLERİ:
    private void readInfos(){
        String path = System.getProperty("user.dir");
        String fileName = "bilgilendirmeler.json";
        String content = RWService.getService().readDataAsText(path, fileName);
        JSONReader jReader = new JSONReader();
        ArrayList<Object> turned = jReader.readJSONArray(content);
        allInfos = new HashMap<String, String>();
        for(Object obj : turned){
            HashMap<String, String> map = (HashMap<String, String>) obj;
            for(String key : map.keySet()){
                allInfos.put(key, map.get(key));
            }
        }
//        for(String key : allInfos.keySet()){
//            System.out.println(key + " -> " + allInfos.get(key));
//        }
    }

//ERİŞİM YÖNTEMLERİ:
    //ANA ERİŞİM YÖNTEMİ:
    public static DataB getdBase(){
        return DataB.dBase;
    }
    public ArrayList<String> getManipulationsFor(boolean forNumber){
        if(forNumber)
            return getManipulationsForNumber();
        return getManipulationsForString();
    }
    public ArrayList<String> getManipulationsForString(){
        if(manipulationsForString == null){
            manipulationsForString = new ArrayList<String>();
            manipulationsForString.add("Eksik verilerle ilgilen");
            manipulationsForString.add("Veri tipini dönüştür");
            manipulationsForString.add("Sütunu kodla");
//            manipulationsForString.add("Metînsel işlemler");
        }
        return manipulationsForString;
    }
    public ArrayList<String> getManipulationsForNumber(){
        if(manipulationsForNumber == null){
            manipulationsForNumber = new ArrayList<String>();
            manipulationsForNumber.add("Eksik verilerle ilgilen");
            manipulationsForNumber.add("Veri tipini dönüştür");
            manipulationsForNumber.add("Sütunu kodla");
            manipulationsForNumber.add("Normalizasyon ve standardizasyon");
            manipulationsForNumber.add("Aykırı verilerle ilgilen");
        }
        return manipulationsForNumber;
    }
    public ArrayList<String> getListOfDataTypes(){
        if(dataTypes == null){
            dataTypes = new ArrayList<String>();
            dataTypes.add(getDataTypeToName().get(Integer.class));
            dataTypes.add(getDataTypeToName().get(Double.class));
//            dataTypes.add(getDataTypeToName().get(Short.class));
//            dataTypes.add(getDataTypeToName().get(Float.class));
            dataTypes.add(getDataTypeToName().get(String.class));
            dataTypes.add(getDataTypeToName().get(Boolean.class));
        }
        return dataTypes;
    }
    public HashMap<Class, String> getDataTypeToName(){
        if(dataTypeToName == null){
            dataTypeToName = new HashMap<Class, String>();
            dataTypeToName.put(Double.class, "Double");
            dataTypeToName.put(Integer.class, "Integer");
            dataTypeToName.put(Short.class, "Short");
            dataTypeToName.put(String.class, "String");
            dataTypeToName.put(Long.class, "Long");
            dataTypeToName.put(Boolean.class, "Boolean");
            dataTypeToName.put(Float.class, "Float");
        }
        return dataTypeToName;
    }
    public ArrayList<String> getLiInterestingOnWrongDataFor(Class dataType){
        boolean isNumber = false;
        if(dataType == Number.class)
            isNumber = true;
        else if(dataType.getSuperclass() != null){
            if(dataType.getSuperclass() == Number.class)
                isNumber = true;
        }
        if(isNumber)
            return getLiInterestingOnWrongDataForNumerical();
        return getLiInterestingOnWrongDataForStringType();
    }
    public ArrayList<String> getLiInterestingOnWrongDataForNumerical(){
        if(liInterestingOnWrongDataForNumerical == null){
            liInterestingOnWrongDataForNumerical = new ArrayList<String>();
            liInterestingOnWrongDataForNumerical.add("Eksik verilerin bulunduğu satırları sil");
            liInterestingOnWrongDataForNumerical.add("Ortalama ile doldur");
            liInterestingOnWrongDataForNumerical.add("En yüksek değerle doldur");
            liInterestingOnWrongDataForNumerical.add("En düşük değerle doldur");
            liInterestingOnWrongDataForNumerical.add("Sıfır(0) ile doldur");
            liInterestingOnWrongDataForNumerical.add("Özel bir değerle doldur");
        }
        return liInterestingOnWrongDataForNumerical;
    }
    public ArrayList<String> getLiInterestingOnWrongDataForStringType(){
        if(liInterestingOnWrongDataForStringType == null){
            liInterestingOnWrongDataForStringType = new ArrayList<String>();
            liInterestingOnWrongDataForStringType.add("Eksik verilerin bulunduğu satırları sil");
            liInterestingOnWrongDataForStringType.add("Boş metin(\"\") ile doldur");
            liInterestingOnWrongDataForStringType.add("En çok tekrar eden değerle doldur");
            liInterestingOnWrongDataForStringType.add("\"null\" ifâdesiyle doldur");
            liInterestingOnWrongDataForStringType.add("Özel bir değerle doldur");
        }
        return liInterestingOnWrongDataForStringType;
    }
    public String getHighLigthedColorForCellBackground(){
        return highLigthedColorForCellBackground;
    }
    public String getHighLigthedColorForCellForeground(){
        return highLigthedColorForCellForeground;
    }
    public ArrayList<String> getCodingSolutions(Class dataType){
        ArrayList<String> val = new ArrayList<String>();
        if(dataType == Boolean.class){
            val.add("Bitsel kodlama");
            return val;
        }
        if(dataType == String.class){
//            val.add("Bitsel kodlama");// Eğer iki değer varsa, bitsel kodlama olabilir
            val.add("Sıralı kodlama");
            val.add("Tek nokta vektörü (One Hot Encoding) biçiminde kodlama");
            return val;
        }
        if(ClassStringDoubleConverter.getService().checkIsNumber(dataType)){// Eğer veri tipi sayı ise
            val.add("Sıralı kodlama");
            val.add("Tek nokta vektörü (One Hot Encoding) biçiminde kodlama");
        }
        return val;
    }
    public ArrayList<HashMap<String, String>> getInfoAboutSoftware() {
        if(infoAboutSoftware == null){
            infoAboutSoftware = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> licenseOfPoi = new HashMap<String, String>();
            licenseOfPoi.put("name", "Apache POI lisansı");
            licenseOfPoi.put("text", RWService.getService().readDataAsText("licenseOfPoi.txt"));
            infoAboutSoftware.add(licenseOfPoi);
        }
        return infoAboutSoftware;
    }
    public ArrayList<String> getLiSolutionsOfNormalization(){
        if(liSolutionsOfNormalization == null){
            liSolutionsOfNormalization = new ArrayList<String>();
            liSolutionsOfNormalization.add("Normalizasyon");
            liSolutionsOfNormalization.add("Standardizasyon");
        }
        return liSolutionsOfNormalization;
    }
    public HashMap<String, String> getMapSolutionOfNormalizationToInfo(){
        HashMap<String, String> values = new HashMap<String, String>();
        values.put("Normalizasyon", allInfos.get("Normalizasyon"));
        values.put("Standardizasyon", allInfos.get("Standardizasyon"));
        return values;
    }
    public String getInfoAboutSolutionOfNormalization(String solutionName){
        if(solutionName == null)
            return "";
        if(solutionName.isEmpty())
            return "";
        return getMapSolutionOfNormalizationToInfo().get(solutionName);
    }
    public ArrayList<String> getLiSolutionsForOutliner(){
        if(liSolutionsForOutliner == null){
            liSolutionsForOutliner = new ArrayList<String>();
            liSolutionsForOutliner.add("Z skoru");
        }
        return liSolutionsForOutliner;
    }
    public String getInfoForCoding(){
        return allInfos.get("Kategorik kodlama");
    }
    public String getInfoForWrongData(){
        return allInfos.get("Eksik veriler");
    }
    public String getInfoForStatistic(){
        return allInfos.get("İstatistikler");
    }
    public String getInfoForOutliner(){
        return allInfos.get("Aykırı veriler");
    }
    public String[] getOutputFileTypesAsStr(){
        String[] types = new String[getMapTextToSupportedFileTypes().keySet().size()];
        getMapTextToSupportedFileTypes().keySet().toArray(types);
        return types;
    }
    public HashMap<String, String> getMapTextToSupportedFileTypes(){
        if(mapTextToSupportedFileTypes == null){
            mapTextToSupportedFileTypes = new HashMap<String, String>();
            mapTextToSupportedFileTypes.put("Excel (xlsx)", "xlsx");
//            mapTextToSupportedFileTypes.put("JSON", "json");// Şimdilik yok
            mapTextToSupportedFileTypes.put("CSV", "csv");
        }
        return mapTextToSupportedFileTypes;
    }
    public IWriter getIWriterFromExtension(String extension){
        if(extension == null)
            return null;
        if(extension.isEmpty())
            return null;
        extension = extension.trim().toLowerCase();
        switch(extension){
            case "xlsx" : {
                return new XlsXWriter();
            }
            case "csv" :{
                return new CSVWriter();
            }
        }
        return null;
    }
}