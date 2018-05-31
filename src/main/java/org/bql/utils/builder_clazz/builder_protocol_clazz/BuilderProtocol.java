package org.bql.utils.builder_clazz.builder_protocol_clazz;

import org.bql.utils.logger.LoggerUtils;
import org.bql.utils.StringUtils;
import org.bql.utils.builder_clazz.PublicPackage.*;
import org.bql.utils.builder_clazz.ann.Protocol;
import org.bql.utils.builder_clazz.PublicPackage.BuilderAll;

import java.lang.annotation.Annotation;
import java.util.*;

public class BuilderProtocol {
    /**
     * 获取拥有Service注解的所有类
     * @param path 包路径
     * @return
     */
    public static void builder(String path){
        List<Class> clazzs = GetFileAllInit.getClasssFromPackage(path);
        Set<CodeModel> protocolSet = new HashSet<>();
        if (clazzs.size()<=0)
            return;
        for (Class c:clazzs) {
            Annotation proco = c.getAnnotation(Protocol.class);
            if(proco instanceof Protocol){
                Protocol proco1 = (Protocol) proco;
                protocolSet.add(new CodeModel(proco1.value(),c));
            }
        }
        reflectBean(protocolSet);
    }
    /**
     * 反射对象
     * @param beans
     */
    private static void reflectBean(Set<CodeModel> beans){
        List<ProtocolPackage> protocolModules = new ArrayList<>();
        if (beans.size()<=0) return;
        Iterator<CodeModel> iterator = beans.iterator();
        while (iterator.hasNext()){
            CodeModel model = iterator.next();
            protocolModules.add(ProtocolHandler.hancler(model));
        }
        checkId(protocolModules);
        builder(protocolModules);
    }
    private static void checkId(List<ProtocolPackage> protocolModules){
        Iterator<ProtocolPackage> iterator = protocolModules.iterator();
        List<Integer> ids = new ArrayList<>(protocolModules.size());
        while (iterator.hasNext()){
            ProtocolPackage module = iterator.next();
            int cazzId = module.getClazzId();
            if(ids.contains(cazzId)){
                LoggerUtils.getPlatformLog().info("注解为"+cazzId+"的id值重复请重新设值，其中一个类名为："+module.getClazzName());
                throw new RuntimeException("注解为"+cazzId+"的id值重复请重新设值，其中一个类名为："+module.getClazzName());
            }
            ids.add(module.getClazzId());
        }
    }
    private static void builder(List<ProtocolPackage> protocolModules){
        StringBuilder sb = new StringBuilder();
        StringBuilder sb1 =new StringBuilder();
        sb.append(BuilderAll.PATH_CNF.getOperateCommandRecivePackage()+"\n");
        Collections.sort(protocolModules);
        Iterator<ProtocolPackage> iterator = protocolModules.iterator();
        while (iterator.hasNext()){
            ProtocolPackage module = iterator.next();
            sb.append("import "+module.getClazzName()+";\n");
        }

        sb1.append(BuilderAll.PATH_CNF.getCommandCodePackage()+"\n");
        sb1.append("public class CommandCode{\n");

        sb.append(BuilderAll.PATH_CNF.getOperateCommandAbstractPackage());
        sb.append("public class OperateCommandRecive{\n");
        sb.append("\tprivate static OperateCommandRecive instance;\n");
        sb.append("\tpublic static OperateCommandRecive getInstance(){\n");
        sb.append("\t\tif(instance == null)\n");
        sb.append("\t\t\tinstance = new OperateCommandRecive();\n");
        sb.append("\t\treturn instance;\n");
        sb.append("\t}\n");
        sb.append("\tpublic OperateCommandAbstract recieve(int id,String[] params){\n");
        sb.append("\t\tswitch (id){\n");
        Iterator<ProtocolPackage> iterator1 = protocolModules.iterator();
        while (iterator1.hasNext()){
            ProtocolPackage module = iterator1.next();
            sb.append("\t\t\tcase "+module.getClazzId()+":\n");
            sb.append("\t\t\t\treturn get"+ StringUtils.substringAfterLast(module.getClazzName(),".")+"(params);\n");
            sb1.append("\tpublic static final int "+ StringUtils.substringAfterLast(module.getClazzName(),".")+" = "+module.getClazzId()+";\n");
        }
        sb1.append("}");
        sb.append("\t\t\tdefault:\n");
        sb.append("\t\t\t\treturn null;\n");
        sb.append("\t\t}\n");
        sb.append("\t}\n");
        Iterator<ProtocolPackage> iterator2 = protocolModules.iterator();
        while (iterator2.hasNext()){
            ProtocolPackage module = iterator2.next();
            sb.append("\tprivate OperateCommandAbstract get"+ StringUtils.substringAfterLast(module.getClazzName(),".")+"(String[] params){\n");
            int i = 0;
            for (String str:module.getField()){
                String type = CheckType.checkProtocolType(str,module.getClazzName());
                if(type.equals("String")){
                    sb.append("\t\t"+type+" value"+i+" = params["+i+"];\n");
                }else if(str.equals("bool")){
                    sb.append("\t\tboolean value"+i+" = "+type+"(params["+i+"]);\n");
                }else {
                    sb.append("\t\t"+str+" value"+i+" = "+type+"(params["+i+"]);\n");
                }
                i++;
            }
            sb.append("\t\treturn new "+ StringUtils.substringAfterLast(module.getClazzName(),".")+"(");
            for (int j = 0;j<module.getField().size();j++){
                if(j<=module.getField().size()-2)
                    sb.append("value"+(j)+",");
                else
                    sb.append("value"+(j));
            }
            sb.append(");\n");
            sb.append("\t}\n");
        }
        sb.append("}");
        WriteFile.writeText("OperateCommandRecive.java",sb.toString(),BuilderAll.PATH_CNF.getOperateCommandRecivePath());
        WriteFile.writeText("CommandCode.java",sb1.toString(),BuilderAll.PATH_CNF.getCommandCodePath());
    }
}
