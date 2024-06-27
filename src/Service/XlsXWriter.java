package Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookType;

public class XlsXWriter implements IWriter{
    private ArrayList<String> errors;

    public XlsXWriter(){
        
    }

//İŞLEM YÖNTEMLERİ:
    @Override
    public boolean writeData(String path, String fileName, Object[][] data){
        return writeData(path, fileName, data, null);
    }
    @Override
    public boolean writeData(String path, String fileName, Object[][] data, String[] columnNames){
        XSSFWorkbook book = null;
        FileOutputStream outStream = null;
        File target = new File(path + "\\" + fileName);
        try{
            boolean isSuccess = target.createNewFile();
            if(!isSuccess)
                throw new IOException("Hedef dizinde verilen isimde dosya oluşturulamadığından veriler kaydedilemedi");
            System.out.println("Dosya oluşturuldu : " + fileName);
            book = new XSSFWorkbook(XSSFWorkbookType.XLSX);
        }
        catch(IOException exc){
            System.err.println(exc.getMessage());
            getErrors().add(exc.getMessage());
            return false;
        }
        XSSFSheet sheet = book.createSheet();
        int rowCounter = 0;
        //Tabloyu verilerle doldur:
        if(columnNames != null){// Eğer sütun ismi verilmişse ilk satıra sütun isimlerini yaz
            XSSFRow row = sheet.createRow(0);
            for(int s2 = 0; s2 < columnNames.length; s2++){
                XSSFCell cell = row.createCell(s2, CellType.STRING);
                if(columnNames[s2] != null)
                    cell.setCellValue(columnNames[s2]);
                else
                    cell.setCellType(CellType.BLANK);
            }
            rowCounter++;
        }
        for(int sayac = 0; sayac < data.length; sayac++){
            XSSFRow row = sheet.createRow(rowCounter);
            for(int s2 = 0; s2 < data[sayac].length; s2++){
                CellType type = getCellType(data[sayac][s2]);
                XSSFCell cell = row.createCell(s2, type);
                if(type == CellType.NUMERIC){// Tamsayıları tam sayı olarak yazamıyorum = ?
                    Number asNum = (Number) data[sayac][s2];
                    cell.setCellValue(asNum.doubleValue());
                }
                else if(type == CellType.BLANK){// Bunu kontrol et; doğru bir işlem mi?
                    cell.setCellValue("");
                }
                else if(type == CellType.STRING)
                    cell.setCellValue(String.valueOf(data[sayac][s2]));
            }
            rowCounter++;
        }
        
        //Verileri dosyaya yaz:
        try{
            outStream = new FileOutputStream(target);
            book.write(outStream);
            outStream.close();
        }
        catch(IOException ex){
            System.err.println("Verileri dosyaya yazma işlemi başarısız!");
            try{
                outStream.close();
            }
            catch(IOException closeEx){
                System.out.println("Yazma akış borusunu kapatırken hatâ alındı : " + closeEx.getLocalizedMessage());
                return false;
            }
            target.delete();
            return false;
        }
        return true;
    }
    @Override
    public String showLastError(){
        if(getErrors().size() > 0)
            return getErrors().get(getErrors().size() - 1);
        return "";
    }
    //ARKAPLAN İŞLEM YÖNTEMLERİ:
    private CellType getCellType(Object value){
        if(value == null)
            return CellType.BLANK;
        Class type = value.getClass();
        boolean isNumber = false;
        boolean setStr = false;
        if(type == Number.class)
            isNumber = true;
        else if(type.getClass().getSuperclass() != null){
            if(type.getClass().getSuperclass() == Number.class)
                isNumber = true;
            else
                setStr = true;
        }
        else if(type == String.class || type == CharSequence.class || type == Character.class)
            setStr = true;
        //Hücre tipini döndür:
        if(isNumber)
            return CellType.NUMERIC;
        if(setStr)
            return CellType.STRING;
        return CellType.BLANK;// CellType.BLANK = ? | CellType.NONE = ?
    }

//ERİŞİM YÖNTEMLERİ:
    public ArrayList<String> getErrors(){
        if(errors == null)
            errors = new ArrayList<String>();
        return errors;
    }
}