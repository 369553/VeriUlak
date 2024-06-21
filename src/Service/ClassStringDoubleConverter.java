package Service;

public class ClassStringDoubleConverter{
    private static ClassStringDoubleConverter serv;

    private ClassStringDoubleConverter(){}

//İŞLEM YÖNTEMLERİ:
    public Class getClassFromShortName(String shortName){
        return getClassFromFullName("java.lang." + shortName);
    }
    public Class getClassFromFullName(String fullName){
        Class cls = null;
        try{
            cls = Class.forName(fullName.trim());
        }
        catch(ClassNotFoundException e){
            System.out.println("Şu isim için sınıf bulunamadı : " + fullName);
        }
        return cls;
    }
    public String getShortNameFromObject(Object data){
        return getShortName(data.getClass());
    }
    public String getShortName(Class cls){
        if(cls == null)
            return "null";
        return getShortName(cls.getName());
    }
    public String getShortName(String fullName){
        if(fullName == null)
            return "null";
        String[] splitted = fullName.split("\\.");
        return splitted[splitted.length - 1];
    }
    public boolean checkIsNumber(Class cls){// Verilen sınıf sayı sınıfı ise (int, double, short, long, float) 'true' değerini döndürür
        if(cls == null)
            return false;
        if(cls == Number.class)
            return true;
        if(cls.getSuperclass() != null){
            if(cls.getSuperclass() == Number.class)
                return true;
        }
        return false;
    }
    public boolean checkIsNumber(Object value){// Verilen değişkenin tipi sayı ise (int, double, short, long, float) 'true' değerini döndürür
        if(value == null)
            return false;
        return checkIsNumber(value.getClass());
    }

//ERİŞİM YÖNTEMLERİ:
    //ANA ERİŞİM YÖNTEMİ:
    public static ClassStringDoubleConverter getService(){
        if(serv == null)
            serv = new ClassStringDoubleConverter();
        return serv;
    }
}