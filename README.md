## POC: Retrieval-Augmented Generation (RAG) with Spring Boot

This proof of concept demonstrates the integration of RAG using various technologies including Spring Boot 3, Apache Lucene, Langchain4j and OpenAI.

### Technologies Used

- **Spring Boot 3**: A framework for building Java-based enterprise applications.
- **Apache Lucene**: A high-performance, full-featured text search engine library written in Java.
- **Langchain4j**: A Java library for building applications with large language models.
- **OpenAI**: An AI research lab and company known for its advanced language models.

### Prerequisites

- Java 21 or higher
- Maven 3.6 or higher
- Git
- OpenAI API Key

### Getting Started

#### Clone the Repository

First, clone the repository to your local machine:

```sh
git clone https://github.com/luishpcosta/rag-example.git
cd rag-example
```

Windows (Command Prompt):
```sh
set OPENAI_KEY=your_api_key
```

Linux / macOS:

```sh
export OPENAI_KEY=your_api_key
```

Use Maven to install project dependencies:

```sh
mvn clean install
```

Run the Project:

```sh
mvn spring-boot:run
```