/*
 * Enfili
 * Project hosted at https://github.com/ryanhosp/enfili/
 * Copyright 2013 Ho Siaw Ping Ryan
 *    
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.r573.enfili.ws.jersey;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

public class StreamStreamingOutput implements StreamingOutput {
	private static final int CHUNK_SIZE = 1024;
	private InputStream sourceStream;

	public StreamStreamingOutput(InputStream sourceStream) {
		this.sourceStream = sourceStream;
	}

	@Override
	public void write(OutputStream out) throws IOException, WebApplicationException {
		try {
			byte[] buf = new byte[CHUNK_SIZE];
			int bytesRead = 0;
			while ((bytesRead = sourceStream.read(buf)) != -1) {
				if (bytesRead < CHUNK_SIZE) {
					byte[] buf2 = new byte[bytesRead];
					System.arraycopy(buf, 0, buf2, 0, bytesRead);
					buf = buf2;
				}
				out.write(buf);
				buf = new byte[CHUNK_SIZE];
			}
			out.close();
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
