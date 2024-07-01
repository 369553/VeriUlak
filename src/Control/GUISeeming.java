package Control;

import View.BtnColumn;
import View.PnlSideMenu;
import View.PnlVariety;
import View.PnlVarietyForButton;
import View.PnlVarietyForText;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.border.Border;

public class GUISeeming{
    protected static GUISeeming seeming = GUISeeming.OrderProducer("Standard");
    String seemingName, hexBackgroundColor, hexButtonColor, hexTextColor,
            hexButtonTextColor, hexBorderColor, hexTextAreaColor, hexTextAreaTextColor,
            hexTextAreaSelectedColor, hexTextAreaSelectedTextColor, hexCursorColor, hexMenuColor,
            hexToolsBackgroundColor, hexMouseOnButtonColor, hexMouseOnButtonTextColor,
            hexTableHeaderBackgroundColor, hexTableHeaderTextColor, hexTableCellColor, hexTableCellTextColor,
            hexTableCellSelectedColor, hexTableCellSelectedTextColor;
    Font fontUI = new Font("Liberation Serif", Font.BOLD | Font.ITALIC, 14);

    public GUISeeming(String seemingName, String hexBackgroundColor, String hexButtonColor, String hexTextColor,
            String hexButtonTextColor, String hexBorderColor, String hexTextAreaColor, String hexTextAreaTextColor,
            String hexTextAreaSelectedColor, String hexTextAreaSelectedTextColor, String hexCursorColor, String hexMenuColor,
            String hexMouseOnButtonColor, String hexMouseOnButtonTextColor, String hexToolsBackgroundColor,
            String hexTableHeaderBackgroundColor, String hexTableHeaderTextColor, String hexTableCellColor,
            String hexTableCellTextColor, String hexTableCellSelectedColor, String hexTableCellSelectedTextColor) {
        this.seemingName = seemingName;
        this.hexBackgroundColor = hexBackgroundColor;
        this.hexButtonColor = hexButtonColor;
        this.hexTextColor = hexTextColor;
        this.hexButtonTextColor = hexButtonTextColor;
        this.hexBorderColor = hexBorderColor;
        this.hexTextAreaColor = hexTextAreaColor;
        this.hexTextAreaTextColor = hexTextAreaTextColor;
        this.hexTextAreaSelectedColor = hexTextAreaSelectedColor;
        this.hexTextAreaSelectedTextColor = hexTextAreaSelectedTextColor;
        this.hexCursorColor = hexCursorColor;
        this.hexMenuColor = hexMenuColor;
        this.hexMouseOnButtonColor = hexMouseOnButtonColor;
        this.hexMouseOnButtonTextColor = hexMouseOnButtonTextColor;
        this.hexToolsBackgroundColor = hexToolsBackgroundColor;
        //Tablo:
        this.hexTableHeaderBackgroundColor = hexTableHeaderBackgroundColor;
        this.hexTableHeaderTextColor = hexTableHeaderTextColor;
        this.hexTableCellColor = hexTableCellColor;
        this.hexTableCellTextColor = hexTableCellTextColor;
        this.hexTableCellSelectedColor = hexTableCellSelectedColor;
        this.hexTableCellSelectedTextColor = hexTableCellSelectedTextColor;
    }

//İŞLEM YÖNTEMLERİ:
    public static GUISeeming OrderProducer(String seemingName){
        switch(seemingName){
            case "Dark" :{
                return new GUISeeming("Dark",//AD9B71
                        "#2d1c3c",//hexBackgroundColor : Arkaplan rengi
                        "#3D0B2C",//hexButtonColor : Düğme rengi//843877 66555D AD9B71 / 280B2C
                        "#eaba55",//hexTextColor : Yazı rengi
                        "#9FD097",//hextButtonTextColor : Düğme yazı rengi//AD9B71 / 50A8AF
                        "#360053",//hexBorderColor : Kenarlık rengi
                        "#300112",//hexTextAreaColor : Yazı alanı rengi
                        "#eaba55",//hexTextAreaTextColor : Yazı alanı yazı rengi
                        "#9f1345",//hexTextAreaSelectedColor : Yazı alanı seçim rengi
                        "#01aa0a",//hexTextAreaSelectedTextColor : Yazı alanı seçilen yazı rengi
                        "#a21320",//hexCursorColor : Yazı alanı imleç rengi
                        "#0b0023",//hexMenuColor : Menü arkaplan rengi
                        "#380600",//hexMouseOnButtonColor : Fare üzerindeyken düğme rengi
                        "#A98800",//hexMouseOnButtonTextColor : Fare üzerindeyken düğme yazısı rengi
                        "#280022",//hexToolsBackgroundColor : Araç kutusu arkaplan rengi
                        
                        "#492A1A",//hexTableHeaderBackgroundColor : Tablo başlığı arkaplan rengi
                        "#E59C72",//hexTableHeaderTextColor : Tablo başlığı yazı rengi
                        "#403548",//hexTableCellColor : Tablo hücre arkaplan rengi
                        "#7797FF",//hexTableCellTextColor : Tablo hücre yazı rengi
                        "#327246",//hexTableCellSelectedColor : Tablo seçili hücre rengi
                        "#FFF2C4");//hexTableCellSelectedTextColor : Tablo seçili hücre yazı rengi
            }
            default :{
                return new GUISeeming("Standard",
                        "#F5F5DC",//hexBackgroundColor : Arkaplan rengi
                        "#cfcfae",//hexButtonColor : Düğme rengi
                        "#7B3F00",//hexTextColor : Yazı rengi
                        "#800000",//hextButtonTextColor : Düğme yazı rengi
                        "#aca090",//hexBorderColor : Kenarlık rengi
                        "#d2c5b4",//hexTextAreaColor : Yazı alanı rengi
                        "#022249",//hexTextAreaTextColor : Yazı alanı yazı rengi
                        "#976c88",//hexTextAreaSelectedColor : Yazı alanı seçim rengi
                        "#000000",//hexTextAreaSelectedTextColor : Yazı alanı seçilen yazı rengi
                        "#9b241b",//hexCursorColor : Yazı alanı imleç rengi
                        "#fc8b8b",//hexMenuColor : Menü arkaplan rengi
                        "#d2d260",//hexMouseOnButtonColor : Fare üzerindeyken düğme rengi
                        "#30d260",//hexMouseOnButtonTextColor : Fare üzerindeyken düğme yazısı rengi
                        "#efcea4",//hexToolsBackgroundColor : Araç kutusu arkaplan rengi
                        
                        "#efcea4",//hexTableHeaderBackgroundColor : Tablo başlığı arkaplan rengi
                        "#1379FF",//hexTableHeaderTextColor : Tablo başlığı yazı rengi
                        "#EDE6D3",//hexTableCellColor : Tablo hücre arkaplan rengi
                        "#100000",//hexTableCellTextColor : Tablo hücre yazı rengi
                        "#30d260",//hexTableCellSelectedColor : Tablo seçili hücre rengi
                        "#800000");//hexTableCellSelectedTextColor : Tablo seçili hücre yazı rengi
                
            }
        }
    }
    public static String[] getSeemingNames(){
        return new String[]{"Ortalama"};
    }
    protected static void appGUI(Component comp){
        if(appGUIForSpecialContainers(comp))
            return;
//        System.out.println("comp.getClass.getName : " + comp.getClass().getName());
        switch(comp.getClass().getName()){
            case "javax.swing.JCheckBox" :{
                JCheckBox chbox = (JCheckBox) comp;
                chbox.setBackground(Color.decode(seeming.hexBackgroundColor));
                chbox.setForeground(Color.decode(seeming.hexTextColor));
                chbox.setBorderPainted(true);
                chbox.setBorder(GUISeeming.getComponentBorder(true, true, true, true, 1));
                chbox.setFont(seeming.fontUI);
                chbox.setMargin(new Insets(9, 2, 2, 2));
                chbox.setFocusPainted(true);
                return;
            }
            case "javax.swing.JButton" :{
                JButton button = (JButton) comp;
                button.setBackground(Color.decode(seeming.hexButtonColor));
                button.setForeground(Color.decode(seeming.hexButtonTextColor));
                button.setBorderPainted(true);
                button.setBorder(GUISeeming.getComponentBorder(true, true, true, true, 1));
                button.setFont(seeming.fontUI);
                button.setMargin(new Insets(9, 2, 2, 2));
                button.addMouseListener(new Movements(button));
                button.setFocusPainted(false);
                return;
            }
            case "javax.swing.JList" :{
                JList list = (JList) comp;
                list.setBackground(Color.decode(seeming.hexBackgroundColor));
                list.setForeground(Color.decode(seeming.hexTextColor));
                list.setSelectionBackground(Color.decode(seeming.hexButtonColor));
                list.setSelectionForeground(Color.decode(seeming.hexButtonTextColor));
                list.setBorder(GUISeeming.getComponentBorder(true, true, true, true, 1));
                for(Component subComp : list.getComponents()){
                    GUISeeming.appGUI(subComp);
                }
                return;
            }
            case "javax.swing.JTextArea" :{
                JTextArea txtArea = (JTextArea) comp;
                txtArea.setBackground(Color.decode(seeming.hexTextAreaColor));
                txtArea.setForeground(Color.decode(seeming.hexTextAreaTextColor));
                txtArea.setCaretColor(Color.decode(seeming.hexCursorColor));
                txtArea.setSelectionColor(Color.decode(seeming.hexTextAreaSelectedColor));
                txtArea.setSelectedTextColor(Color.decode(seeming.hexTextAreaSelectedTextColor));
                txtArea.setBorder(GUISeeming.getComponentBorder(true, true, true, true));
                txtArea.setFont(seeming.fontUI);
                /*if(txtArea.getBorder() != null){
                    txtArea.setBorder(getComponentBorder(true, true, true, true));
                }*/
                return;
            }
            case "javax.swing.JTextField" :{
                JTextField txtArea = (JTextField) comp;
                txtArea.setBackground(Color.decode(seeming.hexTextAreaColor));
                txtArea.setForeground(Color.decode(seeming.hexTextAreaTextColor));
                txtArea.setCaretColor(Color.decode(seeming.hexCursorColor));
                txtArea.setSelectionColor(Color.decode(seeming.hexTextAreaSelectedColor));
                txtArea.setSelectedTextColor(Color.decode(seeming.hexTextAreaSelectedTextColor));
                txtArea.setBorder(GUISeeming.getComponentBorder(true, true, true, true));
                txtArea.setFont(seeming.fontUI);
                return;
            }
            case "javax.swing.JPasswordField" :{
                JTextField txtArea = (JTextField) comp;
                txtArea.setBackground(Color.decode(seeming.hexTextAreaColor));
                txtArea.setForeground(Color.decode(seeming.hexTextAreaTextColor));
                txtArea.setCaretColor(Color.decode(seeming.hexCursorColor));
                txtArea.setSelectionColor(Color.decode(seeming.hexTextAreaSelectedColor));
                txtArea.setSelectedTextColor(Color.decode(seeming.hexTextAreaSelectedTextColor));
                txtArea.setBorder(GUISeeming.getComponentBorder(true, true, true, true));
                txtArea.setFont(seeming.fontUI);
                return;
            }
            case "javax.swing.JComboBox":{
                JComboBox cmbox = (JComboBox) comp;
                cmbox.setFont(seeming.fontUI);
                cmbox.setBackground(Color.decode(seeming.hexButtonColor));
                cmbox.setForeground(Color.decode(seeming.hexButtonTextColor));
                if(cmbox.getRenderer().getClass().getName().contains("ComboBoxRenderer")){
                    cmbox.setRenderer(ListCellRenderStandard.getConfiguredRenderer());
                }
                return;
            }
            case "javax.swing.JScrollPane" :{
                JScrollPane scrPane= (JScrollPane) comp;
                scrPane.setViewportBorder(GUISeeming.getComponentBorder(true, true, true, true, 1));
                scrPane.getViewport().setBackground(Color.decode(seeming.hexBackgroundColor));
                scrPane.setBorder(GUISeeming.getComponentBorder(true, true, true, true, 1));
                GUISeeming.appGUI((Component) scrPane.getViewport());
                /*scrPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                scrPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);*/
                scrPane.getHorizontalScrollBar().setBackground(Color.decode(seeming.hexBackgroundColor));
                scrPane.getVerticalScrollBar().setBackground(Color.decode(seeming.hexBackgroundColor));
                scrPane.getHorizontalScrollBar().setBorder(GUISeeming.getComponentBorder(true, true, true, true, 1));
                scrPane.getVerticalScrollBar().setBorder(GUISeeming.getComponentBorder(true, true, true, true, 1));
                scrPane.getHorizontalScrollBar().setUnitIncrement(16);
                scrPane.getVerticalScrollBar().setUnitIncrement(16);
                scrPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 40));
                scrPane.getHorizontalScrollBar().setPreferredSize(new Dimension(10, 10));
                for(Component component : scrPane.getVerticalScrollBar().getComponents()){
                    component.setBackground(Color.decode(seeming.hexButtonColor));
                    component.addMouseListener(new Movements(component));
                }
                for(Component component : scrPane.getHorizontalScrollBar().getComponents()){
                    component.setBackground(Color.decode(seeming.hexButtonColor));
                    component.addMouseListener(new Movements(component));
                }
                return;
            }
            case "javax.swing.JViewport" :{
                JViewport view = (JViewport) comp;
                if(view.getView() != null){
                    if(view.getView().getClass() == JTable.class)
                        GUISeeming.appGUI(view.getView());
                    else{
                        try{
                            Container cont = (Container) view.getView();
    //                        System.err.println("GÖNDERİLİYOR...");
                            GUISeeming.appGUI(cont);
                        }
                        catch(Exception exc){
                            GUISeeming.appGUI(view.getView());
                        }
                    }
                    
                }
                return;
            }
            case "javax.swing.JTable" :{
                JTable table = (JTable) comp;
                table.getTableHeader().setBackground(Color.decode(seeming.hexTableHeaderBackgroundColor));
                table.getTableHeader().setForeground(Color.decode(seeming.hexTableHeaderTextColor));
                table.setSelectionBackground(Color.decode(seeming.hexTableCellSelectedColor));
                table.setSelectionForeground(Color.decode(seeming.hexTableCellSelectedTextColor));
                table.setGridColor(Color.decode(seeming.hexBorderColor));
                table.setBackground(Color.decode(seeming.hexTableCellColor));
                table.setForeground(Color.decode(seeming.hexTableCellTextColor));
                return;
            }
            case "javax.swing.JSpinner" :{
                JSpinner spinComp  =(JSpinner) comp;
                spinComp.setBackground(Color.decode(seeming.hexBackgroundColor));
                spinComp.setForeground(Color.decode(seeming.hexTextColor));
                spinComp.setBorder(GUISeeming.getComponentBorder(true, true, true, true, 1));
                for(int index = 0; index < spinComp.getComponentCount(); index++){
                    if(spinComp.getComponent(index).getClass().getName().equals("javax.swing.plaf.basic.BasicArrowButton")){
                        javax.swing.plaf.basic.BasicArrowButton btnChanger = (javax.swing.plaf.basic.BasicArrowButton) spinComp.getComponent(index);
                        btnChanger.setBackground(Color.decode(seeming.hexButtonColor));
                        btnChanger.setForeground(Color.decode(seeming.hexButtonTextColor) );
                        btnChanger.addMouseListener(new Movements(btnChanger));
                        btnChanger.setBorderPainted(false);
                        btnChanger.setBorder(GUISeeming.getComponentBorder(true, true, true, true, 1));
                        btnChanger.setBorderPainted(true);
                    }
                }
                if(spinComp.getEditor().getComponent(0).getClass().getName().equals("javax.swing.JFormattedTextField")){
                    JFormattedTextField txtEditor = (JFormattedTextField) spinComp.getEditor().getComponent(0);
                    txtEditor.setBackground(Color.decode(seeming.hexTextAreaColor));
                    txtEditor.setForeground(Color.decode(seeming.hexTextAreaTextColor));
                    txtEditor.setSelectionColor(Color.decode(seeming.hexTextAreaSelectedColor));
                    txtEditor.setSelectedTextColor(Color.decode(seeming.hexTextAreaSelectedTextColor));
                }
                return;
            }
            case "javax.swing.JMenuItem" :{
                JMenuItem mn = (JMenuItem) comp;
                mn.setBackground(Color.decode(seeming.hexButtonColor));
                mn.setForeground(Color.decode(seeming.hexButtonTextColor));
                mn.addMouseListener(new Movements(comp));
                mn.setBorder(GUISeeming.getComponentBorder(true, false, true, false, 1));
                mn.setFont(seeming.fontUI.deriveFont(Font.BOLD));
            }
            default :{
                comp.setBackground(Color.decode(seeming.hexBackgroundColor));
                comp.setForeground(Color.decode(seeming.hexTextColor));
                comp.setFont(seeming.fontUI);
            }
        }
    }
    public static void appGUI(Container container){
        if(appGUIForSpecialContainers(container))
            return;
        container.setBackground(Color.decode(seeming.hexBackgroundColor));
        container.setForeground(Color.decode(seeming.hexTextColor));
//        System.out.println("container.ismi : " + container.getClass().getName());
        container.setFont(seeming.fontUI);
        for(Component component : container.getComponents()){
            appGUI(component);
        }
    }
    public static Color[] getColorsForRenderer(){
        Color[] clrColors = new Color[4];
        clrColors[0] = Color.decode(seeming.hexButtonColor);
        clrColors[1] = Color.decode(seeming.hexButtonTextColor);
        clrColors[2] = Color.decode(seeming.hexMouseOnButtonColor);
        clrColors[3] = Color.decode(seeming.hexMouseOnButtonTextColor);
        return clrColors;
    }
    public static String[] getColorsForContactPanel(){
        String[] strColors = new String[6];
        strColors[0] = seeming.hexBackgroundColor;
        switch(seeming.seemingName){
            case "Standard" :{
                strColors[1] = "#e1625e";//Warning
                strColors[2] = "#44d794";//Succesful
                strColors[3] = "#e1c5bd";//LittlePoint
                strColors[4] = "#778edd";//Info
                strColors[5] = "#fac35b";//Advice
                break;
            }
            case "Pink" :{
                strColors[1] = "#f25701";//Warning
                strColors[2] = "#8fb701";//Succesful
                strColors[3] = "#ffa38d";//LittlePoint
                strColors[4] = "#76a3ee";//Info
                strColors[5] = "#ebc172";//Advice
                break;
            }
            case "Blue" :{
                strColors[1] = "#eb4b72";//Warning
                strColors[2] = "#5ef59c";//Succesful
                strColors[3] = "#e18c8f";//LittlePoint
                strColors[4] = "#9d98fa";//Info
                strColors[5] = "#f89885";//Advice
                break;
            }
            case "Dark" :{
                strColors[1] = "#53060a";//Warning
                strColors[2] = "#041800";//Succesful
                strColors[3] = "#320b1d";//LittlePoint
                strColors[4] = "#070657";//Info
                strColors[5] = "#5e2213";//Advice
                break;
            }
        }
        return strColors;
    }
    public static void mouseOnMovementRefresh(Component comp){
        switch(comp.getClass().getName()){
            default:{
                comp.setBackground(Color.decode(seeming.hexMouseOnButtonColor));
                comp.setForeground(Color.decode(seeming.hexMouseOnButtonTextColor));
            }
        }
    }
    public static void mouseOffMovementRefresh(Component comp){
        switch(comp.getClass().getName()){
            default:{
                comp.setBackground(Color.decode(seeming.hexButtonColor));
                comp.setForeground(Color.decode(seeming.hexButtonTextColor));
            }
        }
    }
    //ARKAPLAN İŞLEM YÖNTEMLERİ:
    private static Border getComponentBorder(boolean isTop, boolean isLeft, boolean isBottom, boolean isRight, int size){
        int top = 0, right = 0, bottom = 0, left = 0;
        if(isTop)
            top = size;
        if(isRight)
            right = size;
        if(isBottom)
            bottom = size;
        if(isLeft)
            left = size;
        return BorderFactory.createMatteBorder(top, right, bottom, left, Color.decode(seeming.hexBorderColor));
    }
    private static Border getComponentBorder(boolean isTop, boolean isLeft, boolean isBottom, boolean isRight){
        return getComponentBorder(isTop, isRight, isBottom, isLeft, 2);
    }
    private static boolean appGUIForSpecialContainers(Component component){//appGUI()'yı tüm alt sınıflar için uygula; sadece bu container için uygulama mâhiyyetinde bir fonksiyon hâzırla
        boolean isVarietyClass = false;
        boolean isVarietyForButtonClass = false;
        boolean isVarietyForTextClass = false;
        switch(component.getClass().getName()){
            case "View.PnlTopMenu" :{
                JPanel panel = (JPanel) component;
                panel.setBorder(GUISeeming.getComponentBorder(false, false, true, false, 2));
                panel.setBackground(Color.decode(seeming.hexMenuColor));
                for(Component subComp : panel.getComponents()){
                    appGUI(subComp);
                }
                return true;
            }
            case "View.PnlMain" :{
                component.setBackground(Color.decode(seeming.hexBackgroundColor));
                component.setForeground(Color.decode(seeming.hexTextColor));
                component.setFont(seeming.fontUI);
                return true;
            }
            case "View.PnlInfoStickForTable" :{
                JPanel panel = (JPanel) component;
                component.setBackground(Color.decode(seeming.hexTextAreaColor));
                component.setForeground(Color.decode(seeming.hexTextAreaSelectedColor));
                component.setFont(seeming.fontUI);
                for(Component subComp : panel.getComponents()){
                    appGUI(subComp);
                }
                return true;
            }
            case "View.BtnColumn" :{
                BtnColumn btn = (BtnColumn) component;
                btn.setBackground(Color.decode(seeming.hexButtonColor));
                btn.setForeground(Color.decode(seeming.hexButtonTextColor));
                btn.setBorderPainted(true);
                btn.setBorder(GUISeeming.getComponentBorder(true, true, true, true, 1));
                btn.setFont(seeming.fontUI);
                btn.setMargin(new Insets(9, 2, 2, 2));
                btn.addMouseListener(new Movements(btn));
                btn.setFocusPainted(false);
                for(Component cmp : btn.getComponents()){
                    GUISeeming.appGUI(cmp);
                }
                GUISeeming.appGUI((Container) btn.getPopmenuCol());
                return true;
            }
            case "View.PnlSideMenu" :{
                JPanel panel = (JPanel) component;
                panel.setBorder(GUISeeming.getComponentBorder(true, true, true, true, 3));
                panel.setBackground(Color.decode(seeming.hexMenuColor));
                component.setFont(seeming.fontUI);
                // ŞU ALTTAKİ SATIRA GEREK YOK GİBİ GÖRÜNÜYOR:
                for(Component subComp : panel.getComponents()){
                    if(subComp.getClass() == JPanel.class)
                        appGUI((Container)subComp);
                    else
                        appGUI(subComp);
                }
                ((PnlSideMenu)component).refreshGUISeeming();
                //PnlColumnDetails biçimlendirilecek bi iznillâh, özel biçimlendirme de gerekiyor, misal renk gibi
                return true;
            }
            case "View.PnlVariety" :{
                isVarietyClass = true;
                break;
            }
            case "View.PnlVarietyForButton" :{
                isVarietyForButtonClass = true;
                break;
            }
            case "View.PnlVarietyForText" :{
                isVarietyForTextClass = true;
                break;
            }
            /*case "Views.C" :{
                GUISeeming.appGUI((Container) component);
                return true;
            }*/
        }
        Class top = component.getClass().getSuperclass();
        if(top != null){
            if(top == PnlVariety.class){
                isVarietyClass = true;
            }
            else if(top == PnlVarietyForButton.class){
                isVarietyForButtonClass = true;
            }
        }
        if(isVarietyClass){
            PnlVariety pnl = (PnlVariety) component;
            pnl.setBorder(GUISeeming.getComponentBorder(true, true, true, true, 1));
            pnl.setBackground(Color.decode(seeming.hexBackgroundColor));
            component.setFont(seeming.fontUI);
            for(Component comp : pnl.getComponents()){
                appGUI(comp);
            }
            appGUI((Component) pnl.getBtnShowHide());
            pnl.getBtnShowHide().setFont(seeming.fontUI.deriveFont(22.0f));
            appGUI(pnl.getGuiLiContent());
            return true;
        }
        else if(isVarietyForButtonClass){
            PnlVarietyForButton pnl = (PnlVarietyForButton) component;
            pnl.setBorder(GUISeeming.getComponentBorder(true, true, true, true, 1));
            pnl.setBackground(Color.decode(seeming.hexBackgroundColor));
            component.setFont(seeming.fontUI);
            for(Component comp : pnl.getComponents()){
                if(comp == pnl.getPnlButtons())
                    appGUI((JPanel) comp);
                else
                    appGUI(comp);
            }
            appGUI((Component) pnl.getBtnShowHide());
            pnl.getBtnShowHide().setFont(seeming.fontUI.deriveFont(22.0f));
            return true;
        }
        else if(isVarietyForTextClass){
            PnlVarietyForText pnl = (PnlVarietyForText) component;
            pnl.setBorder(GUISeeming.getComponentBorder(true, true, true, true, 1));
            pnl.setBackground(Color.decode(seeming.hexBackgroundColor));
            component.setFont(seeming.fontUI);
            for(Component comp : pnl.getComponents()){
                    appGUI(comp);
            }
            appGUI((Component) pnl.getBtnShowHide());
            pnl.getBtnShowHide().setFont(seeming.fontUI.deriveFont(22.0f));
            return true;
        }
        return false;
    }
    /*public void updateSeeming(){
        
    }*/

//ERİŞİM YÖNTEMLERİ:
    public static String getGUISeemingName(){
        return seeming.seemingName;
    }
}