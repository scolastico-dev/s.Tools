package me.scolastico.tools.web.annoations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import me.scolastico.tools.web.enums.SpecialWebsite;

/**
 * This annotation is required for the SimpleWebsiteInterface and the AdvancedWebsiteInterface.
 * Please place this annotation above the function which should be registered in the web server.
 * If some feature that provides values is disabled it will provide NULL.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface WebServerRegistration {

  /**
   * <pre>
   * The path you want to register.
   * You can specify values like an id with a '~' between two '/'.
   * With an '?' after an '/' can you specify that the url CAN
   * end here. This is multiple times possible. With a double '~~'
   * can you define that after that point every string will be
   * stored after an slash. Also its possible to set multiple static
   * paths with '|'. The used static path will then be stored in
   * the path values.
   *
   * ---------------------------------------------------------------------
   *
   * Example: /user/~
   * With this path would you get following values on the following calls:
   *
   * Call: /user
   * Return: 404 not found error!
   *
   * Call: /user/123
   * Return: [0] = 123
   *
   * Call: /user/123/123
   * Return: 404 not found error!
   *
   * ---------------------------------------------------------------------
   *
   * Example: /search/~/?lowToHigh|highToLow/?modifiers/~
   * With this path would you get following values on the following calls:
   *
   * Call: /search
   * Return: 404 not found error!
   *
   * Call: /search/Im%20a%20Test%20Search
   * Return: [0] = Im a Test Search
   *
   * Call: /search/Im%20a%20Test%20Search/lowToHigh
   * Return: [0] = Im a Test Search
   *         [1] = lowToHigh
   *
   * Call: /search/Im%20a%20Test%20Search/someThingElse
   * Return: 404 not found error!
   *
   * Call: /search/Im%20a%20Test%20Search/highToLow/modifiers/reverse
   * Return: [0] = Im a Test Search
   *         [1] = highToLow
   *         [2] = reverse
   *
   * ---------------------------------------------------------------------
   *
   * Example: /settings/?category/~/?view
   * With this path would you get following values on the following calls:
   *
   * Call: /settings
   * Return: [EMPTY]
   *
   * Call: /settings/category/1
   * Return: [0] = 1
   *
   * Call: /settings/category/1/view
   * Return: [0] = 1
   *         [1] = view
   *
   * ---------------------------------------------------------------------
   *
   * Example: /download/~~
   * With this path would you get following values on the following calls:
   *
   * Call: /download
   * Return: 404 not found error!
   *
   * Call: /download/id123
   * Return: [0] = id123
   *
   * Call: /download/id123/id456/id789
   * Return: [0] = id123
   *         [1] = id456
   *         [2] = id789
   *
   * ---------------------------------------------------------------------
   *
   * If you want more examples look in the WebServerTest class.
   *
   * @return The path for the registration
   * </pre>
   */
  public String[] context();

  /**
   * If you use the path values feature please set this to true.
   * Its on default false to save resources.
   * @return Should path values be checked?
   */
  public boolean acceptPathValues() default false;

  /**
   * This is only required on the SimpleWebsiteInterface.
   * In the AdvancedWebsiteInterface will this setting be ignored.
   * @return Should get be accepted?
   */
  public boolean acceptGet() default true;

  /**
   * If this is enabled get values in the url will be provided.
   * @return Should the get values be provided by the web server?
   */
  public boolean acceptGet_GET_VALUES() default false;

  /**
   * This is only required on the SimpleWebsiteInterface.
   * In the AdvancedWebsiteInterface will this setting be ignored.
   * @return Should post be accepted?
   */
  public boolean acceptPost() default false;

  /**
   * Only one data type can be enabled at once!
   * If this is enabled X_WWW_FORM_URLENCODED values in the post request will be provided.
   * Please be aware that any request that does not provide the "content-type" header
   * with the value "application/x-www-form-urlencoded" will be rejected!
   * @return Should the post values be provided by the web server?
   */
  public boolean acceptPost_X_WWW_FORM_URLENCODED() default true;

  /**
   * Only one data type can be enabled at once!
   * If this is enabled the raw string value in the post request will be provided.
   * @return Should the post value be provided by the web server?
   */
  public boolean acceptPost_RAW_STRING() default false;

  /**
   * Only one data type can be enabled at once!
   * If this is enabled files can be uploaded to the webserver.
   * @return Should the web server enable file uploads?
   */
  public boolean acceptPost_FILE() default false;

  /**
   * Only one data type can be enabled at once!
   * If this is enabled json can be passed to the server.
   * Please be aware that any request that does not provide the "content-type" header
   * with the value "application/json" will be rejected!
   * @return Should the web server read the body and parse it to your json object?
   */
  public boolean acceptPost_JSON() default false;

  /**
   * This is only required on the SimpleWebsiteInterface.
   * In the AdvancedWebsiteInterface will this setting be ignored.
   * @return Should put be accepted?
   */
  public boolean acceptPut() default false;

  /**
   * Only one data type can be enabled at once!
   * If this is enabled X_WWW_FORM_URLENCODED values in the post request will be provided.
   * @return Should the post values be provided by the web server?
   */
  public boolean acceptPut_X_WWW_FORM_URLENCODED() default true;

  /**
   * Only one data type can be enabled at once!
   * If this is enabled the raw string value in the post request will be provided.
   * @return Should the post value be provided by the web server?
   */
  public boolean acceptPut_RAW_STRING() default false;

  /**
   * Only one data type can be enabled at once!
   * If this is enabled files can be uploaded to the webserver.
   * @return Should the web server enable file uploads?
   */
  public boolean acceptPut_FILE() default false;

  /**
   * Only one data type can be enabled at once!
   * If this is enabled json can be passed to the server.
   * Please be aware that any request that does not provide the "content-type" header
   * with the value "application/json" will be rejected!
   * @return Should the web server read the body and parse it to your json object?
   */
  public boolean acceptPut_JSON() default false;

  /**
   * This is only required on the SimpleWebsiteInterface.
   * In the AdvancedWebsiteInterface will this setting be ignored.
   * @return Should patch be accepted?
   */
  public boolean acceptPatch() default false;

  /**
   * Only one data type can be enabled at once!
   * If this is enabled X_WWW_FORM_URLENCODED values in the post request will be provided.
   * @return Should the post values be provided by the web server?
   */
  public boolean acceptPatch_X_WWW_FORM_URLENCODED() default true;

  /**
   * Only one data type can be enabled at once!
   * If this is enabled the raw string value in the post request will be provided.
   * @return Should the post value be provided by the web server?
   */
  public boolean acceptPatch_RAW_STRING() default false;

  /**
   * Only one data type can be enabled at once!
   * If this is enabled files can be uploaded to the webserver.
   * @return Should the web server enable file uploads?
   */
  public boolean acceptPatch_FILE() default false;

  /**
   * Only one data type can be enabled at once!
   * If this is enabled json can be passed to the server.
   * Please be aware that any request that does not provide the "content-type" header
   * with the value "application/json" will be rejected!
   * @return Should the web server read the body and parse it to your json object?
   */
  public boolean acceptPatch_JSON() default false;

  /**
   * This is only required on the SimpleWebsiteInterface.
   * In the AdvancedWebsiteInterface will this setting be ignored.
   * @return Should patch be accepted?
   */
  public boolean acceptDelete() default false;

  /**
   * If this is enabled get values in the url will be provided.
   * @return Should the get values be provided by the web server?
   */
  public boolean acceptDelete_GET_VALUES() default false;

  /**
   * If you want to replace the default pages like the 404 page you can define here the page.
   * Should you use this feature its not required for the special page to have a valid context because its ignored if this is set.
   * Special pages cant have passed any values by the webserver except for the HttpExchange object.
   * Can only be used on SimpleWebsiteInterface!
   * @return ENUM which special page should be defined!
   */
  public SpecialWebsite website() default SpecialWebsite.NORMAL_PAGE;

  /**
   * If json should be automatically managed by the webserver set here the class of the json object.
   * @return The class of the json object which should be automatically parsed.
   */
  public Class<?> jsonObject() default void.class;

  /**
   * If you want to generate an map for your webserver set this to true if you are registering an API.
   * In normal cases every time true if you use an AdvancedWebsiteInterface.
   * @return Is this is an API registration?
   */
  public boolean isAPI() default false;

  /**
   * CURRENTLY NOT IN USE! WIP!
   * Max file size in MB if file upload is enabled in this registrations. Default is 256MB.
   * On larger files its recommended to use a synchronized function because the file is
   * until you save it in your function completely in the system ram.
   * @return Max file size for uploads in MB.
   */
  public int maxFileSizeInMb() default 256;

  /**
   * Each call of this site will add to not whitelisted IPs the specified usage weight.
   * If the usage weight exceeds the configured max weight per use of the web server the
   * user gets an 429 error and the LIMIT_REACHED_PAGE will be shown.
   *
   * If the weight is 0 the user can open this page even if he is at max weight.
   *
   * The default max weight is 60000.
   * An user loses every second 100 weight until he is back on 0.
   * @return The weight for each usage.
   */
  public int usageWeight() default 0;

  /**
   * An simple API description.
   * @return An simple API description.
   */
  public String apiDescription() default "";

  /**
   * An example request path.
   * The domain will be added automatically.
   * @return An example request path.
   */
  public String exampleRequest() default "";

  /**
   * An example response.
   * @return An example response.
   */
  public String exampleResponse() default "";

  /**
   * An array list of error messages with their short codes separated by a ' - '.
   * Example: 'not found - The resource could not be found.'
   * @return An array list of error messages with their short codes.
   */
  public String[] errorMessages() default {};

}
