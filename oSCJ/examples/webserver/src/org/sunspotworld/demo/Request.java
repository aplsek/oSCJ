package org.sunspotworld.demo;

import static javax.safetycritical.annotate.Level.LEVEL_1;

import javax.safetycritical.annotate.SCJAllowed;

/**
 * HTTP request.
 */
@SCJAllowed(value=LEVEL_1, members=true)
public class Request {
    
    public Request(String method, String uri) {
        this.method = method;
        this.uri = uri;
        this.uriPrefix = "";
    }
    
    public String method;
    
    public String uri;
    
    public String uriPrefix;
    
    public Properties header = new Properties();
    
    public Properties parms = new Properties();

}
