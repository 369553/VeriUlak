package Control;

import Base.DataAnalyzer;
import Service.CryptService;
import View.ContactPanel;
import View.PnlAdvices;
import View.PnlChooseData;
import View.PnlDataSpliterSave;
import View.PnlSideMenu;
import View.PnlTable;
import java.awt.Dimension;
import java.util.HashMap;

public class Stream{
    private int curr = 1;// Mevcut adım
    private IDARE idare;// Yazılım idârî birimi
    private HashMap<String, Object> packForNext;// Yeni işlem için gereken bilgilerin bulunduğu paket
    private DataAnalyzer analyzer;// Yazılımın ana işlemlerinin yapıldığı sınıf veri analizcisi
    private PnlTable screenOnStep2;// 2. adım ekranı
    private PnlChooseData screenOnStep1;// 1. adım ekranı

    protected Stream(IDARE idare){
    
    }

//İŞLEM YÖNTEMLERİ:
    protected void putPackForNextStep(HashMap<String, Object> pack){
        this.packForNext = pack;
    }
    protected boolean checkPackForNextStep(){
        if(curr == 1){// İYİLEŞTİRİLEBİLİR
            if(packForNext == null)
                return false;
            String ext = (String) packForNext.get("sourceFileExtension");
            String filePath = (String) packForNext.get("sourceFilePath");
            Object[][] data = (Object[][]) packForNext.get("data");
            if(data != null && ext != null && filePath != null)
                return true;
            return false;
        }
        // .;.
        return true;
    }
    protected boolean goToNext(String code){
        System.out.println("stream.goToNext");
        if(!CryptService.getService().getMd5(code).equals(IDARE.getIDARE().getRunningCodeAsMd5()))
            return false;
        if(curr == 1){
            curr++;
            this.analyzer = IDARE.getIDARE().getAnalyzer();
            if(analyzer == null){
                ContactPanel.getContactPanel().showMessage("Veri analizcisi oluşturulamadı! Veriyi kontrol edin", "Error");
                return false;
            }
            //KONTROL - 1 : Veri bomboş mu? (null mı?)
            if(analyzer.isDataNull()){
                ContactPanel.getContactPanel().showMessage("Veri bomboş! Veriyi kontrol edin!", "Error");
                return false;
            }
            
            Class[] dataTypes = analyzer.getDataTypes();// Veri tiplerini alıyor
            
            GUIIdare.getGUIIDARE().getTopMenu().getBtnBack().setEnabled(true);
            GUIIdare.getGUIIDARE().getTopMenu().getBtnNext().setEnabled(true);
            screenOnStep1 = (PnlChooseData) GUIIdare.getGUIIDARE().getCurr();
            // Veri tiplerini PnlTable'a gönder
            screenOnStep2 = new PnlTable(analyzer.getData(), analyzer.getColumnNames(), dataTypes);
            GUIIdare.getGUIIDARE().addToMP(screenOnStep2);
            HashMap<String, Object> vals = analyzer.getColumnsDetail().get(0);
            vals.put("statistic", analyzer.getStatisticForColumn(0));
            GUIIdare.getGUIIDARE().addSideMenu(new PnlSideMenu(vals), true);
            GUIIdare.getGUIIDARE().addSecondSideMenu(PnlAdvices.producePnlAdvices(null, IDARE.getIDARE().getActForAdvices()));
            GUIIdare.getGUIIDARE().closeSecondSideMenu();
            IDARE.getIDARE().triggerForSecondMenu(0);
            return true;
        }
        if(curr == 2){
            curr++;
            GUIIdare.getGUIIDARE().getTopMenu().getBtnBack().setEnabled(true);
            GUIIdare.getGUIIDARE().getTopMenu().getBtnNext().setEnabled(false);
            GUIIdare.getGUIIDARE().addToMP(new PnlDataSpliterSave(IDARE.getIDARE().getAnalyzer().getRowCount()));
            return true;
        }
        
        return false;
    }
    protected boolean goToBack(String code){
        if(!CryptService.getService().getMd5(code).equals(IDARE.getIDARE().getRunningCodeAsMd5()))
            return false;
            switch(curr){
                case 2 :{
                    GUIIdare.getGUIIDARE().addToMP(screenOnStep1);
                    --curr;
                    return true;
                }
                case 3 :{
                    GUIIdare.getGUIIDARE().addToMP(screenOnStep2);
                    HashMap<String, Object> vals = analyzer.getColumnsDetail().get(0);
                    vals.put("statistic", analyzer.getStatisticForColumn(0));
                    GUIIdare.getGUIIDARE().addSideMenu(new PnlSideMenu(vals), true);
                    GUIIdare.getGUIIDARE().addSecondSideMenu(PnlAdvices.producePnlAdvices(null, IDARE.getIDARE().getActForAdvices()));
                    GUIIdare.getGUIIDARE().closeSecondSideMenu();
                    IDARE.getIDARE().triggerForSecondMenu(0);
                    GUIIdare.getGUIIDARE().getTopMenu().getBtnBack().setEnabled(true);
                    GUIIdare.getGUIIDARE().getTopMenu().getBtnNext().setEnabled(true);
                    GUIIdare.getGUIIDARE().getTopMenu().getBtnAdvices().setEnabled(true);
                    --curr;
                    return true;
                }
            }
        // .;.
        return false;
    }

//ERİŞİM YÖNTEMLERİ:
    public int getCurrentStep(){
        return curr;
    }
}