package com.shubham.RAGP01.rag;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader {

    private final VectorStore vectorStore;

    public DataLoader(VectorStore vectorStore){

        this.vectorStore = vectorStore;
    }

    @PostConstruct
    public void loadSampleDocuments() {

        List<Document> documentList = List.of(

                new Document("Product: Smartphone. A smartphone is used for calling, messaging, internet browsing, and apps. It usually has a camera, battery, and touchscreen display."),

                new Document("Product: Laptop. A laptop is a portable computer used for coding, studying, office work, and entertainment. It includes a keyboard, screen, and battery."),

                new Document("Product: Headphones. Headphones are used to listen to music and take calls. They can be wired or wireless and provide good sound quality."),

                new Document("Lifestyle: Morning Routine. A good morning routine includes waking up early, exercising, and having a healthy breakfast. It improves productivity and focus."),

                new Document("Lifestyle: Fitness. Fitness involves regular exercise, gym workouts, and staying active. It helps in maintaining physical and mental health."),

                new Document("Lifestyle: Stress Management. Stress can be reduced by meditation, proper sleep, and relaxation activities. Managing stress improves overall well-being."),

                new Document("Food: Fruits. Fruits like apple, banana, and mango are rich in vitamins and minerals. They are important for a healthy diet."),

                new Document("Food: Fast Food. Fast food includes burgers, pizza, and fries. It is tasty but should be eaten in moderation for good health."),

                new Document("Food: Indian Food. Indian food includes dishes like roti, rice, dal, and curry. It is known for its spices and rich flavors."),

                new Document("Technology: Artificial Intelligence. Artificial Intelligence helps machines learn from data and make decisions. It is used in chatbots, apps, and automation."),

                new Document("Technology: Mobile Apps. Mobile apps are software applications used on smartphones. Examples include social media, banking, and shopping apps."),

                new Document("Technology: Internet. The internet connects people worldwide and provides access to information, websites, and online services."),

                new Document("Travel: Beach. Beaches are popular travel destinations known for sand, sea, and relaxation. People visit for holidays and fun."),

                new Document("Travel: Mountains. Mountains offer fresh air, scenic views, and adventure activities like trekking and camping."),

                new Document("Travel: City Tour. City tours include visiting malls, parks, museums, and local attractions. They are great for exploring urban life."),

                new Document("Business: Startup. A startup is a new business that focuses on innovation and solving problems. It aims for growth and scalability."),

                new Document("Business: Marketing. Marketing helps businesses promote products and attract customers through ads, social media, and branding."),

                new Document("Business: E-commerce. E-commerce is buying and selling products online using platforms like websites and mobile apps.")
        );

        vectorStore.add(documentList);
        System.out.println("Loaded " + documentList.size() + " document into vector store");
    }


}
