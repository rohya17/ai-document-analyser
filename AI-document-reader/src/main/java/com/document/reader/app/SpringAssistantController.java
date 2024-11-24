package com.document.reader.app;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/ai")
public class SpringAssistantController {

	private static final Logger logger = LoggerFactory.getLogger(SpringAssistantController.class);
    
    @Autowired SpringAssistantService assistantService;

    @GetMapping("/ask")
    public String question(@RequestParam(defaultValue = "Summarise the document") String prompt) {
    	logger.info("Prompt : "+prompt);
        return assistantService.ollamaPrompt(prompt);
    }
    
    @PostMapping("/file")
    public String questionWithDocument(@RequestParam(defaultValue = "Summarise the document") String prompt, MultipartFile document) throws IOException {
    	return assistantService.promptWithDocument( prompt, document );
    }
    

}