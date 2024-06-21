package View;

import Control.Add;
import Control.GUISeeming;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class PnlConfigsChangeDataType extends JPanel{
    JLabel lblHead;// Başlık
    JLabel lblDigitNumberText, lblApproachTypeText;
    JCheckBox chEncodeTypeText;
    GridBagLayout compOrder;// Görsel düzen
    JSpinner spinDigitNumber;// Noktalı sayıya çevirirken basamak sayısını belirlemek için
    JComboBox<String> cmboxApproachType;// Noktalı sayıdan tam sayıya geçişte sayı yuvarlama çeşidini belirtmek için

    public PnlConfigsChangeDataType(Class current, Class target){
        this();
        changeConversionType(current, target);
    }
    public PnlConfigsChangeDataType(){
        this.compOrder = new GridBagLayout();
        this.setLayout(compOrder);
        addGUIElements();
        GUISeeming.appGUI(this);
    }

//İŞLEM YÖNTEMLERİ:
    public JLabel getLblHead(){
        if(lblHead == null){
            lblHead = new JLabel("Dönüşüme mahsus ayarlar");
        }
        return lblHead;
    }
    public void changeConversionType(Class currentType, Class targetType){// Seçilen dönüşüm tipine göre konfigirüsyonlar
        this.removeAll();
        addGUIElements();
//        System.out.println("current : " + currentType.getName() + "\ttargetType : " + targetType.getName());
        if(currentType == Boolean.class && (targetType == Integer.class || targetType == Double.class)){
//            Add.addComp(this, getChEncodeTypeText(), 0, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
        }
        else if(targetType == Integer.class){
            Add.addComp(this, getLblApproachTypeText(), 0, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.NONE);
            Add.addComp(this, getCmboxApproachType(), 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE);
        }
        else if(targetType == Double.class){
            Add.addComp(this, getLblDigitNumberText(), 0, 1, 1, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH);
            Add.addComp(this, getSpinDigitNumber(), 1, 1, 1, 1, GridBagConstraints.EAST, GridBagConstraints.BOTH);
        }
        GUISeeming.appGUI(this);
        repaint();
    }
    //ARKAPLAN İŞLEM YÖNTEMLERİ:
    private void addGUIElements(){
        Add.addComp(this, getLblHead(), 0, 0, 2, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL);
    }

//ERİŞİM YÖNTEMLERİ:
    public JSpinner getSpinDigitNumber(){
        if(spinDigitNumber == null){
            spinDigitNumber = new JSpinner(new SpinnerNumberModel(4, 1, 7, 1));
            spinDigitNumber.setPreferredSize(new Dimension(110, 40));
        }
        return spinDigitNumber;
    }
    public JComboBox<String> getCmboxApproachType(){
        if(cmboxApproachType == null){
            cmboxApproachType = new JComboBox<String>(new String[]{"Aşağı yuvarlama", "Yukarı yuvarlama", "Normal yuvarlama"});
            cmboxApproachType.setSelectedIndex(2);
            cmboxApproachType.setPreferredSize(new Dimension(110, 40));
        }
        return cmboxApproachType;
    }
    public JLabel getLblApproachTypeText(){
        if(lblApproachTypeText == null){
            lblApproachTypeText = new JLabel("Sayı yuvarlama yaklaşımı:");
        }
        return lblApproachTypeText;
    }
    public JLabel getLblDigitNumberText(){
        if(lblDigitNumberText == null){
            lblDigitNumberText = new JLabel("Basamak sayısı:");
        }
        return lblDigitNumberText;
    }
    public JCheckBox getChEncodeTypeText(){
        if(chEncodeTypeText == null){
            chEncodeTypeText = new JCheckBox("'true' değerini 1 olarak kodla");
            chEncodeTypeText.setSelected(true);
        }
        return chEncodeTypeText;
    }
}