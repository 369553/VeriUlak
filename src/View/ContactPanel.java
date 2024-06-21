package View;

import Control.GUISeeming;
import Control.RenderForList;
import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class ContactPanel extends JPanel{
    private static ContactPanel contactPanel; 
    private HashMap<String, String> messageTypesAndColor;
    private Font messageFont;
    private String messageColor = "#FF8800";
    private JScrollPane scrpanePanel;
    private JList<JLabel> liMessages;
    private DefaultListModel<JLabel> mdlMessagesList;
    private BorderLayout compOrder;
    private RenderForList renderMessagesList;

    private ContactPanel() {    
        this.setBackground(Color.decode("#F5F5DC"));
        this.setForeground(Color.decode("#7B3F00"));
        this.setLayout(getCompOrder());
        this.setPreferredSize(new Dimension(MainFrame.getFrameMain().getWidth() - 10, MainFrame.getFrameMain().getHeight() / 7));
        this.add(getScrpanePanel(), BorderLayout.CENTER);
        GUISeeming.appGUI(this);
    }

//İŞLEM YÖNTEMLERİ:
    public void showMessage(String messageText, String messageType){
        String strMessageType = getMessageTypesAndColor().get(messageType);
        if(strMessageType != null)
            this.messageColor = strMessageType;
        else
            this.messageColor = getMessageTypesAndColor().get("Standard");
        getMdlMessagesList().addElement(getNewGUITextForMessage(messageText));
        setVisible(true);
    }
    public void changeSize(int width, int height){
        this.setPreferredSize(new Dimension(width, height));
    }

//ERİŞİM YÖNTEMLERİ:
    //ANA ERİŞİM YÖNTEMİ:
    public static ContactPanel getContactPanel() {
        if(contactPanel == null){
            contactPanel = new ContactPanel();
        }
        return contactPanel;
    }
    public HashMap<String, String> getMessageTypesAndColor() {
        if(messageTypesAndColor == null){
            messageTypesAndColor = new HashMap<String, String>();
            messageTypesAndColor.put("Standard", "#FF8800");
            messageTypesAndColor.put("Warning", "#e1625e");
            messageTypesAndColor.put("Successful", "#44d794");
            messageTypesAndColor.put("LittlePoint", "#e1c5bd");
            messageTypesAndColor.put("Info", "#778edd");
            messageTypesAndColor.put("Advice", "#fac35b");
        }
        return messageTypesAndColor;
    }
    public JLabel getNewGUITextForMessage(String messageText) {
        JLabel lblMessage = new JLabel(messageText);
        lblMessage.setFont(getMessageFont());
        lblMessage.setOpaque(true);
        lblMessage.setHorizontalAlignment(JLabel.HORIZONTAL);
        lblMessage.setBackground(Color.decode(messageColor));
        return lblMessage;
    }
    public Font getMessageFont() {
        if(messageFont == null){
            messageFont = new Font("Dialog", Font.BOLD | Font.ITALIC, 14);
        }
        return messageFont;
    }

    public JScrollPane getScrpanePanel() {
        if(scrpanePanel == null){
            scrpanePanel = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrpanePanel.setViewportView(getLiMessages());
        }
        return scrpanePanel;
    }
    public JList<JLabel> getLiMessages() {
        if(liMessages == null){
            liMessages = new JList<JLabel>();
            liMessages.setModel(getMdlMessagesList());
            liMessages.setBackground(Color.decode("#F5F5DC"));
            liMessages.setForeground(Color.decode("#7B3F00"));
            liMessages.setSelectionBackground(Color.decode("#cfcfae"));
            liMessages.setSelectionForeground(Color.decode("#800000"));
            liMessages.setFont(getMessageFont());
            liMessages.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.decode("#aca090")));
            liMessages.setCellRenderer(getRenderMessagesList());
        }
        return liMessages;
    }
    private DefaultListModel<JLabel> getMdlMessagesList(){
        if(mdlMessagesList == null){
            mdlMessagesList = new DefaultListModel<JLabel>();
        }
        return mdlMessagesList;
    }
    public BorderLayout getCompOrder() {
        if(compOrder == null){
            compOrder = new BorderLayout(0, 0);
        }
        return compOrder;
    }
    public RenderForList getRenderMessagesList(){
        if(renderMessagesList == null){
            renderMessagesList = RenderForList.produceConfiguredRenderer(this, "getRenderColor", getMessageFont());
        }
        return renderMessagesList;
    }
    public String getRenderColor(){
        return messageColor;
    }
}