package com.sessionfive.core.ui;

public abstract class AbstractAnimationPathLayouter implements AnimationPathLayouter {

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public boolean equals(Object obj) {
		if (this.getClass() != obj.getClass())
			return false;
		return toString().equals(obj.toString());
	}

}
