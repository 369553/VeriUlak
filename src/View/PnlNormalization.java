package View;

import Control.ActOnPnlNormalization;
import Control.Add;
import Control.GUISeeming;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class PnlNormalization extends JPanel{
    ActOnPnlNormalization act;// İşlem idârecisi
    JButton btnComplete;// Tamâmlamak için tuş
    GridBagLayout compOrder;// Görsel düzen idârecisi
    PnlVariety pnlSolutions;// Çözümler için panel
    PnlVarietyForText pnlInfo;// Çözüm hakkında bilgi sunmak için panel

    public PnlNormalization(int colIndex){
        compOrder = new GridBagLayout();
        this.setLayout(compOrder);
        act = new ActOnPnlNormalization(this, colIndex);
        addGUIElements();
        GUISeeming.appGUI(this);
    }

//İŞLEM YÖNTEMLERİ:
    //ARKAPLAN İŞLEM YÖNTEMLERİ:
    private void addGUIElements(){
        Add.addComp(this, getPnlInfo(), 0, 0, 3, 3, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 1.0, 0.8);
        Add.addComp(this, getPnlSolutions(), 0, 3, 3, 3, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 1.0, 0.4);
        Add.addComp(this, getBtnComplete(), 1, 6, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 1.0, 0.2);
    }

//ERİŞİM YÖNTEMLERİ:
    public JButton getBtnComplete(){
        if(btnComplete == null){
            btnComplete = new JButton("Tamâmla");
            btnComplete.addActionListener(getAct());
        }
        return btnComplete;
    }
    public ActOnPnlNormalization getAct(){
        return act;
    }
    public PnlVariety getPnlSolutions(){
        if(pnlSolutions == null){
            pnlSolutions = new PnlVariety(getAct().getLiSolutions(), "Yöntemler");
            pnlSolutions.setSingleSelection();
            pnlSolutions.getGuiLiContent().addListSelectionListener(getAct());
        }
        return pnlSolutions;
    }
    public PnlVarietyForText getPnlInfo(){
        if(pnlInfo == null){
            pnlInfo = new PnlVarietyForText("Yöntem hakkında bilgi", getAct().getInfoAboutSolution());
        }
        return pnlInfo;
    }
}