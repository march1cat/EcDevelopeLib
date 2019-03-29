package ec.net.httpserver;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;

import ec.net.httpserver.Response.Header;
import ec.system.Runner;

public class ResourceRangeProvider extends Runner {

	private ResponseServerBytesResource res = null;
	private HttpClientRequest request = null;
	private boolean isAllSend = false;
	private OutputStream os;
	private int limitBufferSize = 1024 * 1024 * 20;
	
	public ResourceRangeProvider(HttpClientRequest request,ResponseServerBytesResource res){
		this.res = res;
		this.request = request;
	}
	
	@Override
	protected void running() {
		try {
			os = request.getClientServant().getResponseOutputStream();
			FileInputStream fis = new FileInputStream(res.getFpath());
			int contentSize = fis.available();
			res.markResponsePartialContent();
			String rangeText  = request.headers().get("Range");
			int rangeStart = -1;
			int rangeEnd = -1;
			if(rangeText != null){
				String range = rangeText.substring(rangeText.indexOf("bytes=") + "bytes=".length());
				if(compareValue(range, "0-1")) {
					res.setHttpHeader(Header.CONTENT_LENGTH, "2");
					res.setHttpHeader("Content-Range", "bytes 0-1/" + contentSize);
					os.write(res.getHeader().getBytes());
					os.write("\r\n".getBytes());
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
					
					os.write(res.getHeader().getBytes());
				}
			}
			
			if(rangeStart != -1 && rangeEnd != -1){
				if(rangeStart > 0) {
					if(rangeStart <=  limitBufferSize) {
						byte[] readDone = new byte[rangeStart];
						fis.read(readDone);
					} else {
						byte[] readDone = new byte[limitBufferSize];
						int leftValue = rangeStart;
						while(leftValue > 0){
							if(leftValue >= limitBufferSize){
								leftValue -= limitBufferSize;
							} else {
								readDone = new byte[leftValue];
								leftValue = 0;
							}
							fis.read(readDone);
						}
					}
				}
				
				
				
				final int preferRange = rangeEnd - rangeStart;
				if(preferRange <=  limitBufferSize) {
					byte[] readToSend =  new byte[preferRange];
					fis.read(readToSend);
					try {
						os.write(readToSend);
					} catch(Exception e){
						handleWriteToClientException(e);
					}
					os.flush();
				} else {
					log("Notice PreferRange bigger than buffer Size,PreferR = " + preferRange + ", Buffer = " + limitBufferSize);
					byte[] readToSend =  new byte[limitBufferSize];
					int leftValue = preferRange;
					while(leftValue > 0){
						if(leftValue >= limitBufferSize) {
							leftValue = leftValue - limitBufferSize;
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
						//log("Left out file size = " + leftValue);
					}
					os.flush();
				}
				
				isAllSend = contentSize == rangeEnd + 1;
				fis.close();
			}
		} catch (Exception e) {
			this.exportExceptionText(e);
		}
		
		this.stopRunner();
	}
	
	@Override
	protected void end() {
		if(!isAllSend) {
			try {
				os.write("\r\n\r\n".getBytes());
				os.flush();
			} catch (Exception e) {
				handleWriteToClientException(e);
			}
			((HttpClientServant)request.getClientServant()).switchOnStepParingRequest();
			((HttpClientServant)request.getClientServant()).triggerRunning();
		} else {
			try {
				os.write("\r\n\r\n".getBytes());
				os.flush();
				request.closeClientConnection();
				log("Finish Output File Streaming, File = " + res.getFpath() + ", Close client connection");
			} catch (Exception e) {
				handleWriteToClientException(e);
			}
		}
	}
	
	
	
	private void handleWriteToClientException(Exception e){
		if(e instanceof SocketException && e.getMessage().indexOf("Broken pipe") >= 0){
			//do Nothing
		} else this.exportExceptionText(e);
	}
	
}
