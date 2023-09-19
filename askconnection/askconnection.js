window.utils.getConnectionData("connectionData", (data) => {
  // Display the received data in the HTML page
  const json = JSON.parse(data);
  const connectionDataElement = document.getElementById("device_name");
  connectionDataElement.textContent = `${json.host}`;

  const btn2 = document.getElementById("btn_deny");
  btn2.addEventListener("click", function () {
    window.utils.closeWindow(json.winId);
  });
});
