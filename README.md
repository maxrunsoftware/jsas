# JSAS
 (J)ava (S)imple (A)uthenticator (S)ervice
 
 The goal of this project is to provide a simple solution to retrieving sensitive data from remote systems programmatically without the need of external libraries. Most programming languages have WGet type functionality and support basic authentication. Leveraging these 2 items, we can create a simple HttpGet server that once provided with a username and password will retrieve a specific resource. In this case, the basic authentication username is infact the resource name to retrieve and the password is the password on the resource. But how are the password and resource name assigned to a file? By utilizing the file name we can embed the password and resource name into the file name. This server will serve any resources containing an underscore. So if you wanted the password to be MyPass and the resource name to be MyData you would name your file MyPass_MyData.txt
 
 TXT and HTML file extensions are sent as UTF-8 text content type back to the client. If the file ends in any other extension then the data is sent back to the client as binary.
 
 A description can be included when naming the file in the format Description_Password_Name.ext which is strictly optional and currently serves no other purpose then to group the files in your serving directory.
 
 A docker-compose.yml file is included in the project to quickly deploy the system into Docker. Setting up an https encryption proxy in front of the app is highly recommended. This is a TODO to include in the docker file.
 
