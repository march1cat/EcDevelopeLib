package ec.event;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ec.event.annotation.EcDataChannel;
import ec.event.annotation.EcEventConsumer;
import ec.event.annotation.EventListener;
import ec.reflect.AnnotationUtil;
import ec.resource.ResourceUtil;
import ec.system.Basis;

public class EcEventChannelCenter extends Basis{
	
	private List<EcEventChannel> channels = new ArrayList<EcEventChannel>();
	private String sourcePackage = null;
	public void init(String sourcePackage) throws Exception {
		this.sourcePackage = sourcePackage;
		iniChannels();
		initConsumers();
	}
	
	private void iniChannels() throws Exception {
		Set<Class<?>> classes = AnnotationUtil.scanClasses(EcDataChannel.class , sourcePackage);
		Iterator<Class<?>> iter = classes.iterator();
		while(iter.hasNext()) {
			Class c = iter.next();
			int mod = c.getModifiers();
			if( Modifier.isAbstract(mod) ) continue;
			
			Object inst = c.newInstance();
			if(inst instanceof EcEventChannel) {
				EcEventChannel channel = (EcEventChannel) inst;
				channel.init();
				channels.add(channel);
				this.log("Init EcEventData Channel , Topic = " + channel.getTopic());
			} 
		}
	}
	
	
	private void initConsumers() throws Exception {
		Set<Class<?>> classes = AnnotationUtil.scanClasses(EcEventConsumer.class , sourcePackage);
		Iterator<Class<?>> iter = classes.iterator();
		while(iter.hasNext()) {
			Class c = iter.next();
			if( !((EcEventConsumer) c.getAnnotation(EcEventConsumer.class)).autoGenerate()) continue;
			
			int mod = c.getModifiers();
			if( Modifier.isAbstract(mod) ) continue;
			
			Object inst = c.newInstance();
			if(inst instanceof EcEventSubscriber) {
				subscribe( (EcEventSubscriber) inst );
			} 
			
		}
	}
	
	public void subscribe(EcEventSubscriber subscriber) throws Exception {
		Method[] methods = subscriber.getClass().getDeclaredMethods();
		for(Method method : methods) {
			if(method.isAnnotationPresent(EventListener.class)) {
				String topic = method.getAnnotation(EventListener.class).topic();
				EcEventChannel channel = getChannel(topic);
				if(channel != null) {
					if(channel.addSubscriber(subscriber)) {
						this.log("Add EcEventData Channel[" + channel.getTopic() + "] Subscribe , Subscribe = " + subscriber.getClass().getName());	
					}
				}
			}
		}
	}
	
	
	public EcEventChannel getChannel(String topic) {
		if(this.isListWithContent(channels)) {
			for(EcEventChannel c : channels) {
				if(this.compareValue(c.getTopic(), topic)) return c;
			}
		}
		return null;
	}
	
	
	
	
}
