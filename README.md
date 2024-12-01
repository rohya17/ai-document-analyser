# ai-document-analyser
In this project i have focused on analyzing any document using ollama AI and then asking ai the quetions about the document using (RAG) Retrieval-Augmented Generation

to run this application you need following setup
* First you need to download ollama ai which is open source llm ai model, you can download it from https://ollama.com/download/windows, i have done it on my windows os you can do it on any os you are comfortable with.
* I am using model **mistral** for this project, you can pull the same from command line after istallation using command **ollama run llama3.2**
* If you are on windows like me, remember to run **mistral** model in command prompt before running the project.
* For this project i have used postgres database and along with that we need to use **pgvector** extension, you have to install this extension externally you can do it using this tutorial https://www.mindfiretechnology.com/blog/archive/installing-pgvector-in-preparation-for-retrieval-augmented-generation/
* Create databse with name **pgvector** and run following queries in database 

CREATE EXTENSION IF NOT EXISTS vector;

CREATE EXTENSION IF NOT EXISTS hstore;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS vector_store (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    content text,
    metadata json,
    embedding vector(4096)
);

* After adding database table you can compile and run the project jar
* There are two end points i have created for this project
  1. POST http://localhost:8080/ai/file with 'document' as mandatory request parameter and 'prompt' as non mandatory as default prompt will be to summarize the document
     - This API vectorize the document and store it in database table for vector search so if you call this API it will take time to vectorize and store the document, second         API should be used to ask quetions related to document uploaded.
  3. GET http://localhost:8080/ai/ask with 'prompt' as mandatory request parameter
     - This API takes prompt and considering the information to be collected from database vector store it will return the answer.
