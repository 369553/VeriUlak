package Control;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class ListCellRenderStandard extends JLabel implements ListCellRenderer{
    private static ListCellRenderStandard renConfigured;
    Color[] colors;

    public ListCellRenderStandard() {
        colors = GUISeeming.getColorsForRenderer();
    }

    //İŞLEM YÖNTEMLERİ:
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus){
        setText((String) value);
        if(isSelected){
            setForeground(colors[3]);
            setBackground(colors[0]);
        }
        else{
            setBackground(colors[2]);
            setForeground(colors[1]);
        }
       return this;
    }
    public void updateColors(){
       colors = GUISeeming.getColorsForRenderer();
    }

    //ERİŞİM YÖNTEMLERİ:
    public static ListCellRenderStandard getConfiguredRenderer(){
        if(renConfigured == null){
            renConfigured = new ListCellRenderStandard();
        }
        return renConfigured;
    }
}