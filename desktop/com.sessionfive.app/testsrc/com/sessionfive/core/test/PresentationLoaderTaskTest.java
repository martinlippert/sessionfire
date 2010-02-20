package com.sessionfive.core.test;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;

import org.jdesktop.animation.timing.Animator;

import com.sessionfive.animation.AnimationController;
import com.sessionfive.app.Display;
import com.sessionfive.core.AbstractShape;
import com.sessionfive.core.AnimationStyle;
import com.sessionfive.core.Camera;
import com.sessionfive.core.LayerType;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;
import com.sessionfive.core.ui.HierarchicFileStructureNode;
import com.sessionfive.core.ui.LineLayouter;
import com.sessionfive.core.ui.PresentationLoaderTask;
import com.sessionfive.core.ui.ShapeExtensionCreator;

public class PresentationLoaderTaskTest extends TestCase {

	private Presentation presentation;
	private TestShapeCreator creator;
	private AnimationController animationController;
	private TestAnimationStyle animationStyle;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		presentation = new Presentation();
		creator = new TestShapeCreator();
		animationStyle = new TestAnimationStyle();
		animationController = new TestAnimationController();
		animationController.init(presentation, null);
	}

	public void testCreateShapesFromFilesEmpty() throws Exception {
		HierarchicFileStructureNode fileStructure = new HierarchicFileStructureNode(
				null);

		PresentationLoaderTask loader = new PresentationLoaderTask(
				fileStructure, creator, presentation, null, null, null);

		loader.createShapes(animationStyle, new LineLayouter(), 0f, 0f, 0f,
				null, animationController);

		List<Shape> shapes = presentation.getShapes(LayerType.CAMERA_ANIMATED);
		assertNotNull(shapes);
		assertEquals(0, shapes.size());
	}

	public void testCreateShapesFromFilesFlatHierarchy() throws Exception {
		HierarchicFileStructureNode fileStructure = new HierarchicFileStructureNode(
				null);

		File file1 = new File("shape1");
		File file2 = new File("shape2");
		File file3 = new File("shape3");
		fileStructure.setChilds(new File[] { file1, file2, file3 });

		PresentationLoaderTask loader = new PresentationLoaderTask(
				fileStructure, creator, presentation, null, null, null);

		loader.createShapes(animationStyle, new LineLayouter(), 0f, 0f, 0f,
				null, animationController);

		List<Shape> shapes = presentation.getShapes(LayerType.CAMERA_ANIMATED);
		assertNotNull(shapes);
		assertEquals(3, shapes.size());
		assertEquals(file1, ((TestFileShape) shapes.get(0)).getFile());
		assertEquals(file2, ((TestFileShape) shapes.get(1)).getFile());
		assertEquals(file3, ((TestFileShape) shapes.get(2)).getFile());
	}

	public void testCreateShapesFromFilesDeepHierarchy() throws Exception {
		HierarchicFileStructureNode fileStructure = new HierarchicFileStructureNode(
				null);
		File file1 = new File("top1");
		File file2 = new File("top2");
		File file3 = new File("top3");
		fileStructure.setChilds(new File[] { file1, file2, file3 });

		HierarchicFileStructureNode top2Node = fileStructure.getChild(1);
		File top2child1 = new File("top2child1");
		File top2child2 = new File("top2child2");
		top2Node.setChilds(new File[] { top2child1, top2child2 });

		HierarchicFileStructureNode top2child1Node = top2Node.getChild(0);
		File top2child1child1 = new File("top2child1child1");
		File top2child1child2 = new File("top2child1child2");
		File top2child1child3 = new File("top2child1child3");
		File top2child1child4 = new File("top2child1child4");
		top2child1Node.setChilds(new File[] { top2child1child1,
				top2child1child2, top2child1child3, top2child1child4 });

		PresentationLoaderTask loader = new PresentationLoaderTask(
				fileStructure, creator, presentation, null, null, null);

		loader.createShapes(animationStyle, new LineLayouter(), 0f, 0f, 0f,
				null, animationController);

		List<Shape> shapes = presentation.getShapes(LayerType.CAMERA_ANIMATED);
		assertNotNull(shapes);
		assertEquals(3, shapes.size());
		assertEquals(file1, ((TestFileShape) shapes.get(0)).getFile());
		assertEquals(file2, ((TestFileShape) shapes.get(1)).getFile());
		assertEquals(file3, ((TestFileShape) shapes.get(2)).getFile());

		assertEquals(0, shapes.get(0).getShapes().size());
		assertEquals(2, shapes.get(1).getShapes().size());
		assertEquals(0, shapes.get(2).getShapes().size());

		Shape top2Shape = shapes.get(1);
		List<Shape> childShapes = top2Shape.getShapes();
		assertEquals(top2child1, ((TestFileShape) childShapes.get(0)).getFile());
		assertEquals(top2child2, ((TestFileShape) childShapes.get(1)).getFile());

		assertEquals(4, childShapes.get(0).getShapes().size());
		assertEquals(0, childShapes.get(1).getShapes().size());

		List<Shape> leafShapes = childShapes.get(0).getShapes();
		assertEquals(top2child1child1, ((TestFileShape) leafShapes.get(0))
				.getFile());
		assertEquals(top2child1child2, ((TestFileShape) leafShapes.get(1))
				.getFile());
		assertEquals(top2child1child3, ((TestFileShape) leafShapes.get(2))
				.getFile());
		assertEquals(top2child1child4, ((TestFileShape) leafShapes.get(3))
				.getFile());
	}

	protected static class TestShapeCreator implements ShapeExtensionCreator {
		@Override
		public Shape createShape(File file) {
			TestFileShape shape = new TestFileShape(file);
			return shape;
		}
	}

	protected static class TestFileShape extends AbstractShape {
		private final File file;

		public TestFileShape(File file) {
			this.file = file;
		}

		public File getFile() {
			return file;
		}
	}

	protected static class TestAnimationStyle implements AnimationStyle {

		@Override
		public String getName() {
			return "Test";
		}

		@Override
		public Animator createBackwardAnimator(Camera cameraStart,
				Camera cameraEnd, Display display, Shape endShape) {
			return new Animator(1);
		}

		@Override
		public Animator createForwardAnimator(Camera cameraStart,
				Camera cameraEnd, Display display, Shape endShape) {
			return new Animator(1);
		}

	}
	
	protected static class TestAnimationController extends AnimationController {
		
		@Override
		public void reset() {
		}
		
	}

}
