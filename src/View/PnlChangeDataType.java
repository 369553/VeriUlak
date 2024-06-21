package View;

import Control.ActOnPnlChangeDataType;
import Control.Add;
import Control.GUISeeming;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

public class PnlChangeDataType extends JPanel{
    GridBagLayout compOrder;
    JLabel lblCurDT;// currentDataType, targetDataType
    JButton btnComplete/*, btnCancel*/;
    JCheckBox chChangeDataIfNeed;// Veride tam dönüşmeyen uyumsuzlukları görmezden gelerek veri tipini dönüştür
    JList<String> guiLiTargetTypes;
    DefaultListModel<String> mdlForLiTargetTypes;
    ActOnPnlChangeDataType act;
    int colNumber;
    PnlConfigsChangeDataType pnlConfigs;

    public PnlChangeDataType(Class currentDataType, int colNumber){
        this.act = new ActOnPnlChangeDataType(this, currentDataType);
        this.colNumber = colNumber;
        act.fillLiTargetTypes();
        this.compOrder = new GridBagLayout();
        this.setLayout(compOrder);
        addGUIElements();
        getGuiLiTargetTypes().setSelectedIndex(0);
//        getAct().setConfOnStarting();
        GUISeeming.appGUI(this);
    }

//İŞLEM YÖNTEMLERİ:
    private void addGUIElements(){
        Add.addComp(this, getLblCurDT(), 0, 0, 3, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, 0.0, 0.0);
        Add.addComp(this, getChChangeDataIfNeed(), 0, 1, 3, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, 0.0, 0.0);
        Add.addComp(this, getPnlConfigs(), 0, 2, 3, 3, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0.2, 0.2);
        
        Add.addComp(this, getGuiLiTargetTypes(), 3, 0, 1, 5, GridBagConstraints.EAST, GridBagConstraints.BOTH, 0.1, 0.1);
        Add.addComp(this, getBtnComplete(), 2, 6, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0.1, 0.1);
        //Add.addComp(this, getBtnCancel(), 3, 6, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0.1, 0.0);
    }

//ERİŞİM YÖNTEMLERİ:
    public JLabel getLblCurDT(){
        if(lblCurDT == null){
            lblCurDT = new JLabel("Mevcut veri tipi : " + getAct().getDataTypeToName().get(getAct().getDataType()));
        }
        return lblCurDT;
    }
    public JButton getBtnComplete(){
        if(btnComplete == null){
            btnComplete = new JButton("Tamâmla");
            btnComplete.addActionListener(getAct());
        }
        return btnComplete;
    }
    public JCheckBox getChChangeDataIfNeed(){
        if(chChangeDataIfNeed == null){
            chChangeDataIfNeed = new JCheckBox("Küçük uyumsuzlukları dikkate alma");
            chChangeDataIfNeed.setToolTipText("Misal, kesirden tam sayıya dönüştürürken kesir varsa ihmal edilmesi gibi");
        }
        return chChangeDataIfNeed;
    }
    public Class getDataType(){
        return getAct().getDataType();
    }
    public JList<String> getGuiLiTargetTypes(){
        if(guiLiTargetTypes == null){
            guiLiTargetTypes = new JList<String>();
            guiLiTargetTypes.setModel(getMdlForLiTargetTypes());
            guiLiTargetTypes.addListSelectionListener(getAct());
        }
        return guiLiTargetTypes;
    }
    public ActOnPnlChangeDataType getAct(){
        return act;
    }
    public DefaultListModel<String> getMdlForLiTargetTypes(){
        if(mdlForLiTargetTypes == null){
            mdlForLiTargetTypes = new DefaultListModel<String>();
        }
        return mdlForLiTargetTypes;
    }
    public int getColNumber(){
        return colNumber;
    }
    /*public JButton getBtnCancel(){
        if(btnCancel == null){
            btnCancel = new JButton("İPTAL ET");
            btnCancel.addActionListener(getAct());
        }
        return btnCancel;
    }*/
    public PnlConfigsChangeDataType getPnlConfigs(){
        if(pnlConfigs == null){
            // BURADA KALINDI
            pnlConfigs = new PnlConfigsChangeDataType();
        }
        return pnlConfigs;
    }
}