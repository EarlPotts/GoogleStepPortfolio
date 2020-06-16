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


import com.google.appengine.api.datastore.*;
import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.Query.*;
import java.io.*;
import java.util.*;
import com.google.cloud.translate.*;
import java.util.logging.Logger;


/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  private static final Logger log = Logger.getLogger(DataServlet.class.getName());
	//global boolean to check for if translating after page refresh
	boolean translating = false;


  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {


    //write the json data to the server
		log.info("Translating: " + translating);
		if(!translating){
			List<Comment> comments = getComments();
			response.setContentType("application/json;");
			response.getWriter().println(commentsToJson(comments));
		} else {
			translating = false;
		}
  }

  private String commentsToJson(List<Comment> comments){
      Gson gson = new Gson();
      return gson.toJson(comments);
  }

  private List<Comment> getComments(){
    //get query from the datastore
    Query query = new Query("Comment").addSort("time", SortDirection.DESCENDING);
		List<Comment> comments = new ArrayList();

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);


    //loop through the datastore and add all the comments to the list
    for (Entity entity : results.asIterable()) {
      String text = (String) entity.getProperty("text");
			long time = (long) entity.getProperty("time");
      comments.add(new Comment(text, time));
    }

		return comments;
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    //check if we need to translate only or are we adding a comment
    String isTranslate = request.getParameter("isTranslate");
    log.info("in post Is translate?: " + isTranslate);
    if(isTranslate != null){
			translating = true;
    	System.out.println("in translation");
      //get the lang code from the parameters
      String langCode =  request.getParameter("languageCode");
      List<Comment> translatedComments = translateComments(langCode);

      response.setContentType("application/json;");
      String jsonData = commentsToJson(translatedComments);
    	log.info(jsonData);
      response.getWriter().println(jsonData);
    } else {
      // Get the input from the form.
      String text = request.getParameter("commentTxt");
      //check for empty text box
      if(text == null || text.equals("")){
      	return;
      }

      addComment(text, System.currentTimeMillis());
    }
    //reload the page to show new comment
    response.sendRedirect("/index.html");

  }

	private List<Comment> translateComments(String languageCode){
		Translate translate = TranslateOptions.getDefaultInstance().getService();
		//loop through the list of comments and translate them to new List
		List<Comment> translatedComments = new ArrayList<Comment>();
		List<Comment> comments = getComments();
		for(Comment comment: comments){
			//translate the comment
			Translation translation = translate.translate(
				comment.text,
				Translate.TranslateOption.targetLanguage(languageCode)
			);
			String translatedComment = translation.getTranslatedText();
			translatedComments.add(new Comment(translatedComment, comment.time));
    	System.out.println("Original: " + comment.text + " Translated: " + translatedComment);
		}
		return translatedComments;
	}

	//add a new comment to the page's comments
  private void addComment(String commentText, long time){
    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("text", commentText);
    commentEntity.setProperty("time", time);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);
  }

	//class to store each individual comment on the page
  private class Comment{
      public String text;
      public long time;

      Comment(String text, long time){
          this.text = text;
          this.time = time;
      }
  }
}
