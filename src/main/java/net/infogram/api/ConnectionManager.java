package net.infogram.api;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Abstracts away the connection details.
 */
class ConnectionManager {

    private enum HttpMethod { GET, POST, PUT, DELETE }

    public static HttpURLConnection sendRequest(final String baseUrl, final String requestMethod,
            final List<Parameter> parameters) throws IOException {
        HttpMethod method = HttpMethod.valueOf(requestMethod);
        switch (method) {
        case GET:
            return sendGetRequest(baseUrl, parameters);
        case POST:
            return sendPostRequest(baseUrl, parameters);
        case PUT:
            return sendPutRequest(baseUrl, parameters);
        case DELETE:
            return sendDeleteRequest(baseUrl, parameters);
        default:
            // all request methods should be anticipated
            throw new UnsupportedOperationException("Unexpected request method");
        }
    }

    public static HttpURLConnection sendGetRequest(final String baseUrl, final List<Parameter> parameters)
            throws IOException {
        String url = new String(baseUrl);
        url += "?" + Helpers.encodedParameterStringFromList(parameters);
        System.out.println("REQUEST" + url);
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        return connection;
    }

    /**
     * Overlapping code for sending POST and PUT requests.
     */
    public static HttpURLConnection sendPostPutRequest(final String requestMethod, final String baseUrl,
            final List<Parameter> parameters) throws IOException {

        String postData = Helpers.encodedParameterStringFromList(parameters);
        HttpURLConnection connection = (HttpURLConnection) new URL(baseUrl).openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod(requestMethod);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        // send the request parameters
        OutputStream os = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(postData);
        writer.flush();
        writer.close();

        connection.connect();

        return connection;
    }

    public static HttpURLConnection sendPostRequest(final String baseUrl, final List<Parameter> parameters)
            throws IOException {
        return sendPostPutRequest("POST", baseUrl, parameters);
    }

    public static HttpURLConnection sendPutRequest(final String baseUrl, final List<Parameter> parameters)
            throws IOException {
        return sendPostPutRequest("PUT", baseUrl, parameters);
    }

    public static HttpURLConnection sendDeleteRequest(final String baseUrl, final List<Parameter> parameters)
            throws IOException {
        String url = new String(baseUrl);
        url += "?" + Helpers.encodedParameterStringFromList(parameters);
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("DELETE");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        connection.connect();

        return connection;
    }
}
