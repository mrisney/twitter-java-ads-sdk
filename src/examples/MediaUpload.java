package examples;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.ws.rs.core.MediaType;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.steelhouse.twitter.ads.client.ClientService;
import com.steelhouse.twitter.ads.client.ClientServiceFactory;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

public class MediaUpload {
	
	public static void main(String args[]) throws Exception {

		Properties properties = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("src/main/resources/config.properties");
			properties.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		ClientServiceFactory.consumerKey = properties.getProperty("consumer.key");
		ClientServiceFactory.consumerSecret = properties.getProperty("consumer.secret");
		ClientServiceFactory.accessToken = properties.getProperty("access.token");
		ClientServiceFactory.accessTokenSecret = properties.getProperty("access.secret");

		String domain = "https://upload.twitter.com";
		String resource = "/1.1/media/upload.json";

		// String filePath = "C:/temp/twitter.jpeg";
		String filePath = "src/test/resources/angry.bird.800.320.png";

		ClientService clientService = ClientServiceFactory.getInstance();
		Client client = clientService.getClient();

		WebResource webResource = client.resource(domain);
		webResource.addFilter(new LoggingFilter());

		final FileDataBodyPart filePart = new FileDataBodyPart("media", new File(filePath));

		final FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
		FormDataMultiPart multiPart = (FormDataMultiPart) formDataMultiPart.bodyPart(filePart);

		ClientResponse response = webResource.path(resource).type(MediaType.MULTIPART_FORM_DATA_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class, multiPart);

		if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		}

		String output = response.getEntity(String.class);
		System.out.println(output);
		
		JsonObject jsonObj = new JsonParser().parse(output).getAsJsonObject();
		String mediaId = jsonObj.get("media_id_string").getAsString();
		System.out.println("media id = "+ mediaId);
	}

}
