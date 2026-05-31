package com.shubham.RAGP01.rag;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class amazonLoader {

    private final VectorStore vectorStore;

    public amazonLoader(VectorStore vectorStore){

        this.vectorStore = vectorStore;
    }

    @Value("classpath:amazon-rag.pdf")
    Resource policyFile;

    @PostConstruct
    public void loadData(){

        TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(policyFile);

        List<Document> documents = tikaDocumentReader.get();
        List<Document> chunks = TokenTextSplitter.builder()
                .withChunkSize(300)
                .withMinChunkSizeChars(80)
                .withMaxNumChunks(20)
                .build()
                .split(documents);
        vectorStore.add(chunks);

    }
}
