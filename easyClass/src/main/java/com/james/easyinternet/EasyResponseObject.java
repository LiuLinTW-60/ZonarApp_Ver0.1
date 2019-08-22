/*
 * Copyright 2014 Thinkermobile, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.james.easyinternet;

import org.apache.http.Header;

/**
 * @author JamesX
 */
public class EasyResponseObject {
    private Header[] allHeaders;
    private int header;
    private String body;

    /**
     *
     */
    public EasyResponseObject() {

    }

    /**
     * Parse a string to EasyResponseObject. No need to use this constructor from developer.
     *
     * @param string
     */
    public EasyResponseObject(String string) {
        String separator = Character.toString((char) 11);
        if (string != null && string.contains(separator)) {
            String[] values = string.split(separator);
            if (values.length > 0) {
                header = Integer.parseInt(values[0]);
            }
            if (values.length > 1) {
                body = values[1];
            }
        }
    }

    /**
     * set header
     *
     * @param header
     */
    public void setHeader(int header) {
        this.header = header;
    }

    /**
     * set all headers
     *
     * @param header
     */
    public void setHeaders(Header[] allHeaders) {
        this.allHeaders = allHeaders;
    }

    /**
     * get header
     *
     * @return
     */
    public int getHeader() {
        return header;
    }

    /**
     * get all headers
     *
     * @return
     */
    public Header[] getHeaders() {
        return allHeaders;
    }

    /**
     * @deprecated
     */
    public void setResponse(String response) {
        setBody(response);
    }

    /**
     * @deprecated
     */
    public String getResponse() {
        return getBody();
    }

    /**
     * set body
     *
     * @param body
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * get body
     *
     * @return
     */
    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        String separator = Character.toString((char) 11);
        return header + separator + body;
    }

}
