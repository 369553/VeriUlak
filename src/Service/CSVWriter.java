package Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class CSVWriter implements IWriter{
    private ArrayList<String> errors;

    public CSVWriter(){
    }

//İŞLEM YÖNTEMLERİ:
    @Override
    public boolean writeData(String path, String fileName, Object[][] data){
        File target = new File(path + "\\" + fileName);
        FileOutputStream outStream = null;
        try{
            boolean isSuccess = target.createNewFile();
            if(!isSuccess)
                throw new IOException("Hedef dizinde verilen isimde dosya oluşturulamadığından veriler kaydedilemedi");
            System.out.println("Dosya oluşturuldu : " + fileName);
        }
        catch(IOException exc){
            System.err.println(exc.getMessage());
            getErrors().add(exc.getMessage());
            return false;
        }
        StringBuilder strbuiContent = new StringBuilder();
        for(int sayac = 0; sayac < data.length; sayac++){
            if(sayac != 0)
                strbuiContent.append("\n");
            for(int s2 = 0; s2 < data[sayac].length; s2++){
                if(data[sayac][s2] != null)
                    strbuiContent.append(String.valueOf(data[sayac][s2]));
                if(s2 != data[sayac].length - 1)
                    strbuiContent.append(",");
            }
        }
        //Verileri dosyaya yaz:
        try{
            outStream = new FileOutputStream(target);
            RWService.getService().writeFile(target, strbuiContent.toString());
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
        return null;
    }

//ERİŞİM YÖNTEMLERİ:
    public ArrayList<String> getErrors(){
        if(errors == null)
            errors = new ArrayList<String>();
        return errors;
    }
    
}