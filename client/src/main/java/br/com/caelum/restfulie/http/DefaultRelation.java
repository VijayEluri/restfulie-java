/***
 * Copyright (c) 2009 Caelum - www.caelum.com.br/opensource - guilherme.silveira@caelum.com.br
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.caelum.restfulie.http;

import java.net.URI;

import br.com.caelum.restfulie.Link;
import br.com.caelum.restfulie.RestClient;

/**
 * Default implementation of a transition.
 *
 * @author guilherme silveira
 * @author lucas souza
 */
public class DefaultRelation implements Link {

	private String rel;
	private String href;
	private String type;
	private final RestClient client;

	public DefaultRelation(String rel, String href, String type,  RestClient client) {
		this.rel = rel;
		this.href = href;
		this.client = client;
		this.type = type;
	}

	public String getHref() {
		return href;
	}

	public String getRel() {
		return rel;
	}

	public String getType() {
		return type;
	}
	
	public Request follow() {
		if (href.startsWith("/")) {
			URI uri = client.lastURI();
			return client.at(extractHost(uri) + href).as(type);
		}
		return client.at(href).as(type);
	}

	private String extractHost(URI uri) {
		return uri.toString().replaceFirst(uri.getPath() + "$", "");
	}

	@Override
	public String toString() {
		return rel + " - " + href;
	}
	
}
