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
            txtarea.setLineWrap(true);
            txtarea.setWrapStyleWord(true);
            JScrollPane scrpane = new JScrollPane(txtarea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrpane.setPreferredSize(new Dimension(205, 215));
            JPanel pnl = new JPanel(new GridBagLayout());
            Add.addComp(pnl, lbl, 0, 0, 1, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, 0.1, 0.1);
            Add.addComp(pnl, scrpane, 1, 0, 3, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 1.0, 1.0);
            Add.addComp(getPnlMain(), pnl, 0, sayac, 1, 1, GridBagConstraints.LINE_START, GridBagConstraints.BOTH);
            sayac++;
            GUISeeming.appGUI(pnl);
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