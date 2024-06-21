package Base;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Advice{
    private String strAdvice;
    private Method methodApplyAdvice;
    private Object objOwnerOfMethod;
    private Object[] paramsOfMethod;
    private Object result = null;
    private boolean isSuccessful = false;
    /*private Object[][] data;
    private boolean isAssignAfterInvoke;*/

    public Advice(String strAdvice, Method mthApplyAdvice, Object objOwnerOfMethod, Object[] paramsOfMethod){
        this.strAdvice = strAdvice;
        this.methodApplyAdvice = mthApplyAdvice;
        this.objOwnerOfMethod = objOwnerOfMethod;
        this.paramsOfMethod = paramsOfMethod;
    }

//İŞLEM YÖNTEMLERİ:
    public boolean applyAdvice(){
        if(this.methodApplyAdvice == null)
            return false;
        if(this.objOwnerOfMethod == null)
            return false;
        try{
            if(methodApplyAdvice.getParameterCount() == 0/* && paramsOfMethod == null*/)
                result = methodApplyAdvice.invoke(objOwnerOfMethod, null);
            else
                result = methodApplyAdvice.invoke(objOwnerOfMethod, paramsOfMethod);
            isSuccessful = true;
        }
        catch(IllegalAccessException exc){
            return false;
        }
        catch(InvocationTargetException exc){
            return false;
        }
        catch(IllegalArgumentException exc){
            return false;
        }
        return isSuccessful;
    }

//ERİŞİM YÖNTEMLERİ:
    public String getStrAdvice(){
        return strAdvice;
    }
    public Method getMethodApplyAdvice(){
        return methodApplyAdvice;
    }
    public Object getObjOwnMethod(){
        return objOwnerOfMethod;
    }
    public Object[] getParamsOfMethod(){
        return paramsOfMethod;
    }
    public boolean getIsSuccessful(){
        return isSuccessful;
    }
    public Object getResult(){
        return result;
    }
}