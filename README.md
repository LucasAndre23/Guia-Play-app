# Guia Play: Seu Hub de Entretenimento Descentralizado

### Equipe
* Lucas André
* Heitor Sales

---

## Sobre o Projeto

O Guia Play é um aplicativo inovador que resolve o problema da fragmentação de conteúdo na era dos serviços de streaming. Em vez de ficar preso aos catálogos de plataformas específicas, nosso objetivo é oferecer uma ferramenta neutra e abrangente para a descoberta e a organização do seu entretenimento.

O Guia Play não é mais um serviço de streaming; é o seu **organizador pessoal e imparcial** do universo do entretenimento, colocando o controle e a descoberta nas suas mãos.

## Funcionalidades Principais

### 1. Descoberta Sem Barreiras
A tela **Início** funciona como uma vitrine inteligente, apresentando filmes e séries que são verdadeiramente populares e relevantes, independentemente de qual plataforma os hospeda. Isso garante que você esteja sempre por dentro do que há de mais relevante no mundo do entretenimento.

### 2. Seu Catálogo, Não o Deles
A tela **Minha Lista de Assistidos** permite que você crie e gerencie sua própria lista unificada de filmes e séries. Você pode registrar qualquer conteúdo que assistiu ou queira assistir, de qualquer serviço de streaming, eliminando a necessidade de listas em aplicativos diferentes.

### 3. Organização Centralizada
O aplicativo atua como seu hub central para gerenciar seu histórico de visualização. Tudo o que você consome ou deseja consumir, de qualquer lugar, estará em um só lugar.

## Telas do Aplicativo

* **Início**: Ponto de partida para a descoberta de conteúdo popular e relevante.
* **Login**: Porta de entrada segura para acessar sua lista pessoal de entretenimento.
* **Minha Lista de Assistidos**: Seu catálogo pessoal e organizado de filmes e séries.

---

## Tecnologias e Arquitetura

O projeto Guia Play foi construído utilizando um conjunto de tecnologias modernas e uma arquitetura robusta para garantir desempenho, escalabilidade e facilidade de manutenção.

* **MVVM (Model-View-ViewModel) e View Model:** A arquitetura MVVM separa a lógica de negócio da interface do usuário. Os ViewModels gerenciam o estado da tela e a lógica, tornando o código mais limpo e testável.
* **UI State:** Cada tela principal utiliza um `UI State` (uma `data class`) para representar todos os seus estados possíveis (carregamento, dados, erro). Isso garante que a UI seja sempre um reflexo consistente dos dados.
* **Injeção de Dependência com Koin:** O Koin é usado para gerenciar e fornecer as dependências do projeto (como ViewModels e repositórios), simplificando a criação de objetos e a organização do código.
* **Biblioteca Room:** Utilizada para armazenar dados localmente. O Room permite que o aplicativo armazene a lista de filmes do usuário e outros dados, garantindo que o app funcione mesmo sem conexão com a internet.
* **Jetpack Compose:** A UI foi construída com o kit de ferramentas moderno do Android, o Jetpack Compose. Ele permite criar interfaces de forma declarativa, resultando em um código mais conciso e reativo.
* **Google Firestore:** O Firestore é utilizado como backend para armazenar a lista de filmes e séries, além de gerenciar a autenticação de usuários (login e cadastro) de forma segura e em tempo real.

---

---


## Estrutura do Projeto

A seguir, a estrutura de diretórios do projeto :

.
├── .gradle
│   ├── 8.13
│   │   ├── checksums
│   │   ├── executionHistory
│   │   ├── expanded
│   │   ├── fileChanges
│   │   ├── fileHashes
│   │   └── vcsMetadata
│   ├── buildOutputCleanup
│   └── vcs-1
├── .idea
│   └── inspectionProfiles
├── app
│   └── src
│       ├── androidTest
│       │   └── java
│       │       └── com
│       │           └── example
│       │               └── guia_play
│       ├── main
│       │   ├── java
│       │   │   └── com
│       │   │       └── example
│       │   │           └── guia_play
│       │   │               ├── data
│       │   │               │   ├── datasources
│       │   │               │   ├── local
│       │   │               │   ├── model
│       │   │               │   └── repository
│       │   │               ├── di
│       │   │               └── ui
│       │   │                   ├── components
│       │   │                   ├── screens
│       │   │                   ├── theme
│       │   │                   └── viewmodel
│       │   └── res
│       │       ├── drawable
│       │       ├── mipmap-anydpi-v26
│       │       ├── mipmap-hdpi
│       │       ├── mipmap-mdpi
│       │       ├── mipmap-xhdpi
│       │       ├── mipmap-xxhdpi
│       │       ├── mipmap-xxxhdpi
│       │       ├── values
│       │       └── xml
│       └── test
│           └── java
│               └── com
│                   └── example
│                       └── guia_play
└── gradle
└── wrapper
