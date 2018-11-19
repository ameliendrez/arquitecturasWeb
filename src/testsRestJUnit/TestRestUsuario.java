package testsRestJUnit;

import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import dao.LugarDAO;
import dao.TematicaDAO;
import dao.UsuarioDAO;
import entidades.Lugar;
import entidades.Tematica;
import entidades.Usuario;

public class TestRestUsuario {

	public final String BASE_URL = "http://localhost:8080/TPE-ARQUITECTURAS/api";

	public final HttpClient client = HttpClientBuilder.create().build();

	@Test
	public void testRESTInterface() throws ClientProtocolException, IOException {
		crearUsuarios();
		getUsuario();
		listarUsuarios();
		//updateTematica();
		//deleteTematica();
	}

	private void crearLugar( ) throws ClientProtocolException, IOException{
		String url = BASE_URL + "/lugares";

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode jsonObject = mapper.createObjectNode();
		jsonObject.put("nombre", "Pladema");
		jsonObject.put("ciudad", "Tandil");
		String jsonString = jsonObject.toString();
		HttpPost post = new HttpPost(url);
		post.setEntity(new StringEntity(jsonString, ContentType.APPLICATION_JSON));
		HttpResponse response = client.execute(post);
		String resultContent = getResultContent(response);
	}
	
	public void crearUsuarios() throws ClientProtocolException, IOException {
		System.out.println("UsuarioTest-> Se crean usuarios");
		
//		crearLugar();
//		
//		String url = BASE_URL + "/lugares/1";
//		HttpGet request = new HttpGet(url);
//		HttpResponse response = client.execute(request);
//		String resultContentPlace = getResultContent(response);
//		
//		HttpPost post = new HttpPost(url);
//		post.setEntity(new StringEntity(resultContentPlace, ContentType.APPLICATION_JSON));
//		HttpResponse responsePlace = client.execute(post);
		
//		String url = BASE_URL + "/lugares";
//
//		ObjectMapper mapperPlace = new ObjectMapper();
//		ObjectNode jsonObjectPlace = mapperPlace.createObjectNode();
//		jsonObjectPlace.put("nombre", "Pladema");
//		jsonObjectPlace.put("ciudad", "Tandil");
//		String jsonStringPlace = jsonObjectPlace.toString();
//		HttpPost post = new HttpPost(url);
//		post.setEntity(new StringEntity(jsonStringPlace, ContentType.APPLICATION_JSON));
//		HttpResponse response = client.execute(post);
//		String resultContentPlace = getResultContent(response);
		
//		Lugar lugar = (Lugar) post.getEntity();
		
		Lugar lugar = LugarDAO.getInstance().findById(1);
		System.out.println(lugar);
		
//		String resultPlace = getResultContent(responsePlace);
//		ObjectMapper mapperPlace = new ObjectMapper();
//
//		ObjectNode objectPlace = mapperPlace.createObjectNode();
//		objectPlace.pojoNode(resultPlace);
//		ArrayNode autores = jsonObject.putArray("autores");
//		autores.addPOJO(resultContentUsuario10);
//		HttpEntity lugar = responsePlace.getEntity();
		
//		JSONObject myObject = new JSONObject(responsePlace);

//		Lugar lugar = mapper.readValue(responsePlace.getEntity().getContent(), Lugar.class);

		
//	    String  type_json=result.getString("type");
//	    String name_json=result.getString("name");
//	    String id_json=result.getString("demo");
//	    JsonObject jObj = new JsonObject();
//	    jobj.put("id", id_json);
//	    jobj.put("type", type_json);
//	    jobj.put("name", name_json);
//	    jArray.put(jObj);
		
//		Lugar lugar = mapperPlace.readValue(responsePlace.getEntity().getContent(), Lugar.class);

//		Lugar lugar = mapperPlace.convertValue(responsePlace, Lugar.class);
//		ArrayNode lugarTrabajo = objectPlace.putArray("lugar");
//		lugarTrabajo.addPOJO(responsePlace);
//		HttpEntity  type_json=responsePlace.getEntity();
		

		String url = BASE_URL + "/usuarios";
		
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode jsonObject = mapper.createObjectNode();

		
		jsonObject.put("dni", "41313351");
		jsonObject.put("apellido", "Rampoldi");
		jsonObject.put("nombre", "Santiago");
		jsonObject.putPOJO("lugar", lugar);
		

		String jsonString = jsonObject.toString();
		HttpPost post = new HttpPost(url);
		post.setEntity(new StringEntity(jsonString, ContentType.APPLICATION_JSON));
		HttpResponse response = client.execute(post);

		System.out.println("\nPOST "+url);
		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
		String resultContent = getResultContent(response);
		System.out.println("Response Content : " + resultContent);

		jsonObject = mapper.createObjectNode();
		jsonObject.put("dni", "36626800");
		jsonObject.put("apellido", "Meliendrez");
		jsonObject.put("nombre", "Agustin");
		jsonObject.putPOJO("lugar", lugar);

		jsonString = jsonObject.toString();

		post = new HttpPost(url);
		post.setEntity(new StringEntity(jsonString, ContentType.APPLICATION_JSON));
		response = client.execute(post);

		System.out.println("\nPOST "+url);
		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
		resultContent = getResultContent(response);
		System.out.println("Response Content : " + resultContent);

	}

	private String getResultContent(HttpResponse response) throws IOException {
		HttpEntity entity = response.getEntity();
		if(entity != null) {
			BufferedReader rd = new BufferedReader(new InputStreamReader(entity.getContent()));
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			return result.toString();
		}
		else {
			return "";
		}
	}

	public void listarUsuarios() throws ClientProtocolException, IOException {
		System.out.println("UsuarioTest-> Se traen todos los usuarios");

		String url = BASE_URL + "/usuarios";

		HttpGet request = new HttpGet(url);

		HttpResponse response = client.execute(request);

		System.out.println("\nGET " + url);

		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

		String resultContent = getResultContent(response);

		System.out.println("Response Content : " + resultContent);

	}

	public void getUsuario() throws ClientProtocolException, IOException {
		System.out.println("UsuarioTest-> Se trae una usuario");

		String url = BASE_URL + "/usuarios/36626800";

		HttpGet request = new HttpGet(url);

		HttpResponse response = client.execute(request);

		System.out.println("\nGET " + url);

		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

		String resultContent = getResultContent(response);

		System.out.println("Response Content : " + resultContent);

	}


	public void updateUsuario() throws ClientProtocolException, IOException {

//		ObjectMapper mapper = new ObjectMapper();
//		ObjectNode jsonObject = mapper.createObjectNode();
//		jsonObject.put("nombre", "Roque");
//		jsonObject.put("raza", "Callejero");
//		jsonObject.put("edad", 8);
//		String jsonString = jsonObject.toString();
//
//		String url = BASE_URL + "/perros/1";
//		HttpPut request = new HttpPut(url);
//		request.setEntity(new StringEntity(jsonString, ContentType.APPLICATION_JSON));
//		HttpResponse response = client.execute(request);
//
//		System.out.println("\nPUT "+url);
//
//		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
//
//		String resultContent = getResultContent(response);
//
//		System.out.println("Response Content : " + resultContent);

	}

	public void deleteUsuario() throws ClientProtocolException, IOException {

//		String url = BASE_URL + "/perros/2";
//
//		HttpDelete request = new HttpDelete(url);
//
//		HttpResponse response = client.execute(request);
//
//		System.out.println("\nDELETE "+url);
//
//		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
//
//		String resultContent = getResultContent(response);
//
//		System.out.println("Response Content : " + resultContent);

	}

}
