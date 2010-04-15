package com.sessionfive.app;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.util.List;

import javax.media.opengl.GLCanvas;

import com.sessionfive.core.ui.CentralControlPalette;

public class DropListener implements DropTargetListener {

	private final CentralControlPalette centralControlPalette;
	private final GLCanvas canvas;

	public DropListener(CentralControlPalette centralControlPalette,
			GLCanvas canvas) {
		this.centralControlPalette = centralControlPalette;
		this.canvas = canvas;
	}

	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
		reactOnDrag(dtde);
	}

	@Override
	public void dragOver(DropTargetDragEvent dtde) {
		reactOnDrag(dtde);
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
		reactOnDrag(dtde);
	}

	@Override
	public void dragExit(DropTargetEvent dte) {
	}

	@Override
	public void drop(DropTargetDropEvent dtde) {
		reactOnDrop(dtde);
	}

	private void reactOnDrag(DropTargetDragEvent dtde) {
		if (areFilesDragged(dtde.getTransferable())) {
			dtde.acceptDrag(DnDConstants.ACTION_COPY);
		} else {
			dtde.rejectDrag();
		}
	}

	private void reactOnDrop(DropTargetDropEvent dtde) {
		if (areFilesDragged(dtde.getTransferable())) {
			dtde.acceptDrop(DnDConstants.ACTION_COPY);
			File[] extractedFiles = extractFiles(dtde.getTransferable());
			if (extractedFiles != null && extractedFiles.length > 0) {
				centralControlPalette.loadPresentation(extractedFiles, canvas);
				dtde.dropComplete(true);
			}
			else {
				dtde.rejectDrop();
			}
		} else {
			dtde.rejectDrop();
		}
	}

	protected boolean areFilesDragged(Transferable tr) {
		try {
			DataFlavor[] flavors = tr.getTransferDataFlavors();
			for (int i = 0; i < flavors.length; i++) {
				if (flavors[i].isFlavorJavaFileListType()) {
					return true;
				}
			}
		} catch (Exception e) {
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	protected File[] extractFiles(Transferable tr) {
		File[] result = null;

		try {
			DataFlavor[] flavors = tr.getTransferDataFlavors();
			for (int i = 0; i < flavors.length; i++) {
				if (flavors[i].isFlavorJavaFileListType()) {
					List<File> fileList = (List<File>) tr
							.getTransferData(flavors[i]);
					result = fileList.toArray(new File[fileList.size()]);
				}
			}
		} catch (Exception e) {
			result = null;
		}

		return result;
	}

}
