package com.gomeplus.v.guppy.servlets;

import com.snail.http.SimpleRequest;
import com.snail.http.SimpleResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 *
 * @author pengmeng
 */
public class Welcome {

	public boolean before(String method, SimpleResponse response, SimpleRequest request) {
		
		return true;
	}

	public void do_default(SimpleResponse response, SimpleRequest request) {
		response.http().setStatus(HttpResponseStatus.NOT_FOUND);
		response.write("The request url " + request.getUri() + " can not be found on this server.");
	}

	public void do_time(SimpleResponse response, SimpleRequest request) {
		response.write(System.currentTimeMillis() / 1000);
	}
}
