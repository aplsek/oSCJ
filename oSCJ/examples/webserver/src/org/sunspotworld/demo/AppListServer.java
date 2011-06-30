package org.sunspotworld.demo;

import static javax.safetycritical.annotate.Level.LEVEL_1;
import static javax.safetycritical.annotate.Scope.CALLER;

import javax.safetycritical.annotate.RunsIn;
import javax.safetycritical.annotate.SCJAllowed;
import javax.safetycritical.annotate.SCJRestricted;

@SCJAllowed(value=LEVEL_1, members=true)
public class AppListServer implements WebApplication {

    private String myText;

    public AppListServer(String myText) {
        this.myText = myText;
    }

    @SCJRestricted(maySelfSuspend = true)
    @RunsIn(CALLER)
    public Response serve(Request request) {
        if (request.uri.length() != 0) {
            return new Response(NanoHTTP.HTTP_NOTFOUND, NanoHTTP.MIME_HTML,
                    "<h1>URI path not found:</h1><code>" + request.uri + "</code>");
        }
        StringBuilder res = new StringBuilder();
        res.append("<html>\n<head><title>" + myText + "</title></head>\n");
        res.append("<body>\n<font face=\"arial narrow\" color=\"000000\">\n");
        res.append("<h1><font face=\"arial\" color=\"5382a1\">Available Services</font></h1>");
        res.append("<ol>\n");
        res.append("<li><a href=\"/about\">About This Server</a></li>\n");
        res.append("<li><a href=\"/stats\">Statistics</a></li>\n");
        res.append("<li><a href=\"/files\">Files</a></li>\n");
        res.append("</ol>\n<br>\n");
        res.append("</font>");
        res.append("</body>\n</html>");

        return new Response(NanoHTTP.HTTP_OK, NanoHTTP.MIME_HTML, res.toString());
    }
}
