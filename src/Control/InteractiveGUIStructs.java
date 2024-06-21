package Control;

import java.awt.Dimension;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.NO_OPTION;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;


public class InteractiveGUIStructs {
    int interTypeActive = 0;
    private static InteractiveGUIStructs activeManager;
    Component MP;
    String selectedOne = null;
    String[] selectedOnes = null;
    boolean selectionModeIsSingle = false;
    private static InteractiveGUIStructs tempActiveManager;

    private InteractiveGUIStructs(Component MP){
        this.MP = MP;
    }
    
//İŞLEM YÖNTEMLERİ:
    public String showJustTextField(String topText, String message){ //topText = Üst başlık, message = Gösterilmek istenen yardımcı yazı
        String value = JOptionPane.showInputDialog(MP, message, topText, QUESTION_MESSAGE);
        return value;
    }
    public void showNotAcceptableError(String errorMessage){
        JOptionPane.showMessageDialog(MP, errorMessage);
    }
    public boolean showYesNoQuestion(String questionText, String titleText){
        int value = JOptionPane.showConfirmDialog(MP, questionText, titleText, JOptionPane.YES_NO_OPTION);
        if(value == 0)
            return true;
        return false;
    }
    public int showYesNoCancelQuestion(String queestionText, String titleText){
        return JOptionPane.showConfirmDialog(MP, queestionText, titleText, JOptionPane.YES_NO_CANCEL_OPTION, QUESTION_MESSAGE);
    }
    public void showYesNoQuestion(String questionText){
        Object value = JOptionPane.showConfirmDialog(MP, questionText);
        System.out.println("value : " + (int) value);
    }
    public void showSuccesfulOperationMessage(String message){
        JOptionPane.showMessageDialog(MP, message, "İşlem başarılı!", INFORMATION_MESSAGE);
    }
    
    public int showSpecialGUI(JComponent panel, String topText, String message){
    /*    GUISeeming.appGUI(panel); // Burada bu işlem yapılırsa not rengiyle ilgili panel de arkaplan rengine boyanıyor;
        O yüzden gönderilen panlein gönderilmeden evvel arayüz boyamasının yapılması gerekiyor.*/
        int value = JOptionPane.showOptionDialog(MP, message, topText, NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                null/*Simge yok*/, new Object[]{panel}, null);
        return value;
    }
    
    public int showSpecialGUI(JComponent panel, String topText, String message, int width, int height){
        panel.setSize(width, height);
        panel.setPreferredSize(new Dimension(width, height));
        return showSpecialGUI(panel, topText, message);
    }
    public JPanel prepareSpecialListView(String[] choosedList, boolean isSingleSelection, String textOfCompleteButton){
        JPanel pnlBase = new JPanel();
        BoxLayout order = new BoxLayout(pnlBase, BoxLayout.Y_AXIS);
        pnlBase.setLayout(order); ;
        JList<String> liSources = new JList<String>(choosedList);
        liSources.setSelectedIndex(0);
        if(isSingleSelection){
            liSources.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            selectionModeIsSingle = true;
        }
        Movements mov = new Movements();
        JButton btnComplete = new JButton(textOfCompleteButton);
        btnComplete.addMouseListener(mov);
        JScrollPane scrpanePnlBase = new JScrollPane(liSources,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pnlBase.add(scrpanePnlBase, 0);
        pnlBase.add(btnComplete, 1);
        selectedOne = null;
        selectedOnes = null;
        btnComplete.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if(e.getSource().equals(btnComplete)){
                    if(isSingleSelection)
                        selectedOne = liSources.getSelectedValue();
                    else{
                        selectedOnes = new String[liSources.getSelectedValuesList().size()];
                        liSources.getSelectedValuesList().toArray(selectedOnes);
                    }
                }
            }
        });
            scrpanePnlBase.getHorizontalScrollBar().addMouseListener(mov);
            scrpanePnlBase.getVerticalScrollBar().addMouseListener(mov);
        scrpanePnlBase.setPreferredSize(new Dimension(170, 70));
        btnComplete.setPreferredSize(new Dimension(70, 25));
        GUISeeming.appGUI(pnlBase);
        return pnlBase;
    }
    protected static void startActiveManager(JPanel MP){
        if(activeManager == null)
            activeManager = new InteractiveGUIStructs(MP);
    }
    public static InteractiveGUIStructs runActiveManagerForTemporary(Component baseComponent){
        return new InteractiveGUIStructs(baseComponent);
    }
    public static boolean deleteInteractiveGUIStructs(InteractiveGUIStructs active){
        if(activeManager != null){
            if(active != activeManager)
                if(tempActiveManager != null)
                    if(active == tempActiveManager){
                        tempActiveManager = null;
                        return true;
                    }
        }
        return false;
    }
    
/*YARIM KALDI    public JComponent prepareSpecialPanelForChoosing (String componentTypeName, Object[] data){
        switch(componentTypeName){
            case "JList":{
                
                break;
            }
            case "JComboBox":{
                
                break;
            }
            case "JPanel":{
                
                break;
            }
        }
        return null;
    }*/
    
//ERİŞİM YÖNTEMLERİ:
    //ANA ERİŞİM YÖNTEMİ:
    public static InteractiveGUIStructs getActiveManager(){
        return activeManager;
    }
    public static void setActiveManager(InteractiveGUIStructs activeManager){
        InteractiveGUIStructs.activeManager = activeManager;
    }
    public String getSelectedOne(){
        return selectedOne;
    }
    public String[] getSelectedOnes(){
        return selectedOnes;
    }
}