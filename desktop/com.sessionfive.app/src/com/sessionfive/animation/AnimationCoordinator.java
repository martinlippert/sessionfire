package com.sessionfive.animation;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

import com.sessionfive.core.CameraAnimator;
import com.sessionfive.core.ui.ShapeTimingTarget;

public class AnimationCoordinator {

	private final Map<Animator, List<TimingTarget>> runningAnimators;
	private CameraAnimator currentCameraAnimator;

	public AnimationCoordinator() {
		this.runningAnimators = new ConcurrentHashMap<Animator, List<TimingTarget>>();
	}

	public void startAnimation(CameraAnimator cameraAnimator,
			List<TimingTarget> additionalTargets) {
		addTimingTargets(cameraAnimator.getAnimator(), additionalTargets);
		addClearupTimingTarget(cameraAnimator.getAnimator());
		cleanupRunningShapeAnimations(additionalTargets);

		runningAnimators.put(cameraAnimator.getAnimator(), additionalTargets);

		currentCameraAnimator = cameraAnimator;
		currentCameraAnimator.getAnimator().start();
	}

	public void stopCamera() {
		currentCameraAnimator.removeTargetsFromAnimator();
	}

	public boolean isAnimationRunning() {
		return currentCameraAnimator != null
				&& currentCameraAnimator.getAnimator() != null
				&& currentCameraAnimator.getAnimator().isRunning();
	}

	private void addClearupTimingTarget(final Animator animator) {
		animator.addTarget(new TimingTargetAdapter() {
			@Override
			public void end() {
				runningAnimators.remove(animator);
			}
		});
	}

	private void addTimingTargets(Animator animator, List<TimingTarget> targets) {
		if (targets != null && animator != null) {
			for (TimingTarget timingTarget : targets) {
				animator.addTarget(timingTarget);
			}
		}
	}

	private void cleanupRunningShapeAnimations(
			List<TimingTarget> additionalTargets) {
		for (TimingTarget target : additionalTargets) {
			if (target instanceof ShapeTimingTarget) {
				cleanupRunningShapeAnimations((ShapeTimingTarget) target);
			}
		}
	}

	private void cleanupRunningShapeAnimations(ShapeTimingTarget target) {
		Iterator<Animator> running = runningAnimators.keySet().iterator();
		while (running.hasNext()) {
			Animator animator = running.next();
			List<TimingTarget> runningTargets = runningAnimators.get(animator);

			TimingTarget found = null;
			for (TimingTarget runningTarget : runningTargets) {
				if (runningTarget instanceof ShapeTimingTarget
						&& ((ShapeTimingTarget) runningTarget).getShape() == target
								.getShape()) {
					found = runningTarget;
				}
			}

			if (found != null) {
				animator.removeTarget(found);
				runningTargets.remove(found);
				if (runningTargets.size() == 0) {
					animator.stop();
				}
			}
		}
	}

}
