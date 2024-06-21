package Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XlsXReader implements IReader{
    private File file;//İlgili dosya
    private Workbook book;//Excel kitâbı
    private boolean isInvalid = false;//Dosyanın geçerli bir Excel dosyası olup, olmadığı bilgisini tutmak için
    private Object[][] data;
    private Enum mode;
    private boolean isReaded = false;//'readData()' yönteminin daha önce çalıştırılıp, çalıştırılmadığını anlamak için
    private boolean isModeChanged = false;
    public enum ReadMode{
        EmptyCellAsNull,//Boş satırlar null olarak işâretlenir
        EmptyCellAsEmptyString
    }
    //private boolean isXlsx;

    public XlsXReader(File file){
        this.file = file;
        this.mode = XlsXReader.ReadMode.EmptyCellAsNull;
    }
    public XlsXReader(File file, ReadMode readMode){
        this.file = file;
        this.mode = readMode;
    }

//İŞLEM YÖNTEMLERİ:
    @Override
    public Object[][] getData(){
        return data;
    }
    @Override
    public boolean readData(){//Boş veriler NULL olarak işâretleniyor
        if(!isModeChanged){
            boolean check = checkFileType();
            if(!check){
                return false;
            }
        }
        Sheet xs = null;
        xs = book.getSheetAt(0);//KIRILMA NOKTASI = ...
        int lastRowNo = xs.getLastRowNum() + 1;
        //System.out.println("Son satır numarası : " + lastRowNo);
        if(lastRowNo == 0){
            //..
            return true;
        }
        data = new Object[lastRowNo][];
        for(int sayac = 0; sayac < lastRowNo; sayac++){
            Row row = xs.getRow(sayac);
            if(row == null){
                if(this.mode == ReadMode.EmptyCellAsEmptyString)
                    data[sayac] = new Object[]{""};
                continue;
            }
            int lastCellNo = row.getLastCellNum();
            //System.out.println("Son sütun numarası : " + lastCellNo);
            if(lastCellNo <= 0){
                //..
                System.out.println("Beklenmeyen bir hatâ : Son hücre numarası 0 veyâ eksi sayı çıktı");
                continue;//? uygun mu
            }
            Object[] rowData = new Object[lastCellNo];
            for(int s2 = 0; s2 < lastCellNo; s2++){
                Cell curr = row.getCell(s2);
                if(curr != null)
                    rowData[s2] = findCellValue(curr);
                else{
                if(this.mode == ReadMode.EmptyCellAsEmptyString)
                    data[sayac] = new Object[]{""};
                }
            }
            data[sayac] = rowData;
        }
        isReaded = true;
        return true;
    }
    @Override
    public String showLastError(){
        return null;
    }
    public void setMode(XlsXReader.ReadMode mode){
        Enum oldMode = this.mode;
        this.mode = mode;
        isModeChanged = true;
        applyModeChanges(oldMode, mode);//Değişikliklere göre veri üzerinde gereken işlemi yapmak için
    }
    //ARKAPLAN İŞLEM YÖNTEMLERİ:
    private boolean checkFileType(){
        if(file == null)
            return false;
        if(!file.isFile())
            return false;
        if(!file.canRead())
            return false;
        String[] parts = file.getName().split("\\.");
        boolean noExt = false;
        String ext = "";
        if(parts.length > 1){
            ext = parts[parts.length - 1];
        }
        else
            noExt = true;
        FileInputStream inStream;
       try{
            if(ext.equalsIgnoreCase("xlsx")){
                book = new XSSFWorkbook(file);
            }
            else{
                inStream = new FileInputStream(file);
                book = new HSSFWorkbook(inStream);
            }
            /*if(book.getClass().getName().equals(XSSFWorkbook.class.getName()))
                isXlsx = true;
            else
                isXlsx = false;*/
            return true;
       }
       catch(IOException exc){
           System.err.println("Dosya okuma yazma hatâsı");
           isInvalid = true;
           return false;
       }
       catch(InvalidFormatException exc){
           System.err.println("Format hatâsı");
           isInvalid = true;
           return false;
       }
    }
    private Object findCellValue(Cell cell){
        XSSFCell xssfCell;
        CellType type = cell.getCellTypeEnum();
        Object value = null;
//        System.err.println("Hücre tipi : " + type.name());
        if(type == CellType.NUMERIC){
            value = cell.getNumericCellValue();
        }
        else if(type == CellType.STRING){
            value = cell.getStringCellValue();
        }
        else if(type == CellType.BOOLEAN){
            value = cell.getBooleanCellValue();
        }
        else if(type == CellType.FORMULA){
            value = cell.getCellFormula();
        }
            return value;
        /*else if(type == CellType.BLANK){
            value = "";
        }
        else if(type == CellType._NONE){
            value = null;
        }*/
    }
    private void fillNullRows(){//Satırdaki tüm hücreler 'null' ise satır silinir; değilse bırakılır
        for(int sayac = 0; sayac < data.length; sayac++){
            if(data[sayac] == null){
                data[sayac] = new Object[]{""};
            }
            else{
                for(int s2 = 0; s2 < data[sayac].length; s2++){
                    data[sayac][s2] = "";
                }
            }
        }
    }
    private void applyModeChanges(Enum oldMode, Enum newMode){
        if(oldMode == ReadMode.EmptyCellAsNull && newMode == ReadMode.EmptyCellAsEmptyString){
            fillNullRows();//Veriyi tekrar okumadan 'null' değerleri boş metin("") ile değiştir
        }
        else if(oldMode == ReadMode.EmptyCellAsEmptyString)
            readData();//Veriyi tekrar oku; zîrâ değeri boş metîn olan hücreleri 'null' değerine çevirirsek veriyi bozmuş olabiliriz
        //Mod değiştirildikten sonra gereken değişiklik 'data' değişkenine yansıtıldı:
        isModeChanged = false;
    }

//ERİŞİM YÖNTEMLERİ:
    public boolean getIsReaded(){
        return isReaded;
    }
    //GİZLİ ERİŞİM YÖNTEMLERİ:
    private Workbook getBook(){
        if(!isInvalid)
            return null;
        if(book == null){
            checkFileType();
        }
        return book;
    }
}