package ec.file.doc.pdf2;

import java.util.List;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.ElementListener;

public class NewPageElement implements Element{

	@Override
	public List<Chunk> getChunks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isContent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isNestable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean process(ElementListener arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int type() {
		// TODO Auto-generated method stub
		return 0;
	}

}
