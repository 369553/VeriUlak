package Control;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Container;
import java.awt.Component;

public class Add {
    private static GridBagConstraints gridConstraints = null;

//İŞLEM YÖNTEMLERİ:
    //ANA İŞLEM YÖNTEMİ:
    public static void addComp(Container target, Component comp, int x, int y, int width, int height){
        GridBagConstraints gridCons = setConstraintsAsDefault(new GridBagConstraints());
        gridCons.gridx = x;
        gridCons.gridy = y;
        gridCons.gridwidth = width;
        gridCons.gridheight = height;
        target.add(comp, gridCons);
    }
    public static void addComp(Container target, Component comp, int x, int y, int width, int height, int anchor){
        GridBagConstraints gridCons = setConstraintsAsDefault(new GridBagConstraints());
        gridCons.gridx = x;
        gridCons.gridy = y;
        gridCons.gridwidth = width;
        gridCons.gridheight = height;
        gridCons.anchor = anchor;
        target.add(comp, gridCons);
    }
    public static void addComp(Container target, Component comp, int x, int y, int width, int height, int anchor, int fill){
        GridBagConstraints gridCons = setConstraintsAsDefault(new GridBagConstraints());
        gridCons.gridx = x;
        gridCons.gridy = y;
        gridCons.gridwidth = width;
        gridCons.gridheight = height;
        gridCons.anchor = anchor;
        gridCons.fill = fill;
        target.add(comp, gridCons);
    }
    public static void addComp(Container target, Component comp, int x, int y, int width, int height, int anchor, int fill, double weightX, double weightY){
        GridBagConstraints gridCons = setConstraintsAsDefault(new GridBagConstraints());
        gridCons.gridx = x;
        gridCons.gridy = y;
        gridCons.gridwidth = width;
        gridCons.gridheight = height;
        gridCons.anchor = anchor;
        gridCons.fill = fill;
        gridCons.weightx = weightX;
        gridCons.weighty = weightY;
        target.add(comp, gridCons);
    }
    public static void addComp(Container target, Component comp, int x, int y, int width, int height, int anchor, int fill, double weightX, double weightY, int gapTop, int gapLeft, int gapBottom, int gapRight){
        GridBagConstraints gridCons = setConstraintsAsDefault(new GridBagConstraints());
        gridCons.gridx = x;
        gridCons.gridy = y;
        gridCons.gridwidth = width;
        gridCons.gridheight = height;
        gridCons.anchor = anchor;
        gridCons.fill = fill;
        gridCons.weightx = weightX;
        gridCons.weighty = weightY;
        gridCons.insets = new Insets(gapTop, gapLeft, gapBottom, gapRight);
        target.add(comp, gridCons);
    }
    public static void addComp(Container target, Component comp, int x, int y, int width, int height, int anchor, int fill, double weightX, double weightY, int gapTop, int gapLeft, int gapBottom, int gapRight, int padInternalX , int padInternalY){
        GridBagConstraints gridCons = setConstraintsAsDefault(new GridBagConstraints());
        gridCons.gridx = x;
        gridCons.gridy = y;
        gridCons.gridwidth = width;
        gridCons.gridheight = height;
        gridCons.anchor = anchor;
        gridCons.fill = fill;
        gridCons.weightx = weightX;
        gridCons.weighty = weightY;
        gridCons.ipadx = padInternalX;
        gridCons.ipady = padInternalY;
        gridCons.insets = new Insets(gapTop, gapLeft, gapBottom, gapRight);
        target.add(comp, gridCons);
    }
    //ARKAPLAN İŞLEM YÖNTEMLERİ:
    private static void setConstraintsAsDefault(){
        getGridConstraintsOfOrder().gridx = 0;
        gridConstraints.gridy = 0;
        gridConstraints.gridwidth = 1;
        gridConstraints.gridheight = 1;
        gridConstraints.anchor = GridBagConstraints.CENTER;
        gridConstraints.fill = GridBagConstraints.BOTH;
        gridConstraints.weightx = 0.0;
        gridConstraints.weighty = 0.0;
        gridConstraints.insets = new Insets(5, 5, 5, 5);
        gridConstraints.ipadx = 0;
        gridConstraints.ipady = 0;
    }
    private static GridBagConstraints setConstraintsAsDefault(GridBagConstraints gridConstraints){
        getGridConstraintsOfOrder().gridx = 0;
        gridConstraints.gridy = 0;
        gridConstraints.gridwidth = 1;
        gridConstraints.gridheight = 1;
        gridConstraints.anchor = GridBagConstraints.CENTER;
        gridConstraints.fill = GridBagConstraints.NONE;
        gridConstraints.weightx = 0.0;
        gridConstraints.weighty = 0.0;
        gridConstraints.insets = new Insets(5, 5, 5, 5);
        gridConstraints.ipadx = 0;
        gridConstraints.ipady = 0;
        return gridConstraints;
    }

//ERİŞİM YÖNTEMLERİ:
    private static GridBagConstraints getGridConstraintsOfOrder(){
        if(gridConstraints == null){
        //Bu değerler birer kısıtlamadır. Hücrelerin yerleri, boyları, genişliklerini belirlemede bu kısıtlamalara bakılır.
            gridConstraints = new GridBagConstraints(
                0,//Hücrenin X eksenindeki konumu
                0,//Hücrenin Y eksenindeki konumu
                1,//Hücrening genişliği
                1,//Hücrening uzunluğu
                0.0,//Hücrening x eksenindeki ağırlığı (Artan boşluklar hücrelerin ağırlığına göre dağıtılır)
                0.0,//Hücrening y eksenindeki ağırlığı
                GridBagConstraints.CENTER,//Hücrenin ekrana yerleşme biçimi
                GridBagConstraints.CENTER,//Görsel bileşenin hücreyi doldurma biçimi
                new Insets(5, 5, 5, 5),//Hücreler arasındaki asğarî mesâfe
                0,//Görsel bileşenin hücre kenarlarına olan uzaklığı (x ekseninde)
                0);//Görsel bileşenin hücre kenarlarına olan uzaklığı (y ekseninde)
        }
        return gridConstraints;
    }
}