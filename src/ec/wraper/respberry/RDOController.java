package ec.wraper.respberry;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;

public class RDOController extends RGpioFactory {

	// provision gpio pin #01 as an output pin and turn on
    private GpioPinDigitalOutput gpio_do = null; 
	
	public RDOController(int gpioPinNumber, String controlDeviceName, Object doActionType) {
		super(ControlMode.DataOuput);
		Pin pin = this.LoadPin(gpioPinNumber);
		if(pin != null){
			if(doActionType == DoActionProcessType.HIGH_TO_LOW){
				gpio_do = gpio.provisionDigitalOutputPin(pin, controlDeviceName, PinState.HIGH);
				gpio_do.setShutdownOptions(true, PinState.LOW);
				markInitialFinished();
			} else if(doActionType == DoActionProcessType.LOW_TO_HIGH){
				gpio_do = gpio.provisionDigitalOutputPin(pin, controlDeviceName, PinState.LOW);
				gpio_do.setShutdownOptions(true, PinState.HIGH);
				markInitialFinished();
			}
		}
	}
	
	public void toggle(){
		if(isInitialSuccess()){
			gpio_do.toggle();
		}
	}
	
	public void turnToHigh(){
		if(isInitialSuccess() && !gpio_do.isHigh()){
			gpio_do.high();
		}
	}
	
	public void turnToLow(){
		if(isInitialSuccess() && !gpio_do.isLow()){
			gpio_do.low();
		}
	}
	
	public void timerOn(long time){
		if(isInitialSuccess()){
			gpio_do.pulse(time, true); 
		}
	}
	
	public void shutdown(){
		if(isInitialSuccess()){
			 gpio.shutdown();
		}
	}
	

}
