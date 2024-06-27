package View;

import Control.ActOnPnlDataSplitterSave;
import Control.Add;
import Control.GUIIdare;
import Control.GUISeeming;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class PnlDataSpliterSave extends JPanel{
    JLabel lblDataCount, lblTextOfTestRate;
    JSpinner spinTestRate;
    ActOnPnlDataSplitterSave act;
    GridBagLayout compOrder;
    JButton btnSaveData, btnOpenDirChooser;
    JComboBox<String> cmboxFileTypes;
    JFileChooser dirChooser;
    int size;// Veri büyüklüğü
    Dimension dimOfMP;// Ana görsel elemanın boyutları
    Dimension dim;// Görsel elemanlar için ortalama büyüklük

    public PnlDataSpliterSave(int dataSize){
        this.size = dataSize;
        act = new ActOnPnlDataSplitterSave(this);
        compOrder = new GridBagLayout();
        this.setLayout(compOrder);
        dimOfMP = act.getDimensionOfMainPanel();
        dim = new Dimension(dimOfMP.width / 3, dimOfMP.height / 7);
        addGUIElements();
        GUISeeming.appGUI(this);
        getLblDataCount().setFont(getLblDataCount().getFont().deriveFont(22.0f));
        getLblTextOfTestRate().setFont(getLblTextOfTestRate().getFont().deriveFont(14.0f));
    }

//İŞLEM YÖNTEMLERİ:
    //ARKAPLAN İŞLEM YÖNTEMLERİ:
    private void addGUIElements(){
        Add.addComp(this, getLblDataCount(), 0, 0, 2, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, 2.0, 2.0);
        Add.addComp(this, getLblTextOfTestRate(), 0, 1, 1, 1, GridBagConstraints.EAST, GridBagConstraints.BOTH, 0.0, 0.4);
        Add.addComp(this, getSpinRate(), 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 1.0, 0.4);
        Add.addComp(this, getCmboxFileTypes(), 0, 2, 2, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 1.0, 0.4);
        Add.addComp(this, getBtnOpenDirChooser(), 0, 3, 2, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 1.0, 0.4);
        Add.addComp(this, getBtnSaveData(), 0, 4, 2, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 1.0, 0.4);
    }

//ERİŞİM YÖNTEMLERİ:
    public JButton getBtnSaveData(){
        if(btnSaveData == null){
            btnSaveData = new JButton("Veriyi kaydet");
            btnSaveData.addActionListener(getAct());
            btnSaveData.setPreferredSize(dim);
        }
        return btnSaveData;
    }
    public JButton getBtnOpenDirChooser(){
        if(btnOpenDirChooser == null){
            btnOpenDirChooser = new JButton("Dosya yolu (dizin) seçin");
            btnOpenDirChooser.setPreferredSize(dim);
            btnOpenDirChooser.addActionListener(getAct());
        }
        return btnOpenDirChooser;
    }
    public JSpinner getSpinRate() {
        if(spinTestRate == null){
            spinTestRate = new JSpinner(new SpinnerNumberModel(0, 0, 100, 0.1));
            spinTestRate.setPreferredSize(dim);
        }
        return spinTestRate;
    }
    public JLabel getLblDataCount(){
        if(lblDataCount == null)
            lblDataCount = new JLabel("Veri büyüklüğü: " + size);
        return lblDataCount;
    }
    public ActOnPnlDataSplitterSave getAct() {
        return act;
    }
    public JComboBox<String> getCmboxFileTypes(){
        if(cmboxFileTypes == null){
            cmboxFileTypes = new JComboBox<String>(getAct().getNameOfFileTypes());
            cmboxFileTypes.setPreferredSize(dim);
        }
        return cmboxFileTypes;
    }
    public JLabel getLblTextOfTestRate(){
        if(lblTextOfTestRate == null){
            lblTextOfTestRate = new JLabel("Test verisi oranı (yüzde):");
        }
        return lblTextOfTestRate;
    }
    public JFileChooser getDirChooser(){
        if(dirChooser == null){
            dirChooser = new JFileChooser();
            dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            dirChooser.addActionListener(this.getAct());
            //GUISeeming.appGUI(fChooser);
        }
        return dirChooser;
    }
}