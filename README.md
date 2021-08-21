# JSAS
[Max Run Software](https://www.maxrunsoftware.com)

## (J)ava (S)imple (A)uthenticator (S)ervice
 
 The goal of this project is to provide a simple solution to retrieving sensitive data from remote systems programmatically without the need of external libraries. Most programming languages have WGet type functionality and support basic authentication. Leveraging these 2 items, we can create a simple HttpGet server that once provided with a username and password will retrieve a specific resource. In this case, the basic authentication username is the resource name to retrieve and the password is the password on the resource. But how are the password and resource name assigned to a file? By utilizing the file name we can embed the password and resource name into the file name...
```
<password>_<name>.<ext>
```
 
 **This server will serve any resources containing an underscore in the file name.** So if you wanted the password to be ```MyPass``` and the user name to be ```MyData``` you would name your file...
```
MyPass_MyData.txt
```
 
 .txt and .html file extensions are sent as UTF-8 text content type back to the client. If the file ends in any other extension then the data is sent back to the client as binary.
 
 A description can be included when naming the file in the format...
```
<description>_<password>_<name>.<ext>
```
 ...which is strictly optional and currently serves no other purpose then to group the files in your serving directory.
 
 A docker-compose.yml file is included in the project to quickly deploy the system into Docker. Setting up an https encryption proxy in front of the app is **highly recommended** and the docker-compose is setup to serve via HTTPS, just plug in your public and private keys into certificate.crt and certificate.key respectively.
 
 A tester application is included with the Java app. Just...
```
java -jar jsas.jar https://192.168.0.10 samplePass samplefile
```
...and JSAS will do a query to test that your application is serving. 
 
 **Disclaimer:** We make no claims in the secureness of this application. It seems to be secure enough for internal use. We would be hesitant to recommend exposing it publically without further testing. If you find a bug or security hole, let us know!
 
Environment Variables:
- ```JSAS_LOGGING``` &nbsp;&nbsp;&nbsp;&nbsp; The logging level ```TRACE```/```DEBUG```/```INFO```/```WARN```/```ERROR``` (```INFO```)
- ```JSAS_DIRCACHETIME``` &nbsp;&nbsp;&nbsp;&nbsp; The amount of time in milliseconds the directory scanner will cache the directory listing (```5000```)
- ```JSAS_DIR``` &nbsp;&nbsp;&nbsp;&nbsp; The directory to pull files from (```working directory```)
- ```JSAS_PORT``` &nbsp;&nbsp;&nbsp;&nbsp; Port for the web server (```8080```)
- ```JSAS_MAXTHREADS``` &nbsp;&nbsp;&nbsp;&nbsp; Max threads of the web server (```100```)
- ```JSAS_MINTHREADS``` &nbsp;&nbsp;&nbsp;&nbsp; Min threads of the web server (```10```)
- ```JSAS_IDLETIMEOUT``` &nbsp;&nbsp;&nbsp;&nbsp; Idle timeout of the web server (```120```)
- ```JSAS_JOINTHREAD``` &nbsp;&nbsp;&nbsp;&nbsp; Join the webserver thread (```true```)
