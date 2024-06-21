package Service;

public class DataTypeConverter{
    private static DataTypeConverter serv;

    private DataTypeConverter(){
        
    }

// İŞLEM YÖNTEMLERİ:
    public Object[] convDataType(Object data, Class targetType){// Dönüş tipi : Object[sonuç(başarılı ise 'true', başarısız ise 'false'), dönüştürülen değer (başarılı ise)]
        if(data == null)
            return new Object[]{false};
        Class curr = data.getClass();// Verinin şimdiki tipi
        if(curr == targetType)// Verilerin tipleri aynıysa işlemi durdur
            return new Object[]{true, data};
        targetType = wrapPrimitiveClass(new Class[]{targetType})[0];// Hedef veri tipini kapsülle
        boolean isNumber = false;
        boolean isTargetTypeIsNumber = false;
        String sVal = String.valueOf(data);
        try{
            if(targetType == String.class){// Hedef veri tipi String ise 'String.valueOf()' yöntemi kâfî
                if(sVal == null)
                    throw new Exception();
                return new Object[]{true, sVal};
            }
            else if(targetType == Boolean.class){// Hedef == Boolean ise
                if(sVal.equalsIgnoreCase("true") || sVal.equalsIgnoreCase("t"))
                    data = true;
                else if(sVal.equalsIgnoreCase("false") || sVal.equalsIgnoreCase("f"))
                    data = false;
                else
                    return new Object[]{false};
                return new Object[]{true, data};
            }
            else if(curr == Boolean.class)
                return new Object[]{false};// Mevcut veri tipi Boolean ise yalnızca String'e çevrilebilr; o da yukarıda yapıldığından 'false' döndürebiliriz
            if(targetType == Number.class)
                isTargetTypeIsNumber = true;
            else if(targetType.getSuperclass() == Number.class)
                isTargetTypeIsNumber = true;
            if(isTargetTypeIsNumber){// Hedef = sayı ise
                sVal = sVal.replace(",", ".");
                Number num = null;
                if(targetType == Integer.class){
                    // EK İŞLEM GEREKİYOR  : DOUBLE->INT sıkıntılı ; Double bir String->Int sıkıntılı:
                    String[] splitted = sVal.split("\\.");
                    for(int sayac = 1; sayac < splitted.length; sayac++){// Noktadan sonraki her veriyi gözden geçir
                        for(int s2 = 0; s2 < splitted[sayac].length(); s2++){// Bunların her bir harfini gözden geçir
                            boolean isGoodToConv = false;
                            switch(splitted[sayac].charAt(s2)){// Eğer harf 0 veyâ '.' dışında karakter içeriyorsa 'dönüştürülemez' olarak işâretle ve dur
                                case '0' :{
                                    isGoodToConv = true;
                                    break;
                                }
                                case ' ' :{
                                    isGoodToConv = true;
                                    break;
                                }
                            }
                            if(!isGoodToConv)
                                return new Object[]{false};
                        }
                    }
                    num = Integer.valueOf(splitted[0]);
//                    System.out.println("spli,tted[0] : " + splitted[0]);
                }
                else if(targetType == Double.class)
                    num = Double.valueOf(sVal);
                else if(targetType == Short.class)
                    num = Short.valueOf(sVal);
                else if(targetType == Long.class)
                    num = Long.valueOf(sVal);
                if(num == null)// Hatâ fırlatıldığı için buna gerek yok sanırım
                    throw new Exception();
                return new Object[]{true, num};
            }
        }
        catch(Exception exc){
            System.err.println("Dönüşüm başarısız!\nVeri : " + data + "\tHedef veri tipi : " + targetType.getName() + "\nexc.toString : " + exc.toString());
            return new Object[]{false};
        }
        return new Object[]{false};
    }
    public static Class[] wrapPrimitiveClass(Class[] dTypes){// Daha verimli bir yöntemi olması lazım!
        Class[] values = new Class[dTypes.length];
        for(int sayac = 0; sayac < dTypes.length; sayac++){
            if(dTypes[sayac].equals(int.class))
                values[sayac] = Integer.class;
            else if(dTypes[sayac].equals(double.class))
                values[sayac] = Double.class;
            else if(dTypes[sayac].equals(boolean.class))
                values[sayac] = Boolean.class;
            else if(dTypes[sayac].equals(char.class))
                values[sayac] = String.class;
            else
                values[sayac] = dTypes[sayac];
        }
        return values;
    }

//ERİŞİM YÖNTEMLERİ:
    //ANA ERİŞİM YÖNTEMİ:
    public static DataTypeConverter getService(){
        if(serv == null)
            serv = new DataTypeConverter();
        return serv;
    }
}