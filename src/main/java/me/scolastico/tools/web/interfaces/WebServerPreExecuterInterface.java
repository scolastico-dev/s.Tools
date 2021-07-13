package me.scolastico.tools.web.interfaces;

import com.sun.net.httpserver.HttpExchange;

public interface WebServerPreExecuterInterface {

  public boolean execute(HttpExchange HTTP_EXCHANGE);

}
