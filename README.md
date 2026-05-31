# RAG AI — Chat with Your Documents Locally

I built this project to learn how RAG (Retrieval-Augmented Generation) actually works under the hood — not just the theory, but getting it running end to end with real documents.

The idea is simple: instead of asking an AI a question and hoping it "knows" the answer from training, you give it YOUR document and it answers strictly from that. No hallucination, no made-up facts — just your data.

For this project I used an Amazon company PDF as the knowledge base. You can swap it with any PDF you want.

---

## What it does

You hit an API endpoint with a question like:

```
"What is the base salary of SDE II at Amazon?"
```

Behind the scenes it:
1. Converts your question into a vector (embedding)
2. Searches the vector database for the most similar chunks from the PDF
3. Passes those chunks + your question to a local LLM
4. Returns an answer grounded only in the document

Everything runs locally — Ollama for the LLM, Qdrant for the vector store. No API keys needed.

---

## Stack

- **Spring Boot 4.0.6** — backend
- **Spring AI 2.0.0-M6** — AI abstractions (chat client, vector store, document reader)
- **Ollama** — runs LLM locally (using llama3 for chat, mxbai-embed-large for embeddings)
- **Qdrant** — vector database (runs in Docker)
- **Apache Tika** — reads and parses the PDF
- **Java 21**

---

## Project Structure

```
src/main/java/com/shubham/RAGP01/
│
├── config/
│   └── ChatClientConfig.java        → sets up the ChatClient bean
│
├── controller/
│   └── RagController.java           → two endpoints: /chat and /chat/pdf
│
└── rag/
    ├── amazonLoader.java            → reads the PDF, chunks it, stores in Qdrant on startup
    └── DataLoader.java              → disabled, was used for testing with hardcoded docs

src/main/resources/
├── promptTemplates/
│   ├── ragTemplate.st               → prompt for general /chat
│   └── systemTemplate.st            → prompt for PDF chat (strict document-only answering)
├── amazon-rag.pdf                   → the knowledge base
└── application.properties
```

---

## Running it locally

**You'll need:**
- Java 21
- Docker
- [Ollama](https://ollama.com) installed

**Step 1 — Pull the models**

```bash
ollama pull llama3
ollama pull mxbai-embed-large
```

**Step 2 — Start Qdrant**

```bash
docker run -p 6333:6333 -p 6334:6334 qdrant/qdrant
```

**Step 3 — Run the app**

```bash
mvn spring-boot:run
```

When the app starts, `amazonLoader` runs automatically — it reads the PDF, splits it into chunks, and loads everything into Qdrant. After that you're ready to query.

---

## API

**General chat** (uses sample hardcoded docs — products, food, travel etc.)
```
GET /api/rag/chat?question=what is artificial intelligence
```

**PDF chat** (uses the Amazon PDF)
```
GET /api/rag/chat/pdf?question=what is the base salary of SDE II at Amazon
```

Some questions worth trying on the Amazon PDF:
- What is Amazon's total revenue in 2023?
- What technology stack does Amazon use?
- Who is the CEO of Amazon?
- What are Amazon's leadership principles?
- How does AWS make money?

---

## Config (application.properties)

```properties
spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.chat.model=llama3
spring.ai.ollama.embedding.model=mxbai-embed-large

spring.ai.vectorstore.qdrant.host=localhost
spring.ai.vectorstore.qdrant.port=6334
spring.ai.vectorstore.qdrant.collection-name=rag_ai_p01
spring.ai.vectorstore.qdrant.initialize-schema=true
```

---

## If something's not working

**Getting "Sorry, I could not find relevant information"?**

Open Qdrant dashboard at `http://localhost:6333/dashboard` and check if the collection has vectors. If it's empty, the PDF didn't load — check that `amazon-rag.pdf` is in `src/main/resources/` and `amazonLoader` has the `@Component` annotation.

You can also try lowering the similarity threshold in the controller from `0.5` to `0.3` — sometimes chunks just don't score high enough with the default.

**Ollama not responding?**

```bash
ollama serve
```

---

## What I'd improve next

- Upload any PDF via API instead of hardcoding it in resources
- Multi-turn conversation with chat history
- A basic frontend so it feels less like a raw API
- Streaming responses so you see the answer as it generates

---

## Built by Shubham

Started this to actually understand RAG instead of just reading about it. Spring AI made the integration pretty smooth once I got past the milestone version quirks.
