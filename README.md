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

Padrão UI State (Jetpack Compose)
O projeto Guia Play adota o padrão UI State para gerenciar o estado das telas de forma reativa e centralizada. Em vez de a interface (a View) ter que lidar com lógicas complexas, ela simplesmente observa um único objeto de estado, que é a 'fonte da verdade' da tela.

Como Funciona
Cada tela principal possui uma data class específica (HomeUiState, MyListUiState, AuthUiState) que encapsula todos os estados possíveis da interface, como:

Dados: A lista de filmes a ser exibida (myItems, recommendedItems).

Estado de Carregamento: Uma variável Boolean (isLoading) que informa à tela que uma operação assíncrona está em andamento.

Mensagens de Erro: Uma variável String (error) para comunicar falhas ao usuário.

Vantagens da Abordagem
Unidirecionalidade: O fluxo de dados é sempre do ViewModel para a View, tornando o código mais previsível e fácil de depurar.

Consistência: A tela nunca fica em um estado inconsistente, pois ela sempre se renderiza com base em uma única e completa "fotografia" do estado.

Separação de Responsabilidades: A View é 'burra' (não tem lógica), e o ViewModel é 'inteligente' (gerencia a lógica), o que simplifica a manutenção e os testes.

Com isso, a tela apenas reage a mudanças no UI State, exibindo o conteúdo, um indicador de progresso, uma mensagem de erro ou uma tela de lista vazia de forma automática e eficiente.

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
