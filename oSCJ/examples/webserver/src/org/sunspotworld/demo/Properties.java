package org.sunspotworld.demo;

import static javax.safetycritical.annotate.Level.LEVEL_1;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Set;

import javax.safetycritical.annotate.SCJAllowed;

@SCJAllowed(value=LEVEL_1, members=true)
public class Properties extends HashMap {
    
    public String getProperty(String key) {
        return (String)get(key);
    }
    
    public Set propertyNames() {
        return keySet();
    }
    
}

