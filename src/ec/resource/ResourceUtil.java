package ec.resource;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public  class ResourceUtil {
	public static void bindingResource(Object src , Object target) throws IllegalAccessException {
        final Field[] srcFields = src.getClass().getDeclaredFields();
        Class focusClass = target.getClass();
        do {
            Field[] declaredFields = focusClass.getDeclaredFields();
            for(Field declaredField : declaredFields){
                if(declaredField.isAnnotationPresent(ResouceWired.class) && !declaredField.getAnnotation(ResouceWired.class).isProvider()) {
                	
                	boolean isSettled = false;
                	for(Field srcField : srcFields){
                        if(srcField.getType() == declaredField.getType()) {
                            if(srcField.isAnnotationPresent(ResouceWired.class) && srcField.getAnnotation(ResouceWired.class).isProvider()) {
                            	declaredField.setAccessible(true);
                                srcField.setAccessible(true);
                                declaredField.set(target , srcField.get(src));
                                isSettled = true;
                                break;
                            }
                        }
                    }
                	
                	if(!isSettled && checkIsExtends(src.getClass() , declaredField.getType())) {
                		declaredField.setAccessible(true);
                		declaredField.set(target , src);
                	}
                }
            }
            focusClass = focusClass.getSuperclass();
        } while(focusClass != null);
    }
	
	
	
	private static boolean checkIsExtends(Class srcClass , Class targetClass) {
		Class focusClass = srcClass;
		do {
			if(focusClass.getName().equals(targetClass.getName())) return true;
			focusClass = focusClass.getSuperclass();
		} while(focusClass != null);
		return false;
	}
}
