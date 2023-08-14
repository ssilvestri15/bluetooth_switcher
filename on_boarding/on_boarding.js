//button click
const btn1 = document.getElementById("btn_page1");
const btn2 = document.getElementById("btn_page2");

const page1 = document.getElementById("page1");
const page2 = document.getElementById("page2");

btn1.addEventListener("click", function () {
    // Hide the element by setting its display style to "none"
    page1.style.display = "none";
    page2.style.display = "block";
    page3.style.display = "none";
  });

  btn2.addEventListener("click", function () {
    // Hide the element by setting its display style to "none"
    page1.style.display = "none";
    page2.style.display = "none";
    page3.style.display = "block";
    window.utils.startBleService();
    window.utils.startSocketServer(window);
  });