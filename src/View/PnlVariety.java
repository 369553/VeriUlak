package View;

import Control.Add;
import Control.GUISeeming;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

public class PnlVariety extends JPanel implements ActionListener{
    JList<String> guiLiContent;
    DefaultListModel<String> mdlGUIList;
    JScrollPane scrpaneContent;
    JButton btnMain;
    JButton btnShowHide;
    JPanel pnlContent;
    GridBagLayout compOrder;
    short hGap = 2, vGap = 3;
    boolean isListOpened = true;
    ArrayList<String> liContent;
    String headText;

    public PnlVariety(ArrayList<String> content, String headText){
        this.headText = headText;
        this.liContent = content;
        compOrder = new GridBagLayout();
        this.setLayout(compOrder);
        addMainElements();
        addGUIElements();
        GUISeeming.appGUI(this);
    }

//İŞLEM YÖNTEMLERİ:
    protected void addMainElements(){
        Add.addComp(this, getBtnMain(), 0, 0, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, 0.1, 0.0);
        Add.addComp(this, getScrpaneContent(), 0, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0.1, 0.2);
    }
    @Override
    public void actionPerformed(ActionEvent e){// Tuşa basıldığında açıp, kapatmak için
        if(e.getSource() == getBtnShowHide()){
            isListOpened = !isListOpened;
            getScrpaneContent().setVisible(isListOpened);
            if(getBtnShowHide().getText().equals(">"))
                getBtnShowHide().setText("<");
            else
                getBtnShowHide().setText(">");
        }
    }
    public void setMinimumSize(int width, int height){//120, 330
        this.setMinimumSize(new Dimension(width, height));
    }
    public void addGUIElements(){// Verileri yerleştirmek için
        int sayac = 0;
        for(String str : getLiContent()){
            getMdlGUIList().add(sayac, str);
            sayac++;
        }
    }
    public void changeContent(ArrayList<String> content){
        if(content == null)
            content = new ArrayList<String>();
        this.liContent = content;
        this.reReplaceContent();
    }
    public void reReplaceContent(){// Veri değiştiğinde yeniden yerleştirmek için
        getMdlGUIList().clear();
        addGUIElements();
    }
    public void setSingleSelection(){
        setSelectionType(ListSelectionModel.SINGLE_SELECTION);
    }
    public void setMultipleSelection(){
        setSelectionType(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }
    public void setHeadText(String headText){
        if(headText == null)
            return;
        this.btnMain.setText(headText);
        this.headText = headText;
    }
    //ARKAPLAN İŞLEM YÖNTEMLERİ:
    public void setSelectionType(int type){
        this.getGuiLiContent().setSelectionMode(type);
    }

//ERİŞİM YÖNTEMLERİ:
    public JScrollPane getScrpaneContent(){
        if(scrpaneContent == null){
            scrpaneContent = new JScrollPane(getPnlContent(),
                    ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrpaneContent.setPreferredSize(new Dimension(35, 70));
        }
        return scrpaneContent;
    }
    public JPanel getPnlContent(){
        if(pnlContent == null){
            pnlContent = new JPanel(new BorderLayout(2, 2));
            pnlContent.add(getGuiLiContent(), BorderLayout.CENTER);
        }
        return pnlContent;
    }
    public JButton getBtnMain(){
        if(btnMain == null){
            btnMain = new JButton(headText);
            btnMain.setLayout(new BorderLayout(2, 3));
            btnMain.setPreferredSize(new Dimension(200, 30));
            btnMain.add(getBtnShowHide(), BorderLayout.EAST);
        }
        return btnMain;
    }
    public JButton getBtnShowHide(){
        if(btnShowHide == null){
            btnShowHide = new JButton("<");
//            btnShowHide.setPreferredSize(new Dimension(30, 25));
            btnShowHide.addActionListener(this);
        }
        return btnShowHide;
    }
    public JList<String> getGuiLiContent(){
        if(guiLiContent == null){
            guiLiContent = new JList<String>();
            guiLiContent.setModel(getMdlGUIList());
        }
        return guiLiContent;
    }
    public DefaultListModel<String> getMdlGUIList(){
        if(mdlGUIList == null){
            mdlGUIList = new DefaultListModel<String>();
        }
        return mdlGUIList;
    }
    public ArrayList<String> getLiContent(){
        if(liContent == null){
            liContent = new ArrayList<String>();
        }
        return liContent;
    }
}