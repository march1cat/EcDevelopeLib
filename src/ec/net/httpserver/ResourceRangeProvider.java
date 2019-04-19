package ec.net.httpserver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;

import ec.net.httpserver.Response.Header;
import ec.system.DeveloperMode;
import ec.system.Runner;
import ec.system.controller.QueneDataController;

public class ResourceRangeProvider extends QueneDataController {

	private static int LIMIT_BUFFER_SIZE = 1024 * 1024 * 200;
	private static ResourceRangeProvider inst = null;
	
	protected void hadleFileStream(ResponseServerBytesResource res) {
		OutputStream os = res.getRequest().getClientServant().getResponseOutputStream();
		boolean isAllSend = false;
		try {
			FileInputStream fis = new FileInputStream(res.getFpath());
			int contentSize = fis.available();
			res.markResponsePartialContent();
			String rangeText  = res.getRequest().headers().get("Range");
			int rangeStart = -1;
			int rangeEnd = -1;
			if(rangeText != null){
				String range = rangeText.substring(rangeText.indexOf("bytes=") + "bytes=".length());
				if(compareValue(range, "0-1")) {
					res.setHttpHeader(Header.CONTENT_LENGTH, "2");
					res.setHttpHeader("Content-Range", "bytes 0-1/" + contentSize);
					log("Start provide streaming, File = " + res.getFpath() + ",Range = 0-1"+ "/" + contentSize);
				} else {
					String[] r = range.split("-");
					rangeStart = this.parseInt(r[0], -1);
					if(r.length >= 2) {
						rangeEnd = this.parseInt(r[1], -1);
						res.setHttpHeader("Content-Range", "bytes "+range+"/" + contentSize);
						log("Start provide streaming, File = " + res.getFpath() + ",Range =" + range + "/" + contentSize);
					} else {
						rangeEnd = contentSize - 1;
						res.setHttpHeader("Content-Range", "bytes "+rangeStart + "-" + rangeEnd +"/" + contentSize);
						log("Start provide streaming, File = " + res.getFpath() + ",Range =" + rangeStart + "-" + rangeEnd + "/" + contentSize);
					}
					res.setHttpHeader(Header.CONTENT_LENGTH, String.valueOf(rangeEnd - rangeStart + 1));
					
					
				}
			}
			os.write(res.getHeader().getBytes());
			
			if(rangeStart != -1 && rangeEnd != -1){
				if(rangeStart > 0) {
					log("Skip File Stream, Value = " + rangeStart);
					fis.skip(rangeStart);
				}
				
				final int preferRange = rangeEnd - rangeStart + 1;
				if(preferRange <=  LIMIT_BUFFER_SIZE) {
					byte[] readToSend =  new byte[preferRange];
					fis.read(readToSend);
					try {
						os.write(readToSend);
					} catch(Exception e){
						handleWriteToClientException(e);
					}
				} else {
					log("Notice PreferRange bigger than buffer Size,PreferR = " + preferRange + ", Buffer = " + LIMIT_BUFFER_SIZE);
					byte[] readToSend =  new byte[LIMIT_BUFFER_SIZE];
					int leftValue = preferRange;
					while(leftValue > 0){
						if(leftValue >= LIMIT_BUFFER_SIZE) {
							leftValue = leftValue - LIMIT_BUFFER_SIZE;
						} else {
							readToSend = new byte[leftValue];
							leftValue = 0;
						}
						fis.read(readToSend);
						try {
							os.write(readToSend);
						} catch(Exception e){
							handleWriteToClientException(e);
						}
						
						//log("Left out file size = " + leftValue + ", playBackSession = " + res.getPlayBackSession() + ", range["+rangeStart+","+rangeEnd+"]");
					}
				}
				log("Response playBackSession = " + res.getPlayBackSession() + ", range["+rangeStart+","+rangeEnd+"]");
				isAllSend = contentSize == rangeEnd + 1;
				fis.close();
			}
		} catch (Exception e) {
			this.exportExceptionText(e);
		}
		onRangeStreamingEnd(res,isAllSend);
	}
	
	protected void onRangeStreamingEnd(ResponseServerBytesResource res,boolean isAllSend) {
		if(!isAllSend) {
			try {
				res.getRequest().getClientServant().getResponseOutputStream().write("\r\n\r\n".getBytes());
				res.getRequest().getClientServant().getResponseOutputStream().flush();
				res.getRequest().closeClientConnection();
			} catch (Exception e) {
				handleWriteToClientException(e);
			}
		} else {
			try {
				res.getRequest().getClientServant().getResponseOutputStream().write("\r\n\r\n".getBytes());
				res.getRequest().getClientServant().getResponseOutputStream().flush();
				res.getRequest().closeClientConnection();
				log("Finish Output File Streaming, File = " + res.getFpath() + ", Close client connection");
			} catch (Exception e) {
				handleWriteToClientException(e);
			}
		}
	}
	
	
	
	private void handleWriteToClientException(Exception e){
		if(e instanceof SocketException && e.getMessage().indexOf("Broken pipe") >= 0){
			//do Nothing
			if(DeveloperMode.isON()) this.exportExceptionText(e);
		} else this.exportExceptionText(e);
	}

	@Override
	protected boolean validateNewDataInQuene(Object dataWillInQuene) {
		return dataWillInQuene instanceof ResponseServerBytesResource;
	}

	@Override
	protected void process(Object o) {
		ResponseServerBytesResource data = (ResponseServerBytesResource) o;
		this.hadleFileStream(data);
	}
	
	public static ResourceRangeProvider instance(){
		if( inst == null ) {
			inst = new ResourceRangeProvider();
			inst.startRunner();
		}
		return inst;
	}
	
	protected static void setLimitBufferSize(int size){
		LIMIT_BUFFER_SIZE = size;
	}
	
	
}
