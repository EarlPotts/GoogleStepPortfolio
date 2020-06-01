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

// This method takes in an index for the section meant to switch to, and it
// translates that into the actual navigation section and switches to it
// If the id equals -1 or -2, then the section should switch to the nav item to the
// left and right of the current nav item respectively
function switchSection (i) {
  //adjust the index parameter according to relative direction
  if(i == -1 || i == -2){
    //if we need to move to the left, subtract 1 from the index, add 1 if going right
    console.log("Original i value: " + i);
    i = i == -1 ? currIndex-1:currIndex+1;
    console.log("New i value: " + i);
    if(i == sections.length){
      i = 0;
    }else if(i == -1){
      i = sections.length - 1;
    }

    //adjust the i value if we are wrapping around
  }
  //store the appropriate elements in variables
    const container = document.getElementById('content');
    const div = document.getElementById(sections[i]);
    //clone the div we want to replace the old one with
    const clone = div.cloneNode(true);
    //remove all children of the container div and then add the clone
    while (container.firstChild) container.firstChild.remove();

    container.appendChild(clone);
    clone.style.display = '';
    //set the nav element of the new current section
    const oldNav =  document.getElementById(sections[currIndex] + 'Nav');
    const newNav = document.getElementById(sections[i] + 'Nav')
    oldNav.className = "";
    newNav.className = "current"
    currIndex = i;
}
