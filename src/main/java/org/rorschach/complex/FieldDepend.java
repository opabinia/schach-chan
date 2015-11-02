package org.rorschach.complex;

import java.util.HashSet;
import java.util.Set;

public class FieldDepend {

    private String target;
    private Set<String> depend = new HashSet<>();

    public FieldDepend(String target) {
        this.target = target;
    }

    public String getTarget() {
        return target;
    }

    public Set<String> getDepend() {
        return depend;
    }
}
