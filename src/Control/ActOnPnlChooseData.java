package Control;

import Service.IReader;
import Service.RWService;
import View.ContactPanel;
import View.PnlChooseData;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import javax.swing.JPanel;

public class ActOnPnlChooseData implements ActionListener{
    private PnlChooseData pnl;// İşlemlerin alındığı görsel eleman
    private String path = "C:\\Users\\Yazılım alanı\\Documents";
    private File file;// Veri dosyası
    private IReader reader;// Dosya okuyucusu
    private Class clsReaderClass;// Dosya uzantısına göre oluşturulması gereken okuyucunun (IReader) sınıfı
    private Class acceptedTypeFromReader;// Okuyucunun yapıcı fonksiyonunda istediği değişkenin sınıfı
    private Object objDataForReader = null;// IReader okuyucusuna verilmesi gereken veriyi tek değişken üzerinden vermek için
    private String ext = null;// Dosya uzantısı
    private InteractiveGUIStructs iActiveGUI;// Kolay erişim için saklanan bir referans değişken, kullanıcıya soru sormak gibi etkileşimli işlemlerde kullanılıyor

    public ActOnPnlChooseData(PnlChooseData pnl){
     this.pnl = pnl;
    }

//İŞLEM YÖNTEMLERİ:
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == pnl.getBtnOpenChooser()){
            openChooser("Veri seti seç");
        }
        if(e.getSource() == pnl.getfChooser()){
            this.file = pnl.getfChooser().getSelectedFile();//Dosyayı al
            if(file == null)
                return;
            boolean isSuccessful = false;
            isSuccessful = RWService.getService().canReadableForData(file);//Dosyayı oku (dosya kontrolü, okuma izni kontrolü)
            if(!isSuccessful){
                ContactPanel.getContactPanel().showMessage("Dosya okunamadı", "Error");
                return;
            }
            reader = null;
            String[] nameParts = file.getPath().split("\\.");
            ext = nameParts[nameParts.length - 1];// Dosya uzantısı alınıyor
            if(!isFileTypeRecognized(ext)){// Dosya uzantısı yoksa veyâ bilinen tiplerde bir uzantı değilse
                JPanel liPanel = GUIIdare.getGUIIDARE().getActiveStructsIDARE().prepareSpecialListView(IDARE.getIDARE().getAcceptableExtensions(), true, "Seçimi tamâmla");
                GUIIdare.getGUIIDARE().getActiveStructsIDARE().showSpecialGUI(liPanel, "Dosya tipi", "Dosyanın uzantısını seçiniz:");
                ext = iActiveGUI.getSelectedOne();
                if(ext == null)
                    return;
                if(ext.isEmpty())
                    return;
            }
            clsReaderClass = IDARE.getIDARE().getIReaderClassDependExtension(ext);
            if(clsReaderClass == null){
                ContactPanel.getContactPanel().showMessage("Dosya tipi hatâsı : Dosya uzantısı doğru değil", "Error");
                //Tanınan veri tiplerini göster
                return;
            }
            acceptedTypeFromReader = IDARE.getIDARE().getAcceptedTypeOfIReader(clsReaderClass);
            objDataForReader = null;
            if(acceptedTypeFromReader == String.class)
                objDataForReader = RWService.getService().readDataAsText(file);
            else if(acceptedTypeFromReader == File.class)
                objDataForReader = file;
            try{
                reader = (IReader) clsReaderClass.getConstructor(acceptedTypeFromReader).newInstance(objDataForReader);
            }
            catch(NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException exc){
                ContactPanel.getContactPanel().showMessage("Veri okuyucu oluşturulurken hatâ alındı", "Error");
                return;
            }
            if(reader == null){
                ContactPanel.getContactPanel().showMessage("Veri okuyucusu oluşturulması hatâsı", "Error");
                return;
            }
            boolean isSuccess = reader.readData();
            if(!isSuccess){
                ContactPanel.getContactPanel().showMessage("Veri okunamadı! Dosyayı kontrol edin", "Error");
                return;
            }
            boolean isRowAsData = pnl.getChIsRowsAreData().isSelected();
            Object[][] data = reader.getData();
            if(!isRowAsData){
                data = transpoze(data);
            }
            if(data == null){
                ContactPanel.getContactPanel().showMessage("Veride sıkıntı var; veriyi kontrol edin", "Error");
                return;
            }
            HashMap<String, Object> pack = new HashMap<String, Object>();
            pack.put("data", reader.getData());
            pack.put("sourceFileExtension", ext);
            pack.put("sourceFilePath", file.getAbsolutePath());
            boolean isFirstRowAsColumnNames = GUIIdare.getGUIIDARE().getActiveStructsIDARE().showYesNoQuestion("Verinin ilk satırı sütun isimlerinden mi oluşuyor?", "Analiz öncesi");
            boolean autoDetect = GUIIdare.getGUIIDARE().getActiveStructsIDARE().showYesNoQuestion("Veri tiplerinin otomatik tespit edilip, atanmasını ister misiniz?", "Analiz öncesi");
            pack.put("isFirstRowAsColumnNames", isFirstRowAsColumnNames);
            pack.put("autoDetect", autoDetect);
            reset();
            pnl.getfChooser().setSelectedFile(null);
            IDARE.getIDARE().goToNext(pack);
        }
    }
    //ARKAPLAN İŞLEM YÖNTEMLERİ:
    private void openChooser(String text){
        pnl.getfChooser().showDialog(this.pnl, text);
    }
    private Object[][] transpoze(Object[][] data){//BU YÖNTEM HAKKINDA ŞÜPHELERİM VAR, TEST EDİLMELİ
        int lastColCount = -1;
        for(int sayac = 0; sayac < data.length; sayac++){
            if(data[sayac] == null)
                continue;
            if(data[sayac].length > lastColCount)
                lastColCount = data[sayac].length;
        }
        if(lastColCount <= 0)
            return null;
        Object[][] value = new Object[lastColCount][];
        for(int sayac = 0; sayac < data.length; sayac++){
            if(data[sayac] == null)
                continue;
            for(int s2 = 0; s2 < data[sayac].length; sayac++){
                value[s2][sayac] = data[sayac][s2];
            }
        }
        return value;
    }
    private boolean isFileTypeRecognized(String ext){
        if(ext == null)
            return false;
        if(ext.isEmpty())
            return false;
        for(String s : IDARE.getIDARE().getAcceptableExtensions()){
            if(s.equalsIgnoreCase(ext))
                return true;
        }
        return false;
    }
    private void reset(){
        this.file = null;
        this.reader = null;
    }

//ERİŞİM YÖNTEMLERİ:
    protected IReader getReader(){
        return reader;
    }
}