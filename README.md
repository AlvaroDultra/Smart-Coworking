# 🏢 Sistema de Gestão de Coworking Inteligente

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue.svg)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

> API REST completa para gerenciamento inteligente de espaços de coworking, com recursos de reservas automatizadas, controle financeiro e detecção de conflitos em tempo real.

## 📋 Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Funcionalidades](#-funcionalidades)
- [Tecnologias](#-tecnologias)
- [Arquitetura](#-arquitetura)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação](#-instalação)
- [Configuração](#-configuração)
- [Uso](#-uso)
- [Endpoints da API](#-endpoints-da-api)
- [Exemplos de Requisições](#-exemplos-de-requisições)
- [Testes](#-testes)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Roadmap](#-roadmap)
- [Contribuindo](#-contribuindo)
- [Licença](#-licença)
- [Contato](#-contato)

## 🎯 Sobre o Projeto

O **Sistema de Gestão de Coworking Inteligente** é uma API REST desenvolvida para facilitar a administração de espaços de trabalho compartilhados. O sistema oferece funcionalidades completas de:

- 📅 **Gestão de Reservas** - Agendamento inteligente com detecção automática de conflitos
- 💰 **Controle Financeiro** - Geração automática de cobranças e cálculo dinâmico de preços
- 🏢 **Gestão de Espaços** - Controle completo de salas, mesas e recursos
- 👥 **Gerenciamento de Usuários** - Sistema de autenticação e perfis de acesso

### 🌟 Diferenciais

- ✅ **Automação Inteligente** - Cobranças geradas automaticamente ao criar reservas
- ✅ **Validações Robustas** - Sistema completo de validação de dados e regras de negócio
- ✅ **Exception Handling** - Tratamento profissional de erros com mensagens claras
- ✅ **Arquitetura Escalável** - Padrão MVC em camadas com separação de responsabilidades
- ✅ **API RESTful** - 45+ endpoints seguindo boas práticas REST

## ✨ Funcionalidades

### 👤 Gestão de Usuários
- [x] Cadastro e autenticação de usuários
- [x] Perfis diferenciados (Admin, Membro, Visitante)
- [x] Sistema de créditos
- [x] Histórico completo de atividades

### 🏢 Gestão de Espaços
- [x] Cadastro de salas, mesas fixas e hot desks
- [x] Controle de capacidade e recursos (WiFi, projetor, ar-condicionado)
- [x] Precificação flexível (hora, dia, mês)
- [x] Ativação/desativação de espaços

### 📅 Sistema de Reservas
- [x] Agendamento com data/hora início e fim
- [x] **Detecção automática de conflitos de horário**
- [x] Cálculo automático de valores baseado em duração
- [x] Check-in e check-out digital
- [x] Status de reserva (Pendente, Confirmada, Em Uso, Concluída, Cancelada)

### 💳 Controle Financeiro
- [x] **Geração automática de cobranças**
- [x] Múltiplos status de pagamento
- [x] Relatórios financeiros por usuário
- [x] Sistema de reembolso
- [x] Detecção de inadimplência

## 🛠 Tecnologias

### Backend
- **Java 17** - Linguagem de programação
- **Spring Boot 3.5.7** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **Spring Security** - Segurança e autenticação
- **Spring Validation** - Validação de dados
- **Hibernate** - ORM

### Banco de Dados
- **PostgreSQL 16** - Banco de dados relacional
- **HikariCP** - Pool de conexões

### Ferramentas de Desenvolvimento
- **Maven** - Gerenciamento de dependências
- **Lombok** - Redução de boilerplate
- **Spring DevTools** - Hot reload

### Testes
- **cURL + jq** - Testes automatizados via linha de comando
- **26 cenários de teste** cobrindo todos os fluxos principais

## 🏗 Arquitetura

O projeto segue uma arquitetura em camadas (MVC) com separação clara de responsabilidades:
```
┌─────────────────────────────────────────┐
│           Controllers (REST)            │
│  - Recebe requisições HTTP              │
│  - Valida entrada                       │
│  - Retorna DTOs                         │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│              Services                   │
│  - Lógica de negócio                   │
│  - Validações complexas                │
│  - Orquestração de operações           │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│           Repositories                  │
│  - Acesso a dados                      │
│  - Queries customizadas                │
│  - Spring Data JPA                     │
└──────────────┬──────────────────────────┘
               │
┌──────────────▼──────────────────────────┐
│            Database                     │
│  - PostgreSQL                          │
│  - Relacionamentos                     │
│  - Constraints                         │
└─────────────────────────────────────────┘
```

### 📦 Estrutura de Pacotes
```
com.coworking.smartcoworking
├── config/          # Configurações (Security, etc)
├── controller/      # Controllers REST
├── dto/            # Data Transfer Objects
├── entity/         # Entidades JPA
├── enums/          # Enumerações
├── exception/      # Tratamento de exceções
├── repository/     # Repositórios
├── service/        # Lógica de negócio
└── util/           # Utilitários
```

## 📋 Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- **Java 17** ou superior
- **Maven 3.8+**
- **PostgreSQL 16**
- **Git**
- **cURL** (para testes)
- **jq** (para testes - opcional)

## 🚀 Instalação

### 1. Clone o repositório
```bash
git clone https://github.com/seu-usuario/smart-coworking.git
cd smart-coworking
```

### 2. Configure o banco de dados

Crie um banco de dados PostgreSQL:
```sql
CREATE DATABASE coworking_db;
CREATE USER coworking_user WITH PASSWORD 'coworking123';
GRANT ALL PRIVILEGES ON DATABASE coworking_db TO coworking_user;
```

### 3. Configure as variáveis de ambiente (opcional)
```bash
export DB_URL=jdbc:postgresql://localhost:5432/coworking_db
export DB_USERNAME=coworking_user
export DB_PASSWORD=coworking123
```

### 4. Compile o projeto
```bash
mvn clean install
```

### 5. Execute a aplicação
```bash
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

## ⚙️ Configuração

### application.properties

As principais configurações estão em `src/main/resources/application.properties`:
```properties
# Servidor
server.port=8080

# Banco de Dados
spring.datasource.url=jdbc:postgresql://localhost:5432/coworking_db
spring.datasource.username=coworking_user
spring.datasource.password=coworking123

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

## 💻 Uso

### Executar com Maven
```bash
mvn spring-boot:run
```

### Executar com Java
```bash
java -jar target/smart-coworking-0.0.1-SNAPSHOT.jar
```

### Executar testes automatizados
```bash
chmod +x test-api.sh
./test-api.sh
```

## 📡 Endpoints da API

### 👤 Usuários (`/api/users`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/users` | Criar novo usuário |
| GET | `/api/users` | Listar todos os usuários |
| GET | `/api/users/{id}` | Buscar usuário por ID |
| GET | `/api/users/email/{email}` | Buscar usuário por email |
| GET | `/api/users/active` | Listar usuários ativos |
| PUT | `/api/users/{id}` | Atualizar usuário |
| DELETE | `/api/users/{id}` | Deletar usuário |

### 🏢 Espaços (`/api/spaces`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/spaces` | Criar novo espaço |
| GET | `/api/spaces` | Listar todos os espaços |
| GET | `/api/spaces/{id}` | Buscar espaço por ID |
| GET | `/api/spaces/type/{type}` | Filtrar por tipo |
| GET | `/api/spaces/active` | Listar espaços ativos |
| GET | `/api/spaces/floor/{floor}` | Filtrar por andar |
| GET | `/api/spaces/capacity/{min}` | Filtrar por capacidade mínima |
| PUT | `/api/spaces/{id}` | Atualizar espaço |
| PATCH | `/api/spaces/{id}/activate` | Ativar espaço |
| PATCH | `/api/spaces/{id}/deactivate` | Desativar espaço |
| DELETE | `/api/spaces/{id}` | Deletar espaço |

### 📅 Reservas (`/api/reservations`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/reservations` | Criar nova reserva |
| GET | `/api/reservations` | Listar todas as reservas |
| GET | `/api/reservations/{id}` | Buscar reserva por ID |
| GET | `/api/reservations/user/{userId}` | Listar reservas do usuário |
| GET | `/api/reservations/user/{userId}/upcoming` | Próximas reservas do usuário |
| GET | `/api/reservations/space/{spaceId}` | Reservas do espaço |
| PUT | `/api/reservations/{id}` | Atualizar reserva |
| PATCH | `/api/reservations/{id}/check-in` | Fazer check-in |
| PATCH | `/api/reservations/{id}/check-out` | Fazer check-out |
| PATCH | `/api/reservations/{id}/cancel` | Cancelar reserva |
| DELETE | `/api/reservations/{id}` | Deletar reserva |

### 💰 Cobranças (`/api/billings`)

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/billings` | Criar nova cobrança |
| GET | `/api/billings` | Listar todas as cobranças |
| GET | `/api/billings/{id}` | Buscar cobrança por ID |
| GET | `/api/billings/user/{userId}` | Cobranças do usuário |
| GET | `/api/billings/user/{userId}/pending` | Cobranças pendentes |
| GET | `/api/billings/user/{userId}/debt` | Total de dívida |
| GET | `/api/billings/user/{userId}/paid` | Total pago |
| GET | `/api/billings/user/{userId}/summary` | Resumo financeiro completo |
| GET | `/api/billings/overdue` | Cobranças atrasadas |
| GET | `/api/billings/due-soon?days=X` | Cobranças a vencer em X dias |
| PUT | `/api/billings/{id}` | Atualizar cobrança |
| PATCH | `/api/billings/{id}/pay?paymentMethod=X` | Marcar como paga |
| PATCH | `/api/billings/{id}/cancel` | Cancelar cobrança |
| PATCH | `/api/billings/{id}/refund` | Reembolsar |
| DELETE | `/api/billings/{id}` | Deletar cobrança |

## 📝 Exemplos de Requisições

### Criar Usuário
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao@example.com",
    "password": "senha123",
    "phone": "+5511999999999",
    "role": "MEMBER"
  }'
```

**Resposta (201 Created):**
```json
{
  "id": 1,
  "name": "João Silva",
  "email": "joao@example.com",
  "phone": "+5511999999999",
  "role": "MEMBER",
  "credits": 0.00,
  "active": true,
  "createdAt": "2025-11-17T10:00:00",
  "updatedAt": "2025-11-17T10:00:00"
}
```

### Criar Espaço
```bash
curl -X POST http://localhost:8080/api/spaces \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Sala Zeus",
    "description": "Sala de reunião premium",
    "type": "SALA_REUNIAO",
    "capacity": 10,
    "pricePerHour": 50.00,
    "floor": 3,
    "hasWifi": true,
    "hasProjector": true
  }'
```

### Criar Reserva (com geração automática de cobrança)
```bash
curl -X POST http://localhost:8080/api/reservations \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "spaceId": 1,
    "startDateTime": "2025-11-25T14:00:00",
    "endDateTime": "2025-11-25T16:00:00",
    "notes": "Reunião importante"
  }'
```

**Resposta (201 Created):**
```json
{
  "id": 1,
  "user": {
    "id": 1,
    "name": "João Silva",
    "email": "joao@example.com"
  },
  "space": {
    "id": 1,
    "name": "Sala Zeus",
    "type": "SALA_REUNIAO",
    "pricePerHour": 50.00
  },
  "startDateTime": "2025-11-25T14:00:00",
  "endDateTime": "2025-11-25T16:00:00",
  "totalPrice": 100.00,
  "status": "PENDENTE",
  "notes": "Reunião importante"
}
```

⚠️ **Nota:** Uma cobrança de R$ 100,00 é criada automaticamente!

### Resumo Financeiro do Usuário
```bash
curl http://localhost:8080/api/billings/user/1/summary | jq '.'
```

**Resposta:**
```json
{
  "totalDebt": 100.00,
  "totalPaid": 0.00,
  "hasOverdueBillings": false,
  "pendingCount": 1,
  "status": "REGULAR"
}
```

## 🧪 Testes

O projeto inclui um script completo de testes automatizados que cobre 26 cenários:
```bash
./test-api.sh
```

### Cobertura de Testes

- ✅ CRUD completo de usuários (6 testes)
- ✅ CRUD completo de espaços (7 testes)
- ✅ Sistema de reservas (5 testes)
- ✅ Sistema de cobranças (4 testes)
- ✅ Validações e erros (4 testes)

### Exemplo de Saída
```
╔════════════════════════════════════════╗
║  TESTANDO API - COWORKING INTELIGENTE  ║
╚════════════════════════════════════════╝

[1] CRIANDO USUÁRIO...
✓ Usuário criado com ID: 1

[2] CRIANDO ESPAÇO...
✓ Sala Zeus criada com ID: 1

[14] CRIANDO RESERVA...
✓ Reserva criada com ID: 1

[19] LISTANDO COBRANÇAS DO USUÁRIO...
✓ Cobranças do usuário (criada automaticamente!)

╔════════════════════════════════════════╗
║           TESTES CONCLUÍDOS!           ║
╚════════════════════════════════════════╝

✓ 26/26 testes passaram com sucesso!
```

## 📂 Estrutura do Projeto
```
smart-coworking/
├── src/
│   ├── main/
│   │   ├── java/com/coworking/smartcoworking/
│   │   │   ├── config/
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── OpenApiConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── UserController.java
│   │   │   │   ├── SpaceController.java
│   │   │   │   ├── ReservationController.java
│   │   │   │   └── BillingController.java
│   │   │   ├── dto/
│   │   │   │   ├── user/
│   │   │   │   ├── space/
│   │   │   │   ├── reservation/
│   │   │   │   └── billing/
│   │   │   ├── entity/
│   │   │   │   ├── User.java
│   │   │   │   ├── Space.java
│   │   │   │   ├── Reservation.java
│   │   │   │   ├── Billing.java
│   │   │   │   └── OccupancyLog.java
│   │   │   ├── enums/
│   │   │   │   ├── UserRole.java
│   │   │   │   ├── SpaceType.java
│   │   │   │   ├── ReservationStatus.java
│   │   │   │   └── BillingStatus.java
│   │   │   ├── exception/
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   ├── BusinessException.java
│   │   │   │   ├── ConflictException.java
│   │   │   │   ├── ErrorResponse.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── SpaceRepository.java
│   │   │   │   ├── ReservationRepository.java
│   │   │   │   ├── BillingRepository.java
│   │   │   │   └── OccupancyLogRepository.java
│   │   │   ├── service/
│   │   │   │   ├── UserService.java
│   │   │   │   ├── SpaceService.java
│   │   │   │   ├── ReservationService.java
│   │   │   │   └── BillingService.java
│   │   │   └── SmartCoworkingApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── test-api.sh
├── pom.xml
├── README.md
└── LICENSE
```

## 🗺 Roadmap

### Versão 1.0 (Atual) ✅
- [x] CRUD completo de usuários, espaços e reservas
- [x] Sistema de cobranças automáticas
- [x] Detecção de conflitos de horário
- [x] Exception handling profissional
- [x] Testes automatizados

### Versão 1.1 (Próxima)
- [ ] Autenticação JWT completa
- [ ] Upload de fotos dos espaços
- [ ] Sistema de avaliações
- [ ] Notificações por email
- [ ] Relatórios em PDF

### Versão 2.0 (Futuro)
- [ ] Dashboard administrativo
- [ ] Sistema de pontos/fidelidade
- [ ] Integração com sistemas de pagamento
- [ ] App mobile
- [ ] Analytics avançado

## 🤝 Contribuindo

Contribuições são bem-vindas! Siga estes passos:

1. Faça um Fork do projeto
2. Crie uma Branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona MinhaFeature'`)
4. Push para a Branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

### Diretrizes

- Siga os padrões de código existentes
- Adicione testes para novas funcionalidades
- Atualize a documentação conforme necessário
- Mantenha os commits limpos e descritivos

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 📞 Contato

**Álvaro Dultra** - alvarodultra.dev@gmail.com

Link do Projeto: [https://github.com/seu-usuario/smart-coworking](https://github.com/seu-usuario/smart-coworking)

---

## 🙏 Agradecimentos

- [Spring Boot](https://spring.io/projects/spring-boot)
- [PostgreSQL](https://www.postgresql.org/)
- [Lombok](https://projectlombok.org/)
- Comunidade Java/Spring

---

⭐️ **Se este projeto foi útil para você, considere dar uma estrela!** ⭐️

---

<p align="center">
  Desenvolvido com ❤️ por Álvaro Dultra
</p>
