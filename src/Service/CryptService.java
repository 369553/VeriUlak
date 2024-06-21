package Service;

public class CryptService{
    private static CryptService service;

    private CryptService(){
        
    }

//İŞLEM YÖNTEMLERİ:
    public String getMd5(String text){
        return text;
    }

//ERİŞİM YÖNTEMLERİ:
    //ANA ERİŞİM YÖNTEMİ:
    public static CryptService getService(){
        if(service == null){
            service = new CryptService();
        }
    return service;
    }
}