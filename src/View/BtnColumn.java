package View;

import Control.GUISeeming;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class BtnColumn extends JButton{
    //private int idOfView;
    JLabel lblDataType, lblSizeOfData, lblColName;
    JButton btnOpenClose;// Sütun detayları panelini açıp, kapatmak için tuş
    String colName, strDataType;// Sütun ismi, Sütun veri tipi
    int size = 0;// Veri büyüklüğü (sayısı)
    ActionListener act;
    JPopupMenu popmenuCol;
    JMenuItem menuelementDelCol;

    public BtnColumn(String colName, String dataType, int size, ActionListener act){
        super();
        this.colName = colName;
        this.size = size;
        this.strDataType = dataType;
        this.act = act;
        this.setContentAreaFilled(true);// Bu ne işlem yapar?
        this.setPreferredSize(new Dimension(161, 45));
        setLayout(new BorderLayout(4, 4));
        this.add(getLblSizeOfData(), BorderLayout.WEST);
        this.add(getLblDataType(), BorderLayout.EAST);
        this.add(getLblColName(), BorderLayout.NORTH);
        this.addActionListener(act);
        GUISeeming.appGUI(this);
    }

//İŞLEM YÖNTEMLERİ:
    public void setSizeText(int size){
        this.size = size;
        getLblSizeOfData().setText(String.valueOf(size));
    }
    public void setDataTypeText(String strDataType){
        if(colName == null)
            strDataType = "";
        else
            this.strDataType = strDataType;
        getLblDataType().setText(strDataType);
    }
    public void setColumnNameText(String colName){
        if(colName == null)
            colName = "";
        else
            this.colName = colName;
        this.getLblColName().setText(colName);
    }
    public void updateFor(String name, String strDataType, int size){
        updateForSize(size);
        updateForDataType(strDataType);
        updateForName(name);
    }
    public void updateForSize(int size){
        setSizeText(size);
    }
    public void updateForDataType(String strDataType){
        setDataTypeText(strDataType);
    }
    public void updateForName(String name){
        setColumnNameText(name);
    }

//ERİŞİM YÖNTEMLERİ:
    public JLabel getLblDataType(){
        if(lblDataType == null){
            String[] splitted = strDataType.split("\\.");
            lblDataType = new JLabel(splitted[splitted.length - 1]);
        }
        return lblDataType;
    }
    public JLabel getLblSizeOfData(){
        if(lblSizeOfData == null){
            lblSizeOfData = new JLabel(String.valueOf(size));
        }
        return lblSizeOfData;
    }
    public ActionListener getAct(){
        return act;
    }
    public JLabel getLblColName(){
        if(lblColName == null){
            lblColName = new JLabel(String.valueOf(colName));
        }
        return lblColName;
    }
    public JPopupMenu getPopmenuCol(){
        if(popmenuCol == null){
            popmenuCol = new JPopupMenu("İşlemler");
            popmenuCol.add(getMenuelementDelCol());
            popmenuCol.setPreferredSize(new Dimension(150, 50));
            popmenuCol.setEnabled(true);
//            popmenuCol.setInvoker(this);
        }
        return popmenuCol;
    }
    public JMenuItem getMenuelementDelCol(){
        if(menuelementDelCol == null){
            menuelementDelCol = new JMenuItem("Sütunu sil");
            menuelementDelCol.setName("Sütunu sil");
            menuelementDelCol.addActionListener(getAct());
        }
        return menuelementDelCol;
    }
}