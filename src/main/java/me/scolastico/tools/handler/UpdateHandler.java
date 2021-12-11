package me.scolastico.tools.handler;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import javassist.NotFoundException;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Handler for easy updating of the jar file from a GitHub repo.
 */
public class UpdateHandler {

  /**
   * Get the newest release version of an GitHub repo.
   * @param ghRepo The repo path. Something like 'scolastico-dev/s.Tools'.
   * @return The tag name representing the version.
   * @throws IOException If the connection was not successful.
   */
  public static String getNewestVersion(String ghRepo) throws IOException {
    JSONObject json = new JSONObject(getReleaseData(ghRepo));
    return json.getString("tag_name");
  }

  /**
   * Replace the .jar from your main class with a new one from GitHub.
   * @param ghRepo The repo path. Something like 'scolastico-dev/s.Tools'.
   * @param assetName The asset name which should be downloaded from GitHub.
   * @param mainClass The main class of your application to get the correct path to the .jar file.
   * @throws IOException If the connection was not successful.
   * @throws NotFoundException If the asset file was not found.
   */
  public static void installUpdate(String ghRepo, String assetName, Class<?> mainClass) throws IOException, NotFoundException {
    JSONObject json = new JSONObject(getReleaseData(ghRepo));
    JSONArray assets = json.getJSONArray("assets");
    for (int i = 0; i < assets.length(); i++) {
      JSONObject asset = assets.getJSONObject(i);
      if (asset.getString("name").equals(assetName)) {
        BufferedInputStream in = new BufferedInputStream(new URL(asset.getString("url")).openStream());
        FileOutputStream fileOutputStream = new FileOutputStream(mainClass.getProtectionDomain().getCodeSource().getLocation().getFile());
        byte[] dataBuffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
          fileOutputStream.write(dataBuffer, 0, bytesRead);
        }
        return;
      }
    }
    throw new NotFoundException("No asset with the name '" + assetName + "' was found in the latest release.");
  }

  private static String getReleaseData(String ghRepo) throws IOException {
    HttpsURLConnection con = (HttpsURLConnection)
        new URL("https://api.github.com/repos/" + ghRepo + "/releases/latest")
            .openConnection();
    con.setRequestMethod("GET");
    con.setConnectTimeout(1000);
    con.setReadTimeout(1000);
    int status = con.getResponseCode();
    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    String inputLine;
    StringBuilder content = new StringBuilder();
    while ((inputLine = in.readLine()) != null) {
      content.append(inputLine);
    }
    in.close();
    con.disconnect();
    return content.toString();
  }

}
