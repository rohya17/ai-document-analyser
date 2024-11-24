package com.document.reader.app;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AiDocumentReaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiDocumentReaderApplication.class, args);
	}
	
	@Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        return builder.build();
    }

}
