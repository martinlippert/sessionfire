package com.sessionfive.core.ui;

public abstract class AbstractLayouter implements Layouter {

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
