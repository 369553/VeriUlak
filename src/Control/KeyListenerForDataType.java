package Control;

import Service.DataTypeConverter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Locale;
import javax.swing.JTextField;

public class KeyListenerForDataType implements KeyListener{
    private JTextField txt;// Denetlenen görsel eleman
    private Class dType;// Görsel elaman içerisinde yazılabilecek verinin tipi (örüntü kontrolü için)
    private int limit = Integer.MAX_VALUE;// Görsel eleman yazı uzunluğu limiti (değiştirilebilir)
    private int len = 0;// Görsel elemana yazılan yazının uzunluğu
    private Locale loc = Locale.getDefault();// Harfi küçük harfe çevirirken kullanılan yerel parametre
    private boolean iPWF = false;// isPairedWithFalse, Veri tipinin 'Boolean' olması durumunda, görsel elemana 'false' yazılma örüntüsüne uyulduysa 'true' olmalıdır. Buna göre devâmındaki karakterlerin yazılması
    private boolean iPWT = false;// isPairedWithTrue, Veri tipinin 'Boolean' olması durumunda, görsel elemana 'true' yazılma örüntüsünü tâkip etmek için kullanılan değişken

    public KeyListenerForDataType(JTextField txtField, Class dataType){
        this.txt = txtField;
        txt.addKeyListener(this);
        this.dType = dataType;
    }
    public KeyListenerForDataType(JTextField txtField){
        this(txtField, null);
    }

//İŞLEM YÖNTEMLERİ:
    @Override
    public void keyTyped(KeyEvent e){
        if(e.getSource() != txt)// İlişkilendirilmiş görsel eleman dışındakindekilerden gelenleri işleme alma
            return;
        if(dType != null){
            if(dType != String.class){// 'String' tipindeki veriler için bir denetime gerek yok
                // Veri tipine göre kontrolleri ekle:
                String value = txt.getText() + e.getKeyChar();
                if(value.isEmpty())
                    return;
                boolean keepGo = true;
                if(dType == Boolean.class){
                    if(iPWT){
                        if(isPairingForTrue(e.getKeyChar(), value.length() - 1))
                            return;
                        else
                            e.consume();
                    }
                    else if(iPWF){
                        if(isPairingForFalse(e.getKeyChar(), value.length() - 1))
                            return;
                        else
                            e.consume();
                    }
                    if(isPairingForTrue(e.getKeyChar(), value.length() - 1)){
                        iPWT = true;
                        return;
                    }
                    else if(isPairingForFalse(e.getKeyChar(), value.length() - 1)){
                        iPWT = false;
                        iPWF = true;
                        return;
                    }
                    else// Girilen metin 'true' yâ dâ 'false' ifâdesi örüntüsüyle eşleşmiyorsa
                        e.consume();// İşlemi geri al(Yazılan karakteri sil)
                }
                else if(dType == Integer.class){// || dType == Short.class || ... diye eklenmelidir, anlamlı olarak; yanî DataTypeConverter da buna göre güncellenmelidir.
                    if(!confirmForInteger(e.getKeyChar()))// Rakam girilmediyse
                        e.consume();// İşlemi geri al(Girilen karakteri sil)
                }
                Object[] res = DataTypeConverter.getService().convDataType(value, dType);
                if(!(boolean) res[0])// Eğer veri tipine uygun bir ifâde girilmemişse
                    e.consume();// İşlemi geri al (Yazılan karakteri sil)
            }
        }
        if(len == limit){// Veri uzunluğu belirtilmiş uzunluktan daha büyük olamaz
            e.consume();// İşlemi geri al
        }
    }
    @Override
    public void keyPressed(KeyEvent e){
    
    }
    @Override
    public void keyReleased(KeyEvent e){
    
    }
    public void setDataType(Class dataType){
        if(dataType != null){
            if(dType != dataType){
                if(!txt.getText().isEmpty()){
                    Object[] res = DataTypeConverter.getService().convDataType(txt.getText(), dType);
                    if(!(boolean) res[0])
                        txt.setText("");
                }
                this.dType = dataType;
            }
        }
    }
    public void setLimitOfText(int limit){
        this.limit = limit;
    }
    public void clearTextField(){
        len = 0;
        txt.setText("");
    }
    //ARKAPLAN İŞLEM YÖNTEMLERİ:
    private boolean isPairingForTrue(char c, int numberOfCharacter){
        if(numberOfCharacter == 0)
            if(small(c) == 't')
                return true;
        if(numberOfCharacter == 1)
            if(small(c) == 'r')
                return true;
        if(numberOfCharacter == 2)
            if(small(c) == 'u')
                return true;
        if(numberOfCharacter == 3)
            if(small(c) == 'e')
                return true;
        return false;
    }
    private boolean isPairingForFalse(char c, int numberOfCharacter){
        if(numberOfCharacter == 0)
            if(small(c) == 'f')
                return true;
        if(numberOfCharacter == 1)
            if(small(c) == 'a')
                return true;
        if(numberOfCharacter == 2)
            if(small(c) == 'l')
                return true;
        if(numberOfCharacter == 3)
            if(small(c) == 's')
                return true;
        if(numberOfCharacter == 4)
            if(small(c) == 'e')
                return true;
        return false;
    }
    private char small(char c){
        String s = String.valueOf(c);
        s = s.toLowerCase(loc);
        return s.charAt(0);
    }
    private boolean confirmForInteger(char c){// Rakam ise 'true' döndürür
        boolean confirm = false;
        switch(c){
            case '0' :{
                confirm = true;
                break;
            }
            case '1' :{
                confirm = true;
                break;
            }
            case '2' :{
                confirm = true;
                break;
            }
            case '3' :{
                confirm = true;
                break;
            }
            case '4' :{
                confirm = true;
                break;
            }
            case '5' :{
                confirm = true;
                break;
            }
            case '6' :{
                confirm = true;
                break;
            }
            case '7' :{
                confirm = true;
                break;
            }
            case '8' :{
                confirm = true;
                break;
            }
            case '9' :{
                confirm = true;
                break;
            }
        }
        return confirm;
    }
}