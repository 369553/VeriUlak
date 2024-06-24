package Control;

import Base.DataB;
import Service.ClassStringDoubleConverter;
import View.ContactPanel;
import View.PnlChangeDataType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ActOnPnlChangeDataType implements ActionListener, ListSelectionListener{
    PnlChangeDataType pnl;
    HashMap<Class, String> dataTypeToName = DataB.getdBase().getDataTypeToName();
    private Class dType;// dataType, veri tipi
    private boolean otherProcessContinue = false;

    public ActOnPnlChangeDataType(PnlChangeDataType panel, Class dataType){
        this.pnl = panel;
        this.dType = dataType;
    }

//İŞLEM YÖNTEMLERİ:
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == pnl.getBtnComplete()){
            boolean changeDataIfNeed = pnl.getChChangeDataIfNeed().isSelected();
            int colNumber = pnl.getColNumber();
            String strTarget = pnl.getGuiLiTargetTypes().getSelectedValue();
            Class targetType = ClassStringDoubleConverter.getService().getClassFromShortName(strTarget);
            if(targetType == dType)
                return;
            HashMap<String, Object> confs = null;
            if(changeDataIfNeed){
                confs = new HashMap<String, Object>();
                if(targetType == Double.class && dType != Integer.class){// Tamsayıların kesirli kısmı olmadığından basamak sayısı bilgisine ihtiyaç yoktur
                    confs.put("rollDigitNumber", (int) pnl.getPnlConfigs().getSpinDigitNumber().getValue());
                }
                else if(targetType == Integer.class){
                    String str = (String) pnl.getPnlConfigs().getCmboxApproachType().getSelectedItem();
                    boolean rollToDown = true;
                    boolean rollToUp = true;
                    if(str.equals("Aşağı yuvarlama"))
                        rollToUp = false;
                    else if(str.equals("Yukarı yuvarlama"))
                        rollToDown = false;
                    confs.put("rollToUp", rollToUp);
                    confs.put("rollToDown", rollToDown);
                }
            }
            boolean isSuccess = IDARE.getIDARE().requestChangeDataType(colNumber, targetType, confs);
            if(isSuccess){
                dType = targetType;
                otherProcessContinue = true;
                pnl.getMdlForLiTargetTypes().removeAllElements();
                fillLiTargetTypes();
                pnl.getGuiLiTargetTypes().setSelectedIndex(0);
                pnl.getPnlConfigs().changeConversionType(dType, ClassStringDoubleConverter.getService().getClassFromShortName(pnl.getGuiLiTargetTypes().getSelectedValue()));
                pnl.getLblCurDT().setText("Mevcut veri tipi : " + getDataTypeToName().get(dType));
                // Ekranı kapatmak zorunda değilsin; ama kapatabilirsen iyi olur!!
                ContactPanel.getContactPanel().showMessage(colNumber + " indeksli sütunun veri tipi " + strTarget + " yapıldı", "Successful");
                otherProcessContinue = false;
            }
            else{
                ContactPanel.getContactPanel().showMessage("Dönüşüm işlemi başarısız oldu!", "Warning");
                // Veri tipini kontrol et!
            }
        }
        /*if(e.getSource() == pnl.getBtnCancel()){
            
        }*/
    }
    public void fillLiTargetTypes(){
        for(String str : DataB.getdBase().getListOfDataTypes()){// Tüm veri tiplerini ekle
            pnl.getMdlForLiTargetTypes().addElement(str);
        }
        String[] splitted = dType.getName().split("\\.");
        pnl.getMdlForLiTargetTypes().removeElement(splitted[splitted.length - 1]);// Mevcut veri tipini listeden çıkart
        // YAMA YAPILIYOR, PEK İYİ BİR ÇÖZÜM DEĞİL:
        // .;. Double ise || Integer ise Boolean'ı listeden çıkar; Boolean ise yalnızca String kalsın
    }
    @Override
    public void valueChanged(ListSelectionEvent e){
        if(e.getSource() == pnl.getGuiLiTargetTypes()){
            changeConfOfPnl();
        }
    }
    public void setConfOnStarting(){
        changeConfOfPnl();
    }
    //ARKAPLAN İŞLEM YÖNTEMLERİ:
    private void changeConfOfPnl(){
        if(otherProcessContinue)// Başka bir yöntemdeki işlemden dolayı burası istenmeyen şekilde tetiklendiyse işlem yapma
            return;
        String selected = pnl.getGuiLiTargetTypes().getSelectedValue();
        for(Class cls : getDataTypeToName().keySet()){
            String nameAsShort = ClassStringDoubleConverter.getService().getShortName(cls);
            if(nameAsShort.equals(selected)){
                if(cls == dType)
                    continue;
                pnl.getPnlConfigs().changeConversionType(pnl.getDataType(), cls);
//                return;
            }
        }
    }

//ERİŞİM YÖNTEMLERİ:
    public HashMap<Class, String> getDataTypeToName(){
        return dataTypeToName;
    }
    public Class getDataType(){
        return dType;
    }
}