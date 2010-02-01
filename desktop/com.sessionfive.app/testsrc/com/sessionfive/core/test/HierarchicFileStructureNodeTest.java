package com.sessionfive.core.test;

import java.io.File;

import com.sessionfive.core.ui.HierarchicFileStructureNode;

import junit.framework.TestCase;

public class HierarchicFileStructureNodeTest extends TestCase {
	
	public void testEmptyHierarchicFileStructure() {
		HierarchicFileStructureNode structure = new HierarchicFileStructureNode(null);
		assertEquals(0, structure.getElementCount());
	}
	
	public void testLinearFileStructure() {
		HierarchicFileStructureNode structure = new HierarchicFileStructureNode(null);
		File file1 = new File("test1");
		File file2 = new File("test2");
		File file3 = new File("test3");
		structure.setChilds(new File[] {file1, file2, file3});
		
		assertEquals(3, structure.getElementCount());
		assertEquals(file1, structure.getChild(0).getFile());
		assertEquals(file2, structure.getChild(1).getFile());
		assertEquals(file3, structure.getChild(2).getFile());
		
		assertTrue(structure.hasChilds());
		assertFalse(structure.getChild(0).hasChilds());
		assertFalse(structure.getChild(1).hasChilds());
		assertFalse(structure.getChild(2).hasChilds());
		
		assertEquals(3, structure.getChildCount());
		assertEquals(0, structure.getChild(0).getChildCount());
		assertEquals(0, structure.getChild(0).getChildCount());
		assertEquals(0, structure.getChild(0).getChildCount());
	}

	public void testHierarchicFileStructure() {
		HierarchicFileStructureNode structure = new HierarchicFileStructureNode(null);
		File file1 = new File("top1");
		File file2 = new File("top2");
		File file3 = new File("top3");
		structure.setChilds(new File[] {file1, file2, file3});
		
		HierarchicFileStructureNode top2Node = structure.getChild(1);
		File top2child1 = new File("top2child1");
		File top2child2 = new File("top2child2");
		top2Node.setChilds(new File[] {top2child1, top2child2});
		
		HierarchicFileStructureNode top2child1Node = top2Node.getChild(0);
		File top2child1child1 = new File("top2child1child1");
		File top2child1child2 = new File("top2child1child2");
		File top2child1child3 = new File("top2child1child3");
		File top2child1child4 = new File("top2child1child4");
		top2child1Node.setChilds(new File[] {top2child1child1, top2child1child2, top2child1child3, top2child1child4});
		
		assertEquals(9, structure.getElementCount());
		assertEquals(3, structure.getChildCount());
		assertEquals(file1, structure.getChild(0).getFile());
		assertEquals(file2, structure.getChild(1).getFile());
		assertEquals(file3, structure.getChild(2).getFile());
		
		assertFalse(structure.getChild(0).hasChilds());
		assertTrue(structure.getChild(1).hasChilds());
		assertFalse(structure.getChild(2).hasChilds());
		
		assertEquals(0, structure.getChild(0).getChildCount());
		assertEquals(2, structure.getChild(1).getChildCount());
		assertEquals(0, structure.getChild(2).getChildCount());

		assertEquals(top2child1, structure.getChild(1).getChild(0).getFile());
		assertEquals(top2child2, structure.getChild(1).getChild(1).getFile());

		assertEquals(top2child1child1, structure.getChild(1).getChild(0).getChild(0).getFile());
		assertEquals(top2child1child2, structure.getChild(1).getChild(0).getChild(1).getFile());
		assertEquals(top2child1child3, structure.getChild(1).getChild(0).getChild(2).getFile());
		assertEquals(top2child1child4, structure.getChild(1).getChild(0).getChild(3).getFile());
	}

}
