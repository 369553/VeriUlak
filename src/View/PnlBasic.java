package View;

import Control.Add;
import Control.GUISeeming;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PnlBasic extends JPanel implements IGUIElementOnList{
    JLabel lblNameString;
    JTextField txtName;
    JButton btnEditOrSave;
    private String nameForIdentify;
    ActionListener act;
    GridBagLayout compOrder;
    private boolean isTextFieldIsEditable = false;
    private int colNumber;

    public PnlBasic(HashMap<String, Object> dataPack, ActionListener act){
        this.act = act;
        compOrder = new GridBagLayout();
        setLayout(compOrder);
        addGUIElements();
        changeContent(dataPack);
        GUISeeming.appGUI(this);
    }

//İŞLEM YÖNTEMLERİ:
    public void setEditableOfTextField(boolean isEditable){
        this.isTextFieldIsEditable = isEditable;
        getTxtName().setEditable(isEditable);
    }
    public void setNameForIdentity(String nameNew){
        nameForIdentify = nameNew;
    }
    public void setColNumber(int colNumber){
        this.colNumber = colNumber;
    }
    @Override
    public void changeContent(HashMap<String, Object> dataPack){
        nameForIdentify = (String) dataPack.get("name");
        getTxtName().setText(nameForIdentify);
        int number = (Integer) dataPack.get("number");
        if(colNumber != number)
            this.colNumber = number;
    }

    //GİZLİ ERİŞİM YÖNTEMLERİ:
    private void addGUIElements(){
        Add.addComp(this, getLblNameString(), 0, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, 0.2, 1);
        Add.addComp(this, getTxtName(), 1, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, 0.5, 1);
        Add.addComp(this, getBtnEditOrSave(), 2, 0, 1, 1, GridBagConstraints.EAST, GridBagConstraints.VERTICAL, 0.4, 1);
        
        
    }

//ERİŞİM YÖNTEMLERİ:
    public JLabel getLblNameString(){
        if(lblNameString == null){
            lblNameString = new JLabel("İsim:");
        }
        return lblNameString;
    }
    public JButton getBtnEditOrSave(){
        if(btnEditOrSave == null){
            btnEditOrSave = new JButton("DÜZENLE");
            btnEditOrSave.addActionListener(act);
        }
        return btnEditOrSave;
    }
    public JTextField getTxtName(){
        if(txtName == null){
            txtName = new JTextField();
            txtName.setEditable(false);
        }
        return txtName;
    }
    public ActionListener getAct(){
        return act;
    }
    public String getNameForIdentify(){
        return nameForIdentify;
    }
    public boolean getIsTextFieldIsEditable(){
        return isTextFieldIsEditable;
    }
    public int getColNumber(){
        return colNumber;
    }
}