package me.scolastico.tools.web.admin.webserver;

import com.sun.net.httpserver.HttpExchange;
import java.util.HashMap;
import java.util.List;
import me.scolastico.tools.pairs.Pair;
import me.scolastico.tools.web.annoations.WebServerRegistration;
import me.scolastico.tools.web.interfaces.SimpleWebsiteInterface;
import org.apache.commons.fileupload.FileItem;

public class Assets implements SimpleWebsiteInterface {

  private static final String script = "let lastConsoleLines=[],scrolledToBottom=!1;function logToConsole(o){const e=$(\"#consoleText\").scrollTop();$(\"#consoleText\").scrollTop($(\"#consoleText\")[0].scrollHeight);const t=$(\"#consoleText\").scrollTop()===e,n=$(\"#consoleCode\").html().split(/<br\\s*\\/?>/i);if(n.length>=1000){let o=\"\";n.splice(n.length-1000).forEach((function(e){o+=e+\"<br />\"})),$(\"#consoleCode\").html(o.substring(0,o.length-6))}$(\"#consoleCode\").append(o,\"<br >\"),t?$(\"#consoleText\").scrollTop($(\"#consoleText\")[0].scrollHeight):$(\"#consoleText\").scrollTop(e)}function executeCommand(o){$.ajax({type:\"PUT\",url:\"api/put\",data:JSON.stringify({command:o}),contentType:\"application/json\",dataType:\"json\",statusCode:{403:function(){toastr.error(\"Not logged in! Loading login...\"),setTimeout((function(){window.location.href=\"login\"}),2e3)}}})}function login(o,e){$.ajax({type:\"POST\",url:\"api/login\",data:JSON.stringify({username:o,password:e}),contentType:\"application/json\",dataType:\"json\",statusCode:{200:function(){toastr.success(\"Logged in! Loading panel...\"),setTimeout((function(){window.location.href=\"panel\"}),2e3)},403:function(){toastr.error(\"Password and/or username wrong!\")}}})}function logout(){$.ajax({type:\"GET\",url:\"api/logout\",dataType:\"json\",statusCode:{200:function(){toastr.success(\"Logged out! Loading login...\"),setTimeout((function(){window.location.href=\"login\"}),2e3)},403:function(){toastr.error(\"Not logged in! Loading login...\"),setTimeout((function(){window.location.href=\"login\"}),2e3)}}})}if(toastr.options={closeButton:!1,debug:!1,newestOnTop:!1,progressBar:!0,positionClass:\"toast-top-right\",preventDuplicates:!1,onclick:null,showDuration:\"300\",hideDuration:\"1000\",timeOut:\"5000\",extendedTimeOut:\"1000\",showEasing:\"swing\",hideEasing:\"linear\",showMethod:\"fadeIn\",hideMethod:\"fadeOut\"},$(\"#consoleText\").length){function doResize(){$(\"#consoleText\").height(\"1px\"),$(\"#consoleText\").height($(\"#consoleBody\").innerHeight().toString()+\"px\")}window.addEventListener(\"load\",doResize),window.addEventListener(\"resize\",doResize),window.setInterval((function(){$.ajax({type:\"GET\",url:\"api/get\",dataType:\"json\",statusCode:{403:function(){toastr.error(\"Not logged in! Loading login...\"),setTimeout((function(){window.location.href=\"login\"}),2e3)}},success:function(o){if(0===lastConsoleLines.length)o.output.forEach(o=>logToConsole(o));else{const e=lastConsoleLines[lastConsoleLines.length-1];let t=!1;o.output.forEach((function(o){t&&logToConsole(o),e===o&&(t=!0)})),t||o.output.forEach(o=>logToConsole(o))}lastConsoleLines=o.output}})}),2e3)}";
  private static final String style = ".bootstrap-select .btn:focus{outline:0!important;box-shadow:inset 0 -1px 0 #ddd}[contenteditable].form-control:focus,[type=email].form-control:focus,[type=password].form-control:focus,[type=tel].form-control:focus,[type=text].form-control:focus,button:focus,input.form-control:focus,input[type=email]:focus,input[type=number]:focus,input[type=password]:focus,input[type=text]:focus,textarea.form-control:focus,textarea:focus{box-shadow:inset 0 -1px 0 #ddd;outline:0!important}";

  @Override
  @WebServerRegistration(
      context = {"/.admin/assets/js/script.min.js", "/.admin/assets/css/styles.min.css"}
  )
  public Pair<Integer, String> handleRequest(HttpExchange HTTP_EXCHANGE, String[] PATH_VALUES, HashMap<String, String> GET_VALUES, HashMap<String, String> X_WWW_FORM_URLENCODED, String RAW, List<FileItem> FILES, Object JSON_OBJECT) {
    if (HTTP_EXCHANGE.getRequestURI().getPath().equals("/.admin/assets/js/script.min.js")) {
      return new Pair<>(200, script);
    } else if (HTTP_EXCHANGE.getRequestURI().getPath().equals("/.admin/assets/css/styles.min.css")) {
      return new Pair<>(200, style);
    }
    return new Pair<>(404, "not found");
  }

}
