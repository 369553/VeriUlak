package View;

import Control.ActOnPnlDetectOutlier;
import Control.Add;
import Control.GUISeeming;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

public class PnlDetectOutlier extends JPanel{
    ActOnPnlDetectOutlier act;
    JTable tblColData;// Sütun aykırılık skorlarını göstermek için görsel eleman
    DefaultTableModel mdlForTblColData;// Sütun verilerini tutan tablonun modeli
    JButton btnDeleteOutliners;// 'Tamâmla' tuşu
    PnlVariety pnlSolutions;// Eksik veriyi doldurmak için ilgili yöntemlerin yer aldığı panel
    GridBagLayout compOrder;// Bu sınıfın görsel düzeni
    JScrollPane scrpaneForTblColData;// Sütun verileri tablosu için kaydırılabilir panel
    PnlVarietyForText pnlInfo;// Bilgilendirme için
    JSpinner spinRate;// Aykırı verilerin yüzde kaçının silineceğini seçmek için görsel eleman
    private int numberOfIndexesFromRate = 0;// Seçilen yüzdelik sayıya karşılık gelen satır sayısı

    public PnlDetectOutlier(Object[] colData, String columnName, Class dataType, int colIndex){
        this.act = new ActOnPnlDetectOutlier(this, colData, colIndex, dataType);
        compOrder = new GridBagLayout();
        this.setLayout(compOrder);
        addGUIElements();
        GUISeeming.appGUI(this);
        setColData(colData, columnName, dataType);
        updateBtnRowNumber();
        getAct().setHighlightColor();
        getAct().updateSelectedSolution();
        getAct().highLightOutliers();
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
    public int getCountOfOutliers(){
        return getAct().getCountOfOutliers();
    }
    public void updateBtnRowNumber(){
        getBtnDeleteOutliers().setText("En aykırı " + getCountOfOutliers() + " satırı sil");
    }
    //ARKAPLAN İŞLEM YÖNTEMLERİ:
    private void addGUIElements(){
        Add.addComp(this, getScrpaneForTblColData(), 0, 0, 2, 5, GridBagConstraints.WEST, GridBagConstraints.BOTH, 0.1, 0.05);
        Add.addComp(this, getPnlSolutions(), 2, 0, 2, 2, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0.1, 0.3);
        Add.addComp(this, getBtnDeleteOutliers(), 2, 3, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0.05, 0.05);
        Add.addComp(this, getSpinRate(), 3, 3, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0.05, 0.05);
        Add.addComp(this, getPnlInfo(), 2, 4, 2, 2, GridBagConstraints.WEST, GridBagConstraints.BOTH, 0.1, 0.05);
        getBtnDeleteOutliers().setFont(getBtnDeleteOutliers().getFont().deriveFont(12.0f));
    }

//ERİŞİM YÖNTEMLERİ:
    public ActOnPnlDetectOutlier getAct(){
        return act;
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
    public JButton getBtnDeleteOutliers(){
        if(btnDeleteOutliners == null){
            btnDeleteOutliners = new JButton();
            btnDeleteOutliners.addActionListener(getAct());
        }
        return btnDeleteOutliners;
    }
    public PnlVariety getPnlSolutions(){
        if(pnlSolutions == null){
            pnlSolutions = new PnlVariety(getAct().getLiNameOfsolutions(), "Çözümler");
            pnlSolutions.setSingleSelection();
            pnlSolutions.getGuiLiContent().setSelectedIndex(0);
            pnlSolutions.getGuiLiContent().addListSelectionListener(act);
        }
        return pnlSolutions;
    }
    public PnlVarietyForText getPnlInfo(){
        if(pnlInfo == null){
            pnlInfo = new PnlVarietyForText("Bilgilendirme", getAct().getInfoText());
        }
        return pnlInfo;
    }
    public JSpinner getSpinRate(){
        if(spinRate == null){
            spinRate = new JSpinner(new SpinnerNumberModel(0, 0, 100, 0.1));
            spinRate.addChangeListener(getAct());
        }
        return spinRate;
    }
}