package com.polymeric.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.polymeric.base.FileResponse;
import com.polymeric.constants.Constants;


public class GenericityUtil {

	 /**
     * @category 泛型给不同对象添加属性值
     * @param <T>
     * @param t
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static <T> T setDate(T t) throws IllegalAccessException, InvocationTargetException{
        Date date = new Date();
        BeanUtils.setProperty(t, "setTime", date);
        BeanUtils.setProperty(t, "gmtModified", date);
        return t;
    }

    public static <T> T updateDate(T t) throws IllegalAccessException, InvocationTargetException{
        Date date = new Date();
        BeanUtils.setProperty(t, "gmtModified", date);
        return t;
    }
    
    public static <T> T setFileData(T t,FileResponse response) throws IllegalAccessException, InvocationTargetException{
        BeanUtils.setProperty(t, "fileUrl", response.getUrl());
        BeanUtils.setProperty(t, "filePath", response.getPath());
        BeanUtils.setProperty(t, "fileName", response.getFilename());
        BeanUtils.setProperty(t, "fileLable", response.getLable());
        Date date = new Date();
        BeanUtils.setProperty(t, "setTime", date);
        BeanUtils.setProperty(t, "gmtModified", date);
        return t;
    }
    
    
    public static <T> T setDateStr(T t) throws IllegalAccessException, InvocationTargetException{
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        BeanUtils.setProperty(t, "setTime", format.format(date));
        BeanUtils.setProperty(t, "gmtModified", format.format(date));
        return t;
    }
    
    public static <T> T setTokenDateStr(T t) throws IllegalAccessException, InvocationTargetException{
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        BeanUtils.setProperty(t, "createdAt", format.format(date));
        BeanUtils.setProperty(t, "updatedAt", format.format(date));
        return t;
    }

    
    //分页
    public static <T> List<T> Page(List<T> list, Integer pageNumber, Integer pageSize){
    	if(pageSize >= list.size()) {
    		pageNumber = Constants.PAGENUMBER ;
    		pageSize = Constants.PAGESIZE;
		}
		int indexNum = (pageNumber-1 )*pageSize;
		int endNum = (pageNumber -1 )*pageSize+pageSize;
		if(endNum > list.size()) {
			endNum  = list.size();
		}
		if( indexNum > endNum) {
			indexNum = (Constants.PAGENUMBER -1 )*Constants.PAGESIZE;
			endNum = (Constants.PAGENUMBER -1 )*Constants.PAGESIZE+Constants.PAGESIZE;
		}
		list = list.subList(indexNum, endNum);
	    return list;
	 }
   
    public static <T> List<T> toObject(Class<T> tClass,List<Map<String, Object>> list){
    	List<T> t = new ArrayList<T>();
    	for (int i = 0; i < list.size(); i++) {
		}
        return t;
    }
    
    public static boolean adminJudge(List<Integer> roleIdList,List<Integer> newRoleLIst){
    	boolean containsAdminRole = false;
		for (Integer adminRole : newRoleLIst) {
		    if (roleIdList.contains(adminRole)) {
		        containsAdminRole = true;
		        break; // 如果找到一个匹配的角色，跳出循环
		    }
		}
        return containsAdminRole;
    }
    
    public static String getFieldValue(Object object, String fieldName) {
        try {
            Class<?> clazz = object.getClass();
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true); // 设置为可访问，即使是私有字段也可以获取值
            Object value = field.get(object);

            if (value != null) {
                return value.toString();
            } else {
                return ""; // 如果字段值为null，返回空字符串，你可以根据需要进行调整
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return ""; // 处理异常，你可以根据需要进行调整
        }
    }
    
    
    /**
     * 判断对象是否非空且包含至少一个有值字段
     */
    public static boolean hasContent(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(obj);
                if (value != null && !value.toString().trim().isEmpty()) {
                    return true; // 只要有一个字段有内容，就返回 true
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

   
}
