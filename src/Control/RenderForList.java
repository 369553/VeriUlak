package Control;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.lang.reflect.InvocationTargetException;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class RenderForList extends JLabel implements ListCellRenderer{
    String renderColor = "#7B3F00";
    String methodNameForTakingColor;
    Object object;
    Font font;

    private RenderForList(Object object, String methodNameForTakingColor, Font font){
        this.object = object;
        this.methodNameForTakingColor = methodNameForTakingColor;
        this.font = font;
    }

    //İŞLEM YÖNTEMLERİ:
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus){
        JLabel lblValue = (JLabel) value;
        if(isSelected){
            lblValue.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.decode("#5B1E1D")));
        }
        else{
            lblValue.setBorder(null);
        }
       return lblValue;
    }
    //ÜRETİM YÖNTEMİ:
    public static RenderForList produceConfiguredRenderer(Object anyThing, String methodNameForTakingColor, Font font){
        return new RenderForList(anyThing, methodNameForTakingColor, font);
    }

//ERİŞİM YÖNTEMLERİ:
    public String getRenderColor(){
        try {
            renderColor = (String) object.getClass().getMethod(methodNameForTakingColor, null).invoke(object, null);
    //        System.err.println("renderColor : " + renderColor);
        } 
        catch(NoSuchMethodException ex){
            ex.printStackTrace();
        }
        catch(SecurityException ex){
            ex.printStackTrace();
        }
        catch(IllegalAccessException ex){
            ex.printStackTrace();
        }
        catch(IllegalArgumentException ex){
            ex.printStackTrace();
        }
        catch(InvocationTargetException ex){
            ex.printStackTrace();
        }
        return renderColor;
    }
    public String getMethodNameForTakingColor(){
        return methodNameForTakingColor;
    }
    public Object getObject(){
        return object;
    }
}