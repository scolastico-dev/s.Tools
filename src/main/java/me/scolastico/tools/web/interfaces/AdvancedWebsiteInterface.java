package me.scolastico.tools.web.interfaces;

import com.sun.net.httpserver.HttpExchange;
import java.util.HashMap;
import java.util.List;
import me.scolastico.tools.pairs.Pair;
import org.apache.commons.fileupload.FileItem;

public interface AdvancedWebsiteInterface {

  /**
   * The web request handle function is called then somebody calls the path from the WebServerRegistration.
   * Please dont forget to register this function with the @WebServerRegistration annotation!
   * If you want anything else than the HttpExchange you need to specify this in the registration!
   * @param HTTP_EXCHANGE The http exchange object.
   * @param PATH_VALUES The path values if enabled. Otherwise NULL.
   * @param GET_VALUES The get values if enabled. Otherwise NULL.
   * @return A pair with the http status code and the string which should be send to the client. If the return is NULL an 404 error will be send. If the code is 0 no answer will be send by the webserver by default. You can still send a response with the HttpExchange object.
   */
  public Pair<Integer, String> getRequest(HttpExchange HTTP_EXCHANGE, String[] PATH_VALUES, HashMap<String, String> GET_VALUES);

  /**
   * The web request handle function is called then somebody calls the path from the WebServerRegistration.
   * Please dont forget to register this function with the @WebServerRegistration annotation!
   * If you want anything else than the HttpExchange you need to specify this in the registration!
   * @param HTTP_EXCHANGE The http exchange object.
   * @param PATH_VALUES The path values if enabled. Otherwise NULL.
   * @param X_WWW_FORM_URLENCODED The X_WWW_FORM_URLENCODED values if enabled. Otherwise NULL.
   * @param RAW The raw value if enabled. Otherwise NULL.
   * @param FILES The files if something was uploaded. Otherwise NULL.
   * @param JSON_OBJECT The json object if the json object is managed by the webserver. Otherwise NULL.
   * @return A pair with the http status code and the string which should be send to the client. If the return is NULL an 404 error will be send. If the code is 0 no answer will be send by the webserver by default. You can still send a response with the HttpExchange object.
   */
  public Pair<Integer, String> postRequest(HttpExchange HTTP_EXCHANGE, String[] PATH_VALUES, HashMap<String, String> X_WWW_FORM_URLENCODED, String RAW, List<FileItem> FILES, Object JSON_OBJECT);

  /**
   * The web request handle function is called then somebody calls the path from the WebServerRegistration.
   * Please dont forget to register this function with the @WebServerRegistration annotation!
   * If you want anything else than the HttpExchange you need to specify this in the registration!
   * @param HTTP_EXCHANGE The http exchange object.
   * @param PATH_VALUES The path values if enabled. Otherwise NULL.
   * @param X_WWW_FORM_URLENCODED The X_WWW_FORM_URLENCODED values if enabled. Otherwise NULL.
   * @param RAW The raw value if enabled. Otherwise NULL.
   * @param FILES The files if something was uploaded. Otherwise NULL.
   * @param JSON_OBJECT The json object if the json object is managed by the webserver. Otherwise NULL.
   * @return A pair with the http status code and the string which should be send to the client. If the return is NULL an 404 error will be send. If the code is 0 no answer will be send by the webserver by default. You can still send a response with the HttpExchange object.
   */
  public Pair<Integer, String> putRequest(HttpExchange HTTP_EXCHANGE, String[] PATH_VALUES, HashMap<String, String> X_WWW_FORM_URLENCODED, String RAW, List<FileItem> FILES, Object JSON_OBJECT);

  /**
   * The web request handle function is called then somebody calls the path from the WebServerRegistration.
   * Please dont forget to register this function with the @WebServerRegistration annotation!
   * If you want anything else than the HttpExchange you need to specify this in the registration!
   * @param HTTP_EXCHANGE The http exchange object.
   * @param PATH_VALUES The path values if enabled. Otherwise NULL.
   * @param X_WWW_FORM_URLENCODED The X_WWW_FORM_URLENCODED values if enabled. Otherwise NULL.
   * @param RAW The raw value if enabled. Otherwise NULL.
   * @param FILES The files if something was uploaded. Otherwise NULL.
   * @param JSON_OBJECT The json object if the json object is managed by the webserver. Otherwise NULL.
   * @return A pair with the http status code and the string which should be send to the client. If the return is NULL an 404 error will be send. If the code is 0 no answer will be send by the webserver by default. You can still send a response with the HttpExchange object.
   */
  public Pair<Integer, String> patchRequest(HttpExchange HTTP_EXCHANGE, String[] PATH_VALUES, HashMap<String, String> X_WWW_FORM_URLENCODED, String RAW, List<FileItem> FILES, Object JSON_OBJECT);

  /**
   * The web request handle function is called then somebody calls the path from the WebServerRegistration.
   * Please dont forget to register this function with the @WebServerRegistration annotation!
   * If you want anything else than the HttpExchange you need to specify this in the registration!
   * @param HTTP_EXCHANGE The http exchange object.
   * @param PATH_VALUES The path values if enabled. Otherwise NULL.
   * @param GET_VALUES The get values if enabled. Otherwise NULL.
   * @return A pair with the http status code and the string which should be send to the client. If the return is NULL an 404 error will be send. If the code is 0 no answer will be send by the webserver by default. You can still send a response with the HttpExchange object.
   */
  public Pair<Integer, String> deleteRequest(HttpExchange HTTP_EXCHANGE, String[] PATH_VALUES, HashMap<String, String> GET_VALUES);

}
