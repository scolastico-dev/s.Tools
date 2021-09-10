package me.scolastico.tools.web.dataholder;

import me.scolastico.tools.web.annoations.WebServerRegistration;
import me.scolastico.tools.web.enums.WebServerRequestType;
import me.scolastico.tools.web.interfaces.AdvancedWebsiteInterface;
import me.scolastico.tools.web.interfaces.SimpleWebsiteInterface;
import me.scolastico.tools.web.interfaces.SimpleWebsiteInterfaceWithPreHandler;

public class WebServerRegistrationData {

  private final AdvancedWebsiteInterface advancedWebsiteInterface;
  private final SimpleWebsiteInterface simpleWebsiteInterface;
  private final SimpleWebsiteInterfaceWithPreHandler simpleWebsiteInterfaceWithPreHandler;
  private final WebServerRequestType webServerRequestType;
  private final WebServerRegistration registration;

  public WebServerRegistrationData(AdvancedWebsiteInterface advancedWebsiteInterface, WebServerRegistration registration, WebServerRequestType requestType) {
    this.advancedWebsiteInterface = advancedWebsiteInterface;
    this.registration = registration;
    this.simpleWebsiteInterface = null;
    this.simpleWebsiteInterfaceWithPreHandler = null;
    this.webServerRequestType = requestType;
  }

  public WebServerRegistrationData(SimpleWebsiteInterface simpleWebsiteInterface, WebServerRegistration registration) {
    this.simpleWebsiteInterface = simpleWebsiteInterface;
    this.registration = registration;
    this.advancedWebsiteInterface = null;
    this.simpleWebsiteInterfaceWithPreHandler = null;
    this.webServerRequestType = WebServerRequestType.SIMPLE;
  }

  public WebServerRegistrationData(SimpleWebsiteInterfaceWithPreHandler simpleWebsiteInterfaceWithPreHandler, WebServerRegistration registration) {
    this.simpleWebsiteInterfaceWithPreHandler = simpleWebsiteInterfaceWithPreHandler;
    this.registration = registration;
    this.simpleWebsiteInterface = null;
    this.advancedWebsiteInterface = null;
    this.webServerRequestType = WebServerRequestType.SIMPLE;
  }

  public SimpleWebsiteInterfaceWithPreHandler getSimpleWebsiteInterfaceWithPreHandler() {
    return simpleWebsiteInterfaceWithPreHandler;
  }

  public AdvancedWebsiteInterface getAdvancedWebsiteInterface() {
    return advancedWebsiteInterface;
  }

  public SimpleWebsiteInterface getSimpleWebsiteInterface() {
    return simpleWebsiteInterface;
  }

  public WebServerRequestType getWebServerRequestType() {
    return webServerRequestType;
  }

  public WebServerRegistration getRegistration() {
    return registration;
  }

}
