package View;

import Control.ActOnPnlTopMenu;
import Control.GUISeeming;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class PnlTopMenu extends JPanel{
    MnPreferences menu;
    JButton btnNext, btnBack;
    ActOnPnlTopMenu act;
    int vGap = 2, hGap = 7;//hGap = horizontalGap, vGap = verticalGap
    FlowLayout compOrder;

    public PnlTopMenu(){
        this.setLayout(getCompOrder());
        this.add(getBtnBack());
        this.add(getBtnNext());
        /*this.setPreferredSize(new Dimension(((int) GUIIdare.getGUIIDARE().getDimensionOfMainPanel().getWidth()),
                ((int) GUIIdare.getGUIIDARE().getDimensionOfMainPanel().getHeight() / 14)));*/
        GUISeeming.appGUI(this);
    }

//İŞLEM YÖNTEMLERİ:
    

//ERİŞİM YÖNTEMLERİ:
    public FlowLayout getCompOrder(){
        if(compOrder == null){
            compOrder = new FlowLayout(FlowLayout.RIGHT, hGap, vGap);
        }
        return compOrder;
    }

    public JButton getBtnBack(){
        if(btnBack == null){
            btnBack = new JButton("GERİ");
            btnBack.setPreferredSize(new Dimension(42, 25));
            btnBack.addActionListener(getAct());
            btnBack.setEnabled(false);
        }
        return btnBack;
    }
    public JButton getBtnNext(){
        if(btnNext == null){
            btnNext = new JButton("İLERİ");
            btnNext.setPreferredSize(new Dimension(42, 25));
            btnNext.addActionListener(getAct());
            btnNext.setEnabled(false);
        }
        return btnNext;
    }
    public ActOnPnlTopMenu getAct(){
        if(act == null){
            act = new ActOnPnlTopMenu(this);
        }
        return act;
    }
}