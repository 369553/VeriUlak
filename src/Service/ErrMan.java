package Service;
// ErrMan : Error Management

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class ErrMan{
    private Object owner;// Hatâ idâre servisinin kullanıcısı; işlem güvenliği için
    private Method showErrorMethod;// Hatâ oluştuğunda hatânın gösterileceği yöntem
    private Object ownerOfShowMethod;// Hatânın gösterileceği yöntem hangi nesne üzerinde çalıştırılacak
    private ArrayList<String> errorTypes;// Hatâ tipleri
    private ArrayList<Error> errors;// Hatâlar
    private boolean invokeErrorMethodWhenErrorOccurred = false;
    private int cursor = 0;

    public ErrMan(){
        
    }

//İŞLEM YÖNTEMLERİ:
    //ÜRETİM YÖNTEMİ:
    public ErrMan produceErrMan(Object owner){
        return new ErrMan();
    }
    public void setOwner(Object owner, Object newOwner){
        if(owner == null)
            return;
        if(owner != this.owner)
            return;
        this.owner = newOwner;
    }
    public boolean setShowErrorMethod(Object owner, Object ownerOfShowMethod, Method showErrorMethod){
        if(owner == null)
            return false;
        if(owner == this.owner){
            if(ownerOfShowMethod == null)
                return false;
            if(showErrorMethod == null)
                return false;
            this.ownerOfShowMethod = ownerOfShowMethod;
            this.showErrorMethod = showErrorMethod;
            return true;
        }
        return false;
    }
    public boolean setErrorTypes(Object owner, ArrayList<String> errorTypes){
        if(owner == null)
            return false;
        if(owner != this.owner)
            return false;
        if(errorTypes == null)
            return false;
        if(errorTypes.isEmpty())
            return false;
        this.errorTypes = errorTypes;
        return true;
    }
    public void setErrors(Object owner, ArrayList<Error> errors){
        if(owner == null)
            return;
        if(owner != this.owner)
            return;
        this.errors = errors;
    }
    public boolean addError(Object owner, String errorType, String message, String explanation){
        if(owner == this.owner){
            HashMap<String, String> content = new HashMap();
            content.put("message", message);
            content.put("explanation", explanation);
            //Hatâ târihi eklenebilir
            Error err = new Error(errorType, content);
            getErrors().add(err);
            cursor++;
        }
        return false;
    }
    //
    

//ERİŞİM YÖNTEMLERİ:
    public String getLastErrorMessage(){
        return this.getErrors().get(cursor).getMessage();
    }
    public Method getShowErrorMethod(){
        return showErrorMethod;
    }
    public Object getOwnerOfShowMethod(){
        return ownerOfShowMethod;
    }
    public ArrayList<String> getErrorTypes(){
        if(errorTypes == null){
            errorTypes = new ArrayList<String>();
        }
        return errorTypes;
    }
    public ArrayList<Error> getErrors(){
        if(errors == null){
            errors = new ArrayList<Error>();
        }
        return errors;
    }
    

//İÇ SINIF:
    public class Error{
        private HashMap<String, String> content;
        private String errorType;

        private Error(String errorType, HashMap<String, String> content){
            this.errorType = errorType;
            this.content = content;
        }

//İÇ SINIF.İŞLEM YÖNTEMLERİ:
    void setErrorType(String errorType){
        this.errorType = errorType;
    }

//İÇ SINIF.ERİŞİM YÖNTEMLERİ:
        public HashMap<String, String> getContent(){
            if(content == null){
                content = new HashMap<String, String>();
            }
            return content;
        }
        public String getErrorType(){
            return errorType;
        }
        public String getMessage(){
            return getContent().get("message");
        }
    }
}