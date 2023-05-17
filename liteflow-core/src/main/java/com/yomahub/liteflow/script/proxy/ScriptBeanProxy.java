//package com.yomahub.liteflow.script.proxy;
//
//import cn.hutool.core.collection.ListUtil;
//import cn.hutool.core.util.*;
//import com.yomahub.liteflow.exception.LiteFlowException;
//import com.yomahub.liteflow.exception.ScriptBeanMethodInvokeException;
//import com.yomahub.liteflow.script.annotation.ScriptBean;
//import com.yomahub.liteflow.util.LiteFlowProxyUtil;
//import com.yomahub.liteflow.util.SerialsUtil;
//import net.bytebuddy.ByteBuddy;
//import net.bytebuddy.implementation.InvocationHandlerAdapter;
//import net.bytebuddy.matcher.ElementMatchers;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import java.lang.reflect.InvocationHandler;
//import java.lang.reflect.Method;
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class ScriptBeanProxy {
//
//    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
//
//    private final Object bean;
//
//    private final Class<?> orignalClass;
//
//    private final ScriptBean scriptBeanAnno;
//
//    public ScriptBeanProxy(Object bean, Class<?> orignalClass, ScriptBean scriptBeanAnno) {
//        this.bean = bean;
//        this.orignalClass = orignalClass;
//        this.scriptBeanAnno = scriptBeanAnno;
//    }
//
//    public Object getProxyScriptBean(){
//        //获取bean里所有的method，包括超类里的
//        List<String> methodNameList = Arrays.stream(orignalClass.getMethods()).map(Method::getName).collect(Collectors.toList());
//
//        //首先看@ScriptBean标注里的includeMethodName属性
//        //如果没有配置，则认为是全部的method，如果有配置，就取配置的method
//        if (ArrayUtil.isNotEmpty(scriptBeanAnno.includeMethodName())){
//            methodNameList = methodNameList.stream().filter(
//                    methodName -> ListUtil.toList(scriptBeanAnno.includeMethodName()).contains(methodName)
//            ).collect(Collectors.toList());
//        }
//
//        //其次看excludeMethodName的配置
//        if (ArrayUtil.isNotEmpty(scriptBeanAnno.excludeMethodName())){
//            methodNameList = methodNameList.stream().filter(
//                    methodName -> !ListUtil.toList(scriptBeanAnno.excludeMethodName()).contains(methodName)
//            ).collect(Collectors.toList());
//        }
//
//        try{
//            return new ByteBuddy().subclass(orignalClass)
//                    .name(StrUtil.format("{}.ByteBuddy${}",
//                            ClassUtil.getPackage(orignalClass),
//                            SerialsUtil.generateShortUUID()))
//                    .method(ElementMatchers.any())
//                    .intercept(InvocationHandlerAdapter.of(new AopInvocationHandler(bean, methodNameList)))
//                    .annotateType(orignalClass.getAnnotations())
//                    .make()
//                    .load(ScriptBeanProxy.class.getClassLoader())
//                    .getLoaded()
//                    .newInstance();
//        }catch (Exception e){
//            throw new LiteFlowException(e);
//        }
//
//    }
//
//    public class AopInvocationHandler implements InvocationHandler {
//
//        private final Object bean;
//
//        private final Class<?> clazz;
//
//        private final List<String> canExecuteMethodNameList;
//
//        public AopInvocationHandler(Object bean, List<String> canExecuteMethodNameList) {
//            this.bean = bean;
//            this.clazz = LiteFlowProxyUtil.getUserClass(bean.getClass());
//            this.canExecuteMethodNameList = canExecuteMethodNameList;
//        }
//
//        @Override
//        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//            Method invokeMethod = Arrays.stream(clazz.getMethods()).filter(
//                    m -> m.getName().equals(method.getName()) && m.getParameterCount() == method.getParameterCount()
//            ).findFirst().orElse(null);
//
//            if (invokeMethod == null){
//                String errorMsg = StrUtil.format("cannot find method[{}]", clazz.getName(), method.getName());
//                throw new ScriptBeanMethodInvokeException(errorMsg);
//            }
//
//            if (!canExecuteMethodNameList.contains(method.getName())){
//                String errorMsg = StrUtil.format("script bean method[{}.{}] cannot be executed", clazz.getName(), method.getName());
//                throw new ScriptBeanMethodInvokeException(errorMsg);
//            }
//
//            return invokeMethod.invoke(bean, args);
//        }
//    }
//}
