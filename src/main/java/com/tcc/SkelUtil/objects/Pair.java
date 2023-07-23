package com.tcc.SkelUtil.objects;

import java.util.Objects;

public class Pair<A, B> {

	private A a;
	private B b;

	public Pair(A a, B b) {
		this.a = a;
		this.b = b;
	}
	
	public A getFirst() {
		return a;
	}
	
	public B getSecond() {
		return b;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Pair<?, ?>)) {
			return false;
		}

		Pair<?, ?> otherPair = (Pair<?, ?>) other;

		if (otherPair.a.equals(this.a) && otherPair.b.equals(this.b)) {
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {

		return Objects.hash(this.a, this.b);
	}
}

