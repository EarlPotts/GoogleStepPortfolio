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

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['Hello world!', '¡Hola Mundo!', '你好，世界！', 'Bonjour le monde!'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}
