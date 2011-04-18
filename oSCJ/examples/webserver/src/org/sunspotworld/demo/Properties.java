package org.sunspotworld.demo;

import static javax.safetycritical.annotate.Level.LEVEL_1;

import java.util.Hashtable;
import java.util.Enumeration;

import javax.safetycritical.annotate.SCJAllowed;

@SCJAllowed(value=LEVEL_1, members=true)
public class Properties extends Hashtable {
    
    public String getProperty(String key) {
        return (String)get(key);
    }
    
    public Enumeration propertyNames() {
        return keys();
    }
    
}

