# 📱 Registro de Presença do Aluno

Aplicativo Android para registro de presença de alunos utilizando **biometria**, **GPS** e **Supabase** (backend em nuvem).

---

## 🚀 Funcionalidades

- ✅ Cadastro inicial do aluno (RA)
- ✅ Autenticação biométrica (digital)
- ✅ Captura de localização do aluno (GPS)
- ✅ Busca dos dados do aluno no Supabase
- ✅ Comparação de localização (aluno vs escola)
- ✅ Registro de presença com data, hora e status
- ✅ Sincronização online com Supabase

---

## 🛠️ Tecnologias Utilizadas

| Categoria | Tecnologia | Versão |
|-----------|------------|--------|
| **Linguagem** | Java | 8 |
| **IDE** | Android Studio | Koala |
| **Arquitetura** | MVVM (Model-View-ViewModel) | - |
| **Autenticação** | BiometricPrompt | 1.2.0 |
| **Localização** | Google Play Services Location | 21.3.0 |
| **Rede** | Retrofit + Gson | 2.11.0 |
| **Backend** | Supabase (PostgreSQL) | - |
| **Segurança** | EncryptedSharedPreferences | 1.0.0 |

---

## 📁 Estrutura do Projeto
app/src/main/java/com/exemplo/registropresenca/
├── data/
│ ├── model/ # Aluno.java, Presenca.java
│ ├── repository/ # AlunoRepository, PresencaRepository
│ ├── network/ # SupabaseApiService, RetrofitClient
│ └── local/ # TokenManager (RA criptografado)
├── ui/
│ ├── cadastro/ # CadastroActivity, CadastroViewModel
│ ├── login/ # BiometricActivity, BiometricViewModel
│ └── registro/ # RegistroActivity, RegistroViewModel, SucessoActivity
├── utils/
│ ├── LocationHelper.java # GPS e permissões
│ └── BiometricHelper.java # Autenticação biométrica
└── MyApplication.java

---

## 🔄 Fluxo de Dados do App
1.	Usuário abre o app

2.	Tela de autenticação biométrica
      ├── Primeira vez → Tela de cadastro (RA)
      └── Biometria OK → Tela de registro

3.	Tela de registro
      ├── Exibe nome e turma do aluno
      ├── Usuário clica em "Registrar Presença"
      ├── App captura localização (GPS)
      ├── Compara distância com coordenadas da escola
      └── Se distância ≤ 100m → salva presença no Supabase

---

## 🗄️ Estrutura do Banco (Supabase)

### Tabela `CadAluno`

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| ra | TEXT (PK) | Registro do aluno |
| nome | TEXT | Nome completo |
| turma | TEXT | Turma do aluno |
| lat_escola | DOUBLE | Latitude da unidade escolar |
| lng_escola | DOUBLE | Longitude da unidade escolar |

### Tabela `Presencas`

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| id | BIGSERIAL (PK) | ID automático |
| aluno_ra | TEXT | RA do aluno |
| nome | TEXT | Nome do aluno |
| turma | TEXT | Turma do aluno |
| data | DATE | Data do registro |
| horario | TIME | Horário do registro |
| lat_aluno | DOUBLE | Latitude do aluno no momento |
| lng_aluno | DOUBLE | Longitude do aluno no momento |
| status | TEXT | "Presente" |

---

## 🔧 Configuração para Desenvolvimento

### 1. Clone o repositório
```bash
git clone https://github.com/SEU_USUARIO/RegistroPresencaAluno.git



