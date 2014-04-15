package org.apache.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * 请先启动服务器 启动命令: mvn jetty:run
 * @author izerui.com
 * @version createtime：2013年8月3日 下午10:22:52
 */
public class TestRest {
	public static void main(String[] args) {
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope("localhost", 8080), new UsernamePasswordCredentials("kermit",
				"kermit"));
		CloseableHttpClient httpclient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
		try {
			HttpGet httpget = new HttpGet(
					"http://localhost:8080/activiti-explorer/service/repository/process-definitions");
			System.out.println("executing request" + httpget.getRequestLine());
			CloseableHttpResponse response = httpclient.execute(httpget);
			try {
				writeMessage(response);
			} finally {
				response.close();
			}
		}catch(Exception e) {
			e.getMessage();
		}finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void writeMessage(HttpResponse response) throws IllegalStateException, IOException {
		System.out.println("results: ");
		BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
		String output;
		while ((output = br.readLine()) != null) {
			System.out.println(output);
		}
	}
}
