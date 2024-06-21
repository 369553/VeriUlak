package View;

import Control.Add;
import Control.GUISeeming;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PnlInfoStickForTable extends JPanel{
    private ActionListener act;
    private JLabel lblCellDType;//Hücredeki verinin tipini göstermek için
    private JLabel lblRowCount;//Toplam satır sayısını göstermek için
    private JLabel lblColCount;//Toplam sütun sayısını göstermek için
    private JLabel lblCellLoc;// Seçili hücrenin veri içerisindeki mevkîsini göstermek için
    private JButton btnEditAndSave;
    private GridBagLayout compOrder;//Görsel düzen
    private int hGap = 2, vGap = 2;
    private int rowCount, colCount;
    private String strRow, strCol, strLoc, strDType;

    public PnlInfoStickForTable(ActionListener actionsForInfoOfTable){
        this.act = actionsForInfoOfTable;
        assignStrValues();
        this.setLayout(getCompOrder());
        Add.addComp(this, getLblRowCount(), 0, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 1.0, 1.0);
        Add.addComp(this, getLblColCount(), 1, 0, 1, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, 1.0, 1.0);
        Add.addComp(this, getLblCellLocation(), 2, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 1.0, 1.0);
        Add.addComp(this, getLblCellDataType(), 3, 0, 2, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 1.0, 1.0);
        Add.addComp(this, getBtnEditAndSave(), 5, 0, 1, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, 1.0, 1.0);
        GUISeeming.appGUI(this);
    }

//İŞLEM YÖNTEMLERİ:
    public void setColCountString(int colCount){
        this.colCount = colCount;
        getLblColCount().setText(strCol + colCount);
    }
    public void setRowCountString(int rowCount){
        this.rowCount = rowCount;
        getLblRowCount().setText(strRow + rowCount);
    }
    public void setCellDataTypeString(Class dataType){
        String str = strDType + "";
        if(dataType == null)
            str += "null";
        else{
            String[] strParts = dataType.getName().split("\\.");
            str += strParts[strParts.length - 1];
        }
        getLblCellDataType().setText(str);
    }
    public void setCellLocationString(int row, int col){
        String str = strLoc + "[" + row + "][" + col + "]";
        getLblCellLocation().setText(str);
    }
    //ARKAPLAN İŞLEM YÖNTEMLERİ:
    private void assignStrValues(){
        strRow = "Satır sayısı : ";
        strCol = "Sütun sayısı : ";
        strLoc = "Seçili hücre : ";
        strDType = "Seçili değerin veri tipi : ";
    }

//ERİŞİM YÖNTEMLERİ:
    public ActionListener getAct(){
        return act;
    }
    public JLabel getLblCellDataType(){
        if(lblCellDType == null){
            lblCellDType = new JLabel(strDType);
            lblCellDType.setToolTipText(strDType);
        }
        return lblCellDType;
    }
    public JLabel getLblRowCount(){
        if(lblRowCount == null){
            lblRowCount = new JLabel(strRow);
            lblRowCount.setToolTipText(strRow);
        }
        return lblRowCount;
    }
    public JLabel getLblColCount(){
        if(lblColCount == null){
            lblColCount = new JLabel(strCol);
            lblColCount.setToolTipText(strCol);
        }
        return lblColCount;
    }
    public JLabel getLblCellLocation(){
        if(lblCellLoc == null){
            lblCellLoc = new JLabel(strLoc);
            lblCellLoc.setToolTipText(strLoc);
        }
        return lblCellLoc;
    }
    public JButton getBtnEditAndSave(){
        if(btnEditAndSave == null){
            btnEditAndSave = new JButton("DÜZENLE");
            btnEditAndSave.addActionListener(getAct());
        }
        return btnEditAndSave;
    }
    public GridBagLayout getCompOrder(){
        if(compOrder == null){
            compOrder = new GridBagLayout();
        }
        return compOrder;
    }
    public int getColCount(){
        return colCount;
    }
    public int getRowCount(){
        return rowCount;
    }
}