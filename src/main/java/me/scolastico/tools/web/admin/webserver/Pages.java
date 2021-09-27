package me.scolastico.tools.web.admin.webserver;

import com.sun.net.httpserver.HttpExchange;
import java.util.HashMap;
import java.util.List;
import me.scolastico.tools.pairs.Pair;
import me.scolastico.tools.web.annoations.WebServerRegistration;
import me.scolastico.tools.web.interfaces.SimpleWebsiteInterface;
import org.apache.commons.fileupload.FileItem;

public class Pages implements SimpleWebsiteInterface {

  public final static String panel =
      "<!DOCTYPE html>\n"
          + "<html lang=\"en\" style=\"width: 100%;height: 100%;\">\n"
          + "\n"
          + "<head>\n"
          + "    <meta charset=\"utf-8\">\n"
          + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, shrink-to-fit=no\">\n"
          + "    <title>AdminPanel</title>\n"
          + "    <link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootswatch@5.1.0/dist/darkly/bootstrap.min.css\">\n"
          + "    <link rel=\"stylesheet\" href=\"https://fonts.googleapis.com/css2?family=Lato:ital,wght@0,400;0,700;1,400&amp;display=swap\">\n"
          + "    <link rel=\"stylesheet\" href=\"assets/css/styles.min.css\">\n"
          + "</head>\n"
          + "\n"
          + "<body class=\"d-flex flex-column h-100\">\n"
          + "    <header class=\"d-flex justify-content-center\">\n"
          + "        <div class=\"row\" style=\"width: 75%;margin-bottom: 20px;margin-top: 20px;\">\n"
          + "            <div class=\"col d-flex align-items-center\">\n"
          + "                <h1>Admin Panel</h1>\n"
          + "            </div>\n"
          + "            <div class=\"col-auto d-flex align-items-center\">\n"
          + "                <div class=\"dropdown\"><button class=\"btn btn-primary dropdown-toggle\" aria-expanded=\"false\" data-bs-toggle=\"dropdown\" type=\"button\">Options</button>\n"
          + "                    <div class=\"dropdown-menu dropdown-menu-dark\"><a class=\"dropdown-item\" href=\"#\" onclick=\"logout();return false;\">Logout</a></div>\n"
          + "                </div>\n"
          + "            </div>\n"
          + "        </div>\n"
          + "    </header>\n"
          + "    <main class=\"d-flex justify-content-center flex-grow-1\">\n"
          + "        <div style=\"width: 75%;height: 100%;padding: 0px;padding-bottom: 20px;\">\n"
          + "            <div class=\"d-flex flex-column h-100\" style=\"width: 100%;border-style: solid;background: #000000;\">\n"
          + "                <div class=\"font-monospace flex-grow-1\" id=\"consoleBody\" style=\"background: #000000;\">\n"
          + "                    <div id=\"consoleText\" style=\"overflow: scroll;\">\n"
          + "                        <div style=\"padding: 5px;\"><code class=\"text-nowrap\" id=\"consoleCode\" style=\"color: rgb(255,255,255);margin: 0px;line-height: 1;padding: 0px;height: 500px;overflow: scroll;font-size: 16px;\"></code></div>\n"
          + "                    </div>\n"
          + "                </div>\n"
          + "                <div>\n"
          + "                    <div style=\"width: 100%;height: 100%;border-top-width: 3px;border-top-style: solid;\"><input id=\"commandInput\" type=\"text\" style=\"width: 100%;background: rgb(0,0,0);color: rgb(255,255,255);border: 0px none rgb(0,0,0);border-top-style: solid;border-top-color: rgb(255,255,255);outline: 0;overflow: visible;padding: 5px;\" placeholder=\"Write your commands "
          + "here...\"></div>\n"
          + "                </div>\n"
          + "            </div>\n"
          + "        </div>\n"
          + "    </main>"
          + "    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js\"></script>\n"
          + "    <script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.bundle.min.js\"></script>\n"
          + "    <link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/toastr.js/2.1.4/toastr.css\" integrity=\"sha512-oe8OpYjBaDWPt2VmSFR+qYOdnTjeV9QPLJUeqZyprDEQvQLJ9C5PCFclxwNuvb/GQgQngdCXzKSFltuHD3eCxA==\" crossorigin=\"anonymous\" referrerpolicy=\"no-referrer\" />\n"
          + "    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/toastr.js/2.1.4/toastr.min.js\" integrity=\"sha512-lbwH47l/tPXJYG9AcFNoJaTMhGvYWhVM9YI43CT+uteTRRaiLCui8snIgyAN8XWgNjNhCqlAUdzZptso6OCoFQ==\" crossorigin=\"anonymous\" referrerpolicy=\"no-referrer\"></script>\n"
          + "    <script src=\"assets/js/script.min.js\"></script>\n"
          + "    <script>\n"
          + "    $(\"#commandInput\").keyup(function(event) {\n"
          + "      if (event.keyCode === 13) {\n"
          + "        executeCommand($(\"#commandInput\").val());\n"
          + "        $(\"#commandInput\").val(\"\");\n"
          + "      }\n"
          + "    });"
          + "    </script>\n"
          + "</body>\n"
          + "\n"
          + "</html>";

  public final static String login =
      "<!DOCTYPE html>\n"
          + "<html lang=\"en\" style=\"width: 100%;height: 100%;\">\n"
          + "\n"
          + "<head>\n"
          + "    <meta charset=\"utf-8\">\n"
          + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, shrink-to-fit=no\">\n"
          + "    <title>AdminPanel</title>\n"
          + "    <link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootswatch@5.1.0/dist/darkly/bootstrap.min.css\">\n"
          + "    <link rel=\"stylesheet\" href=\"https://fonts.googleapis.com/css2?family=Lato:ital,wght@0,400;0,700;1,400&amp;display=swap\">\n"
          + "    <link rel=\"stylesheet\" href=\"assets/css/styles.min.css\">\n"
          + "</head>\n"
          + "\n"
          + "<body style=\"width: 100%;height: 100%;\">\n"
          + "    <main class=\"d-flex justify-content-center align-items-center\" style=\"width: 100%;height: 100%;\">\n"
          + "        <div class=\"card\" style=\"width: 40%;\">\n"
          + "            <div class=\"card-body\">\n"
          + "                <form method=\"post\">\n"
          + "                    <h1 class=\"text-center\">Admin Login</h1><label class=\"form-label\" for=\"username\">Username *</label><input class=\"form-control\" type=\"text\" id=\"username\" style=\"margin-bottom: 10px;color: rgb(255,255,255);\" required=\"\"><label class=\"form-label\" for=\"password\">Password *</label><input class=\"form-control\" type=\"password\" id=\"password\" style=\"margin-bottom: 20px;color: rgb(255,255,255);\" required=\"\">\n"
          + "                    <div class=\"d-flex justify-content-end\"><a class=\"btn btn-primary\" role=\"button\" href=\"#\" onclick=\"if($(&quot;#username&quot;).val() === &quot;&quot; || $(&quot;#password&quot;).val() === &quot;&quot;){toastr[&quot;warning&quot;](&quot;Missing username and/or password...&quot;);}login($(&quot;#username&quot;).val(), $(&quot;#password&quot;).val());$(&quot;#password&quot;).val(&quot;&quot;);return false;\">Login</a></div>\n"
          + "                </form>\n"
          + "            </div>\n"
          + "        </div>\n"
          + "    </main>"
          + "    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js\"></script>\n"
          + "    <script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.bundle.min.js\"></script>\n"
          + "    <link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/toastr.js/2.1.4/toastr.css\" integrity=\"sha512-oe8OpYjBaDWPt2VmSFR+qYOdnTjeV9QPLJUeqZyprDEQvQLJ9C5PCFclxwNuvb/GQgQngdCXzKSFltuHD3eCxA==\" crossorigin=\"anonymous\" referrerpolicy=\"no-referrer\" />\n"
          + "    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/toastr.js/2.1.4/toastr.min.js\" integrity=\"sha512-lbwH47l/tPXJYG9AcFNoJaTMhGvYWhVM9YI43CT+uteTRRaiLCui8snIgyAN8XWgNjNhCqlAUdzZptso6OCoFQ==\" crossorigin=\"anonymous\" referrerpolicy=\"no-referrer\"></script>\n"
          + "    <script src=\"assets/js/script.min.js\"></script>\n"
          + "</body>\n"
          + "\n"
          + "</html>";

  public final static String redirect =
      "<head>\n"
          + "  <meta http-equiv=\"refresh\" content=\"2; URL=login\" />\n"
          + "</head>\n"
          + "<body>\n"
          + "  <p>Redirecting to login... If you are not redirected in five seconds, <a href=\"login\">click here</a>.</p>\n"
          + "</body>";

  @Override
  @WebServerRegistration(
      context = {"/.admin/", "/.admin/panel", "/.admin/login"},
      acceptGet_GET_VALUES = true
  )
  public Pair<Integer, String> handleRequest(HttpExchange HTTP_EXCHANGE, String[] PATH_VALUES, HashMap<String, String> GET_VALUES, HashMap<String, String> X_WWW_FORM_URLENCODED, String RAW, List<FileItem> FILES, Object JSON_OBJECT) {
    if (HTTP_EXCHANGE.getRequestURI().getPath().equals("/.admin/")) {
      return new Pair<>(200, redirect);
    } else if (HTTP_EXCHANGE.getRequestURI().getPath().equals("/.admin/login")) {
      return new Pair<>(200, login);
    } else if (HTTP_EXCHANGE.getRequestURI().getPath().equals("/.admin/panel")) {
      return new Pair<>(200, panel);
    }
    return new Pair<>(404, "not found");
  }

}
