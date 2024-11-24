# ai-document-analyser
In this project i have focused on analysing any document using ollama AI and then asking ai the quetions about the document

to run this application you need following setup
* First you need to download ollama ai which is open source llm ai model, you can download it from https://ollama.com/download/windows, i have done it on my windows os you can do it on any os you are comfortable with.
* I am using model llama3.2 for this project, you can pull the same from command line after istallation using command **ollama run llama3.2**
* Remember to run llama3.2 model in command prompt before running the project
* For this project i have used postgres database and along with that we need to use **pgvector** extension, you have to install this extension externally you can do it using this tutorial https://www.mindfiretechnology.com/blog/archive/installing-pgvector-in-preparation-for-retrieval-augmented-generation/
* CREATE EXTENSION IF NOT EXISTS vector;
CREATE EXTENSION IF NOT EXISTS hstore;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS vector_store (
    id uuid DEFAULT uuid_generate_v4() PRIMARY KEY,
    content text,
    metadata json,
    embedding vector(1536)  -- 1536 is the default embedding dimension
);
