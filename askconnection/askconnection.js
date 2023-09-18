window.utils.getConnectionData("connectionData", (data) => {
  // Display the received data in the HTML page
  const json = JSON.parse(data);
  const connectionDataElement = document.getElementById("device_name");
  connectionDataElement.textContent = `${json.host}`;
});
