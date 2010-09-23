package com.sessionfire.live;

import com.sessionfive.animation.AnimationController;
import com.sessionfive.app.SessionFiveApplication;
import com.sessionfive.core.Presentation;

public class LiveController {
	
	private Live live;

	public LiveController() {
		live = new Live();
		
		AnimationController animationController = SessionFiveApplication.getInstance().getAnimationController();
		new LiveSync(animationController, live);
	}

	public void upload() {
		Presentation presentation = SessionFiveApplication.getInstance().getPresentation();
		new JSONOutput().upload(presentation);
	}

	public void setLiveSync(boolean selected) {
		live.setEnabled(selected);
	}

}
