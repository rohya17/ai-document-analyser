package com.document.reader.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SpringAssistantService {
	
	private static final Logger logger = LoggerFactory.getLogger(SpringAssistantService.class);
	private static final String storagePath = "D:/dump/";
	
	@Autowired private JdbcClient jdbcClient;
	@Autowired private VectorStore vectorStore;
	@Autowired private ChatClient chatClient;
	
	@Value("classpath:templates/Template.st")
    private Resource sbPromptTemplate;
	
	public String ollamaPrompt( String prompt ){
		
		logger.info("Creating prompt template for ollama");
		
        PromptTemplate promptTemplate = new PromptTemplate(sbPromptTemplate);
        promptTemplate.add("input", prompt);
        promptTemplate.add("documents", String.join("\n", findSimilarDocuments(prompt)));
        
        Prompt promptObj = promptTemplate.create();
        
        logger.info("Sending created prompt to ollama");
        
        return chatClient.prompt(promptObj)
		      .call()
		      .content();
	}
	
	private void storeToVectorStore(String originalFilename) {
    	
		// vector store can store many documents but we will analyze one document at time
		// hence truncating vector store to remove previous document data
    	jdbcClient.sql("truncate vector_store").update();
    	logger.info("Truncated vector store");
    	
    	logger.info("Adding new document to vector store");
    	TikaDocumentReader documentReader = 
    			new TikaDocumentReader(
    					new FileSystemResource(
    							new File( storagePath + originalFilename )));
    	var textSplitter = new TokenTextSplitter();
        vectorStore.add(textSplitter.apply(documentReader.get()));
        logger.info("New document added to vector store");
	}

	private void saveFile(String path, MultipartFile resume) throws IOException {
		
		logger.info("Saving file : "+resume.getOriginalFilename());
    	File newFile = new File("D:/dump/", resume.getOriginalFilename() );
		FileOutputStream outputStream = new FileOutputStream(newFile);
		FileCopyUtils.copy(resume.getInputStream(), outputStream);
		outputStream.close();
		logger.info("Saved file : "+resume.getOriginalFilename());
		
	}

	private List<String> findSimilarDocuments(String message) {
		
		logger.info("Finding similar context for : "+message);
        List<Document> similarDocuments = vectorStore.similaritySearch(SearchRequest.query(message).withTopK(3));
        return similarDocuments.stream().map(Document::getContent).toList();
    }

	public String promptWithDocument(String prompt, MultipartFile resume) throws IOException {
		
		saveFile(storagePath, resume);
		storeToVectorStore( resume.getOriginalFilename() );
		return ollamaPrompt(prompt);
	}
	
}
