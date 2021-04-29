package ec.event;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

import ec.resource.ResouceWired;
import ec.system.Basis;

public class EcEventPublisher extends Basis{

	@ResouceWired
	private EcEventChannel channel;

	protected EcEventChannel channel() {
		return channel;
	}
	
	public void publish(EcEvent event) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		channel().publish(event);
	}
	
}
