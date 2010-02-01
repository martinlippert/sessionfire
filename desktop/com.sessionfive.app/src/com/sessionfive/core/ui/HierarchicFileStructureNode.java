package com.sessionfive.core.ui;

import java.io.File;

public class HierarchicFileStructureNode {
	
	private File nodeElement;
	private HierarchicFileStructureNode[] childs;
	
	public HierarchicFileStructureNode(File file) {
		this.nodeElement = file;
		this.childs = new HierarchicFileStructureNode[0];
	}
	
	public File getFile() {
		return nodeElement;
	}

	public int getElementCount() {
		int count = nodeElement != null ? 1 : 0;
		for (HierarchicFileStructureNode child : childs) {
			count += child.getElementCount();
		}
		return count;
	}

	public void setChilds(File[] files) {
		this.childs = new HierarchicFileStructureNode[files.length];
		for (int i = 0; i < files.length; i++) {
			this.childs[i] = new HierarchicFileStructureNode(files[i]);
		}
	}
		
	public HierarchicFileStructureNode getChild(int index) {
		if (index < 0 || index >= childs.length) throw new IllegalArgumentException("index not correct");
		return childs[index];
	}

	public boolean hasChilds() {
		return childs.length > 0;
	}

	public int getChildCount() {
		return childs.length;
	}

}
