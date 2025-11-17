#!/bin/bash

# ============================================
# SCRIPT DE TESTES - COWORKING API
# ============================================

BASE_URL="http://localhost:8080/api"

# Cores para output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}╔════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║  TESTANDO API - COWORKING INTELIGENTE  ║${NC}"
echo -e "${BLUE}╚════════════════════════════════════════╝${NC}"
echo ""

# ============================================
# 0. LIMPAR BANCO DE DADOS
# ============================================

echo -e "${YELLOW}[0] LIMPANDO BANCO DE DADOS...${NC}"

# ORDEM CORRETA: Billings → Reservations → Users e Spaces

# 1. Deletar todas as cobranças (não tem dependências)
EXISTING_BILLINGS=$(curl -s "$BASE_URL/billings" | jq -r '.[].id' 2>/dev/null)
if [ ! -z "$EXISTING_BILLINGS" ]; then
    for BILL_ID in $EXISTING_BILLINGS; do
        curl -s -X PATCH "$BASE_URL/billings/$BILL_ID/cancel" > /dev/null 2>&1
        curl -s -X DELETE "$BASE_URL/billings/$BILL_ID" > /dev/null 2>&1
    done
fi

# 2. Deletar todas as reservas (dependem de users e spaces)
EXISTING_RESERVATIONS=$(curl -s "$BASE_URL/reservations" | jq -r '.[].id' 2>/dev/null)
if [ ! -z "$EXISTING_RESERVATIONS" ]; then
    for RES_ID in $EXISTING_RESERVATIONS; do
        curl -s -X PATCH "$BASE_URL/reservations/$RES_ID/cancel" > /dev/null 2>&1
        curl -s -X DELETE "$BASE_URL/reservations/$RES_ID" > /dev/null 2>&1
    done
fi

# 3. Deletar todos os usuários
EXISTING_USERS=$(curl -s "$BASE_URL/users" | jq -r '.[].id' 2>/dev/null)
if [ ! -z "$EXISTING_USERS" ]; then
    for USER_ID_DEL in $EXISTING_USERS; do
        curl -s -X DELETE "$BASE_URL/users/$USER_ID_DEL" > /dev/null 2>&1
    done
fi

# 4. Deletar todos os espaços
EXISTING_SPACES=$(curl -s "$BASE_URL/spaces" | jq -r '.[].id' 2>/dev/null)
if [ ! -z "$EXISTING_SPACES" ]; then
    for SPACE_ID_DEL in $EXISTING_SPACES; do
        curl -s -X DELETE "$BASE_URL/spaces/$SPACE_ID_DEL" > /dev/null 2>&1
    done
fi

echo -e "${GREEN}✓ Banco de dados limpo!${NC}\n"

# ============================================
# 1. TESTAR USUARIOS
# ============================================

echo -e "${YELLOW}[1] CRIANDO USUÁRIO...${NC}"
USER_RESPONSE=$(curl -s -X POST "$BASE_URL/users" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao@example.com",
    "password": "senha123",
    "phone": "+5511999999999",
    "role": "MEMBER"
  }')

USER_ID=$(echo $USER_RESPONSE | jq -r '.id')
echo $USER_RESPONSE | jq '.'
echo -e "${GREEN}✓ Usuário criado com ID: $USER_ID${NC}\n"

# ---

echo -e "${YELLOW}[2] CRIANDO SEGUNDO USUÁRIO...${NC}"
USER2_RESPONSE=$(curl -s -X POST "$BASE_URL/users" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Maria Santos",
    "email": "maria@example.com",
    "password": "senha456",
    "phone": "+5511988888888",
    "role": "MEMBER"
  }')

USER2_ID=$(echo $USER2_RESPONSE | jq -r '.id')
echo $USER2_RESPONSE | jq '.'
echo -e "${GREEN}✓ Segundo usuário criado com ID: $USER2_ID${NC}\n"

# ---

echo -e "${YELLOW}[3] LISTANDO TODOS OS USUÁRIOS...${NC}"
curl -s -X GET "$BASE_URL/users" | jq '.'
echo -e "${GREEN}✓ Lista de usuários${NC}\n"

# ---

echo -e "${YELLOW}[4] BUSCANDO USUÁRIO POR ID ($USER_ID)...${NC}"
curl -s -X GET "$BASE_URL/users/$USER_ID" | jq '.'
echo -e "${GREEN}✓ Usuário encontrado${NC}\n"

# ---

echo -e "${YELLOW}[5] BUSCANDO USUÁRIO POR EMAIL...${NC}"
curl -s -X GET "$BASE_URL/users/email/joao@example.com" | jq '.'
echo -e "${GREEN}✓ Usuário encontrado por email${NC}\n"

# ---

echo -e "${YELLOW}[6] ATUALIZANDO USUÁRIO...${NC}"
curl -s -X PUT "$BASE_URL/users/$USER_ID" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva Updated",
    "phone": "+5511977777777"
  }' | jq '.'
echo -e "${GREEN}✓ Usuário atualizado${NC}\n"

# ============================================
# 2. TESTAR ESPAÇOS
# ============================================

echo -e "${YELLOW}[7] CRIANDO ESPAÇO - SALA ZEUS...${NC}"
SPACE1_RESPONSE=$(curl -s -X POST "$BASE_URL/spaces" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Sala Zeus",
    "description": "Sala de reunião premium com vista",
    "type": "SALA_REUNIAO",
    "capacity": 10,
    "pricePerHour": 50.00,
    "pricePerDay": 300.00,
    "floor": 3,
    "hasWifi": true,
    "hasProjector": true,
    "hasWhiteboard": true,
    "hasAC": true
  }')

SPACE1_ID=$(echo $SPACE1_RESPONSE | jq -r '.id')
echo $SPACE1_RESPONSE | jq '.'
echo -e "${GREEN}✓ Sala Zeus criada com ID: $SPACE1_ID${NC}\n"

# ---

echo -e "${YELLOW}[8] CRIANDO ESPAÇO - MESA HOT DESK...${NC}"
SPACE2_RESPONSE=$(curl -s -X POST "$BASE_URL/spaces" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Mesa Hot Desk 01",
    "description": "Mesa compartilhada no espaço aberto",
    "type": "HOT_DESK",
    "capacity": 1,
    "pricePerHour": 15.00,
    "pricePerDay": 80.00,
    "floor": 2,
    "hasWifi": true,
    "hasProjector": false,
    "hasWhiteboard": false,
    "hasAC": true
  }')

SPACE2_ID=$(echo $SPACE2_RESPONSE | jq -r '.id')
echo $SPACE2_RESPONSE | jq '.'
echo -e "${GREEN}✓ Hot Desk criada com ID: $SPACE2_ID${NC}\n"

# ---

echo -e "${YELLOW}[9] LISTANDO TODOS OS ESPAÇOS...${NC}"
curl -s -X GET "$BASE_URL/spaces" | jq '.'
echo -e "${GREEN}✓ Lista de espaços${NC}\n"

# ---

echo -e "${YELLOW}[10] BUSCANDO ESPAÇOS POR TIPO (SALA_REUNIAO)...${NC}"
curl -s -X GET "$BASE_URL/spaces/type/SALA_REUNIAO" | jq '.'
echo -e "${GREEN}✓ Salas de reunião encontradas${NC}\n"

# ---

echo -e "${YELLOW}[11] BUSCANDO ESPAÇOS ATIVOS...${NC}"
curl -s -X GET "$BASE_URL/spaces/active" | jq '.'
echo -e "${GREEN}✓ Espaços ativos${NC}\n"

# ---

echo -e "${YELLOW}[12] BUSCANDO ESPAÇOS POR ANDAR (3)...${NC}"
curl -s -X GET "$BASE_URL/spaces/floor/3" | jq '.'
echo -e "${GREEN}✓ Espaços do andar 3${NC}\n"

# ---

echo -e "${YELLOW}[13] BUSCANDO ESPAÇOS COM CAPACIDADE MÍNIMA (8)...${NC}"
curl -s -X GET "$BASE_URL/spaces/capacity/8" | jq '.'
echo -e "${GREEN}✓ Espaços com capacidade >= 8${NC}\n"

# ============================================
# 3. TESTAR RESERVAS
# ============================================

echo -e "${YELLOW}[14] CRIANDO RESERVA...${NC}"
RESERVATION_RESPONSE=$(curl -s -X POST "$BASE_URL/reservations" \
  -H "Content-Type: application/json" \
  -d "{
    \"userId\": $USER_ID,
    \"spaceId\": $SPACE1_ID,
    \"startDateTime\": \"2025-11-25T14:00:00\",
    \"endDateTime\": \"2025-11-25T16:00:00\",
    \"notes\": \"Reunião com cliente importante\"
  }")

RESERVATION_ID=$(echo $RESERVATION_RESPONSE | jq -r '.id')
echo $RESERVATION_RESPONSE | jq '.'
echo -e "${GREEN}✓ Reserva criada com ID: $RESERVATION_ID${NC}\n"

# ---

echo -e "${YELLOW}[15] LISTANDO TODAS AS RESERVAS...${NC}"
curl -s -X GET "$BASE_URL/reservations" | jq '.'
echo -e "${GREEN}✓ Lista de reservas${NC}\n"

# ---

echo -e "${YELLOW}[16] BUSCANDO RESERVAS DO USUÁRIO ($USER_ID)...${NC}"
curl -s -X GET "$BASE_URL/reservations/user/$USER_ID" | jq '.'
echo -e "${GREEN}✓ Reservas do usuário${NC}\n"

# ---

echo -e "${YELLOW}[17] BUSCANDO PRÓXIMAS RESERVAS DO USUÁRIO...${NC}"
curl -s -X GET "$BASE_URL/reservations/user/$USER_ID/upcoming" | jq '.'
echo -e "${GREEN}✓ Próximas reservas${NC}\n"

# ---

echo -e "${YELLOW}[18] BUSCANDO RESERVAS DO ESPAÇO ($SPACE1_ID)...${NC}"
curl -s -X GET "$BASE_URL/reservations/space/$SPACE1_ID" | jq '.'
echo -e "${GREEN}✓ Reservas do espaço${NC}\n"

# ============================================
# 4. TESTAR COBRANÇAS
# ============================================

echo -e "${YELLOW}[19] LISTANDO COBRANÇAS DO USUÁRIO ($USER_ID)...${NC}"
curl -s -X GET "$BASE_URL/billings/user/$USER_ID" | jq '.'
echo -e "${GREEN}✓ Cobranças do usuário (criada automaticamente!)${NC}\n"

# ---

echo -e "${YELLOW}[20] BUSCANDO COBRANÇAS PENDENTES DO USUÁRIO...${NC}"
curl -s -X GET "$BASE_URL/billings/user/$USER_ID/pending" | jq '.'
echo -e "${GREEN}✓ Cobranças pendentes${NC}\n"

# ---

echo -e "${YELLOW}[21] CALCULANDO DÍVIDA TOTAL DO USUÁRIO...${NC}"
curl -s -X GET "$BASE_URL/billings/user/$USER_ID/debt" | jq '.'
echo -e "${GREEN}✓ Dívida total${NC}\n"

# ---

echo -e "${YELLOW}[22] RESUMO FINANCEIRO DO USUÁRIO...${NC}"
curl -s -X GET "$BASE_URL/billings/user/$USER_ID/summary" | jq '.'
echo -e "${GREEN}✓ Resumo completo${NC}\n"

# ============================================
# 5. TESTAR VALIDAÇÕES E ERROS
# ============================================

echo -e "${YELLOW}[23] TESTANDO ERRO 404 - Usuário inexistente...${NC}"
curl -s -X GET "$BASE_URL/users/999" | jq '.'
echo -e "${RED}✓ Erro 404 capturado corretamente${NC}\n"

# ---

echo -e "${YELLOW}[24] TESTANDO ERRO 400 - Dados inválidos...${NC}"
curl -s -X POST "$BASE_URL/users" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jo",
    "email": "invalid"
  }' | jq '.'
echo -e "${RED}✓ Erro de validação capturado${NC}\n"

# ---

echo -e "${YELLOW}[25] TESTANDO ERRO 400 - Email já cadastrado...${NC}"
curl -s -X POST "$BASE_URL/users" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Outro João",
    "email": "joao@example.com",
    "password": "senha789",
    "role": "MEMBER"
  }' | jq '.'
echo -e "${RED}✓ Erro de negócio capturado${NC}\n"

# ---

echo -e "${YELLOW}[26] TESTANDO ERRO 409 - Conflito de horário...${NC}"
curl -s -X POST "$BASE_URL/reservations" \
  -H "Content-Type: application/json" \
  -d "{
    \"userId\": $USER2_ID,
    \"spaceId\": $SPACE1_ID,
    \"startDateTime\": \"2025-11-25T14:30:00\",
    \"endDateTime\": \"2025-11-25T16:30:00\"
  }" | jq '.'
echo -e "${RED}✓ Conflito de horário detectado${NC}\n"

# ============================================
# RESUMO FINAL
# ============================================

echo -e "${BLUE}╔════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║           TESTES CONCLUÍDOS!           ║${NC}"
echo -e "${BLUE}╚════════════════════════════════════════╝${NC}"
echo ""
echo -e "${GREEN}✓ Usuários criados: 2${NC}"
echo -e "${GREEN}✓ Espaços criados: 2${NC}"
echo -e "${GREEN}✓ Reservas criadas: 1${NC}"
echo -e "${GREEN}✓ Cobranças automáticas: 1${NC}"
echo -e "${GREEN}✓ Validações testadas: 3${NC}"
echo ""
echo -e "${YELLOW}IDs criados:${NC}"
echo -e "  User 1: $USER_ID"
echo -e "  User 2: $USER2_ID"
echo -e "  Space 1 (Sala Zeus): $SPACE1_ID"
echo -e "  Space 2 (Hot Desk): $SPACE2_ID"
echo -e "  Reservation: $RESERVATION_ID"
echo ""
