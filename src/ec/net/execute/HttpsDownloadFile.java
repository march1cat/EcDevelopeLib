package ec.net.execute;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class HttpsDownloadFile extends HttpsWebQueryFactory{

	private String saveTo = null;
	
	public boolean saveTo(String url,String saveTo){
		this.setHost(url);
		this.saveTo = saveTo;
		String data = queryWeb(null);
		return compareValue("OK", data);
	}
	
	@Override
	protected String Read() throws Exception {
		InputStream is = urlCon.getInputStream();
		OutputStream output = null;
        byte[] buffer = new byte[1024];
        String result = null;
        try {
            output = new FileOutputStream(saveTo);
            for (int length; (length = is.read(buffer)) > 0;) {
                output.write(buffer, 0, length);
            }
            result = "OK";
        } catch(Exception e){
        	this.exportExceptionText(e);
        } finally {
            if (output != null) try { output.close(); } catch (IOException logOrIgnore) {}
            if (is != null) try { is.close(); } catch (IOException logOrIgnore) {}
        }
        return result;
	}
}
