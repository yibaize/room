package org.bql.utils.builder_clazz.PublicPackage;

import org.bql.utils.logger.LoggerUtils;
import org.bql.utils.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class CheckType {
    /**
     * 根据类型获取该生成的类型
     * @param t
     * @return
     */
    public static String getArrType(String t,String beanName,String fieldName){
        String type = "";
        switch (t){
            case "java.lang.String":
                type = "string";
                break;
            case "java.lang.Double":
                type = "double";
                break;
            case "java.lang.Boolean":
                type = "bool";
                break;
            case "java.lang.Short":
                type = "short";
                break;
            case "java.lang.Byte":
                type = "byte";
                break;
            case "java.lang.Float":
                type = "float";
                break;
            case "java.lang.Long":
                type = "long";
                break;
            case "java.lang.Char":
                type = "char";
                break;
            case "java.lang.Integer":
                type = "int";
                break;

            case "String":
                type = "string";
                break;
            case "double":
                type = "double";
                break;
            case "boolean":
                type = "bool";
                break;
            case "short":
                type = "short";
                break;
            case "byte":
                type = "byte";
                break;
            case "float":
                type = "float";
                break;
            case "long":
                type = "long";
                break;
            case "char":
                type = "char";
                break;
            case "int":
                type = "int";
                break;
            default:
                type = StringUtils.substringAfterLast(t,".");
        }
        if (StringUtils.isBlank(type)) {
            type = "Object";
            LoggerUtils.getPlatformLog().info("对象"+beanName+"的"+fieldName+"变量的类型属性"+t+"不正确：反射数据类型异常将自动转为string类型");
        }
        return type;
    }
//    public static String getType(Type t,String beanName,String fieldName){
//        String type = "";
//        System.out.println(t.getTypeName());
//        if(t == String.class)
//            type = "string";
//        else if(t == byte.class)
//            type = "byte";
//        else if(t == char.class)
//            type = "char";
//        else if(t == short.class)
//            type = "string";
//        else if(t == int.class)
//            type = "int";
//        else if(t == boolean.class)
//            type = "bool";
//        else if(t == float.class)
//            type = "float";
//        else if(t == double.class)
//            type = "double";
//        else if(t == long.class)
//            type = "long";
//        else
//            type = t.getClass().getSimpleName();
//        if (StringUtils.isBlank(type)) {
//            type = "Object";
//            LoggerUtils.getLogicLog().debug("对象"+beanName+"的"+fieldName+"变量的类型属性"+t+"不正确：反射数据类型异常将自动转为string类型");
//        }
//        return type;
//    }
//    public static String getType(Class<?> t,String beanName,String fieldName){
//        String type = "";
//        if(t == String.class)
//            type = "string";
//        else if(t == byte.class)
//            type = "byte";
//        else if(t == char.class)
//            type = "char";
//        else if(t == short.class)
//            type = "string";
//        else if(t == int.class)
//            type = "int";
//        else if(t == boolean.class)
//            type = "bool";
//        else if(t == float.class)
//            type = "float";
//        else if(t == double.class)
//            type = "double";
//        else if(t == long.class)
//            type = "long";
//        else
//            type = t.getSimpleName();
//        if (StringUtils.isBlank(type)) {
//            type = "Object";
//            LoggerUtils.getLogicLog().debug("对象"+beanName+"的"+fieldName+"变量的类型属性"+t+"不正确：反射数据类型异常将自动转为string类型");
//        }
//        return type;
//    }
    public static String checkProtocolType(String str,String clazz){
        String type = "";
        switch (str){
            case "boolean":
                type = "Boolean.parseBoolean";
                break;
            case "bool":
                type = "Boolean.parseBoolean";
                break;
            case "int":
                type = "Integer.parseInt";
                break;
            case "short":
                type = "Short.parseShort";
                break;
            case "byte":
                type = "Byte.parseByte";
                break;
            case "long":
                type = "Long.parseLong";
                break;
            case "float":
                type = "Float.parseFloat";
                break;
            case "double":
                type = "Double.parseDouble";
                break;

            case "Bool":
                type = "Boolean.parseBoolean";
                break;
            case "Boolean":
                type = "Boolean.parseBoolean";
                break;
            case "Integer":
                type = "Integer.parseInt";
                break;
            case "Short":
                type = "Short.parseShort";
                break;
            case "Byte":
                type = "Byte.parseByte";
                break;
            case "Long":
                type = "Long.parseLong";
                break;
            case "Float":
                type = "Float.parseFloat";
                break;
            case "Double":
                type = "Double.parseDouble";
                break;

            default:
                type = "String";
                break;
        }
        if(StringUtils.isEmpty(type))
            LoggerUtils.getPlatformLog().debug("协议类"+clazz+"的"+str+"类型异常错误");
        return type;
    }
    /**
     * 获取集合类型
     * @param
     * @return
     */
    public static String getGather(Field ft, String beanName, String fieldName){
        String type = "";
        switch (ft.getType().toString()){
            case "interface java.util.List":
                String ty = getGatherType(false,ft.getGenericType(),beanName,fieldName);
                type = "List<"+ty+">";
                break;
            case "interface java.util.Set":
                String ty1 = getGatherType(false,ft.getGenericType(),beanName,fieldName);
                type = "Set<"+ty1+">";
                break;
            case "interface java.util.Map":
                String ty2 = getGatherType(true,ft.getGenericType(),beanName,fieldName);
                type = "Dictionary<"+ty2+">";
                break;
        }
        if (StringUtils.isBlank(type))
            LoggerUtils.getPlatformLog().debug("反射数据类型异常");
        return type;
    }


    private static String getGatherType(boolean isMap, Type t, String beanName, String fieldName){
        String type = "";
        ParameterizedType pt = (ParameterizedType) t;
        if(isMap){
            Class<?> genericClazz = (Class)pt.getActualTypeArguments()[0];
            Class<?> genericClazz1 = (Class)pt.getActualTypeArguments()[1];
            String k = CheckType.getArrType(genericClazz.getName(),beanName,fieldName);
            String v = CheckType.getArrType(genericClazz1.getName(),beanName,fieldName);
            type = k+","+v;
        }else {
            Class genericClazz = (Class)pt.getActualTypeArguments()[0];
            type = CheckType.getArrType(genericClazz.getName(),beanName,fieldName);
        }
        if (StringUtils.isBlank(type))
            LoggerUtils.getPlatformLog().debug("集合类型错误异常");
        return type;
    }
    public static Object getType(Class<?> clazz,String value){
        Object type = null;
        if(clazz == int.class)
            type = Integer.valueOf(value);
        else if(clazz == float.class)
            type = Float.valueOf(value);
        else if(clazz == double.class)
            type = Double.valueOf(value);
        else if(clazz == long.class)
            type = Long.valueOf(value);
        else if(clazz == boolean.class)
            type = Boolean.valueOf(value);
        else if(clazz == byte.class)
            type = Byte.valueOf(value);
        else if(clazz == short.class)
            type = Short.valueOf(value);
        else if(clazz == String.class)
            type = value;
        return type;
    }
    public static String typeStr(Field field,String beanName){
        String typestr = "";
        //忽略生成字段
        Type isType = field.getGenericType();
        String fieldName = field.getName();
        //泛型类型
        if(isType instanceof ParameterizedType){
            typestr = CheckType.getGather(field,beanName,fieldName);
            //数组
        }else if(field.getType().isArray()){
            String arrFieldType = field.getGenericType().getTypeName();
            Class<?> arr = field.getType();
            arrFieldType = StringUtils.substringBeforeLast(arrFieldType,"[");
            typestr = CheckType.getArrType(arrFieldType,beanName,fieldName)+"[]";
        }
        else {
            Type type = field.getGenericType();
            typestr = CheckType.getArrType(type.getTypeName(),beanName,fieldName);
        }
        return typestr;
    }
}