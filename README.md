# Read Me 

### Technology Used
* Docker
* Postgres
* Maven
* Java
* Spring Boot
* Google Cloud Vision API

## First time Set Up
### Running Postgres with docker
Pull latest docker image
* docker pull postgres

Run docker image
* docker run --name hebTakehome -e POSTGRES_PASSWORD=password -d -p 5432:5432 postgres

### Creating our database and tables
After starting the docker image, Exec bash in the image
* docker exec -it hebTakehome bash

Log into postgres
* psql -U postgres

Create database
* create database imagetaggingdatabase;

Connect to database
* \c imagetaggingdatabase

Create tables using create statements from create.sql

### Setting up Google Cloud API 
Follow this guide fully to allow tagging API calls to occur successfully:

* [Google Cloud Vision Setup Guide](https://cloud.google.com/vision/docs/detect-labels-image-client-libraries)

### Building jar
* Download code
* Build in Intellij
* mvn clean install
* run generated jar, imageTagging.jar


## API 
### A limitation
I returned the image data as a byte[]. A front end of some sort would be required to take the byte[] and properly display the image in the response to the user.

Spring can send and properly display an image natively, but not display an image and send the metadata as one response.

### localhost:8080/images POST
Parameters:
* file
* label
* taggingEnabled
* url

Behavior:
* Takes a file or url for an image as input. 
* User can provide a label. If they do not, one will be supplied.
* If tagging is enabled, perform tagging of image using Google Cloud Vision API
* Save image and tags if enabled to database
* Return image as byte[] and any metadata.

Either file or url is required. taggingEnabled is required. Label is Optional

### localhost:8080/images/{imageId} GET
Behavoir:
* Searches database for image by image Id
* Return image(s) as byte[] and any metadata saved with it

### localhost:8080/images/?objects={dog,cat} GET 
Behavoir:
* Search database for all images that match any of the tags included in the query
  * So in the above example, return all images that were tagged as containing a dog or a cat
* Return image(s) as byte[] and any metadata saved with it


### localhost:8080/images/ GET
Behavoir:
* Return all images as byte[] and any metadata saved with them

## Testing
I recommend using [Postman](https://www.postman.com/)



