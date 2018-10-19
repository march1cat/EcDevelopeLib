package ec.swing.event;

import javax.swing.JComponent;

public interface EcTriggerEventListener {

	public void onEventTriggered(Object eventType,JComponent eventTriggerComp);
}
