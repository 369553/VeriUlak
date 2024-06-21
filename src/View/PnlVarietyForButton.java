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
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class PnlVarietyForButton extends JPanel implements ActionListener{
    JScrollPane scrpaneContent;// 'pnlButtons'ın kaydırılabilir olması için kaydırılabilir panel
    ArrayList<JButton> liButtons;// Tuşların listesi
    JPanel pnlButtons;// Tuşların yerleştirildiği panel
    GridBagLayout compOrder;// Panel görsel düzeni
    JButton btnMain;// Ana tuş
    JButton btnShowHide;// Paneli açma - kapama tuşu
    GridBagLayout compOrderForInside;// İçerideki panel (pnlButtons)in görsel düzeni
    ActionListener act;// Tuşların eylem dinleyicisi
    int sizeOfContent;// İçerik sayısı
    String headText;// Başlık
    boolean isPanelOpened = true;// Panel açıldıysa 'true' olmalıdır
    boolean isOnX = true;
    int locOnX = 0;
    int locOnY = 0;

    public PnlVarietyForButton(int buttonNumber, String[] buttonNames, ActionListener actForButton, String headText){
        this(buttonNumber, buttonNames, actForButton, headText, true);
    }
    public PnlVarietyForButton(int buttonNumber, String[] buttonNames, ActionListener actForButton, String headText, boolean isDirectionOnX){
        this.sizeOfContent = buttonNumber;
        this.act = actForButton;
        this.headText = headText;
        this.isOnX = isDirectionOnX;
        produceButtons(buttonNames);
        compOrder = new GridBagLayout();
        this.setLayout(compOrder);
        Add.addComp(this, getBtnMain(), 0, 0, 1, 1, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, 0.1, 0.0);
        Add.addComp(this, getScrpaneContent(), 0, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, 0.1, 0.2);
        addGUIElements();
        GUISeeming.appGUI(this);
    }

//İŞLEM YÖNTEMLERİ:
    @Override
    public void actionPerformed(ActionEvent e){// Tuşa basıldığında açıp, kapatmak için
        if(e.getSource() == getBtnShowHide()){
            isPanelOpened = !isPanelOpened;
            getScrpaneContent().setVisible(isPanelOpened);
            if(getBtnShowHide().getText().equals(">"))
                getBtnShowHide().setText("<");
            else
                getBtnShowHide().setText(">");
        }
    }
    public void addSpecialComponentAtCellX(){
        //.;.
    }
    public void changeContent(String[] namesOfButtons){
        produceButtons(namesOfButtons);
        reReplaceContent();
    }
    public void reReplaceContent(){// Veri değiştiğinde yeniden yerleştirmek için
        getPnlButtons().removeAll();
        addGUIElements();
    }
    //ARKAPLAN İŞLEM YÖNTEMLERİ:
    private void addGUIElements(){
        if(liButtons == null)
            return;
        locOnX = 0;
        locOnY = 0;
        for(JButton btn : liButtons){
            Add.addComp(getPnlButtons(), btn, locOnX, locOnY, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH);
            if(isOnX)
                locOnY++;
            else
                locOnX++;
        }
    }
    private void produceButtons(String[] colNames){
        if(colNames == null)
            return;
        if(liButtons == null)
            liButtons = new ArrayList<JButton>();
        else
            liButtons.clear();
        for(String str : colNames){
            JButton btn = new JButton(str);
            btn.setName(str);
            if(act != null)
                btn.addActionListener(act);
            liButtons.add(btn);
        }
    }

//ERİŞİM YÖNTEMLERİ:
    public ArrayList<JButton> getLiButtons(){
        if(liButtons == null){
            liButtons = new ArrayList<JButton>();
        }
        return liButtons;
    }
    public ActionListener getAct(){
        return act;
    }
    public JPanel getPnlButtons(){
        if(pnlButtons == null){
            pnlButtons = new JPanel(getCompOrderForInside());
        }
        return pnlButtons;
    }
    public JScrollPane getScrpaneContent(){
        if(scrpaneContent == null){
            scrpaneContent = new JScrollPane(getPnlButtons(),
                    ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//            scrpaneContent.setPreferredSize(new Dimension(35, 70));// ? 
        }
        return scrpaneContent;
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
    //GİZLİ ERİŞİM YÖNTEMLERİ:
    private GridBagLayout getCompOrderForInside(){
        if(compOrderForInside == null)
            compOrderForInside = new GridBagLayout();
        return compOrderForInside;
    }
}