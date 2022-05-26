package com.bemonovoid.playsqd.core.model;

import java.io.Serializable;

public record Tuple<L extends Serializable, R extends Serializable>(L left, R right) {
}
