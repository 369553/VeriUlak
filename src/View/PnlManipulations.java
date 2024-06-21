package View;

import Control.GUISeeming;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class PnlManipulations extends JPanel{
    BorderLayout compOrder;
    int hGap = 2, vGap = 4;
    JScrollPane scrpaneMain;
    ArrayList<JButton> btnList;
    ArrayList<String> contentList;
    JPanel pnlContent;
    ActionListener act;
    boolean isNumber = true;
    ArrayList<String> fullList;// Tüm durumlar için (veri tipinin sayı olduğu durum + veri tipinin String olduğu durum) tüm işlemlerin listesi

    public PnlManipulations(ArrayList<String> contentList, ActionListener act, KeyListener keyAct, boolean isNumber, ArrayList<String> fullList){
        this.isNumber = isNumber;
        this.fullList = fullList;
        this.act = act;
        this.contentList = contentList;
        this.setLayout(getCompOrder());
        this.add(getScrpaneMain(), BorderLayout.CENTER);
        produceButtons();
        addGUIElements();
//        GUISeeming.appGUI(this);
    }

//İŞLEM YÖNTEMLERİ:
    public void changeContent(ArrayList<String> contentList, boolean isNumber){// Burada seçilen sütunun veri tipine göre bâzı seçenekleri göster, bâzılarını gösterme
        this.contentList = contentList;
        reReplaceContent();
    }
    public void reReplaceContent(){
        this.getPnlContent().removeAll();
        addGUIElements();
    }
    //ARKAPLAN İŞLEM YÖNTEMLERİ:
    private void addGUIElements(){
        for(JButton btn : getBtnList()){
            if(contentList.indexOf(btn.getText()) == -1)
                continue;
            getPnlContent().add(btn);
        }
        GUISeeming.appGUI(this);
    }
    private void produceButtons(){
        if(btnList == null)
            btnList= new ArrayList<JButton>();
        for(String str : getFullList()){
            JButton btn = new JButton(str);
            if(act != null)
                btn.addActionListener(this.act);
            btnList.add(btn);
        }
    }

//ERİŞİM YÖNTEMLERİ:
    public BorderLayout getCompOrder(){
        if(compOrder == null){
            compOrder = new BorderLayout(hGap, vGap);
        }
        return compOrder;
    }
    public ArrayList<String> getContentList(){
        if(contentList == null)
            contentList = new ArrayList<String>();
        return contentList;
    }
    public ArrayList<JButton> getBtnList(){
        return btnList;
    }
    public JScrollPane getScrpaneMain(){
        if(scrpaneMain == null){
            scrpaneMain = new JScrollPane(getPnlContent(),
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        }
        return scrpaneMain;
    }
    public JPanel getPnlContent(){
        if(pnlContent == null){
            pnlContent = new JPanel(new GridLayout(this.fullList.size(), 1, 2, 2));
        }
        return pnlContent;
    }
    public ActionListener getAct(){
        return act;
    }
    public ArrayList<String> getFullList(){
        if(fullList == null)
            fullList = new ArrayList<String>();
        return fullList;
    }
    
}