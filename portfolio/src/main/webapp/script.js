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

var currSection = 'aboutMe'
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

function switchSection (id) {
  //store the appropriate elements in variables
    const container = document.getElementById('content');
    const div = document.getElementById(id);
    //clone the div we want to replace the old one with
    const clone = div.cloneNode(true);
    //remove all children of the container div and then add the clone
    while (container.firstChild) container.firstChild.remove();

    container.appendChild(clone);
    clone.style.display = '';
    //set the nav element of the new current section
    const oldNav =  document.getElementById(currSection + 'Nav');
    const newNav = document.getElementById(id + 'Nav')
    oldNav.className = "";
    newNav.className = "current"
    currSection = id;
}
