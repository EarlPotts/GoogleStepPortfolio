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

var sections = ['aboutMe', 'education', 'experience'];
var currIndex = 0;
function animateTitleText() {
  //store the element containing my name
  var animBox = document.getElementById('nameTitle');

  // the frames for the actual animation, every 5 frames, it will increase
  // font size by 1 until it reaches the max, and then will decrease
  var id = setInterval(frame, 5);
  var inc = 99;
  var decreasing = true;
  function frame() {
    if (inc == 100 && decreasing) {
      clearInterval(id);
    } else if (decreasing){
      //set the font size to a decreasing percentage to make the font smaller
        animBox.style.fontSize = inc + "%";
        inc--;
        //once it reaches 1% then have it switch to increasing font size
        if(inc == 1){
          decreasing = false;
        }
    }else{
      animBox.style.fontSize = inc + "%";
      inc++;
      if(inc == 175){
        decreasing = true;
      }
    }
  }
}

function moveLeft(){
  var index = currIndex===0 ?  sections.length - 1 : currIndex - 1;
  switchSection(index);
}

function moveRight(){
  var index = currIndex===sections.length - 1 ?  0: currIndex + 1;
  switchSection(index);
}

// This method takes in an index for the section meant to switch to, and it
// translates that into the actual navigation section and switches to it
// If the id equals -1 or -2, then the section should switch to the nav item to the
// left and right of the current nav item respectively
function switchSection (sectionIndex) {
  //store the appropriate elements in variables
  const newSection = document.getElementById(sections[sectionIndex]);
  const oldSection = document.getElementById(sections[currIndex]);
  //change the respective classes of the divs
  oldSection.className = "inactiveSection"
  newSection.className = "currentSection"
  //set the nav element of the new current section
  const oldNav =  document.getElementById(sections[currIndex] + 'Nav');
  const newNav = document.getElementById(sections[sectionIndex] + 'Nav');
  oldNav.className = "";
  newNav.className = "current"
  currIndex = sectionIndex;
}

async function fetchServlet(){
    const commentsList = document.getElementById('commentsList');
    await fetch('/data')
    .then(response => response.json())
    .then((comment) => {
        //loop through the comments on the server
        comment.forEach(comment => {
            //create aq new list item with the comment txt and add it to the list
            let newListItem = document.createElement("li");
            newListItem.appendChild(document.createTextNode(comment.text));
            commentsList.appendChild(newListItem);
            commentsList.appendChild(document.createElement("hr"));
    	    console.log("Comment: " + comment.text);
        });
    });
}

async function postData(commentText){
    const commentsList = document.getElementById('commentsList');
    await fetch('/data', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(commentText),
    })
    .then(response => response.json())
    .then((comment) => {
        //loop through the comments on the server
        comment.forEach(comment => {
            //create aq new list item with the comment txt and add it to the list
            let newListItem = document.createElement("li");
            newListItem.appendChild(document.createTextNode(comment.text));
            commentsList.appendChild(newListItem);
            commentsList.appendChild(document.createElement("hr"));
    	    console.log("Comment: " + comment.text);
        });
    });
}
