package View;

import Control.Add;
import Control.GUISeeming;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class PnlInfo extends JPanel{
    JScrollPane scrpaneMain;
    JPanel pnlMain;
    ArrayList<HashMap<String, String>> content;

    public PnlInfo(ArrayList<HashMap<String, String>> content){
        this.content = content;
        this.setLayout(new BorderLayout(2, 2));
        this.add(getScrpaneMain(), BorderLayout.CENTER);
        addGUIElements();
        GUISeeming.appGUI(this);
    }

//İŞLEM YÖNTEMLERİ:
    //ARKAPLAN İŞLEM YÖNTEMLERİ:
    private void addGUIElements(){
        int sayac = 0;
        for(HashMap<String, String> map : content){
            JLabel lbl = new JLabel(map.get("name"));
            JTextArea txtarea = new JTextArea(map.get("text"));
            txtarea.setEditable(false);
            JScrollPane scrpane = new JScrollPane(txtarea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            JPanel pnl = new JPanel(new GridLayout(1, 2, 2, 2));
            pnl.setPreferredSize(new Dimension(270, 70));
            pnl.add(lbl);
            pnl.add(scrpane);
            Add.addComp(getPnlMain(), pnl, 0, sayac, 1, 1, GridBagConstraints.LINE_START, GridBagConstraints.BOTH);
            sayac++;
        }
    }

//ERİŞİM YÖNTEMLERİ:
    public JScrollPane getScrpaneMain(){
        if(scrpaneMain == null){
            scrpaneMain = new JScrollPane(getPnlMain(),
                    ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        }
        return scrpaneMain;
    }
    public JPanel getPnlMain(){
        if(pnlMain == null)
            pnlMain = new JPanel(new GridBagLayout());
        return pnlMain;
    }
}
