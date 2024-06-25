package Control;

import Base.DataB;
import Base.Statistic;
import Service.ClassStringDoubleConverter;
import View.ContactPanel;
import View.PnlBasic;
import View.PnlChangeDataType;
import View.PnlCoding;
import View.PnlInterestWrongData;
import View.PnlManipulations;
import View.PnlNormalization;
import View.PnlSideMenu;
import View.PnlStatistic;
import View.PnlVarietyForText;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JButton;

public class ActOnPnlSideMenu implements ActionListener, KeyListener{
    private PnlSideMenu SM;
    private boolean isNumber;
    private HashMap<String, Object> dataPack;// Gösterilmesi gereken verileri tutan değişken
    private int colNumber = 0;
    private HashMap<String, Method> mapMethodsFromManipulations;// Her bir işlem için hangi yöntemin çalıştırılacağının tutulduğu harita

    public ActOnPnlSideMenu(PnlSideMenu sideMenu, HashMap<String, Object> dataPack){
        this.SM = sideMenu;
        this.dataPack = dataPack;
        this.isNumber = (Boolean) dataPack.get("isNumber");
        this.colNumber = (Integer) dataPack.get("number");
    }

//İŞLEM YÖNTEMLERİ:
    @Override
    public void actionPerformed(ActionEvent e){
        Object obj = e.getSource();
        Object topObj = null;
        if(obj == SM.getBtnMain()){
            SM.getBtnMain().getPopmenuCol().show(SM.getBtnMain(), 40, 50);
            return;
        }
        else if(obj == SM.getPnlStats().getBtnMain()){
            GUIIdare.getGUIIDARE().getActiveStructsIDARE().showSpecialGUI(new PnlVarietyForText("Bilgilendirme", DataB.getdBase().getInfoForStatistic()), "İstatistik verileri hakkında", "", 290, 210); 
        }
        else if(obj == SM.getPnlBasic().getBtnEditOrSave()){
            boolean isEditables = SM.getPnlBasic().getIsTextFieldIsEditable();
            if(isEditables){
                // yeni sütun ismini kaydet
                String newName = SM.getPnlBasic().getTxtName().getText();
                String oldName = (String) dataPack.get("name");
                boolean isSuccess = IDARE.getIDARE().requestChangingNameOfColumn(oldName, newName);
                if(!isSuccess){
                    ContactPanel.getContactPanel().showMessage("Sütun ismi değiştirilemedi", "Warning");
                    return;
                }
                SM.getPnlBasic().getBtnEditOrSave().setText("DÜZENLE");
            }
            else{
                SM.getPnlBasic().getBtnEditOrSave().setText("KAYDET");
            }
            SM.getPnlBasic().setEditableOfTextField(!isEditables);
            SM.getPnlBasic().getTxtName().setEditable(!isEditables);
            return;
        }
        if(obj == SM.getBtnMain().getMenuelementDelCol()){
            boolean isConfirmed = GUIIdare.getGUIIDARE().getActiveStructsIDARE().showYesNoQuestion(colNumber + " indeksli sütunu gerçekten silmek istiyor musunuz?", "Onay");
            if(!isConfirmed)
                return;
            boolean isSuccess = IDARE.getIDARE().requestDeleteColumn(colNumber);
            if(!isSuccess)
                ContactPanel.getContactPanel().showMessage("Sütun silinemedi!", "Error");
            else
                ContactPanel.getContactPanel().showMessage(colNumber + " indisli sütun silindi!", "Successful");
            // JTable güncellenmeli
            // Sütun bilgilerinde değişiklik
            // PnlSideMenu başka bir sütun bilgisini göstermeli
            HashMap<Integer, HashMap<String, Object>> mapAllCols = IDARE.getIDARE().getColumnsDetail();
            if(mapAllCols.size() == 0){
                updateDataFor(null);
                // JTable sıfırlanmalı
                // BURADA KALINDI
            }
            this.dataPack = mapAllCols.get(0);// Başa dönmemesi için iyileştirilebilir
            dataPack.put("statistic", IDARE.getIDARE().getStatisticsForColumn(0));
            this.colNumber = 0;
            SM.getBtnSlideToBack().setEnabled(false);
            if(mapAllCols.size() > 1)
                SM.getBtnSlideToNext().setEnabled(true);
            updateDataFor();
            return;
        }
        if(obj == SM.getBtnSlideToBack() || obj == SM.getBtnSlideToNext()){
            int maxNumber = IDARE.getIDARE().getAnalyzer().getColumnCount();
            if(obj == SM.getBtnSlideToBack()){
                if(colNumber == 0)// İlk sütundan önce sütun olamaz
                    return;
                SM.getBtnSlideToNext().setEnabled(true);// Tek sütun yoksa ileriye geçildiğinde 'ileri tuşu' aktif edilmeli
                this.colNumber -= 1;// Sütun numarasını bir azalt
            }
            else{// obj == SM.getBtnSlideToNext()
                if(colNumber ==  -1)//Burası MaxColNumber olmalı
                    return;
                SM.getBtnSlideToBack().setEnabled(true);// Tek sütun yoksa ileriye geçildiğinde 'geri tuşu' aktif edilmeli
                this.colNumber += 1;// Sütun numarasını bir arttır
            }
            this.dataPack = IDARE.getIDARE().getColumnDetails(colNumber);// ESKİ YÖNTEM
//            System.err.println("colNumber : " + colNumber);
            dataPack.put("statistic", IDARE.getIDARE().getStatisticsForColumn(colNumber));// ESKİ YÖNTEM
            this.updateDataFor();// ESKİ YÖNTEM
            IDARE.getIDARE().triggerForSecondMenu(colNumber);
            if(colNumber == 0)
                SM.getBtnSlideToBack().setEnabled(false);
            if(colNumber == maxNumber - 1)
                SM.getBtnSlideToNext().setEnabled(false);
            return;
        }
//        else
//            return;
        try{
            topObj = ((Component) obj).getParent();
        }
        catch(Exception exc){
            System.err.println("exc.toString : " + exc.toString());
        }
        if(topObj != null){// Sinyal alt sınıflardan birinden gelmiş gibi...
            if(topObj.getClass() == PnlBasic.class){// Sinyal PnlBasic'teki bir elemandan gelmiş
                
            }
            else if(topObj.getClass() == PnlStatistic.class){// Sinyal PnlStatistic'teki bir elemandan gelmiş
                
            }
            else if(topObj == SM.getPnlManp().getPnlContent()){// Sinyal PnlManipulations'taki bir elemandan..
                PnlManipulations pnlManp = SM.getPnlManp();
                String processName = null;
                int numberOfBtn = -1;
                for(int sayac = 0; sayac < pnlManp.getBtnList().size(); sayac++){
                    JButton btn = pnlManp.getBtnList().get(sayac);
                    if(e.getSource() == pnlManp.getBtnList().get(sayac)){
                        processName = btn.getText();
                        numberOfBtn = sayac;
                    }
                }
                if(processName != null){
                    Method toRun = getMapMethodsFromManipulations().get(processName);
                    if(toRun == null)
                        return;
                    try{
                        toRun.invoke(this, null);
                        // .;. : Yöntemi çalıştır (Veri üzerinde ilgili işlemi yapmak için gerekli ekran açılması vs)
                    }
                    catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException exc){
                        System.out.println("(ActOnPnlSideMenu . actionPerformerd)exc.toString : " + exc.toString());
                    }
                }
            }
        }
    }
    public void updateDataFor(HashMap<String, Object> processDataPack){
        if(processDataPack.get("processType") == null){
            this.dataPack = IDARE.getIDARE().getLastDataPack();
            updateDataFor();
        }
        String processType = (String) processDataPack.get("processType");
        if(processType.equals("changeColName")){
            int colNumber = (int) IDARE.getIDARE().getLastDataPack().get("number");
            if(colNumber == this.colNumber){
                String name = (String) IDARE.getIDARE().getLastDataPack().get("name");
                SM.getBtnMain().setColumnNameText(name);
                this.getDataPack().put("name", name);
            }
        }
        if(processType.equals("changeCellData")){
            int colNumber = (int) IDARE.getIDARE().getLastDataPack().get("number");
            if(colNumber == this.colNumber){
                this.dataPack = IDARE.getIDARE().getLastDataPack();
                updateDataFor();
            }
        }
        if(processType.equals("changeDataType")){
            int colNumber = (int) IDARE.getIDARE().getLastDataPack().get("number");
            if(colNumber == this.colNumber){
                this.dataPack = IDARE.getIDARE().getLastDataPack();
                updateDataFor();
            }
            else{
                SM.getBtnMain().updateForDataType(ClassStringDoubleConverter.getService().getShortName((String) IDARE.getIDARE().getLastDataPack().get("dataType")));
                this.dataPack.put("dataType", processDataPack.get("dataType"));
            }
        }
        else if(processType.equals("fillEmptyCells")){
            int colNumber = (int) IDARE.getIDARE().getLastDataPack().get("number");
            if(colNumber == this.colNumber){
                this.dataPack = IDARE.getIDARE().getLastDataPack();
                updateDataFor();
            }
        }
        else if(processType.equals("codeColumn")){
            int colNumber = (int) IDARE.getIDARE().getLastDataPack().get("number");
            if(colNumber == this.colNumber){
                this.dataPack = IDARE.getIDARE().getLastDataPack();
                updateDataFor();
            }
        }
        else if(processType.equals("normalizeColumn")){
            int colNumber = (int) IDARE.getIDARE().getLastProccessInfo().get("colNumber");
            if(this.colNumber == colNumber){
                this.dataPack = IDARE.getIDARE().getLastDataPack();
                updateDataFor();
            }
        }
    }
    public void updateDataFor(){
        if(this.dataPack == null){
            SM.getBtnMain().removeAll();
            SM.getPnlStats().removeAll();
            SM.getPnlManp().removeAll();
//            SM.getPnlBasic().removeAll();
            return;
        }
        boolean isNumber = (Boolean) dataPack.get("isNumber");
        String[] splitted = ((String) dataPack.get("dataType")).split("\\.");
        boolean isCategorical = (boolean) dataPack.get("isCategorical");
        SM.getBtnMain().updateFor((String) dataPack.get("name"), splitted[splitted.length - 1], (Integer) dataPack.get("size"));
        SM.getPnlBasic().changeContent(dataPack);
        if(!isCategorical){
//            System.err.println("normal görünüyor");
            SM.getPnlStats().setHeadText("İstatistikler");
            SM.getPnlStats().changeContent((Statistic) dataPack.get("statistic"));
        }
        else{
//            System.err.println("kategorik görünüyor");
            SM.getPnlStats().setHeadText("Kategorik istatistikler");
            SM.getPnlStats().changeContent((ArrayList<String>) dataPack.get("categoricalStatistic"));
        }
        if(this.isNumber != isNumber){
        this.isNumber = isNumber;
            SM.getPnlManp().changeContent(getListOfManipulationsAsStr(), isNumber);
        }
        this.colNumber = (Integer) dataPack.get("number");
    }
    public ArrayList<String> getListOfManipulationsAsStr(){
        return DataB.getdBase().getManipulationsFor(isNumber);
    }
    public ArrayList<String> getFullListOfManipulationsAsStr(){
        ArrayList<String> fullList = new ArrayList<String>();
        for(String str : DataB.getdBase().getManipulationsForNumber()){
            fullList.add(str);
        }
        for(String str : DataB.getdBase().getManipulationsForString()){
            if(fullList.indexOf(str) == -1)
                fullList.add(str);
        }
        return fullList;
    }
    @Override
    public void keyTyped(KeyEvent e){
        //.;.
    }
    @Override
    public void keyPressed(KeyEvent e){
        //.;.
    }
    @Override
    public void keyReleased(KeyEvent e){
        //.;.
    }
    //ARKAPLAN İŞLEM YÖNTEMLERİ:
    private void openPnlChangeDataType(){// Veri tipini dönüştürmek için paneli aç
        String dataTypeClassName = (String) dataPack.get("dataType");
        Class cls = null;
        try{
            cls = Class.forName(dataTypeClassName);
        }
        catch(ClassNotFoundException exc){
            System.out.println("exc.toString : " + exc.toString());
            return;
        }
        GUIIdare.getGUIIDARE().getActiveStructsIDARE().showSpecialGUI(new PnlChangeDataType(cls, colNumber), "Veri tipini değiştir", "", 420, 250);
    }
    private void openPnlInterestWrongData(){// Eksik verilerin işlenmesi için paneli aç
        GUIIdare.getGUIIDARE().getActiveStructsIDARE().showSpecialGUI(new PnlInterestWrongData(IDARE.getIDARE().getColumnNames()), "Eksik verilerle ilgilen", "", 450, 370);
    }
    private void openPnlCoding(){// Sütunları kodlamak için paneli aç
        GUIIdare.getGUIIDARE().getActiveStructsIDARE().showSpecialGUI(new PnlCoding(IDARE.getIDARE().getColumnData(colNumber),  (String) this.getDataPack().get("name"), ClassStringDoubleConverter.getService().getClassFromFullName((String) dataPack.get("dataType"))), "Sütunları kodla", "", 450, 370);
    }
    private void openPnlNormalization(){// Sayısal veriler için normalizasyon ve standardizasyon işlemi
        GUIIdare.getGUIIDARE().getActiveStructsIDARE().showSpecialGUI(new PnlNormalization(colNumber), "Normalizasyon ve standardizasyon", "", 390, 310);
    }

//ERİŞİM YÖNTEMLERİ:
    public boolean getIsNumber(){
        return isNumber;
    }
    public HashMap<String, Object> getDataPack(){
        return dataPack;
    }
    public HashMap<String, Method> getMapMethodsFromManipulations(){
        if(mapMethodsFromManipulations == null){
            mapMethodsFromManipulations = new HashMap<String, Method>();
            try{
                 mapMethodsFromManipulations.put("Veri tipini dönüştür", this.getClass().getDeclaredMethod("openPnlChangeDataType", null));
                 mapMethodsFromManipulations.put("Eksik verilerle ilgilen", this.getClass().getDeclaredMethod("openPnlInterestWrongData", null));
                 mapMethodsFromManipulations.put("Sütunu kodla", this.getClass().getDeclaredMethod("openPnlCoding", null));
                 mapMethodsFromManipulations.put("Normalizasyon ve standardizasyon", this.getClass().getDeclaredMethod("openPnlNormalization", null));
            }
           catch(NoSuchMethodException | SecurityException exc){
               System.err.println("yöntem alınırken hatâ alındı : " + exc.toString());
           }
        }
        return mapMethodsFromManipulations;
    }
}