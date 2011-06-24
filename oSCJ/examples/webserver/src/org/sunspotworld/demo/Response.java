package org.sunspotworld.demo;

import static javax.safetycritical.annotate.Level.LEVEL_1;

import java.io.*;

import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.Scope;
import static javax.safetycritical.annotate.Scope.IMMORTAL;

/**
 * HTTP response.
 * Return one of these from serve().
 */
@SCJAllowed(value=LEVEL_1, members=true)
public class Response {
    
    /**
     * Default constructor: response = HTTP_OK, data = mime = 'null'
     */
    public Response() {
        this.status = NanoHTTP.HTTP_OK;
    }
    
    /**
     * Basic constructor.
     */
    public Response(@Scope(IMMORTAL) String status,@Scope(IMMORTAL) String mimeType, InputStream data ) {
        this.status = status;
        this.mimeType = mimeType;
        this.data = data;
    }

    /**
     * Basic constructor. Use when content length is known.
     */
    public Response(@Scope(IMMORTAL) String status,@Scope(IMMORTAL) String mimeType, InputStream data, int contentLength ) {
        this.status = status;
        this.mimeType = mimeType;
        this.data = data;
        this.contentLength = contentLength;
    }

    /**
     * Convenience method that makes an InputStream out of
     * given text.
     */
    public Response(@Scope(IMMORTAL) String status,@Scope(IMMORTAL) String mimeType, String txt ) {
        this(status, mimeType, new ByteArrayInputStream(txt.getBytes()), txt.length());
    }

    /**
     * Adds given line to the header.
     */
    public void addHeader( String name, String value ) {
        header.put( name, value );
    }
    
    /**
     * HTTP status code after processing, e.g. "200 OK", HTTP_OK
     */
    @Scope(IMMORTAL) public String status;
    
    /**
     * MIME type of content, e.g. "text/html"
     */
    @Scope(IMMORTAL) public String mimeType;
    
    /**
     * Content length of the response or -1 if now known.
     */
    public int contentLength = -1;
    
    /**
     * Data of the response, may be null.
     */
    public InputStream data;
    
    /**
     * Headers for the HTTP response. Use addHeader()
     * to add lines.
     */
    public Properties header = new Properties();
}
