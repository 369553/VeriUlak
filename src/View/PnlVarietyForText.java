package View;

import Control.GUISeeming;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class PnlVarietyForText extends JPanel implements ActionListener{
    JScrollPane scrpaneContent;// Metni gösterebilmek için kaydırılabilir görsel eleman
    BorderLayout compOrder;// Panel görsel düzeni
    JButton btnMain;// Ana tuş
    JButton btnShowHide;// Paneli açma - kapama tuşu
    String headText;// Başlık
    String content;// Gösterilmek istenen muhtevâ
    JTextArea txtAContent;// Muhtevâyı göstermek için görsel eleman
    boolean isPanelOpened = true;// Panel açıldıysa 'true' olmalıdır
    short hGap = 3, vGap = 3;// Panelin hâricî panellerle olan boşluğu

    public PnlVarietyForText(String headText, String content){
        this.headText = headText;
        this.content = content;
        this.setLayout(new BorderLayout(hGap, vGap));
        this.add(getScrpaneContent(), BorderLayout.CENTER);
        this.add(getBtnMain(), BorderLayout.NORTH);
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
    public void setHorizontalGap(int horizontalGap){
        if(horizontalGap > Short.MAX_VALUE)
            this.hGap = Short.MAX_VALUE;
        if(horizontalGap < 0)
            this.hGap = 0;
        this.hGap = (short) horizontalGap;
    }
    public void setVerticalGap(int verticalGap){
        if(verticalGap > Short.MAX_VALUE)
            this.vGap = Short.MAX_VALUE;
        if(verticalGap < 0)
            this.vGap = 0;
        else
            this.vGap = (short) verticalGap;
    }
    public void setEditableOfTextArea(boolean isEditable){
        this.getTxtareaContent().setEditable(isEditable);
    }
    public void changeContent(String content){
        if(content == null)
            content = "";
        this.getTxtareaContent().setText(content);
    }

//ERİŞİM YÖNTEMLERİ:
    public JScrollPane getScrpaneContent(){
        if(scrpaneContent == null){
            scrpaneContent = new JScrollPane(getTxtareaContent(),
                    ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
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
    public JTextArea getTxtareaContent(){
        if(txtAContent == null){
            txtAContent = new JTextArea(content);
            txtAContent.setEditable(false);
        }
        return txtAContent;
    }
    public short gethGap(){
        return hGap;
    }
    public short getvGap(){
        return vGap;
    }
}