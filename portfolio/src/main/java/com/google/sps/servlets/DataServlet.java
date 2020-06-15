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

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {


  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {


    //write the json data to the server
    List<String> comments = getComments();
    response.setContentType("application/json;");
    response.getWriter().println(commentsToJson(comments));
  }

  private String commentsToJson(List<Comment> comments){
      Gson gson = new Gson();
      return gson.toJson(comments);
  }

  private List<String> getComments(){
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
    boolean isTranslate = request.getParameter("isTranslate");
    if(isTranslate == true){
      //get the lang code from the parameters
      String langCode =  request.getParameter("languageCode");
      Translate translate = TranslateOptions.getDefaultInstance().getService();
      //loop through the list of comments and translate them to new List
      List<String> translatedComments = new ArrayList<String>();
      List<String> comments = getComments();
      for(String comment: comments){
        //translate the comment
        Translation translation = translate.translate(
          comment,
          Translate.TranslateOption.targetLanguage(languageCode)
        );
        String translatedComment = translation.getTranslatedText();
        translatedComments.add(comment);
      }
      //write the new translate
      response.setContentType("application/json;");
      response.getWriter().println(commentsToJson(comments));
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
      String text;
      long time;

      Comment(String text, long time){
          this.text = text;
          this.time = time;
      }
  }
}
