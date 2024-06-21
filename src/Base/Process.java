package Base;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class Process{
    public enum PROCESS_TYPE{
        UPDATE,
        ADD,
        DELETE,
        CONVERT,
        OTHER
    }
    Method method;// İşlemin uygulandığı yöntem
    ArrayList<Object> paramsOfMethod;// İşlem yönteminin girdi parametreleri
    PROCESS_TYPE type;// İşlemin tipi
    boolean isAppliedOnRow = false;// İşlem satır üzerinde uygulandıysa 'true' olmalı
    boolean isAppliedOnCol = false;// İşlem sütun üzerinde uygulandıysa 'true' olmalı
    int startPointOfRange = 0;// İşlem satır veyâ sütun üzerinde gerçekleştiyse, uygulandığı aralığın başlangıç noktası
    int endPointOfRange = 0;// İşlem satır veyâ sütun üzerinde gerçekleştiyse, uygulandığı aralığın bitiş noktası
    //String exp;// İşlemle ilgili açıklama
    HashMap<String, Object> otherInfo;// Diğer bilgiler, misal işlemden önceki veri bilgisi gibi bilgiler buraya konabilir

    public Process(Method method, ArrayList<Object> paramsOfMethod, PROCESS_TYPE type, boolean isAppliedOnRow, boolean isAppliedOnCol, int startPointOfRange, int endPointOfRange){
        this(method, paramsOfMethod);
        this.type = type;
        this.isAppliedOnRow = isAppliedOnRow;
        this.isAppliedOnCol = isAppliedOnCol;
        this.startPointOfRange = startPointOfRange;
        this.endPointOfRange = endPointOfRange;
    }
    public Process(Method method, ArrayList<Object> paramsOfMethod){
        this.method = method;
        this.paramsOfMethod = paramsOfMethod;
        this.type = PROCESS_TYPE.OTHER;
    }

//İŞLEM YÖNTEMLERİ:
    public void setOtherInfo(HashMap<String, Object> otherInfo){
        this.otherInfo = otherInfo;
    }
    

//ERİŞİM YÖNTEMLERİ:
    public Method getMethod(){
        return method;
    }
    public ArrayList<Object> getParamsOfMethod(){
        return paramsOfMethod;
    }
    public PROCESS_TYPE getType(){
        return type;
    }
    public boolean isIsAppliedOnRow(){
        return isAppliedOnRow;
    }
    public boolean isIsAppliedOnCol(){
        return isAppliedOnCol;
    }
    public int getStartPointOfRange(){
        return startPointOfRange;
    }
    public int getEndPointOfRange(){
        return endPointOfRange;
    }
    public HashMap<String, Object> getOtherInfo(){
        return otherInfo;
    }
}