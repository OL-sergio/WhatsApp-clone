# 📱 WhatsApp Clone

Uma aplicação Android desenvolvida em Java que simula um serviço de chat em tempo real, utilizando o Firebase como backend para autenticação de usuários, banco de dados em tempo real e armazenamento de dados.

## 🎯 Objetivos do Projeto

- **Autenticação de Usuários:** Implementar um sistema seguro de login e cadastro utilizando o Firebase Authentication (Email/Senha).
- **Chat em Tempo Real:** Permitir a troca de mensagens instantâneas entre usuários, com sincronização de dados via Firebase Realtime Database.
- **Gerenciamento de Contatos:** Exibir uma lista de usuários cadastrados com os quais é possível iniciar uma conversa.
- **Interface Intuitiva:** Criar uma interface de usuário limpa e responsiva, inspirada no WhatsApp e baseada nos princípios do Material Design.

---

## 🛠️ Tecnologias e Ferramentas

| Tecnologia | Ícone | Descrição |
| :--- | :---: | :--- |
| **Java** | ![Java](https://img.icons8.com/color/48/000000/java-coffee-cup-logo.png) | Linguagem de programação principal para a lógica da aplicação. |
| **XML** | ![XML](https://img.icons8.com/color/48/000000/xml.png) | Utilizado para a definição dos layouts e da interface do usuário. |
| **Gradle** | ![Gradle](https://img.icons8.com/color/48/000000/gradle.png) | Ferramenta de automação de build para gerenciar dependências e compilar o projeto. |
| **Android Studio** | ![Android Studio](https://img.icons8.com/color/48/000000/android-studio--v2.png) | IDE oficial para o desenvolvimento de aplicações Android. |
| **Firebase** | ![Firebase](https://img.icons8.com/color/48/000000/firebase.png) | Plataforma de backend como serviço (BaaS) para autenticação e banco de dados. |

---

## 🏗️ Estrutura do Projeto

A estrutura do projeto segue o padrão recomendado para aplicações Android, separando responsabilidades em pacotes distintos.

````

app/
 └─ src/main/
     ├─ java/com/olsergio/whatsappclone/
     │   ├─ core/                  # Componentes compartilhados
     │   │   ├─ di/                # Injeção de dependência (Hilt/Dagger)
     │   │   └─ network/           # Configuração de rede (FirebaseHelper)
     │   ├─ data/                  # Fontes de dados e repositórios
     │   │   ├─ model/             # Modelos de dados (User, Message)
     │   │   └─ repository/        # Repositórios (ex: UserRepository, ChatRepository)
     │   ├─ ui/                    # Camada de UI (Views e ViewModels)
     │   │   ├─ auth/              # Funcionalidade de autenticação
     │   │   │   ├─ LoginActivity.java
     │   │   │   └─ LoginViewModel.java
     │   │   ├─ main/              # Tela principal e seus fragmentos
     │   │   │   ├─ MainActivity.java
     │   │   │   ├─ MainViewModel.java
     │   │   │   ├─ chat/
     │   │   │   │   ├─ ChatFragment.java
     │   │   │   │   └─ ChatAdapter.java
     │   │   │   └─ contacts/
     │   │   │       ├─ ContactsFragment.java
     │   │   │       └─ ContactsAdapter.java
     │   │   └─ settings/          # Tela de configurações
     │   │       ├─ ConfigFragment.java
     │   │       └─ ConfigViewModel.java
     └─ res/
         ├─ layout/
         ├─ menu/
         ├─ drawable/
         └─ values/
         
````
---

## 🧩 Componentes e Funcionalidades

### Classes Principais
- **`MainActivity.java`**: Atua como o contêiner principal da UI, gerenciando a navegação entre os fragmentos. Também é responsável por inflar o menu de opções (`menu_main.xml`), que inclui pesquisa, configurações e logout.
- **`ContactsFragment.java`**: Exibe a lista de todos os usuários cadastrados utilizando um `RecyclerView`. Permite iniciar uma nova conversa ao clicar em um contato.
- **`ChatFragment.java`**: Exibe as conversas ativas do usuário.
- **`ConfigFragment.java`**: Permite ao usuário visualizar suas informações e realizar o logout da aplicação.
- **`FirebaseHelper.java`**: Centraliza e abstrai a lógica de comunicação com o Firebase, como autenticação, leitura e escrita no Realtime Database.

### Componentes Android
- **Fragments**: A arquitetura é baseada em fragmentos para modularizar a interface, permitindo que as seções de Contatos, Conversas e Configurações sejam independentes e reutilizáveis.
- **RecyclerView**: Componente essencial para exibir listas de dados dinâmicas de forma eficiente, utilizado tanto na lista de contatos quanto na exibição de mensagens dentro de uma conversa.
- **Adapters**: `ContactsAdapter` e `ChatAdapter` são as pontes entre os dados (`List<User>` e `List<Message>`) e a `RecyclerView`, responsáveis por criar e vincular as `ViewHolders` para cada item da lista.

### Firebase
- **Authentication**: Gerencia o ciclo de vida do usuário (cadastro, login, logout) de forma segura.
- **Realtime Database**: Armazena e sincroniza os dados da aplicação (usuários, mensagens) em tempo real entre todos os clientes conectados.

---

## 📚 Bibliotecas e Frameworks

- **AndroidX**: Conjunto de bibliotecas que oferece componentes modernos e retrocompatibilidade, incluindo `AppCompat`, `Fragment`, e `RecyclerView`.
- **Material Components for Android**: Fornece componentes de UI que seguem as diretrizes do Material Design, como `TabLayout`, `CardView`, e `TextInputLayout`, garantindo uma aparência consistente e moderna.

---

## ⚙️ Configuração e Execução

Siga os passos abaixo para configurar e executar o projeto localmente.

1.  **Clonar o Repositório**
    ```bash
    git clone https://github.com/OL-sergio/WhatsApp-clone.git
    ```

2.  **Abrir no Android Studio**
    - Inicie o Android Studio.
    - Selecione `File` > `Open` e navegue até o diretório onde o projeto foi clonado.

3.  **Configurar o Firebase**
    - Acesse o [Firebase Console](https://console.firebase.google.com/) e crie um novo projeto.
    - Adicione um novo aplicativo Android ao seu projeto Firebase, utilizando `com.olsergio.whatsappclone` como o nome do pacote.
    - Baixe o arquivo de configuração `google-services.json` e mova-o para o diretório `app/` do seu projeto no Android Studio.
    - No console do Firebase, habilite os seguintes serviços:
        - **Authentication**: Ative o provedor "Email/Senha".
        - **Realtime Database**: Crie um banco de dados e defina as regras de segurança conforme necessário (para desenvolvimento, pode-se iniciar com regras abertas).

4.  **Executar a Aplicação**
    - Aguarde o Android Studio sincronizar o projeto com os arquivos Gradle.
    - Conecte um dispositivo Android ou inicie um emulador.
    - Clique no botão **Run 'app'** (▶️) na barra de ferramentas.

---

## 🛡️ Permissões

A aplicação requer as seguintes permissões, que devem ser declaradas no `AndroidManifest.xml`:
-   `android.permission.INTERNET`: Para acesso à rede e comunicação com o Firebase.
-   `android.permission.ACCESS_NETWORK_STATE`: Para verificar o estado da conexão de rede.

---

## 🖼️ Imagens e Ícones

-   Os ícones utilizados na aplicação, como os do menu de busca e configurações, estão localizados em `app/src/main/res/drawable/`.

---

## 📲 Instalação via Android Studio

Para instalar o aplicativo diretamente do Android Studio em um dispositivo físico ou emulador:

1.  **Pré-requisitos**: Tenha o Android Studio (versão Narwhal | 2025.1.2 ou superior) instalado.
2.  **Importar Projeto**: Siga os passos da seção "Configuração e Execução" para abrir e configurar o projeto.
3.  **Habilitar Depuração USB** (para dispositivos físicos): No seu dispositivo Android, vá para `Configurações > Sobre o telefone`, toque em "Número da versão" 7 vezes para habilitar as "Opções do desenvolvedor". Em seguida, ative a "Depuração USB".
4.  **Executar**: Com o projeto aberto e um dispositivo/emulador selecionado, clique em **Run 'app'**. O Android Studio irá compilar, instalar e iniciar o aplicativo automaticamente.

5.  
