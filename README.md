#  Bingo - JavaFX

Um sistema de sorteio de Bingo completo, desenvolvido em Java moderno, com foco em otimização de algoritmos e estrutura de dados. O projeto apresenta uma interface gráfica fluida em única janela, simulando uma roleta de sorteio e mantendo o histórico em tempo real.

---

##  Arquitetura e Lógica de Negócio

O principal foco de desenvolvimento deste projeto foi a implementação do "motor" do Bingo, garantindo eficiência e evitando processamentos desnecessários. 

Para a lógica de sorteio, em vez de gerar números aleatórios a cada clique (o que exigiria verificações constantes de repetição), optei por utilizar a abordagem de **Sorteio sem Reposição**:

* **Preparação (O Globo):** No início do jogo, uma lista é populada com todos os números sequenciais definidos pelo usuário (ex: 1 a 75).
* **Embaralhamento:** A lista inteira é embaralhada utilizando `Collections.shuffle()`.
* **Sorteio:** A cada rodada, o sistema simplesmente extrai o primeiro elemento (índice 0) da lista já misturada e o move para a lista de histórico.

### Reflexão sobre Complexidade Computacional (Big O)
Durante o desenvolvimento, analisei o custo de uma possível funcionalidade de "Desfazer Sorteio". 
* Retirar o último número do histórico (final da lista) tem um custo computacional **O(1)**. 
* No entanto, devolver essa bolinha para a primeira posição (índice 0) de um vetor (`ArrayList`) exigiria um deslocamento de memória de todos os outros elementos, alterando a complexidade da operação para **O(n)**. 

Essa análise reforçou a importância de escolher a estrutura de dados correta dependendo da necessidade de escalabilidade do sistema.

---

##  Tecnologias Utilizadas

* **Java 17+** (Linguagem principal)
* **JavaFX** (Construção da Interface Gráfica)
* **CSS Nativo** (Estilização de componentes, cores hexadecimais e bordas)

---

##  Nota de Transparência

A lógica central, algoritmos de sorteio, manipulação de listas e arquitetura de dados foram o meu foco de estudo e desenvolvimento principal neste projeto. Para a construção da interface gráfica (UI/UX) em JavaFX e a estilização dos componentes em tela única (`StackPane`), utilizei assistência de Inteligência Artificial. Essa separação de escopos me permitiu focar 100% nas regras de negócio e no comportamento da memória do software.

---

##  Como Executar o Projeto

1. Certifique-se de ter o **Java 17** (ou superior) instalado em sua máquina.
2. Faça o clone deste repositório:
   `git clone https://github.com/Pissolatto-dev/bingo-em-java.git`
3. Abra o projeto em sua IDE de preferência (IntelliJ IDEA recomendado).
4. Execute o arquivo `Launcher.java` para iniciar a aplicação com as dependências do JavaFX carregadas corretamente.
5. Pressione **F11** durante o jogo para alternar entre o modo janela e tela cheia.
