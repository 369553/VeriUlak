package Service;

public interface IWriter{
//İŞLEM YÖNTEMLERİ:
    public boolean writeData(String path, String fileName, Object[][] data);//Veriyi verilen adrese yazar
    public String showLastError();//Son hatâyı göster
}