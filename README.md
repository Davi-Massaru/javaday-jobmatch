# 📌 RH Analyst – Compatibilidade de Candidatos e Vagas

Este projeto é uma aplicação de **análise inteligente de compatibilidade entre candidatos e vagas**, construída com **Quarkus** e **LangChain4j**.  

A aplicação utiliza **IA Generativa + bancos de dados (MongoDB e Qdrant)** para:  
- Buscar informações de uma vaga de emprego.  
- Buscar informações detalhadas de um candidato.  
- Avaliar a **compatibilidade entre ambos**.  

---

## ⚙️ Tecnologias principais

- **Quarkus** – framework Java para microsserviços  
- **LangChain4j** – integração com LLMs  
- **MongoDB** – armazenamento de candidatos e vagas  
- **Qdrant** – banco vetorial para embeddings e busca semântica  
- **Docker Compose** – infraestrutura local  

---

## 🚀 Como executar

### 1. Subir infraestrutura com Docker
```bash
docker-compose up -d
```

## 🌐 URLs locais

| Serviço        | URL Local                                  |
|----------------|--------------------------------------------|
| **API Quarkus (Swagger/OpenAPI)** | [http://localhost:8080/q/swagger-ui](http://localhost:8080/q/swagger-ui) |
| **Mongo Express** | [http://localhost:8081](http://localhost:8081) |
| **Ollama API** | [http://localhost:11434](http://localhost:11434) |
| **Open WebUI (Frontend para Ollama)** | [http://localhost:3000](http://localhost:3000) |
| **Qdrant (Dashboard HTTP)** | [http://localhost:6333/dashboard](http://localhost:6333/dashboard) |

---

## 📡 Rotas principais da API

### 🔹 Avaliar compatibilidade
**POST** `/api/classificacao/{vagaId}/{candidatoId}`  
Avalia a compatibilidade entre uma vaga e um candidato.  

# IMPORTANTE

A branch 'main' está pronta para operar com Ollama (modelos locais) e Quarkus + Langchain4j

A branch 'quarkus-langchain4j-openai' está pronta para operar com OpenAi para integração com ChatGPT da OpenAI.
