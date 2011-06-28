package org.sunspotworld.demo;

import static javax.safetycritical.annotate.Level.LEVEL_1;
import static javax.safetycritical.annotate.Scope.CALLER;

import javax.safetycritical.annotate.RunsIn;
import javax.safetycritical.annotate.SCJAllowed;

/**
 * Interface defining an Application for the NanoHTTP server.
 */
@SCJAllowed(value=LEVEL_1, members=true)
public interface WebApplication {

    /**
     * Processes a HTTP request and generates a response. For convenience, the
     * web server can take care of error messages in the case of exceptions.
     * @param request not including the application specific prefix. 
     * @returns Response HTTP response
     */
    @RunsIn(CALLER)
    public Response serve(Request request) throws Exception;

}

