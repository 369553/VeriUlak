package View;

import Control.ActOnPnlChooseData;
import Control.Add;
import Control.GUIIdare;
import Control.GUISeeming;
import Control.IDARE;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

public class PnlChooseData extends JPanel{
    JButton btnOpenChooser;
    JCheckBox chIsRowsAreData;
    ActOnPnlChooseData act;
    GridBagLayout compOrder;
    JFileChooser fChooser;

    public PnlChooseData(){
        compOrder = new GridBagLayout();
        this.setLayout(compOrder);
        Add.addComp(this, getBtnOpenChooser(), 0, 0, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.CENTER);
        Add.addComp(this, getChIsRowsAreData(), 0, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.CENTER);
        GUISeeming.appGUI(this);
    }

//İŞLEM YÖNTEMLERİ:
    

//ERİŞİM YÖNTEMLERİ:
    public ActOnPnlChooseData getAct(){
        if(act == null){
            act = new ActOnPnlChooseData(this);
        }
        return act;
    }
    public JButton getBtnOpenChooser(){
        if(btnOpenChooser == null){
            btnOpenChooser = new JButton("Veri setini yükleyin");
            Dimension dimOfMP = GUIIdare.getGUIIDARE().getDimensionOfMainPanel();
//            System.out.println("MainPanel ölçüsü :\nGenişlik : " + dimOfMP.width + "\tYükseklik : " + dimOfMP.height);
            btnOpenChooser.setPreferredSize(new Dimension(dimOfMP.width / 4, dimOfMP.height / 7));
            btnOpenChooser.addActionListener(getAct());
        }
        return btnOpenChooser;
    }
    public JFileChooser getfChooser(){
        if(fChooser == null){
            fChooser = new JFileChooser();
            fChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fChooser.addActionListener(this.getAct()) ;
            fChooser.setAcceptAllFileFilterUsed(true);
            fChooser.setFileFilter(IDARE.getIDARE().getDataSetFileFilter());
            //GUISeeming.appGUI(fChooser);
        }
        return fChooser;
    }
    public GridBagLayout getCompOrder(){
        return compOrder;
    }
    public JCheckBox getChIsRowsAreData(){
        if(chIsRowsAreData == null){
            chIsRowsAreData = new JCheckBox("Veriler satır olarak sıralanmış");
            chIsRowsAreData.setSelected(true);
        }
        return chIsRowsAreData;
    }
}