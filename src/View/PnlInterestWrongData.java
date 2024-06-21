package View;

import Control.ActOnPnlInterestWrongData;
import Control.Add;
import Control.GUISeeming;
import Control.KeyListenerForDataType;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

public class PnlInterestWrongData extends JPanel{
    ActOnPnlInterestWrongData act;
    PnlVariety pnlColumns;// Sütun isimleri kısmı
    JTable tblColData;// Sütun verileri paneli
    DefaultTableModel mdlForTblColData;// Sütun verilerini tutan tablonun modeli
    JButton btnComplete;// 'Tamâmla' tuşu
    PnlVariety pnlSolutions;// Eksik veriyi doldurmak için ilgili yöntemlerin yer aldığı panel
    GridBagLayout compOrder;// Bu sınıfın görsel düzeni
    JScrollPane scrpaneForTblColData;// Sütun verileri tablosu için kaydırılabilir panel
    JLabel lblTextOfDataType;// Sütunun veri tipini belirtmek için
    ArrayList<String> liColNames;// Sütun isimleri listesi
    JTextField txtValue;// 'Özel değer'in girilmesi için yazı alanı
    JLabel lblValueText;// 'Özel değer'in' girilmesi gerektiğini belirten yazı
    KeyListenerForDataType keyActForTxtValue;

    public PnlInterestWrongData(ArrayList<String> columnNames){
        this.liColNames = columnNames;
        this.act = new ActOnPnlInterestWrongData(this);
        compOrder = new GridBagLayout();
        this.setLayout(compOrder);
        addGUIElements();
        GUISeeming.appGUI(this);
        getAct().setHighlightColor();
    }

//İŞLEM YÖNTEMLERİ:
    public void setColData(Object[] colData, String colName, Class dataType){
        Object[][] asRow = new Object[colData.length][1];
        for(int sayac = 0; sayac < colData.length; sayac++){
            asRow[sayac][0] = colData[sayac];
        }
        getMdlForTblColData().setDataVector(asRow, new String[]{colName});
    }
    public void refresh(){
        this.repaint();
        this.setVisible(true);
    }
    //ARKAPLAN İŞLEM YÖNTEMLERİ:
    private void addGUIElements(){
        Add.addComp(this, getLblTextOfDataType(), 0, 0, 1, 1, GridBagConstraints.LINE_START, GridBagConstraints.NONE, 0.0, 0.0);
        Add.addComp(this, getScrpaneForTblColData(), 0, 1, 2, 4, GridBagConstraints.WEST, GridBagConstraints.BOTH, 0.1, 0.05);
        Add.addComp(this, getPnlColumns(), 2, 1, 1, 2, GridBagConstraints.EAST, GridBagConstraints.BOTH, 0.05, 0.05);
        Add.addComp(this, getPnlSolutions(), 2, 3, 1, 2, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0.1, 0.3);
        Add.addComp(this, getLblValueText(), 0, 5, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, 0.0, 0.0);
        Add.addComp(this, getTxtValue(), 1, 5, 2, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0.05, 0.05);
        Add.addComp(this, getBtnComplete(), 1, 6, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0.1, 0.05);
        getTxtValue().setVisible(true);
        getTxtValue().setEnabled(false);
    }

//ERİŞİM YÖNTEMLERİ:
    public ActOnPnlInterestWrongData getAct(){
        return act;
    }
    public PnlVariety getPnlColumns(){
        if(pnlColumns == null){
            pnlColumns = new PnlVariety(liColNames, "Sütunlar");
            pnlColumns.setSingleSelection();
            pnlColumns.getGuiLiContent().addListSelectionListener(act);
        }
        return pnlColumns;
    }
    public GridBagLayout getCompOrder(){
        return compOrder;
    }
    public JScrollPane getScrpaneForTblColData(){
        if(scrpaneForTblColData == null){
            scrpaneForTblColData = new JScrollPane(getTblColData(),
                    ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        }
        return scrpaneForTblColData;
    }
    public JTable getTblColData(){
        if(tblColData == null){
            tblColData = new JTable(getMdlForTblColData());
            tblColData.setCellSelectionEnabled(true);
            tblColData.setColumnSelectionAllowed(false);
        }
        return tblColData;
    }
    public DefaultTableModel getMdlForTblColData(){
        if(mdlForTblColData == null){
            mdlForTblColData = new DefaultTableModel(){
                @Override
                public boolean isCellEditable(int row, int column){
                    return false;
                }
            };
        }
        return mdlForTblColData;
    }
    public JButton getBtnComplete(){
        if(btnComplete == null){
            btnComplete = new JButton("TAMÂMLA");
            btnComplete.addActionListener(getAct());
        }
        return btnComplete;
    }
    public PnlVariety getPnlSolutions(){
        if(pnlSolutions == null){
            pnlSolutions = new PnlVariety(getAct().getListOfProcesses(), "Çözümler");
            pnlSolutions.setSingleSelection();
            pnlSolutions.getGuiLiContent().addListSelectionListener(act);
        }
        return pnlSolutions;
    }
    public JLabel getLblTextOfDataType(){
        if(lblTextOfDataType == null){
            lblTextOfDataType = new JLabel("Veri tipi : ");
        }
        return lblTextOfDataType;
    }
    public JTextField getTxtValue(){
        if(txtValue == null){
            txtValue = new JTextField();
            txtValue.setToolTipText("Değeri giriniz");
            txtValue.setPreferredSize(new Dimension(170, 35));
            keyActForTxtValue = new KeyListenerForDataType(txtValue);
        }
        return txtValue;
    }
    public KeyListenerForDataType getKeyActForTxtValue(){
        return keyActForTxtValue;
    }
    public JLabel getLblValueText(){
        if(lblValueText == null){
            lblValueText = new JLabel("Özel değer : ");
        }
        return lblValueText;
    }
}