// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*; 
import java.util.*; 
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

    ArrayList<String> comments = new ArrayList(Arrays.asList(
        "Love the site",
        "Black lives matter",
        "Great resume",
				"Final comment"
    ));

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    response.setContentType("application/json;");
		String[] commentsArray = getComments();
		String jsonData = commentsToJsonStateless(commentsArray);
    response.getWriter().println(jsonData);
  }

  private String[] getComments(){
      String[] commentsArray = comments.toArray(new String[0]);
      return commentsArray;
  }

	private String commentsToJsonStateless(String[] comments){
		String jsonComments = "[";
		for(int i = 0; i < comments.length; i++){
			jsonComments+= "{\"text\": \"" + comments[i] + "\"}";
			if(i!= comments.length - 1){
				jsonComments+= ",";
			}

		}
		jsonComments+= "]";
		return jsonComments;
	}

  private String commentsToJson(){
    String jsonComments = "[";
    for(int i = 0; i < comments.size(); i++){
        jsonComments+= "{\"text\": \"" + comments.get(i) + "\"}";
        if(i!= comments.size() - 1){
            jsonComments+= ",";
        }
    
    }
    jsonComments+= "]";
    return jsonComments;
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get the input from the form.
    String text = request.getParameter("commentTxt");
    //check for empty text box
    if(text == null || text.equals(""))
    	return;

    //add the new comment to the list and generate new json
    comments.add(text);
	String[] commentsArray = getComments();
	String jsonData = commentsToJsonStateless(commentsArray);

    //write the comments back to the servlet
    response.setContentType("application/json;");
    response.getWriter().println(jsonData);
    response.sendRedirect("/index.html");

  }
}
