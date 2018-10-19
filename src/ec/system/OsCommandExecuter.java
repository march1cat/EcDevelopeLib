package ec.system;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import ec.system.Runner;
import ec.system.controller.QueneDataController;

public class OsCommandExecuter extends QueneDataController{

	@Override
	protected boolean validateNewDataInQuene(Object dataWillInQuene) {
		return dataWillInQuene instanceof OsCommandInvoker;
	}

	@Override
	protected void process(Object dataInQuene) {
		OsCommandInvoker invoker = (OsCommandInvoker) dataInQuene;
		invoker.linkCommandExecuter(this);
		try {
			invoker.onStart();
        	Runtime runtime = Runtime.getRuntime();
        	Process p = runtime.exec(invoker.command());
        	
        	OsCommandConsoleReporter stdReporter =  new OsCommandConsoleReporter(
        					new BufferedReader(new InputStreamReader(p.getInputStream())),
        					invoker,
        					OsCommandConsoleReporter.MessageType.STD_OUT);
        	stdReporter.startRunner();
        	
        	OsCommandConsoleReporter errReporter =  new OsCommandConsoleReporter(
					new BufferedReader(new InputStreamReader(p.getErrorStream())),
					invoker,
					OsCommandConsoleReporter.MessageType.ERR_OUT);
        	errReporter.startRunner();
        	
        	
        	p.waitFor();
        	stdReporter.stopRunner();
            errReporter.stopRunner();
            invoker.onExist(p.exitValue());
            p.destroy();
		} catch (Exception e) {
			invoker.onError(e);
		}
	}
}
