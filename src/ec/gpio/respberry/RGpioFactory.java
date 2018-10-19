package ec.gpio.respberry;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

public abstract class RGpioFactory {

	public enum ControlMode {
		DataOuput, DataInput
	}

	public enum DoActionProcessType {
		HIGH_TO_LOW, LOW_TO_HIGH
	}

	protected final GpioController gpio = GpioFactory.getInstance();
	
	private Object controlMode = null;
	private boolean initialFinished = false;

	public RGpioFactory(Object controlMode) {
		this.controlMode = controlMode;
	}
	
	public void markInitialFinished(){
		initialFinished = true;
	}
	
	public boolean isInitialSuccess(){
		return initialFinished;
	}

	
	public Pin LoadPin(int pinNumber) {
		Pin rp = null;

		switch (pinNumber) {
		case 1:
			rp = RaspiPin.GPIO_01;
			break;
		case 2:
			rp = RaspiPin.GPIO_02;
			break;
		case 3:
			rp = RaspiPin.GPIO_03;
			break;
		case 4:
			rp = RaspiPin.GPIO_04;
			break;
		case 5:
			rp = RaspiPin.GPIO_05;
			break;
		case 6:
			rp = RaspiPin.GPIO_06;
			break;
		case 7:
			rp = RaspiPin.GPIO_07;
			break;
		case 8:
			rp = RaspiPin.GPIO_08;
			break;
		case 9:
			rp = RaspiPin.GPIO_09;
			break;
		case 10:
			rp = RaspiPin.GPIO_10;
			break;
		case 11:
			rp = RaspiPin.GPIO_11;
			break;
		case 12:
			rp = RaspiPin.GPIO_12;
			break;
		case 13:
			rp = RaspiPin.GPIO_13;
			break;
		case 14:
			rp = RaspiPin.GPIO_14;
			break;
		case 15:
			rp = RaspiPin.GPIO_15;
			break;
		case 16:
			rp = RaspiPin.GPIO_16;
			break;
		case 17:
			rp = RaspiPin.GPIO_17;
			break;
		case 18:
			rp = RaspiPin.GPIO_18;
			break;
		case 19:
			rp = RaspiPin.GPIO_19;
			break;
		case 20:
			rp = RaspiPin.GPIO_20;
			break;
		case 21:
			rp = RaspiPin.GPIO_21;
			break;
		case 22:
			rp = RaspiPin.GPIO_22;
			break;
		case 23:
			rp = RaspiPin.GPIO_23;
			break;
		}
		if(pinNumber == 20) rp = null;
		return rp;
	}

}
