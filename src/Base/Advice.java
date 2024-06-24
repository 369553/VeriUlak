package Base;

public class Advice{
    private String strAdvice;
    private String keywordOfAdvice;

    public Advice(String strAdvice, String keywordOfAdvice){
        this.strAdvice = strAdvice;
        this.keywordOfAdvice = keywordOfAdvice;
    }

//İŞLEM YÖNTEMLERİ:
    

//ERİŞİM YÖNTEMLERİ:
    public String getStrAdvice(){
        return strAdvice;
    }
    public String getKeywordOfAdvice(){
        return keywordOfAdvice;
    }
}