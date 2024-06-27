package Service;

public interface IWriter{
//İŞLEM YÖNTEMLERİ:
    public boolean writeData(String path, String fileName, Object[][] data);//Veriyi verilen adrese yazmalı
    public boolean writeData(String path, String fileName, Object[][] data, String[] columnNames);// Veriyi yazar; hedef dosyanın ilk satırına sütun isimlerini ('columnNames') yazmalı
    public String showLastError();//Son hatâyı göstermeli
}