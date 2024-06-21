package View;

import Control.ActOnPnlCoding;
import Control.Add;
import Control.GUISeeming;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

public class PnlCoding extends JPanel{
    PnlVariety pnlSolutions;// Çözümler için panel
    JTable tblData;// Verileri göstermek için tablo
    JScrollPane scrpaneForTblColData;// Tabloyu kaydırabilmek için kaydırılabilir panel
    DefaultTableModel mdlForTblColData;// Sütun verilerini tutan tablonun modeli
    JButton btnComplete;// 'Tamâmla' tuşu
    ActOnPnlCoding act;// İşlem idârecisis
    GridBagLayout compOrder;// Bu sınıfın görsel düzeni
    JLabel lblTextOfDataType;// 'Veri tipi' yazısı için JLabel
    JPanel pnlConfigs;// Kodlama işlemi yapılandırma ayarı için panel
    JCheckBox chCodeTrueAsOne;// 'true' değerlerini '1' olarak kodlama seçeneği için seçilebilir arayüz elemanı
    JCheckBox chCode1AsTrue;// '1' değerlerini 'true' olarak kodlama seçeneği için seçilebilir arayüz elemanı
    PnlVarietyForOrderingTheList pnlSpecialConf;

    public PnlCoding(Object[] rawData, String headTextOfCol, Class dataType){
        this.act = new ActOnPnlCoding(this, rawData, headTextOfCol, dataType);
        compOrder = new GridBagLayout();
        this.setLayout(compOrder);
        addGUIElements();
        setTableData(act.getRawData(), act.getHeadTextOfRaw());
        GUISeeming.appGUI(this);
    }

//İŞLEM YÖNTEMLERİ:
    public void setTableData(Object[] colData, String colName){
        Object[][] asRow = new Object[colData.length][1];
        for(int sayac = 0; sayac < colData.length; sayac++){
            asRow[sayac][0] = colData[sayac];
        }
        getMdlForColData().setDataVector(asRow, new String[]{colName});
    }
   public void setConfigurationsOfCoding(String codingName){
       boolean clear = false;
       if(codingName == null)
           clear = true;
       else if(codingName.isEmpty())
           clear = true;
       getPnlConfigs().removeAll();// Konfigürasyon panelindeki her şeyi kaldır
       if(clear)// Bir kodlama tipi gönderilmediyse işlemi bitir
           return;
       switch(codingName){
           case "Bitsel kodlama" :{
                if(getAct().getDataType() == Boolean.class)
                    Add.addComp(getPnlConfigs(), getChCodeTrueAs1(), 0, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
                else
                    Add.addComp(getPnlConfigs(), getChCode1AsTrue(), 0, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
                break;
           }
           case "Sıralı kodlama" :{
               pnlSpecialConf = new PnlVarietyForOrderingTheList(getAct().getUniqueDataAsStringList(), "Sıralı kodlama dizilimi");
               Add.addComp(getPnlConfigs(), pnlSpecialConf, 0, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
               break;
           }
//           case "Tek nokta vektörü (One Hot Encoding) biçiminde kodlama" :{
//               // Yapılacak bir yapılandırma ayarı yok
//               break;
//           }//           case "Tek nokta vektörü (One Hot Encoding) biçiminde kodlama" :{
//               // Yapılacak bir yapılandırma ayarı yok
//               break;
//           }
       }
       GUISeeming.appGUI(this.getPnlConfigs());
       getPnlConfigs().repaint();
       this.setVisible(true);
   }
    // ARKAPLAN İŞLEM YÖNTEMLERİ:
    private void addGUIElements(){
        Add.addComp(this, getLblTextOfDataType(), 0, 0, 1, 1, GridBagConstraints.LINE_START, GridBagConstraints.NONE, 0.0, 0.0);
        Add.addComp(this, getScrpaneForTblColData(), 0, 1, 3, 3, GridBagConstraints.WEST, GridBagConstraints.BOTH, 0.1, 0.05);
        Add.addComp(this, getPnlSolutions(), 3, 1, 2, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0.5, 0.3);
        Add.addComp(this, getPnlConfigs(), 3, 2, 2, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0.5, 0.3);
        Add.addComp(this, getBtnComplete(), 4, 3, 2, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0.1, 0.05);
    }

//ERİŞİM YÖNTEMLERİ:
    public JScrollPane getScrpaneForTblColData(){
        if(scrpaneForTblColData == null){
            scrpaneForTblColData = new JScrollPane(getTblData(),
                    ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        }
        return scrpaneForTblColData;
    }
    public JTable getTblData(){
        if(tblData == null){
            tblData = new JTable(getMdlForColData());
            tblData.setCellSelectionEnabled(false);
            tblData.setColumnSelectionAllowed(false);
        }
        return tblData;
    }
    public DefaultTableModel getMdlForColData(){
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
    public ActOnPnlCoding getAct(){
        return act;
    }
    public GridBagLayout getCompOrder(){
        return compOrder;
    }
    public PnlVariety getPnlSolutions(){
        if(pnlSolutions == null){
            pnlSolutions = new PnlVariety(getAct().getLiSolutions(), "Çözümler");
            pnlSolutions.getGuiLiContent().addListSelectionListener(getAct());
        }
        return pnlSolutions;
    }
    public JLabel getLblTextOfDataType() {
        if(lblTextOfDataType == null){
            lblTextOfDataType = new JLabel("Veri tipi : ");
        }
        return lblTextOfDataType;
    }
    public JPanel getPnlConfigs(){
        if(pnlConfigs == null){
            pnlConfigs =  new JPanel(new GridBagLayout());
        }
        return pnlConfigs;
    }
    public JCheckBox getChCodeTrueAs1(){
        if(chCodeTrueAsOne == null){
            chCodeTrueAsOne = new JCheckBox("'true' değerini '1' olarak kodla");
            chCodeTrueAsOne.setSelected(true);
        }
        return chCodeTrueAsOne;
    }
    public JCheckBox getChCode1AsTrue(){
        if(chCode1AsTrue == null){
            chCode1AsTrue = new JCheckBox("'1' değerini 'true' olarak kodla");
            chCode1AsTrue.setSelected(true);
        }
        return chCode1AsTrue;
    }
    public PnlVarietyForOrderingTheList getPnlSpecilConf(){
        return pnlSpecialConf;
    }
    public PnlVarietyForOrderingTheList getPnlSpecialConf(){
        return pnlSpecialConf;
    }
    
}