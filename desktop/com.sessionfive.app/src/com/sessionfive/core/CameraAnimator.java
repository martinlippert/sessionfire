package com.sessionfive.core;

import java.util.ArrayList;
import java.util.List;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;

public class CameraAnimator {
	
	private final Animator animator;
	private final List<TimingTarget> cameraTargets;
	
	public CameraAnimator(Animator animator, TimingTarget cameraTarget) {
		super();
		this.animator = animator;
		this.cameraTargets = new ArrayList<TimingTarget>();
		this.cameraTargets.add(cameraTarget);
	}
	
	public Animator getAnimator() {
		return animator;
	}
	
	public List<TimingTarget> getCameraTarget() {
		return cameraTargets;
	}
	
	public void addCameraTarget(TimingTarget timingTarget) {
		this.animator.addTarget(timingTarget);
		this.cameraTargets.add(timingTarget);
	}

	public void removeTargetsFromAnimator() {
		for (TimingTarget target : cameraTargets) {
			this.animator.removeTarget(target);
		}
	}
	
}
