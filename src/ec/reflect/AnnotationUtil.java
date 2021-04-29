package ec.reflect;

import java.lang.annotation.Annotation;
import java.util.Set;

import org.reflections.Reflections;

public class AnnotationUtil {

	public static Set<Class<?>> scanClasses(Class embededAnnotClass) {
		return scanClasses(embededAnnotClass , ".*");
	}
	
	public static Set<Class<?>> scanClasses(Class embededAnnotClass , String prefix) {
		Reflections reflections = new Reflections(prefix);
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(embededAnnotClass);
        return annotatedClasses;
	}
	
	
	
}
