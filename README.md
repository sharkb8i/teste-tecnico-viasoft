# Email Integration Service

Este projeto implementa um serviço REST para integração de envio de emails com diferentes provedores (AWS e OCI), desenvolvido como parte de um desafio técnico para vaga de Dev Sênior.

## 🚀 Funcionalidades

- **API REST** para recebimento de dados de email
- **Adaptação dinâmica** para diferentes provedores (AWS/OCI) baseada em configuração
- **Validação** de dados de entrada
- **Serialização JSON** com impressão no console
- **Tratamento de erros** padronizado
- **Arquitetura limpa** com aplicação de Design Patterns
- **Testes unitários** para validar funções

## 🏗️ Arquitetura e Design Patterns Implementados

### Padrões Utilizados:

1. **Adapter Pattern:** Para adaptação entre diferentes formatos de DTOs
2. **Factory Pattern:** Para criação dinâmica de adapters
3. **Strategy Pattern:** Diferentes estratégias de integração (AWS/OCI)
4. **Dependency Injection:** Através do Spring Framework
5. **DTO Pattern:** Para transferência de dados
6. **Exception Handling Pattern:** Tratamento centralizado de exceções

### Arquitetura em Camadas:

- **Business:** Camada de lógica de negócio
- **Infrastructure:** Camada de infraestrutura (configurações e utilitários)
- **Integration:** Camada de integração com sistemas externos
- **Presentation:** Camada de apresentação/controle
- **Shared**: Camada de compatilhamento de componentes

## 📋 Requisitos Atendidos

- ✅ **Requisito 1:** Aplicação REST com endpoint
- ✅ **Requisito 2:** Classe DTO com validações
- ✅ **Requisito 3:** Controller recebe instâncida da classe DTO
- ✅ **Requisito 4:** Configuração `mail.integracao` no application.properties
- ✅ **Requisito 5:** Classe EmailAwsDTO com limitações de caracteres
- ✅ **Requisito 6:** Classe EmailOciDTO com limitações de caracteres
- ✅ **Requisito 7:** Adaptação baseada na configuração
- ✅ **Requisito 8:** Serialização JSON e impressão no console
- ✅ **Requisito 9:** Retorno HTTP 204 em caso de sucesso
- ✅ **Requisito 10:** Tratamento de erros HTTP 400/500

## 🛠️ Tecnologias

- **Java 17**
- **Spring Boot** (v3.5.4)
- **Spring Web**
- **Spring Validation**
- **Jackson** (JSON)
- **Maven** (v3.9.0)

## 📁 Estrutura do Projeto

```bash
src/main/java/com/emailservice/
├── EmailIntegrationServiceApplication.java
│
├── presentation/                    # CAMADA DE APRESENTAÇÃO
│   ├── controller/
│   │   └── EmailController.java
│   └── dto/
│       └── EmailRequestDTO.java
│
├── business/                        # CAMADA DE NEGÓCIO
│   ├── service/
│   │   └── EmailProcessingService.java
│   ├── domain/
│   │   ├── dto/
│   │   │   └── EmailRequestDTO.java
│   │   ├── model/
│   │   │   └── Email.java
│   │   └── service/
│   │       └── EmailDomainService.java
│   └── exception/
│       └── EmailBusinessException.java
│
├── integration/                     # CAMADA DE INTEGRAÇÃO
│   ├── service/
│   │   └── EmailIntegrationService.java
│   ├── adapter/
│   │   ├── IEmailProviderAdapter.java
│   │   └── impl/
│   │       ├── AwsEmailAdapter.java
│   │       └── OciEmailAdapter.java
│   ├── dto/
│   │   ├── EmailAwsDTO.java
│   │   └── EmailOciDTO.java
│   ├── factory/
│   │   └── EmailAdapterFactory.java
│   └── exception/
│       └── EmailIntegrationException.java
│
├── infrastructure/                  # CAMADA DE INFRAESTRUTURA
│   ├── config/
│   │   └── EmailConfiguration.java
│   ├── exception/
│   │   └── GlobalExceptionHandler.java
│   └── util/
│       └── JsonSerializer.java
│
└── shared/                         # COMPONENTES COMPARTILHADOS
    ├── constants/
    │   └── EmailConstants.java
    └── enums/
        └── IntegrationType.java
```

## ⚙️ Configuração

### application.properties

```properties
# Configuração da integração (AWS ou OCI)
mail.integracao=AWS

# Configurações da aplicação
server.port=8080
spring.application.name=email-service
```

## 🚦 Como Executar

1. **Pré-requisitos:** Java 17 e Maven instalados.
2. **Clone o projeto** ou extraia os arquivos.
3. **Execute a aplicação:**

    ```bash
    mvn spring-boot:run

    # ou

    ./mvnw spring-boot:run
    ```

4. **Acesse:** [http://localhost:8080](http://localhost:80800).

## 📝 Como Usar

### Endpoint Principal

```bash
POST /api/email/enviar
Content-Type: application/json
```

### Estrutura do JSON

```json
{
	"recipientEmail":"dest@example.com",
	"recipientName":"Destinatário Teste",
	"senderEmail":"remetente@example.com",
	"subject":"Assunto de Teste",
	"content":"Corpo do email"
}
```

### Exemplo de Uso

```bash
curl -X POST http://localhost:8080/api/email/enviar \
  -H "Content-Type: application/json" \
  -d '{
    "recipientEmail": "teste@exemplo.com",
    "recipientName": "Teste Usuario",
    "senderEmail": "remetente@exemplo.com",
    "subject": "Email de Teste",
    "content": "Este é um email de teste"
  }'
```

## 🔄 Configuração de Provedores

## Para usar AWS:

```properties
mail.integracao=AWS
```

## Para usar OCI:

```properties
mail.integracao=OCI
```

## 🎯 Validações Implementadas

- **Email válido** para destinatário e remetente
- **Campos obrigatórios** não podem estar vazios
- **Limitações de caracteres** específicas para cada provedor:
  - **AWS:** recipient (45), recipientName (60), sender (45), subject (120), content (256)
  - **OCI:** recipientEmail (40), recipientName (50), senderEmail (40), subject (100), body (250)

## 🚨 Tratamento de Erros

- **400 Bad Request:** Dados inválidos ou configuração incorreta
- **500 Internal Server Error:** Erros internos da aplicação
- **Logs detalhados** para troubleshooting

## 🧪 Testes

Para executar apenas os testes é necessário rodar:

```bash
mvn test

# ou

./mvnw test
```