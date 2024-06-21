package Service;

public interface IReader{
//İŞLEM YÖNTEMLERİ:
    public Object[][] getData();//[Satır][Sütun] biçiminde veriyi döndür
    public boolean readData();//Veriyi oku
    public String showLastError();//Son hatâyı göster
}