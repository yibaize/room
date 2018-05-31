package org.bql.utils.builder_clazz.builder_protostuff_clazz;

import org.bql.utils.StringUtils;
import org.bql.utils.builder_clazz.ann.OverlookField;
import org.bql.utils.builder_clazz.ann.Protostuff;
import org.bql.utils.builder_clazz.PublicPackage.CheckType;
import org.bql.utils.builder_clazz.PublicPackage.CodeModel;
import org.bql.utils.builder_clazz.PublicPackage.GetFileAllInit;
import org.bql.utils.builder_clazz.PublicPackage.WriteFile;
import org.bql.utils.builder_clazz.PublicPackage.BuilderAll;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

public class BuilderProtostuff {
    /**
     * 获取拥有Service注解的所有类
     * @param path 包路径
     * @return
     */
    public static void builder(String path){
        List<Class> clazzs = GetFileAllInit.getClasssFromPackage(path);
        Set<CodeModel> set = new HashSet<>();
        if (clazzs.size()<=0)
            return;
        for (Class c:clazzs) {
            Annotation proco = c.getAnnotation(Protostuff.class);
            if(proco instanceof Protostuff){
                Protostuff proco1 = (Protostuff) proco;
                set.add(new CodeModel("",c));
            }
        }
        reflectBean(set);
    }
    /**
     * 反射对象
     * @param
     */
    private static void reflectBean(Set<CodeModel> set){
        if (set.size()<=0) return;
        Iterator<CodeModel> iterator = set.iterator();
        while (iterator.hasNext()){
            Object o = null;
            try {
                CodeModel model = iterator.next();
                o = model.getClazz().newInstance();
                List<String> bean = reflectField(model.getId(), o);
                builder(o.getClass().getSimpleName(), bean);
                } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 获取对象属性名并生成协议类（java、C#）
     */
    private static List<String> reflectField(String id,Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        List<String> sb = new ArrayList<>();
        if (fields.length<=0)
            return null;
        String beanName = o.getClass().getName();
        String clazz = "public class "+o.getClass().getSimpleName()+" : IProtostuff {\n";
        sb.add(clazz);
        for (int i = 0;i< fields.length;i++){
            //忽略生成字段
            if(checkOverlook(fields[i]))
                continue;
            //属性名
            String field = fields[i].getName();
            field = StringUtils.capitalize(field);
            String s = "\tpublic "+CheckType.typeStr(fields[i],beanName)+" "+field+"{get;set;}"+"\n";
            sb.add(s);
        }
        sb.add("}");
        return sb;
    }

    /**
     * 是否可以忽略生成该字段
     * @return
     */
    private static boolean checkOverlook(Field f){
        Annotation ann = f.getAnnotation(OverlookField.class);
        return  (ann instanceof OverlookField);
    }
    private static void builder(String beanName, List<String> s){
        StringBuilder sb = new StringBuilder();
        sb.append("using System;\n");
        sb.append("using System.Collections.Generic;\n");
        sb.append("using ProtoBuf;\n");
        sb.append("[ProtoContract]\n");
        for (int i = 0;i<s.size();i++){
            sb.append(s.get(i));
            if(i<s.size()-2)
                sb.append("\t[ProtoMember("+(i+1)+")]"+"\n");
        }
        WriteFile.writeText(beanName+".cs",sb.toString(),BuilderAll.PATH_CNF.getCsPath());
    }

}
