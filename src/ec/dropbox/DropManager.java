package ec.dropbox;

import java.io.FileInputStream;
import java.io.InputStream;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.users.FullAccount;

import ec.system.Basis;

public class DropManager extends Basis{
	
	private String ACCESS_TOKEN = null;
	private FullAccount account = null;
	private DbxClientV2 client = null;
	
	public DropManager(String ACCESS_TOKEN){
		this.ACCESS_TOKEN = ACCESS_TOKEN;
	}
	
	public void login(){
		DbxRequestConfig config = new DbxRequestConfig("dropbox/java-tutorial", "en_US");
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
        try {
			FullAccount account = client.users().getCurrentAccount();
			this.account = account;
			this.client = client;
		} catch (Exception e) {
			this.exportExceptionText(e);
		}
	}
	public boolean isLoginSuccess(){
		return client != null && account != null;
	}
	
	public void uploadFile(String fUri,String saveDpbxFileUri){
		if(isLoginSuccess()){
			try (InputStream in = new FileInputStream(fUri)) {
			    FileMetadata metadata = client.files().uploadBuilder("/" + saveDpbxFileUri) .uploadAndFinish(in);
			} catch(Exception e){
				e.printStackTrace();
			}
		} else log("Dropbox Not Login Success yet!!");
	}
}
