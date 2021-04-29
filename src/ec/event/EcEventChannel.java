package ec.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import ec.event.annotation.EcDataChannel;
import ec.event.annotation.EventListener;
import ec.resource.ResourceUtil;
import ec.system.Basis;

@EcDataChannel(topic = "")
public abstract class EcEventChannel<E extends EcEvent>  extends Basis{

	private String topic = null;
	private List<EcEventSubscriber> subscribers = new ArrayList<EcEventSubscriber>();
	
	public void init() throws Exception {
		if(this.getClass().isAnnotationPresent(EcDataChannel.class)) {
			topic = resolveTopic();
		} else {
			throw new Exception("No topic assign exception!!");
		}
	}
	
	
	public EcEventPublisher buildPublisher() throws Exception {
		EcEventPublisher publisher = new EcEventPublisher();
		ResourceUtil.bindingResource(this, publisher);
		return publisher;
	}
	
	public boolean addSubscriber(EcEventSubscriber subscriber) throws Exception {
		if(!subscribers.contains(subscriber)) {
			subscribers.add(subscriber);
			return true;
		}  else {
			return false;
		}
	}
	
	public void removeSubscriber(EcEventSubscriber subscriber) throws Exception {
		subscribers.remove(subscriber);
	}
	
	public void publish(E event) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if(this.isListWithContent(subscribers)) {
			for(EcEventSubscriber subscriber : subscribers) {
				Method[] methods = subscriber.getClass().getDeclaredMethods();
				for(Method method : methods) {
					if(method.isAnnotationPresent(EventListener.class)) {
						String topic = method.getAnnotation(EventListener.class).topic();
						if(compareValue(topic, this.topic)) {
							Parameter[]  params = method.getParameters();
							if(params.length > 0 && event.getClass() == params[0].getParameterizedType()) {
								method.setAccessible(true);
								method.invoke(subscriber, event);
							}
						}
					}
				}
			}
		}
	}
	
	private String resolveTopic() throws Exception {
		String topic = this.getClass().getAnnotation(EcDataChannel.class).topic();
		if(topic == null || "".equals(topic)) 
			throw new Exception("Topic[value=" + topic + "] in invalid!!");
		else 
			return topic;
	}
	

	public String getTopic() {
		return topic;
	}
	
	
	
}
