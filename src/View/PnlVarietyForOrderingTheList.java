package View;

import Control.Add;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;

public class PnlVarietyForOrderingTheList extends PnlVariety implements ActionListener{
    JButton btnMoveUp, btnMoveDown;// Seçili elemana listede yukarı aşağı ilerletmek için

    public PnlVarietyForOrderingTheList(ArrayList<String> liContent, String headText){
        super(liContent, headText);
        super.setSingleSelection();
        
    }

//İŞLEM YÖNTEMLERİ:
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == this.getBtnShowHide()){
            isListOpened = !isListOpened;
            getScrpaneContent().setVisible(isListOpened);
            getBtnMoveUp().setVisible(isListOpened);
            getBtnMoveDown().setVisible(isListOpened);
            if(getBtnShowHide().getText().equals(">"))
                getBtnShowHide().setText("<");
            else
                getBtnShowHide().setText(">");
            return;
        }
        int selectedIndex = getGuiLiContent().getSelectedIndex();
        int targetIndex = selectedIndex;
        if(selectedIndex == -1)// Hiçbir eleman seçilmediyse
            return;
        if(e.getSource() == getBtnMoveDown()){
            if(selectedIndex == getLiContent().size() - 1)
                return;
            targetIndex = selectedIndex + 1;
        }
        else if(e.getSource() == getBtnMoveUp()){
            if(selectedIndex == 0)
                return;
            targetIndex = selectedIndex - 1;
        }
//        liOrderOfLiContent = MatrixFunctions.exchangeElementOnTheList(liOrderOfLiContent, String[].class, selectedIndex, targetIndex);
        String value = liContent.get(selectedIndex);
        this.liContent.remove(selectedIndex);
        this.liContent.add(targetIndex, value);
        reReplaceContent();
        this.getGuiLiContent().setSelectedIndex(targetIndex);
    }
    @Override
    protected void addMainElements(){// Düğmeleri ekle
        Add.addComp(this, getBtnMain(), 0, 0, 3, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, 0.1, 0.0);
        Add.addComp(this, getScrpaneContent(), 0, 1, 2, 5, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0.1, 0.2);
        Add.addComp(this, getBtnMoveUp(), 2, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 0.05, 0.0);
        Add.addComp(this, getBtnMoveDown(), 2, 2, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, 0.05, 0.0);
    }

//ERİŞİM YÖNTEMLERİ:
    public JButton getBtnMoveDown(){
        if(btnMoveDown == null){
            btnMoveDown = new JButton("Aşağı taşı");
            btnMoveDown.setPreferredSize(new Dimension(30, 40));
            btnMoveDown.addActionListener(this);
        }
        return btnMoveDown;
    }
    public JButton getBtnMoveUp(){
        if(btnMoveUp == null){
            btnMoveUp = new JButton("Yukarı taşı");
            btnMoveUp.setPreferredSize(new Dimension(30, 40));
            btnMoveUp.addActionListener(this);
        }
        return btnMoveUp;
    }
}