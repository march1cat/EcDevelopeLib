package ec.swing;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.List;

public abstract class EcFileDragableSelectBox extends EcJInfoBox{

	public EcFileDragableSelectBox(int width, int height, int xpos, int ypos) {
		super(width, height, xpos, ypos);
	}
	
	
	protected abstract void onDragFileToFrameEvent(String fileUri);
	
	protected void prepareFileDragListenerForAllFrame(){
		addDragListener(this);
		for(Component com : this.getContentPane().getComponents()){
			addDragListener(com);
		}
	}
	
	protected void addDragListener(Component component){
		component.setDropTarget(new DropTarget() {
		    public synchronized void drop(DropTargetDropEvent evt) {
		        try {
		            evt.acceptDrop(DnDConstants.ACTION_COPY);
		            List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
		            for (File file : droppedFiles) {
		            	onDragFileToFrameEvent(file.getPath());
		                break;
		            }
		        } catch (Exception ex) {
		            ex.printStackTrace();
		        }
		    }
		});
	}

}
